package ru.vlsu.hotel_kurs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vlsu.hotel_kurs.entity.Event;
import ru.vlsu.hotel_kurs.entity.User;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Event findByName(String name);

    List<Event> findAllByCreator(User user);
}