package com.vitya017.minigame.listeners;

import com.vitya017.minigame.Main;
import com.vitya017.minigame.lootchest.LootChest;
import com.vitya017.minigame.utils.Utils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class ChestModeListener implements Listener {

    public Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onPlaceChest(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block placedBlock = event.getBlockPlaced();
        ItemStack chest = player.getInventory().getItemInMainHand();
        if(Utils.chest != null && chest.getItemMeta().getDisplayName().equals(Utils.chest.getItemMeta().getDisplayName())) {
            String arenaName = chest.getItemMeta().getDisplayName();
            int lootIndex = plugin.arenaManager.getCurrentArena(arenaName).getLootChests().size();
            if (plugin.arenaManager.getCurrentArena(arenaName).isChestMode() && plugin.arenaManager.getCurrentArena(arenaName) != null) {
                Directional blockFace = (Directional) placedBlock.getBlockData();
                World world = placedBlock.getWorld();
                double x = placedBlock.getLocation().getX();
                double y = placedBlock.getLocation().getY();
                double z = placedBlock.getLocation().getZ();
                Location loc = new Location(world, x, y, z);
                LootChest lootChest = new LootChest(Integer.toString(lootIndex), plugin.arenaManager.getCurrentArena(arenaName), loc, blockFace.getFacing().name());
                player.sendMessage("§a[ChestMode] §eLáda letéve a következő pozícióra: ");
                player.sendMessage("§7" + "X:"+x+", Y:"+y+", Z:"+ z);
                plugin.arenaManager.getCurrentArena(arenaName).getLootChests().put(lootIndex, lootChest);
                plugin.arenaManager.saveLootChest(plugin.arenaManager.getCurrentArena(arenaName), lootChest.getDisplay(), world, loc, blockFace);
                lootIndex++;
            }
        }
    }

}
