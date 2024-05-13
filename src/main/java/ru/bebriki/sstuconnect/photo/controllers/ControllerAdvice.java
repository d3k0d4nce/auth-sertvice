package ru.bebriki.sstuconnect.photo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.bebriki.sstuconnect.photo.dtos.ExceptionBody;
import ru.bebriki.sstuconnect.photo.exceptions.ImageDownloadException;
import ru.bebriki.sstuconnect.photo.exceptions.ImageUploadException;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = {ImageUploadException.class})
    public ResponseEntity<Object> handleImageUploadException(ImageUploadException imageUploadException) {

        ExceptionBody imageException = new ExceptionBody(
                imageUploadException.getMessage(),
                imageUploadException.getCause(),
                HttpStatus.BAD_REQUEST
        );

        return new ResponseEntity<>(imageException, imageException.getHttpStatus());

    }

    @ExceptionHandler(value = {ImageDownloadException.class})
    public ResponseEntity<Object> handleImageUploadException(ImageDownloadException imageDownloadException) {

        ExceptionBody imageException = new ExceptionBody(
                imageDownloadException.getMessage(),
                imageDownloadException.getCause(),
                HttpStatus.BAD_REQUEST
        );

        return new ResponseEntity<>(imageException, imageException.getHttpStatus());

    }

}
