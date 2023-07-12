package com.vitya017.minigame.listeners;

import com.vitya017.minigame.Main;
import com.vitya017.minigame.arenamanager.Arena;
import com.vitya017.minigame.arenastates.ArenaState;
import com.vitya017.minigame.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class PlayerKillListener implements Listener {

    private final Main plugin;

    public static World world;
    public static double x;
    public static double y;
    public static double z;

    public PlayerKillListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPvpDisableOnWarmup(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();
        if(player instanceof Player) {
            Arena arena = plugin.arenaManager.findArenaByPlayer(player.getUniqueId());
            if (arena == null) return;
            if (arena.getState() == ArenaState.WARMUP) {
                player.sendMessage("Warmup alatt nincs PVP");
                event.setCancelled(true);
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTakeDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER) return;
        Player player = (Player) event.getEntity();
        Player killer = player.getKiller();
        Location playerLocation = player.getLocation();
        if (player != null && player instanceof Player && killer instanceof Player) {
            Arena arena = plugin.arenaManager.findArenaByPlayer(killer.getUniqueId());

            if (arena == null) return;

            if (!arena.getJoinedPlayers().contains(killer) || !arena.getJoinedPlayers().contains(player)) return;

            if (player.getHealth() - event.getFinalDamage() <= 0) {
                Utils.setPlayer(player);
                Utils.setKiller(killer);
                player.getWorld().strikeLightningEffect(playerLocation);
                world = player.getWorld();
                x = player.getLocation().getX();
                y = player.getLocation().getY();
                z = player.getLocation().getZ();
                player.setFireTicks(0);
                player.setHealth(20);
                player.setGameMode(GameMode.SPECTATOR);
                event.setCancelled(true);
                arena.getJoinedPlayers().remove(player);
                arena.getDiedPlayers().add(player);
                Bukkit.broadcastMessage(Utils.replace("&6SurvivalGames &9► &6%player% &chas killed by &4%killer%"));
                for (ItemStack item : player.getInventory()) {
                    if (item != null) {
                        player.getWorld().dropItemNaturally(player.getLocation(), item);
                    }
                }
                if (arena.getJoinedPlayers().size() == 1) {
                    plugin.arenaManager.end(arena);
                }
            }
        }
    }


}
