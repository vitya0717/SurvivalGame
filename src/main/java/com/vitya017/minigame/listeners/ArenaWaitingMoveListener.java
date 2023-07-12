package com.vitya017.minigame.listeners;

import com.vitya017.minigame.Main;
import com.vitya017.minigame.arenamanager.Arena;
import com.vitya017.minigame.arenastates.ArenaState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ArenaWaitingMoveListener implements Listener {

    private final Main plugin;

    public ArenaWaitingMoveListener(Main plugin) {
        this.plugin=plugin;
    }

    @EventHandler
    public void onPlayerFreezWhileWaiting(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        Arena arena = plugin.arenaManager.findArenaByPlayer(p.getUniqueId());
        if(arena == null) return;
        if(arena.getState() == ArenaState.WAITING_FOR_PLAYERS || arena.getState() == ArenaState.COUNTDOWN && arena.getJoinedPlayers().contains(p)) {
            event.setCancelled(true);
        }
        if(arena.getState() == ArenaState.RUNNING) {
            event.setCancelled(false);
        }
    }
}
