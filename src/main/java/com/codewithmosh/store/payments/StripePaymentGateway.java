package com.codewithmosh.store.payments;

import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.entities.OrderItem;
import com.codewithmosh.store.entities.PaymentStatus;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class StripePaymentGateway implements PaymentGateway{
    @Value("${website.url}")
    private String websiteUrl;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {
        //Create a checkout session
        try {
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
                    .setCancelUrl(websiteUrl + "/checkout-cancel")
                    .setPaymentIntentData(createPaymentIntent(order));

            order.getItems().forEach(items -> {
                        var lineItem = createLineItem(items);
                        builder.addLineItem(lineItem);
            });


            var session = Session.create(builder.build());
            return new CheckoutSession(session.getUrl());

        } catch (StripeException ex) {
            System.out.println(ex.getMessage());
            throw new PaymentException();
        }
    }

    private SessionCreateParams.PaymentIntentData createPaymentIntent(Order order) {
        return SessionCreateParams.PaymentIntentData.builder()
                .putMetadata("order_id", String.valueOf(order.getId())).build();
    }

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {
        try {
            // WebhookRequest -> { orderId, paymentStatus } (paymentResult)
            var payload = request.getPayload();
            var signature = request.getHeaders().get("stripe-signature");
            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);


            return switch (event.getType()){
                case "payment_intent.succeeded" ->
                     Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.PAID));

                case "payment_intent.failed" ->
                     Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.FAILED));

                case "payment_intent.completed" ->
                     Optional.of(new PaymentResult(extractOrderIdFromSession(event), PaymentStatus.PAID));

                default -> Optional.empty();
            };

        } catch (SignatureVerificationException e) {
            throw new PaymentException("Invalid Signature");
        }
    }

    private Long extractOrderId(Event event){
        var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow(
                () -> new PaymentException("Could not deserialize Stripe event. Check the SDK and API version")
        );
        var paymentIntent = (PaymentIntent) stripeObject;
        return Long.valueOf(paymentIntent.getMetadata().get("order_id"));

    }

    private Long extractOrderIdFromSession(Event event) {
        var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow(
                () -> new PaymentException("Could not deserialize Stripe event. Check the SDK and API version")
        );

        if (stripeObject instanceof Session session) {
            var orderId = session.getMetadata().get("order_id");
            if (orderId == null) {
                throw new PaymentException("Session is missing order_id metadata");
            }
            return Long.valueOf(orderId);
        }

        throw new PaymentException("Unsupported Stripe object: " + stripeObject.getClass().getName());
    }

    private SessionCreateParams.LineItem createLineItem(OrderItem items) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(items.getQuantity()))
                .setPriceData(createPriceData(items)).build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(OrderItem items) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmountDecimal(
                        items.getUnitPrice().multiply(BigDecimal.valueOf(100)))
                .setProductData(createProductData(items)
                ).build();
    }

    private SessionCreateParams.LineItem.PriceData.ProductData createProductData(OrderItem items) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(items.getProduct().getName()
                ).build();
    }
}
