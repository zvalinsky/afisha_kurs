package ru.vlsu.hotel_kurs.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vlsu.hotel_kurs.entity.User;
import ru.vlsu.hotel_kurs.services.UserDetailsServiceImpl;

@Controller()
public class AuthController {

    private final UserDetailsServiceImpl userDetailsService;

    public AuthController(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/registration")
    public String registrationPage() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               Model model) {
        // Проверка на совпадение паролей
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "registration";
        }

        // Попытка зарегистрировать пользователя
        try {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setEnabled(true);  // По умолчанию активирован

            userDetailsService.registerUser(newUser);

            return "redirect:/login"; // Перенаправление на страницу входа после успешной регистрации
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed. Please try again.");
            return "registration";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(Model model) {
        // Получение текущего аутентифицированного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Добавление информации в модель для передачи на страницу
        model.addAttribute("username", username);

        return "redirect:/home"; // Перенаправление после успешной аутентификации
    }
}
