package ru.s21school.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "peer")
public class Peer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String name;

    @Column(name = "age")
    @Min(value = 1, message = "Age should be greater than 0")
    private int age;

    @Column(name = "email")
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "address")
    @Pattern(regexp = "[A-Z]\\w+, [A-Z]\\w+, \\d{1,3}", message = "Your address should be in this form: City, Street, House")
    private String address;
}
