package com.example.shapelibrary.repository;

import com.example.shapelibrary.repository.entities.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreatorRepository extends JpaRepository<Creator, Long> {
    public Optional<Creator> findByName(String name);

    @Override
    @Query("SELECT cr FROM Creator cr JOIN FETCH cr.shapes")
    public List<Creator> findAll();
}
