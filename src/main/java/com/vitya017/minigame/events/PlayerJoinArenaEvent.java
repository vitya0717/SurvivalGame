package com.vitya017.minigame.events;

import com.vitya017.minigame.arenamanager.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinArenaEvent extends Event {

    private final Arena arena;
    private final Player player;
    private String joinMessage;

    private static final HandlerList handlers = new HandlerList();
    private String joinOthersMessage;

    public PlayerJoinArenaEvent(Arena arena, Player player, String joinMessage, String joinOthersMessage) {
        this.arena = arena;
        this.player =player;
        setJoinMessage(joinMessage);
        setJoinOthersMessage(joinOthersMessage);
    }

    public Player getPlayer() {
        return player;
    }

    public Arena getArena() {
        return arena;
    }

    public String getJoinMessage() {
        return joinMessage;
    }

    public void setJoinMessage(String joinMessage) {
        this.joinMessage = joinMessage;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public void setJoinOthersMessage(String joinOthersMessage) {
        this.joinOthersMessage = joinOthersMessage;
    }

    public String getJoinOthersMessage() {
        return joinOthersMessage;
    }
}
