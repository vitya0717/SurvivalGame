package com.vitya017.minigame.listeners;

import com.vitya017.minigame.Main;
import com.vitya017.minigame.arenamanager.Arena;
import com.vitya017.minigame.arenastates.ArenaState;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class WallCheckListener implements Listener {

    private final Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onWallCheck(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location playerLocation = player.getLocation();
        double playerX = playerLocation.getX();
        double playerY = playerLocation.getY();
        double playerZ = playerLocation.getZ();
        Arena arena = plugin.arenaManager.findArenaByPlayer(player.getUniqueId());
        if(arena != null && arena.getState().equals(ArenaState.RUNNING) && player.getGameMode().equals(GameMode.SPECTATOR) && (arena.getJoinedPlayers().contains(player)  || arena.getDiedPlayers().contains(player))) {
            double minX = Math.min(arena.getPos1().getX(), arena.getPos2().getX());
            double maxX = Math.max(arena.getPos1().getX(), arena.getPos2().getX());
            double minY = Math.min(arena.getPos1().getY(), arena.getPos2().getY());
            double maxY = Math.max(arena.getPos1().getY(), arena.getPos2().getY());
            double minZ = Math.min(arena.getPos1().getZ(), arena.getPos2().getZ());
            double maxZ = Math.max(arena.getPos1().getZ(), arena.getPos2().getZ());

            if(playerX >= minX && playerX <= maxX && playerY >= minY && playerY <= maxY && playerZ >= minZ && playerZ <= maxZ) {
                return;
            } else {
                player.teleport(new Location(PlayerKillListener.world,PlayerKillListener.x,PlayerKillListener.y,PlayerKillListener.z));
                player.sendMessage("Kívül vagy..");

            }
        }
    }
}
