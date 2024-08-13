package com.pointOfSale.src.data.repository;

import com.pointOfSale.src.data.entity.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {
    Tool findByCode(String code);
}