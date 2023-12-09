package ru.vlsu.hotel_kurs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.vlsu.hotel_kurs.entity.City;
import ru.vlsu.hotel_kurs.entity.Event;
import ru.vlsu.hotel_kurs.entity.EventType;
import ru.vlsu.hotel_kurs.repositories.CityRepository;
import ru.vlsu.hotel_kurs.repositories.EventTypeRepository;
import ru.vlsu.hotel_kurs.services.EventService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @GetMapping
    public String getAllEvents(@RequestParam(required = false) String eventType,
                               @RequestParam(required = false) String city,
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(required = false) Date periodStart,
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(required = false) Date periodEnd,
                               Model model) {

        List<Event> filteredEvents = eventService.filterEvents( eventType, city, periodStart, periodEnd);

        model.addAttribute("cities", cityRepository.findAll());
        model.addAttribute("eventTypes", eventTypeRepository.findAll());
        model.addAttribute("roleList", getCurrentRoles());
        model.addAttribute("eventList", filteredEvents);


        return "eventsList";
    }
    @PostMapping("/{eventId}/checkIn")
    public String checkIn(@PathVariable long eventId) {
        eventService.checkIn(eventId,getCurrentUsername());
        return "redirect:/events";
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

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        if (principal instanceof Principal) {
            return ((Principal) principal).getName();
        }
        return String.valueOf(principal);
    }
}
