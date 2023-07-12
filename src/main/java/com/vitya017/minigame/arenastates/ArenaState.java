package com.vitya017.minigame.arenastates;

public enum ArenaState {
    COUNTDOWN, DISABLED, RUNNING, NOT_RUNNING, WAITING_FOR_PLAYERS, WARMUP;

    private ArenaState state;

    public void setState(ArenaState state) {
        this.state = state;
    }

    public ArenaState getState() {
        return state;
    }
}
