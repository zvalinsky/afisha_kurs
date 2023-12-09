package ru.vlsu.hotel_kurs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vlsu.hotel_kurs.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}