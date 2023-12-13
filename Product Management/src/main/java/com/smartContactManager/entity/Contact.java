package com.smartContactManager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@ToString
@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cId;
    private String name;
    private double price;

    @Column(length = 1000)
    private String description;

    private int quantity;
    private String image;

    @ManyToOne()
    @JsonIgnore
    private User user;

    @Override
    public boolean equals(Object obj) {
        return this.cId == ((Contact)obj).getCId();
    }
}
