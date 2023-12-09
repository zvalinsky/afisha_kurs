package ru.vlsu.hotel_kurs.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class EventType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
}
