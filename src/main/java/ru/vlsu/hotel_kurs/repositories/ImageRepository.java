package ru.vlsu.hotel_kurs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vlsu.hotel_kurs.entity.EventType;
import ru.vlsu.hotel_kurs.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}