package com.example.shapelibrary.repository.entities;

import com.example.shapelibrary.domain.ValidShape;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "shape_type")
@ValidShape
public abstract class Shape {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @ElementCollection(fetch = FetchType.EAGER)
    double[] parameters;

    public abstract String getType();

    abstract double calculateArea();

    public abstract int getRequiredParameterCount();
}
