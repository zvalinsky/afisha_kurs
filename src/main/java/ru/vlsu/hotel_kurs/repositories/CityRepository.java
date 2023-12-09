package ru.vlsu.hotel_kurs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vlsu.hotel_kurs.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    City findByName(String name);

}