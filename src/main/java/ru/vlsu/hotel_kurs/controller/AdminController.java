package ru.vlsu.hotel_kurs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.vlsu.hotel_kurs.entity.City;
import ru.vlsu.hotel_kurs.entity.EventType;
import ru.vlsu.hotel_kurs.repositories.CityRepository;
import ru.vlsu.hotel_kurs.repositories.EventTypeRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/admin")

public class AdminController {
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @GetMapping("/manageEntities")
    public String manageEntities(Model model) {


        model.addAttribute("city", new City());
        model.addAttribute("eventType", new EventType());
        model.addAttribute("cities", cityRepository.findAll());
        model.addAttribute("eventTypes", eventTypeRepository.findAll());
        model.addAttribute("roleList", getCurrentRoles());

        return "manageEntitiesAdmin";
    }

    @PostMapping("/createCity")
    public String createCity(@ModelAttribute City city) {
        cityRepository.save(city);
        return "redirect:/admin/manageEntities";
    }

    @PostMapping("/createEventType")
    public String createEventType(@ModelAttribute EventType eventType) {
        eventTypeRepository.save(eventType);
        return "redirect:/admin/manageEntities";
    }

    @GetMapping("/deleteCity/{id}")
    public String deleteCity(@PathVariable Long id) {
        cityRepository.deleteById(id);
        return "redirect:/admin/manageEntities";
    }

    @GetMapping("/deleteEventType/{id}")
    public String deleteEventType(@PathVariable Long id) {
        eventTypeRepository.deleteById(id);
        return "redirect:/admin/manageEntities";
    }


    private List<String> getCurrentRoles() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            List<java.lang.String> roles = new ArrayList<>();
            Collection<? extends GrantedAuthority> authorities = ((UserDetails) principal).getAuthorities();
            authorities.forEach(e -> {
                roles.add(e.getAuthority());
            });
            return roles;
        }
        return new ArrayList<java.lang.String>();
    }
}
