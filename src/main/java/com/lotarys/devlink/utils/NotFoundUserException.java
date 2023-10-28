package com.lotarys.devlink.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotFoundUserException extends RuntimeException {
    private String message;
}
