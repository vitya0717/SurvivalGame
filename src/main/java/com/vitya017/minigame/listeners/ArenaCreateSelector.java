package com.vitya017.minigame.listeners;

import com.vitya017.minigame.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;


public class ArenaCreateSelector implements Listener {

    public static Location pos1;
    public static Location pos2;

    @EventHandler
    public void onArenaSelect(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        EquipmentSlot mainHand = event.getHand();


        if (mainHand != EquipmentSlot.HAND || !event.getPlayer().hasPermission("minigame.admin") || !event.getPlayer().getInventory().getItemInMainHand().isSimilar(Utils.selectorAxe.convertToItemStack()))
            return;

        if (event.getClickedBlock() == null || event.getClickedBlock().getType() == Material.AIR) return;

        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            pos1 = event.getClickedBlock().getLocation();
            Bukkit.getLogger().info("pos1_X: " + pos1.getBlockX());
            Bukkit.getLogger().info("pos1_Y: " + pos1.getBlockY());
            Bukkit.getLogger().info("pos1_Z: " + pos1.getBlockZ());
            player.sendMessage("pos1 selected");
            event.setCancelled(true);
            return;
        }

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            pos2 = event.getClickedBlock().getLocation();
            Bukkit.getLogger().info("pos2_X: " + pos2.getBlockX());
            Bukkit.getLogger().info("pos2_Y: " + pos2.getBlockY());
            Bukkit.getLogger().info("pos2_Z: " + pos2.getBlockZ());
            player.sendMessage("pos2 selected");
            event.setCancelled(true);
        }
    }
}
