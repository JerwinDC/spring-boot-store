package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Category;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.CategoryRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.temporal.ValueRange;
import java.util.List;
import java.util.Set;

@Tag(name = "Product")
@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @GetMapping()
    public ResponseEntity<List<ProductDto>> getAllProducts(
          //@RequestHeader(required = false, name = "x-auth-token") String token,
          @RequestParam(required = false, defaultValue = "", name = "categoryId")
          Long categoryId
    ){

        List<Product> productDtoList;
        if(categoryId != null){
            productDtoList = productRepository.findByCategoryId(categoryId);
        } else {
            productDtoList = productRepository.findAllWithCategory();
        }

        /*
        if(productDtoList.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        */
        List<ProductDto> productDtos = productDtoList.stream().map(productMapper::toDto).toList();

        return ResponseEntity.ok(productDtos);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id){
        var product = productRepository.findById(id).orElse(null);

        if(product==null){
            return ResponseEntity.notFound().build();
        }

        var productDto = productMapper.toDto(product);
        return ResponseEntity.ok(productDto);
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto request,
            UriComponentsBuilder uriBuilder
    ){
        var product = productMapper.toEntity(request);

        Category category = categoryRepository.findById(request.getCategoryId()).orElse(null);

        if(category == null){
            return ResponseEntity.badRequest().build();
        }
        product.setCategory(category);

        productRepository.save(product);
        var productDto = productMapper.toDto(product);
        var uri = uriBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();
        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDto request
    ){
        var product =  productRepository.findById(id).orElse(null);
        if(product == null){
            return ResponseEntity.notFound().build();
        }

        Category category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if(category == null){
            return ResponseEntity.badRequest().build();
        }
        /*
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());


         */
        productMapper.update(request, product);
        product.setCategory(category);
        productRepository.save(product);
        request.setId(product.getId());
        return ResponseEntity.ok(request);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        var product = productRepository.findById(id).orElse(null);
        if(product == null){
            return ResponseEntity.notFound().build();
        }

        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}
