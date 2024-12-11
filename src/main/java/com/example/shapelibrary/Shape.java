package com.example.shapelibrary;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "shape_type")
public abstract class Shape {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    double[] doubles;
    private String type;
    abstract String getType();
    abstract void setParameters(double[]doubles);
    abstract double calculateArea();

}
