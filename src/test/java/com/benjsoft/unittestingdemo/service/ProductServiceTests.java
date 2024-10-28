package com.benjsoft.unittestingdemo.service;

import com.benjsoft.unittestingdemo.exception.ProductNotFoundException;
import com.benjsoft.unittestingdemo.model.Product;
import com.benjsoft.unittestingdemo.repository.ProductRepository;
import com.benjsoft.unittestingdemo.utils.PriceCalculator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Product Service Tests")
public class ProductServiceTests {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private PriceCalculator priceCalculator;

    private ProductService productService;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        // Arrange - Common test setup
        closeable = MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository, priceCalculator);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Nested
    @DisplayName("Update Product Price Tests")
    class UpdateProductPriceTests {

        @Test
        @DisplayName("Should successfully update product price when valid data is provided")
        void shouldUpdateProductPriceSuccessfully() throws ProductNotFoundException {
            // Arrange
            Long productId = 1L;
            double newBasePrice = 100.0;
            double calculatedPrice = 120.0;
            Product existingProduct = new Product(productId, "Test Product", 90.0);
            Product updatedProduct = new Product(productId, "Test Product", calculatedPrice);

            when(productRepository.findById(productId))
                    .thenReturn(Optional.of(existingProduct));
            when(priceCalculator.calculateFinalPrice(newBasePrice))
                    .thenReturn(calculatedPrice);
            when(productRepository.save(any(Product.class)))
                    .thenReturn(updatedProduct);

            // Act
            Product result = productService.updateProductPrice(productId, newBasePrice);

            // Assert
            assertNotNull(result, "Updated product should not be null");
            assertEquals(calculatedPrice, result.getPrice(),
                    "Product price should match calculated price");
            verify(productRepository).findById(productId);
            verify(priceCalculator).calculateFinalPrice(newBasePrice);
            verify(productRepository).save(any(Product.class));
        }

        @Test
        @DisplayName("Should throw exception when product is not found")
        void shouldThrowExceptionWhenProductNotFound() {
            // Arrange
            Long productId = 999L;
            when(productRepository.findById(productId))
                    .thenReturn(Optional.empty());

            // Act & Assert
            ProductNotFoundException exception = assertThrows(
                    ProductNotFoundException.class,
                    () -> productService.updateProductPrice(productId, 100.0),
                    "Should throw ProductNotFoundException when product not found"
            );

            assertTrue(exception.getMessage().contains(String.valueOf(productId)),
                    "Exception message should contain the product ID");
            verify(productRepository).findById(productId);
            verify(priceCalculator, never()).calculateFinalPrice(anyDouble());
        }

        @ParameterizedTest(name = "Should throw exception for invalid input: {0}")
        @CsvSource({
                "0, 100.0, Invalid product data provided",
                "1, -50.0, Invalid product data provided"
        })
        @DisplayName("Should throw exception for invalid inputs")
        void shouldThrowExceptionForInvalidInputs(Long productId, double price, String expectedMessage) {
            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> productService.updateProductPrice(productId, price),
                    "Should throw IllegalArgumentException for invalid inputs"
            );

            assertEquals(expectedMessage, exception.getMessage(),
                    "Exception message should match expected message");
            verify(productRepository, never()).findById(any());
            verify(priceCalculator, never()).calculateFinalPrice(anyDouble());
        }
    }
}

