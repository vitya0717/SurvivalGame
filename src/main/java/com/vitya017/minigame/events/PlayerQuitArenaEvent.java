package com.vitya017.minigame.events;

import com.vitya017.minigame.arenamanager.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerQuitArenaEvent extends Event {

    private final Arena arena;
    private final Player player;
    private String quitMessage;
    private String quitOthersMessage;

    private static final HandlerList handlers = new HandlerList();

    public PlayerQuitArenaEvent(Arena arena, Player player, String quitMessage, String quitOthersMessage) {
        this.arena = arena;
        this.player = player;
        setQuitOthersMessage(quitOthersMessage);
        setQuitMessage(quitMessage);
    }

    public Player getPlayer() {
        return player;
    }

    public Arena getArena() {
        return arena;
    }

    public String getQuitMessage() {
        return quitMessage;
    }

    public void setQuitMessage(String quitMessage) {
        this.quitMessage = quitMessage;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getQuitOthersMessage() {
        return quitOthersMessage;
    }

    public void setQuitOthersMessage(String quitOthersMessage) {
        this.quitOthersMessage = quitOthersMessage;
    }
}