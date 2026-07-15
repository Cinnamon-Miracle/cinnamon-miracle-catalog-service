package com.cinnamonmiracle.catalog.repository;

import com.cinnamonmiracle.catalog.entity.ReceivedStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReceivedStockRepository extends JpaRepository<ReceivedStock, String> {
    Optional<ReceivedStock> findByReceivedProductID(String receivedProductID);
}
