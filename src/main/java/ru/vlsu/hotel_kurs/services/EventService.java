package ru.vlsu.hotel_kurs.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vlsu.hotel_kurs.entity.Event;
import ru.vlsu.hotel_kurs.entity.User;
import ru.vlsu.hotel_kurs.repositories.EventRepository;
import ru.vlsu.hotel_kurs.repositories.UserRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
public class EventService {

    @Autowired
    EventRepository eventRepository;
    @Autowired
    UserRepository userRepository;
    public void saveEvent(Event event){
        eventRepository.save(event);
    }
    public Event getEventById(long id){
        return eventRepository.findById(id).orElse(null);
    }
    public List<Event> getAllByUser(User user){
        return eventRepository.findAllByCreator(user);
    }

    public List<Event> filterEvents(String eventType, String city, Date periodStart, Date periodEnd) {
        List<Event> all = eventRepository.findAll();

        log.info(" filter " + eventType +" " + city + " " + periodStart + " " + periodEnd);

        if(Objects.nonNull(eventType) && !eventType.isEmpty()){
           all = all.stream().filter(event -> {
                return event.getEventType().getName().equals(eventType);
            }).toList();
        }
        if(Objects.nonNull(city)  && !city.isEmpty()){
            all = all.stream().filter(event -> {
                return event.getCity().getName().equals(city);
            }).toList();
        }
        if(Objects.nonNull(periodStart)){
            all = all.stream().filter(event -> {
                return (event.getEndDate().after(periodStart) || event.getEndDate().equals(periodStart));
            }).toList();
        }
        if(Objects.nonNull(periodEnd)){
            all = all.stream().filter(event -> {
                return (event.getStartDate().before(periodEnd) || event.getEndDate().equals(periodEnd));
            }).toList();
        }
        return  all;

    }

    public void checkIn(long eventId, String userName){
        User user = userRepository.findByUsername(userName);

        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isPresent()) {
            log.info(" check in " + userName + " on " + eventOptional.get().getName());
            eventOptional.get().getVisitors().add(user);
            eventRepository.save(eventOptional.get());
        }

    }

}
