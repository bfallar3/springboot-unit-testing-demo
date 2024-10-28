package com.benjsoft.unittestingdemo.service;

import com.benjsoft.unittestingdemo.exception.ProductNotFoundException;
import com.benjsoft.unittestingdemo.model.Product;
import com.benjsoft.unittestingdemo.repository.ProductRepository;
import com.benjsoft.unittestingdemo.utils.PriceCalculator;

public class ProductService {
    private final ProductRepository productRepository;
    private final PriceCalculator priceCalculator;

    public ProductService(ProductRepository productRepository, PriceCalculator priceCalculator) {
        this.productRepository = productRepository;
        this.priceCalculator = priceCalculator;
    }

    public Product updateProductPrice(Long productId, double newBasePrice) throws ProductNotFoundException {
        if (productId == 0 || newBasePrice < 0) {
            throw new IllegalArgumentException("Invalid product data provided");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));

        double finalPrice = priceCalculator.calculateFinalPrice(newBasePrice);
        product.setPrice(finalPrice);

        return productRepository.save(product);
    }
}
