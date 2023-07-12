package com.vitya017.minigame.utils.itembuild;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemBuild extends Items {

    private final ItemStack itemStack;
    private final Material material;
    private int amount;
    private ItemMeta itemMeta = null;

    public ItemBuild(Material material, int amount) {
        this.amount = amount;
        this.material = material;
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public String getName() {
        String display = null;
        ItemMeta meta = itemStack.getItemMeta();
        display = meta.getDisplayName();
        itemStack.setItemMeta(meta);
        return display;
    }

    @Override
    public ItemBuild setName(String name) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(Colorize(name));
        itemStack.setItemMeta(meta);
        return this;
    }

    private String Colorize(String name) {
        return ChatColor.translateAlternateColorCodes('&', name);
    }

    private List<String> Colorize(List<String> lores) {
        return lores.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
    }


    @Override
    public int getAmount() {
        return itemStack.getAmount();
    }

    @Override
    public ItemBuild setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    @Override
    public ItemBuild setEnchant(Enchantment enchant, int level) {
        itemStack.addUnsafeEnchantment(enchant, level);
        return this;
    }

    @Override
    public Map<Enchantment, Integer> getEnchants() {
        Map<Enchantment, Integer> enchantmentIntegerMap = null;
        ItemMeta meta = itemStack.getItemMeta();
        enchantmentIntegerMap = meta.getEnchants();
        meta.getEnchants();
        return enchantmentIntegerMap;
    }

    @Override
    public ItemBuild removeEnchant(Enchantment enchant) {
        itemStack.removeEnchantment(enchant);
        return this;
    }

    @Override
    public ItemBuild setLore(List<String> lores) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(Colorize(lores));
        itemStack.setItemMeta(meta);
        return this;
    }

    @Override
    public String getLores() {
        ItemMeta meta = itemStack.getItemMeta();
        String lore = null;
        for (int i = 0; i < meta.getLore().size(); i++) {
            lore = meta.getLore().get(i);
        }
        itemStack.setItemMeta(meta);
        return lore;
    }

    @Override
    public String getLore(int index) {
        ItemMeta meta = itemStack.getItemMeta();
        String lore = null;
        for (int i = 0; i < meta.getLore().size(); i++) {
            if (index > -1 && index <= meta.getLore().size() - 1) {
                lore = meta.getLore().get(index);
            } else {
                System.out.println("§cThe index bigger than -1 but smaller/equal than the lore last index!");
                break;
            }
        }
        itemStack.setItemMeta(meta);
        return lore;
    }


    @Override
    public ItemBuild setFlag(Flag flag) {

        switch (flag) {

            case HIDE_ENCHANTS:
                itemMeta = itemStack.getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                itemStack.setItemMeta(itemMeta);
                break;
            case HIDE_ATTRIBUTES:
                itemMeta = itemStack.getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                itemStack.setItemMeta(itemMeta);
                break;
            case HIDE_DESTROYS:
                itemMeta = itemStack.getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
                itemStack.setItemMeta(itemMeta);
                break;
            case HIDE_PLACED_ON:
                itemMeta = itemStack.getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
                itemStack.setItemMeta(itemMeta);
                break;
            case HIDE_POTION_EFFECTS:
                itemMeta = itemStack.getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                itemStack.setItemMeta(itemMeta);
                break;
            case HIDE_UNBREAKABLE:
                itemMeta = itemStack.getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                itemStack.setItemMeta(itemMeta);
                break;
            case HIDE_ALL_FLAG:
                itemMeta = itemStack.getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
                itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
                itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                itemStack.setItemMeta(itemMeta);
                break;

            default:
                break;
        }

        return this;
    }

    @Override
    public int getMaxStack() {
        return itemStack.getMaxStackSize();
    }

    @Override
    public ItemBuild setMaxStack(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    @Override
    public boolean isUnbreakable() {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta.isUnbreakable()) {
            itemStack.setItemMeta(meta);
            return true;
        }
        return false;
    }

    @Override
    public ItemBuild setUnbreakable(boolean bool) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setUnbreakable(bool);
        itemStack.setItemMeta(meta);
        return this;
    }

    @Override
    public ItemBuild setSkullOwner(OfflinePlayer owner) {
        try {
            SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
            meta.setOwningPlayer(owner);
            itemStack.setItemMeta(meta);
        } catch (ClassCastException e) {
            e.getStackTrace();
        }
        return this;
    }

    public ItemStack convertToItemStack() {
        return itemStack;
    }

    public Material convertToMaterial() {
        return material;
    }
}
