package com.fitness_monolith.fitness.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 🔴 Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException ex
    ) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        ApiError error = new ApiError(errorMessage, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 🔴 Handle runtime exceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(
            RuntimeException ex
    ) {
        ApiError error = new ApiError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}


/*
🌍 @RestControllerAdvice — QUICK REVISION NOTES

👉 Used for GLOBAL exception handling
👉 Works for ALL @RestController classes
👉 Returns JSON response automatically

🧠 Meaning:
"If any error happens in any controller, handle it in ONE common place."

🔁 Why use it?
✔ Avoid try-catch in every controller
✔ Clean & readable code
✔ Same error format everywhere

🧩 Combines:
@ControllerAdvice + @ResponseBody

🛑 Handles:
✔ Validation errors (@Valid)
✔ Runtime exceptions
✔ 400 / 403 / 404 errors

🧪 Flow:
Bad request ❌ → Exception → Advice → JSON response ✅

🎯 One-liner:
"Centralized exception handling for REST APIs."

⭐ Use it in all production REST apps!
*/

