package com.vitya017.minigame.utils.itembuild;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;

import java.util.List;
import java.util.Map;

public abstract class Items {

    public abstract Material getMaterial();

    public abstract String getName();

    public abstract ItemBuild setName(String name);

    public abstract int getAmount();

    public abstract ItemBuild setAmount(int amount);

    public abstract ItemBuild setEnchant(Enchantment enchant, int i);

    public abstract Map<Enchantment, Integer> getEnchants();

    public abstract ItemBuild removeEnchant(Enchantment enchant);

    public abstract ItemBuild setLore(List<String> lores);

    public abstract String getLores();

    public abstract String getLore(int index);

    public abstract ItemBuild setFlag(Flag flag);

    public abstract int getMaxStack();

    public abstract ItemBuild setMaxStack(int amount);

    public abstract boolean isUnbreakable();

    public abstract ItemBuild setUnbreakable(boolean bool);

    public abstract ItemBuild setSkullOwner(OfflinePlayer owner);
}
