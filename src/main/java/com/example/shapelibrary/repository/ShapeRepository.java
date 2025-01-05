package com.example.shapelibrary.repository;

import com.example.shapelibrary.repository.entities.User;
import com.example.shapelibrary.repository.entities.Shape;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShapeRepository extends JpaRepository<Shape,Long> {
    public List<Shape> findByType(String type);

    public List<Shape> findByUser(User user);
}
