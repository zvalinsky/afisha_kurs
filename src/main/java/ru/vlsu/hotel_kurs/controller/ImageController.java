package ru.vlsu.hotel_kurs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.vlsu.hotel_kurs.entity.Image;
import ru.vlsu.hotel_kurs.repositories.ImageRepository;

import java.util.Optional;

// Пример контроллера для обслуживания изображений (вам нужно адаптировать его к своему коду)
@Controller
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping("/images/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long imageId) {
        Optional<Image> byId = imageRepository.findById(imageId);
        if (byId.isPresent()) {
            byte[] imageBytes = byId.get().getPicture();
                    HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>( HttpStatus.NO_CONTENT);
        }


    }
}
