package com.vitya017.minigame.lootchest;

import com.vitya017.minigame.arenamanager.Arena;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LootChest {

    private String display;
    private Arena arena;
    private Location location;

    private BlockFace chestFacing;
    private List<ItemStack> items = new ArrayList<>();

    public LootChest(String display, Arena arena, Location location, String chestFacing) {
        this.display = display;
        this.arena = arena;
        this.location = location;
        this.chestFacing = BlockFace.valueOf(chestFacing);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public void spawn() {
        location.getBlock().setType(Material.CHEST);
        Chest lc = (Chest) location.getBlock().getState();
        Inventory chestInv = lc.getInventory();
        for (ItemStack item : items) {
            if (item != null) {
                chestInv.addItem(item);
            }
        }
    }

    @Override
    public String toString() {
        return "LootChest{" +
                "display='" + display + '\'' +
                ", arena=" + arena +
                ", location=" + location +
                ", items=" + items +
                '}';
    }

    public BlockFace getChestFacing() {
        return chestFacing;
    }

    public void setChestFacing(BlockFace chestFacing) {
        this.chestFacing = chestFacing;
    }
}
