package ru.bebriki.sstuconnect.photo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ExceptionBody {

    private final String message;

    private final Throwable throwable;

    private final HttpStatus httpStatus;

}
