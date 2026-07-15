package com.cinnamonmiracle.catalog.controller;

import com.cinnamonmiracle.catalog.dto.ProductInternalDto;
import com.cinnamonmiracle.catalog.entity.Product;
import com.cinnamonmiracle.catalog.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Internal, service-to-service endpoints used by sales-service to validate and
 * decrement product stock. These are NOT routed through the API gateway and are
 * excluded from JWT auth (see WebConfig).
 */
@RestController
@RequestMapping("/internal/products")
public class InternalProductController {

    private final ProductService productService;

    public InternalProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductInternalDto> getProduct(@PathVariable String id) {
        Product product = productService.getProductOrNull(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDto(product));
    }

    @PostMapping("/{id}/decrement-stock")
    public ResponseEntity<ProductInternalDto> decrementStock(
            @PathVariable String id, @RequestBody Map<String, Object> body) {
        double quantity = toDouble(body.get("quantity"));
        Product product = productService.decrementStock(id, quantity);
        return ResponseEntity.ok(toDto(product));
    }

    private ProductInternalDto toDto(Product product) {
        return new ProductInternalDto(product.getId(), product.getItemName(), product.getStocks());
    }

    private double toDouble(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Number n) {
            return n.doubleValue();
        }
        return Double.parseDouble(value.toString());
    }
}
