package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private CartMapper cartMapper;

    public CartDto createCart(){
        Cart cart = new Cart();
        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    public CartItemDto addToCart(UUID cartId, Long productId){
        var cart = cartRepository.findById(cartId).orElse(null);

        if(cart == null){
            throw new CartNotFoundException();
        }

        var product = productRepository.findById(productId).orElse(null);
        if(product == null){
            throw new ProductNotFoundException();
        }

        var cartItem = cart.addItem(product);
        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);

    }

    public CartDto getCartItems(UUID cartId){
        var cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }
        return cartMapper.toDto(cart);
    }

    public CartItemDto updateCartItem(UUID cartId, Long productId, Integer quantity){
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        var cartItem = cart.getItem(productId);

        if(cartItem == null){
            throw new ProductNotFoundException();
        }

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public void  removeItem(UUID cartId, Long productId){
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        var cartItem = cart.removeItem(productId);

        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId){
        var cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }

        cart.clear();
        cartRepository.save(cart);
    }
}
