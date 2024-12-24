package com.luv2code.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "country")
@Setter
@Getter
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id" )
    private int id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    //Set up One-to-Many with states
    @OneToMany(mappedBy = "country")
    @JsonIgnore
    private List<State> states;
}
