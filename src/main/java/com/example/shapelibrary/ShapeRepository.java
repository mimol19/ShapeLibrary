package com.example.shapelibrary;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShapeRepository extends JpaRepository<ShapeEntity,Long> {
    public List<ShapeEntity> findByType(String type);
}
