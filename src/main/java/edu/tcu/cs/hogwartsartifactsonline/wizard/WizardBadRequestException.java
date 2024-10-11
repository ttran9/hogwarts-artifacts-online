package edu.tcu.cs.hogwartsartifactsonline.wizard;

import java.util.Map;

public class WizardBadRequestException extends RuntimeException {

    private Map<String, String> errors;
    public WizardBadRequestException() {
    }
}
