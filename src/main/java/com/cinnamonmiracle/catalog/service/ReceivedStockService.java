package com.cinnamonmiracle.catalog.service;

import com.cinnamonmiracle.catalog.entity.ReceivedStock;
import com.cinnamonmiracle.catalog.repository.ReceivedStockRepository;
import com.cinnamonmiracle.common.exception.ApiException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Port of controllers/recievedStockController.js */
@Service
public class ReceivedStockService {

    private final ReceivedStockRepository receivedStockRepository;
    private final Validator validator;

    public ReceivedStockService(ReceivedStockRepository receivedStockRepository, Validator validator) {
        this.receivedStockRepository = receivedStockRepository;
        this.validator = validator;
    }

    public Map<String, Object> getAllReceivedStocks() {
        try {
            List<ReceivedStock> data = receivedStockRepository.findAll();
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("success", true);
            response.put("count", data.size());
            response.put("data", data);
            return response;
        } catch (Exception e) {
            throw new ApiException(500, Map.of("success", false, "error", "Server Error"));
        }
    }

    public Map<String, Object> getReceivedStock(String id) {
        try {
            ReceivedStock stock = receivedStockRepository.findById(id).orElse(null);
            if (stock == null) {
                throw new ApiException(404, Map.of("success", false, "error", "No received stock found"));
            }
            return successData(stock);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(500, Map.of("success", false, "error", "Server Error"));
        }
    }

    public Map<String, Object> createReceivedStock(ReceivedStock stock) {
        validateOrThrow(stock);
        try {
            ReceivedStock saved = receivedStockRepository.save(stock);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("success", true);
            response.put("data", saved);
            return response;
        } catch (DataIntegrityViolationException e) {
            throw new ApiException(400, Map.of("success", false, "error", "Duplicate value entered"));
        } catch (Exception e) {
            throw new ApiException(500, Map.of("success", false, "error", "Server Error"));
        }
    }

    public Map<String, Object> updateReceivedStock(String id, Map<String, Object> body) {
        ReceivedStock stock = receivedStockRepository.findById(id).orElse(null);
        if (stock == null) {
            throw new ApiException(404, Map.of("success", false, "error", "No received stock found"));
        }

        if (body.containsKey("receivedProductID")) {
            stock.setReceivedProductID((String) body.get("receivedProductID"));
        }
        if (body.containsKey("receivedProductName")) {
            stock.setReceivedProductName((String) body.get("receivedProductName"));
        }
        if (body.containsKey("category")) {
            stock.setCategory((String) body.get("category"));
        }
        if (body.containsKey("qty")) {
            stock.setQty(toDouble(body.get("qty")));
        }
        if (body.containsKey("remark")) {
            stock.setRemark((String) body.get("remark"));
        }
        if (body.containsKey("createdBy")) {
            stock.setCreatedBy((String) body.get("createdBy"));
        }

        validateOrThrow(stock);

        try {
            ReceivedStock saved = receivedStockRepository.save(stock);
            return successData(saved);
        } catch (Exception e) {
            throw new ApiException(500, Map.of("success", false, "error", "Server Error"));
        }
    }

    public Map<String, Object> deleteReceivedStock(String id) {
        try {
            ReceivedStock stock = receivedStockRepository.findById(id).orElse(null);
            if (stock == null) {
                throw new ApiException(404, Map.of("success", false, "error", "No received stock found"));
            }
            receivedStockRepository.deleteById(id);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("success", true);
            response.put("data", Map.of());
            return response;
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(500, Map.of("success", false, "error", "Server Error"));
        }
    }

    private void validateOrThrow(ReceivedStock stock) {
        Set<ConstraintViolation<ReceivedStock>> violations = validator.validate(stock);
        if (!violations.isEmpty()) {
            List<String> messages = violations.stream().map(ConstraintViolation::getMessage).toList();
            throw new ApiException(400, Map.of("success", false, "error", messages));
        }
    }

    private Map<String, Object> successData(ReceivedStock stock) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("data", stock);
        return response;
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
}
