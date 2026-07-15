package com.cinnamonmiracle.catalog.service;

import com.cinnamonmiracle.catalog.entity.Product;
import com.cinnamonmiracle.catalog.entity.ReceivedStock;
import com.cinnamonmiracle.catalog.repository.ProductRepository;
import com.cinnamonmiracle.catalog.repository.ReceivedStockRepository;
import com.cinnamonmiracle.common.exception.ApiException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Port of controllers/productController.js */
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ReceivedStockRepository receivedStockRepository;

    public ProductService(ProductRepository productRepository, ReceivedStockRepository receivedStockRepository) {
        this.productRepository = productRepository;
        this.receivedStockRepository = receivedStockRepository;
    }

    public Map<String, Object> getAllProducts(int page, int size) {
        try {
            if (page < 1) {
                page = 1;
            }
            long totalProducts = productRepository.count();
            int totalPages = (int) Math.ceil((double) totalProducts / size);

            List<Product> products = productRepository.findAll(PageRequest.of(page - 1, size)).getContent();

            Map<String, Object> pagination = new LinkedHashMap<>();
            pagination.put("currentPage", page);
            pagination.put("totalPages", totalPages);
            pagination.put("totalProducts", totalProducts);
            pagination.put("size", size);
            pagination.put("hasNextPage", page < totalPages);
            pagination.put("hasPrevPage", page > 1);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("products", products);
            response.put("pagination", pagination);
            return response;
        } catch (Exception e) {
            throw new ApiException(500, Map.of("message", "Server error", "error",
                    e.getMessage() == null ? "" : e.getMessage()));
        }
    }

    public Product getProduct(String id) {
        try {
            if (!isValidId(id)) {
                throw new ApiException(400, Map.of("message", "Invalid product ID format"));
            }
            return productRepository.findById(id)
                    .orElseThrow(() -> new ApiException(404, Map.of("message", "Product not found")));
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(500, Map.of("message", "Server error", "error",
                    e.getMessage() == null ? "" : e.getMessage()));
        }
    }

    public Product createProduct(Map<String, Object> body) {
        try {
            Product product = new Product();
            applyProductFields(product, body);
            return productRepository.save(product);
        } catch (Exception e) {
            throw new ApiException(400, Map.of("message", "Invalid data", "error",
                    e.getMessage() == null ? "" : e.getMessage()));
        }
    }

    public Product updateProduct(String id, Map<String, Object> body, String userId) {
        try {
            if (!isValidId(id)) {
                throw new ApiException(400, Map.of("message", "Invalid product ID format"));
            }

            Product product = productRepository.findById(id).orElse(null);
            if (product == null) {
                throw new ApiException(404, Map.of("message", "Product not found"));
            }

            Double oldStocks = product.getStocks();

            applyProductFields(product, body);

            Object updatedBy = body.get("updatedBy");
            product.setUpdatedBy(updatedBy != null ? updatedBy.toString() : userId);

            Product updatedProduct = productRepository.save(product);

            if (body.containsKey("stocks")) {
                Double newStock = toDouble(body.get("stocks"));
                if (newStock != null && oldStocks != null && newStock > oldStocks) {
                    double stockDifference = newStock - oldStocks;

                    ReceivedStock receivedStock = receivedStockRepository
                            .findByReceivedProductID(product.getItemCode())
                            .orElse(null);

                    if (receivedStock == null) {
                        receivedStock = new ReceivedStock();
                        receivedStock.setReceivedProductID(product.getItemCode());
                        receivedStock.setReceivedProductName(product.getItemName());
                        receivedStock.setQty(stockDifference);
                        Object categoryName = body.get("categoryName");
                        receivedStock.setCategory(categoryName != null ? categoryName.toString() : null);
                        String updatedByStr = updatedBy != null ? updatedBy.toString() : "admin";
                        receivedStock.setRemark("Stock updated by " + updatedByStr);
                        receivedStock.setCreatedBy(updatedBy != null ? updatedBy.toString()
                                : (userId != null ? userId : "admin"));
                    } else {
                        receivedStock.setQty(Math.max(0, receivedStock.getQty() - stockDifference));
                    }

                    receivedStockRepository.save(receivedStock);
                }
            }

            return updatedProduct;
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(400, Map.of("message", "Invalid data", "error",
                    e.getMessage() == null ? "" : e.getMessage()));
        }
    }

    public Map<String, Object> deleteProduct(String id) {
        try {
            if (!isValidId(id)) {
                throw new ApiException(400, Map.of("message", "Invalid product ID format"));
            }
            Product product = productRepository.findById(id).orElse(null);
            if (product == null) {
                throw new ApiException(404, Map.of("message", "Product not found"));
            }
            productRepository.deleteById(id);
            return Map.of("message", "Product deleted");
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(500, Map.of("message", "Server error", "error",
                    e.getMessage() == null ? "" : e.getMessage()));
        }
    }

    // ---- Internal (service-to-service) helpers used by sales-service ----

    public Product getProductOrNull(String id) {
        if (!isValidId(id)) {
            return null;
        }
        return productRepository.findById(id).orElse(null);
    }

    public Product decrementStock(String id, double quantity) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new ApiException(404, Map.of("message", "Product not found"));
        }
        double current = product.getStocks() == null ? 0 : product.getStocks();
        product.setStocks(current - quantity);
        return productRepository.save(product);
    }

    private void applyProductFields(Product product, Map<String, Object> body) {
        if (body.containsKey("itemCode")) {
            product.setItemCode(str(body.get("itemCode")));
        }
        if (body.containsKey("price")) {
            product.setPrice(toDouble(body.get("price")));
        }
        if (body.containsKey("brandName")) {
            product.setBrandName(str(body.get("brandName")));
        }
        if (body.containsKey("itemName")) {
            product.setItemName(str(body.get("itemName")));
        }
        if (body.containsKey("productImage")) {
            product.setProductImage(str(body.get("productImage")));
        }
        if (body.containsKey("categoryCode")) {
            product.setCategoryCode(str(body.get("categoryCode")));
        }
        if (body.containsKey("description")) {
            product.setDescription(str(body.get("description")));
        }
        if (body.containsKey("category")) {
            product.setCategory(str(body.get("category")));
        }
        if (body.containsKey("isActive")) {
            product.setIsActive(toBoolean(body.get("isActive")));
        }
        if (body.containsKey("updatedBy")) {
            product.setUpdatedBy(str(body.get("updatedBy")));
        }
        if (body.containsKey("manufacturerName")) {
            product.setManufacturerName(str(body.get("manufacturerName")));
        }
        if (body.containsKey("manufacturerCode")) {
            product.setManufacturerCode(str(body.get("manufacturerCode")));
        }
        if (body.containsKey("warrantyPeriod")) {
            product.setWarrantyPeriod(toDouble(body.get("warrantyPeriod")));
        }
        if (body.containsKey("warrantyDetails")) {
            product.setWarrantyDetails(str(body.get("warrantyDetails")));
        }
        if (body.containsKey("warrantyClaimProcess")) {
            product.setWarrantyClaimProcess(str(body.get("warrantyClaimProcess")));
        }
        if (body.containsKey("manufactureDate")) {
            product.setManufactureDate(toInstant(body.get("manufactureDate")));
        }
        if (body.containsKey("expireDate")) {
            product.setExpireDate(toInstant(body.get("expireDate")));
        }
        if (body.containsKey("stocks")) {
            product.setStocks(toDouble(body.get("stocks")));
        }
    }

    private boolean isValidId(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }
        try {
            java.util.UUID.fromString(id);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private String str(Object v) {
        return v == null ? null : v.toString();
    }

    private Double toDouble(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number n) {
            return n.doubleValue();
        }
        return Double.valueOf(value.toString());
    }

    private Boolean toBoolean(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean b) {
            return b;
        }
        return Boolean.valueOf(value.toString());
    }

    private Instant toInstant(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number n) {
            return Instant.ofEpochMilli(n.longValue());
        }
        String s = value.toString().trim();
        try {
            return Instant.parse(s);
        } catch (Exception ignored) {
            // ignore
        }
        try {
            return OffsetDateTime.parse(s).toInstant();
        } catch (Exception ignored) {
            // ignore
        }
        return LocalDate.parse(s).atStartOfDay(ZoneOffset.UTC).toInstant();
    }
}
