package org.home.todo.util;

import org.home.todo.util.exceptions.EntityRejectedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

@Component
public class CheckUtil {

    public void checkForValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorsAccumulator = new StringBuilder();
            bindingResult.getFieldErrors()
                    .forEach(error -> errorsAccumulator.append(error.getField())
                            .append(" - ")
                            .append(error.getDefaultMessage())
                            .append(";"));
            throw new EntityRejectedException(errorsAccumulator.toString());
        }
    }

}
