package com.example.shapelibrary.repository;

import com.example.shapelibrary.repository.entities.Creator;
import com.example.shapelibrary.repository.entities.Shape;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShapeRepository extends JpaRepository<Shape,Long> {
    public List<Shape> findByType(String type);

    public List<Shape> findByCreator(Creator creator);
}
