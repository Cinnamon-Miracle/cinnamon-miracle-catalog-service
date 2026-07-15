package com.cinnamonmiracle.catalog.controller;

import com.cinnamonmiracle.catalog.entity.ReceivedStock;
import com.cinnamonmiracle.catalog.service.ReceivedStockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** routes/recievedStockRoutes.js (all routes protected by authMiddleware). */
@RestController
@RequestMapping("/api/received-stocks")
public class ReceivedStockController {

    private final ReceivedStockService receivedStockService;

    public ReceivedStockController(ReceivedStockService receivedStockService) {
        this.receivedStockService = receivedStockService;
    }

    @GetMapping("/page/{page}/size/{size}")
    public ResponseEntity<Map<String, Object>> getAllReceivedStocks(
            @PathVariable String page, @PathVariable String size) {
        return ResponseEntity.ok(receivedStockService.getAllReceivedStocks());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createReceivedStock(@RequestBody ReceivedStock stock) {
        return ResponseEntity.status(HttpStatus.CREATED).body(receivedStockService.createReceivedStock(stock));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getReceivedStock(@PathVariable String id) {
        return ResponseEntity.ok(receivedStockService.getReceivedStock(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateReceivedStock(
            @PathVariable String id, @RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(receivedStockService.updateReceivedStock(id, body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteReceivedStock(@PathVariable String id) {
        return ResponseEntity.ok(receivedStockService.deleteReceivedStock(id));
    }
}
