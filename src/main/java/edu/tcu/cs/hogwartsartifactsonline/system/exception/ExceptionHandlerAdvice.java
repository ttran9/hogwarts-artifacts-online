package edu.tcu.cs.hogwartsartifactsonline.system.exception;

import edu.tcu.cs.hogwartsartifactsonline.system.Result;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    /**
     * The below method will assist Spring with handling the ArtifactNotFoundException
     * and will wrap the relevant information into a custom POJO and return it.
     * @param ex The exception to be caught from spring IoC and handled
     * @return A JSON object that has been serialized and returned to the user.
     */
//    @ExceptionHandler(ArtifactNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND) // in HTTP header
//    Result handleArtifactNotFoundException(ArtifactNotFoundException ex) {
        // no need to send payload so it is null so we use a 3 argument constructor.
        // in response body.
//        return new Result(false, StatusCode.NOT_FOUND.getHttpStatusCodeValue(), ex.getMessage());
//    }

//    @ExceptionHandler(WizardNotFoundException.class)
//    Result handleWizardNotFoundException(WizardNotFoundException ex) {
//        return new Result(false, StatusCode.NOT_FOUND.getHttpStatusCodeValue(), ex.getMessage());
//    }

//    @ExceptionHandler({ArtifactNotFoundException.class, WizardNotFoundException.class})
    @ExceptionHandler(ObjectNotFoundException.class)
    Result handleObjectNotFoundException(ObjectNotFoundException ex) {
        return new Result(false, StatusCode.NOT_FOUND.getHttpStatusCodeValue(), ex.getMessage());
    }
//
//    @ExceptionHandler(WizardBadRequestException.class)
//    Result handleWizardBadRequestException(WizardBadRequestException ex) {
//        return generateErrorMap(null);
//    }

    /**
     * This handles invalid inputs.
     * @param ex The exception that has been caught and will be handled.
     * @return A wrapper object with detailed error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleValidationException(MethodArgumentNotValidException ex) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        Map<String, String> map = new HashMap<>(errors.size());
        errors.forEach((error) -> {
            String key = ((FieldError) error).getField();
            String val = error.getDefaultMessage();
            map.put(key, val);
        });
        return new Result(false, StatusCode.INVALID_ARGUMENT.getHttpStatusCodeValue(), "Provided arguments are invalid, see data for details.", map);
//        return generateErrorMap(errors);
    }

    // unused for now.
//    private Result generateErrorMap(List<ObjectError> errors) {
//        Map<String, String> map = new HashMap<>(errors.size());
//        errors.forEach((error) -> {
//            String key = ((FieldError) error).getField();
//            String val = error.getDefaultMessage();
//            map.put(key, val);
//        });
//        return new Result(false, StatusCode.INVALID_ARGUMENT.getHttpStatusCodeValue(), "Provided arguments are invalid, see data for details.", map);
//    }
}
