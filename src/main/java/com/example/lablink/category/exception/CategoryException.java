package com.example.lablink.category.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryException extends RuntimeException {
    private final CategoryErrorCode errorCode;
}
