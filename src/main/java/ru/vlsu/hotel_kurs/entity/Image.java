package ru.vlsu.hotel_kurs.entity;

import lombok.Data;

import javax.persistence.*;
@Entity
@Data
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private byte[] picture;

    @OneToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
