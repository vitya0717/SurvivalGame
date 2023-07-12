package com.vitya017.minigame.listeners;

import com.vitya017.minigame.Main;
import com.vitya017.minigame.arenamanager.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class InventoryResetListener implements Listener {

    private static final Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onInventoryResetAfterLeft(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Arena arena = plugin.arenaManager.findArenaByPlayer(player.getUniqueId());
        if(arena != null) {
            plugin.arenaManager.restorePlayerInventory(player.getUniqueId());
            plugin.arenaManager.leaveArena(player.getUniqueId(), arena.getArenaName());
        }
    }

}
