package ru.vlsu.hotel_kurs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vlsu.hotel_kurs.entity.EventType;

@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Long> {
    EventType findByName(String name);
}

