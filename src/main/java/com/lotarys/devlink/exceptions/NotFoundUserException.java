package com.lotarys.devlink.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotFoundUserException extends RuntimeException {
    private String message;
}
