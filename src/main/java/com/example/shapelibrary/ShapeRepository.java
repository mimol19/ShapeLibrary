package com.example.shapelibrary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ShapeRepository extends JpaRepository<Shape,Long> {
    public List<Shape> findByType(String type);
}
