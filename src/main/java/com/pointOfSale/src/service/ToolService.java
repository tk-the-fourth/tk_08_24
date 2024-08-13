package com.pointOfSale.src.service;

import com.pointOfSale.src.data.entity.Tool;
import com.pointOfSale.src.data.repository.ToolRepository;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ToolService {
    private final ToolRepository toolRepository;

    public List<Tool> list() {
        return this.toolRepository.findAll();
    }

    public Tool findByCode(String code) {
        return this.toolRepository.findByCode(code);
    }

}