package com.vitya017.minigame.listeners;

import com.vitya017.minigame.Main;
import com.vitya017.minigame.arenamanager.Arena;
import com.vitya017.minigame.arenastates.ArenaState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandControllListener implements Listener {
    private Main plugin;
    public CommandControllListener(Main plugin) {
        this.plugin=plugin;
    }

    @EventHandler
    public void onCommnandControll(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Arena arena = plugin.arenaManager.findArenaByPlayer(player.getUniqueId());
        if(arena != null) {
            if((!event.getMessage().contains("/event leave ") && arena.getJoinedPlayers().contains(player)) &&
                    (arena.getState() == ArenaState.RUNNING || arena.getState() == ArenaState.WAITING_FOR_PLAYERS)) {
                player.sendMessage("Játék közben nem használhatsz parancsokat! /event leave "+arena.getArenaName());
                event.setCancelled(true);
            }
        }
    }

}
