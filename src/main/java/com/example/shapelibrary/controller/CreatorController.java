package com.example.shapelibrary.controller;

import com.example.shapelibrary.repository.CreatorRepository;
import com.example.shapelibrary.repository.entities.Creator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/creators")
public class CreatorController {

    private CreatorRepository creatorRepository;

    @GetMapping
    public ResponseEntity<List<Creator>> getAllCreators() {
        List<Creator> creators = creatorRepository.findAll();
        if (creators.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(creators);
    }
}
