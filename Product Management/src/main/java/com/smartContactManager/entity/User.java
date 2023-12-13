package com.smartContactManager.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "Name field is required")
    @Size(min = 2, max = 25)
    private String name;

    @Column(length = 500)
    private String about;

    @Column(unique = true)
    private String email;

    private String password;
    private String imageUrl;
    private String role;
    private boolean enabled;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "user", orphanRemoval = true)
    List<Contact> contacts = new ArrayList<>();
    
}
