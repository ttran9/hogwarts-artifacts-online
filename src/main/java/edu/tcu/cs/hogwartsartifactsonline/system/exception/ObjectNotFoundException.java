package edu.tcu.cs.hogwartsartifactsonline.system.exception;

public class ObjectNotFoundException extends RuntimeException {
    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public ObjectNotFoundException(String objectName, String id) {
        super(String.format("Could not find %s with Id %s :(", objectName, id));
    }

    public ObjectNotFoundException(String objectName, Integer id) {
        super(String.format("Could not find %s with Id %s :(", objectName, id));
    }
}