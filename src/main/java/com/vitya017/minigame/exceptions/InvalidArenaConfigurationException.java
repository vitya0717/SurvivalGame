package com.vitya017.minigame.exceptions;

public class InvalidArenaConfigurationException extends RuntimeException {

    private final String invalidArena;

    public InvalidArenaConfigurationException(String invalidArena) {
        this.invalidArena = invalidArena;
    }

    @Override
    public String getMessage() {
        return "Your "+invalidArena+"'s configuration is invalid!";
    }
}
