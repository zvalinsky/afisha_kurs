package ru.vlsu.hotel_kurs.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.vlsu.hotel_kurs.entity.EventType;
import ru.vlsu.hotel_kurs.entity.City;
import ru.vlsu.hotel_kurs.entity.Role;
import ru.vlsu.hotel_kurs.entity.User;
import ru.vlsu.hotel_kurs.repositories.EventTypeRepository;
import ru.vlsu.hotel_kurs.repositories.CityRepository;
import ru.vlsu.hotel_kurs.repositories.RoleRepository;
import ru.vlsu.hotel_kurs.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class MyApplicationRunner implements ApplicationRunner {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    EventTypeRepository eventTypeRepository;
    @Autowired
    CityRepository cityRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Role admin = roleRepository.findByName("ADMIN");
        Role user = roleRepository.findByName("USER");
        Role manager = roleRepository.findByName("MANAGER");

        if (Objects.isNull(admin)){
            admin = new Role();
            admin.setName("ADMIN");

        }
        if (Objects.isNull(user)){
            user = new Role();
            user.setName("USER");

        }
        if (Objects.isNull(manager)){
            manager = new Role();
            manager.setName("MANAGER");

        }


        User adminUser = userRepository.findByUsername("admin");
        if (Objects.isNull(adminUser)) {
            adminUser = new User();
            List<Role> roleList = new ArrayList<>();
            roleList.add(admin);
            adminUser.setUsername("admin");
            adminUser.setRoles(roleList);
            adminUser.setPassword(passwordEncoder.encode("admin"));
            adminUser.setEnabled(true);
            userRepository.save(adminUser);
            ArrayList<User> users = new ArrayList<>();
            users.add(adminUser);
            admin.setUsers(users);
            roleRepository.save(admin);
        }


        User managerUser = userRepository.findByUsername("manager");
        if (Objects.isNull(managerUser)) {
            managerUser = new User();
            List<Role> roleList = new ArrayList<>();
            roleList.add(manager);
            managerUser.setRoles(roleList);
            managerUser.setUsername("manager");
            managerUser.setPassword(passwordEncoder.encode("manager"));
            managerUser.setEnabled(true);
            userRepository.save(managerUser);
            ArrayList<User> users = new ArrayList<>();
            users = new ArrayList<>();
            users.add(managerUser);
            manager.setUsers(users);
            roleRepository.save(manager);
        }


        User userUser = userRepository.findByUsername("user");
        if (Objects.isNull(userUser)) {
            userUser = new User();
            List<Role> roleList = new ArrayList<>();
            roleList.add(user);
            userUser.setRoles(roleList);
            userUser.setUsername("user");
            userUser.setPassword(passwordEncoder.encode("user"));
            userUser.setEnabled(true);
            userRepository.save(userUser);
            ArrayList<User> users = new ArrayList<>();

            users = new ArrayList<>();
            users.add(userUser);
            user.setUsers(users);
            roleRepository.save(user);
        }


        addEventType("Концерт");
        addEventType("Выставка");
        addEventType("Фестиваль");

        addCity("Москва");
        addCity("Владимир");
        addCity("Санкт-Петербург");
    }
    private void addEventType(String name){
        EventType eventType = eventTypeRepository.findByName(name);
        if (Objects.isNull(eventType)) {
            eventType = new EventType();
            eventType.setName(name);
            eventTypeRepository.save(eventType);
        }
    }
    private void addCity(String name){
        City city = cityRepository.findByName(name);
        if (Objects.isNull(city)) {
            city = new City();
            city.setName(name);
            cityRepository.save(city);
        }
    }

}