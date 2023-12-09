package ru.vlsu.hotel_kurs.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToOne
    private City city;

    @ManyToOne
    private EventType eventType;
    @DateTimeFormat(pattern = "yyyy-MM-dd")

    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Image images;

    @ManyToOne
    private User creator;

    @ManyToMany
    private List<User> visitors;


}
