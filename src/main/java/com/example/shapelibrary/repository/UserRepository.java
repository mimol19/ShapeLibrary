package com.example.shapelibrary.repository;

import com.example.shapelibrary.repository.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByName(String name);

    @Override
    @Query("SELECT ur FROM User ur JOIN FETCH ur.shapes")
    public List<User> findAll();
}
