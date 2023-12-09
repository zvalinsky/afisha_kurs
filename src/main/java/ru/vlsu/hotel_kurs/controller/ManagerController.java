package ru.vlsu.hotel_kurs.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.vlsu.hotel_kurs.entity.*;
import ru.vlsu.hotel_kurs.repositories.CityRepository;
import ru.vlsu.hotel_kurs.repositories.EventTypeRepository;
import ru.vlsu.hotel_kurs.repositories.ImageRepository;
import ru.vlsu.hotel_kurs.repositories.UserRepository;
import ru.vlsu.hotel_kurs.services.EventService;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/manager")
@Slf4j
public class ManagerController {
    @Autowired
    private EventService eventService;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private EventTypeRepository eventTypeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageRepository imageRepository;

    @GetMapping("events")
    public String managerEvents(Model model) {


        model.addAttribute("roleList", getCurrentRoles());

        User byUsername = userRepository.findByUsername(getCurrentUsername());

        model.addAttribute("eventList", eventService.getAllByUser(byUsername));

        return "managegerEventList";
    }

    @GetMapping("events/create")
    public String showCreateEventForm(Model model) {
        List<City> cities = cityRepository.findAll();
        List<EventType> eventTypes = eventTypeRepository.findAll();
        model.addAttribute("roleList", getCurrentRoles());

        Event event = new Event();
        model.addAttribute("event", event);
        model.addAttribute("cities", cities);
        model.addAttribute("eventTypes", eventTypes);

        return "createEventForm";
    }
    @PostMapping("events/create")
    public String createEvent(@ModelAttribute("event") Event event) {

        String currentUsername = getCurrentUsername();

        User byUsername = userRepository.findByUsername(currentUsername);

        event.setCreator(byUsername);

        eventService.saveEvent(event);

        return "redirect:/manager/events/edit/" + event.getId();

    }
    @GetMapping("/events/edit/{eventId}")
    public String showEditEventForm(@PathVariable Long eventId, Model model) {
        Event event = eventService.getEventById(eventId);
        List<City> cities = cityRepository.findAll();
        List<EventType> eventTypes = eventTypeRepository.findAll();

        model.addAttribute("roleList", getCurrentRoles());
        model.addAttribute("event", event);
        model.addAttribute("cities", cities);
        model.addAttribute("eventTypes", eventTypes);

        return "editEventForm";
    }

    @PostMapping("/events/edit/{eventId}")
    public String editEvent(@PathVariable Long eventId, @ModelAttribute("event") Event updatedEvent) {

        Event existingEvent = eventService.getEventById(eventId);
        existingEvent.setName(updatedEvent.getName());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setCity(updatedEvent.getCity());
        existingEvent.setEventType(updatedEvent.getEventType());
        existingEvent.setStartDate(updatedEvent.getStartDate());
        existingEvent.setEndDate(updatedEvent.getEndDate());

        eventService.saveEvent(existingEvent);

        return "redirect:/manager/events/edit/" + eventId;
    }

    @PostMapping("/events/addImage/{eventId}")
    public String addImage(@PathVariable Long eventId, @RequestParam("picture") MultipartFile picture) {
        Event existingEvent = eventService.getEventById(eventId);
        Image image = new Image();
            try {
                image.setPicture(picture.getBytes());
            } catch (IOException e) {
                log.info("Ошибка разбора изображения");
            }
            image.setEvent(existingEvent);
            imageRepository.save(image);

            existingEvent.setImages(image);

        eventService.saveEvent(existingEvent);

        return "redirect:/manager/events/edit/" + eventId;
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



    private List<String> getCurrentRoles() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof  UserDetails) {
                List<String> roles = new ArrayList<>();
                Collection<? extends GrantedAuthority> authorities = ((UserDetails) principal).getAuthorities();
                authorities.forEach(e -> {
                    roles.add(e.getAuthority());
                });
                return roles;
            }
        return new ArrayList<String>();
    }

}
