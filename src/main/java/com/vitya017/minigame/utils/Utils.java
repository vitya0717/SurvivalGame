package com.vitya017.minigame.utils;

import com.vitya017.minigame.Main;
import com.vitya017.minigame.arenamanager.Arena;
import com.vitya017.minigame.utils.itembuild.Flag;
import com.vitya017.minigame.utils.itembuild.ItemBuild;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Utils {

    private static Player killer;
    private static Player winner;
    public static Main plugin = Main.getPlugin(Main.class);
    private static Player player;
    private static Arena arena;
    private static String arenaName = null;
    private static final Random random = new Random();
    private static int spawnCount = -1;

    public static double randomChanceNumber;

    public static ItemStack chest = null;

    public static Object randomChoice(List<?> list) {
        return list.get(random.nextInt(list.size()));
    }

    public static ItemBuild selectorAxe;

    public static String replace(String n) {
        n = ChatColor.translateAlternateColorCodes('&', n);
        n = n.replace("%prefix%", ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("MessageConfiguration.PluginPrefix")));
        if(player != null) {
            n = n.replace("%player%", player.getName());
        }
        if(killer != null) {
            n = n.replace("%killer%", killer.getName());
        }
        if(arenaName != null) {
            n = n.replace("%arena%", arenaName);
        }
        if(arena != null) {
            n = n.replace("%arena%", arena.getArenaName());
            n = n.replace("%min-player%", String.valueOf(arena.getMinPlayers()));
            n = n.replace("%max-player%", String.valueOf(arena.getMaxPlayers()));
            if(arena.getArenaCountdown() != null) {
                n = n.replace("%countdown-seconds%", String.valueOf(arena.getArenaCountdown().getTime()));
            }
            if(spawnCount != -1) {
                n = n.replace("%spawncount%", String.valueOf(spawnCount));
            }

            if(winner != null && arena.getJoinedPlayers().size() == 1) {
                n = n.replace("%winner%", arena.getJoinedPlayers().get(0).getName());
            }
            if(arena.getArenaTimer() != null) {
                n = n.replace("%arena-time%", String.valueOf(arena.getArenaTimer().getTime()));
            } else {
                n = n.replace("%arena-time%", String.valueOf(300));
            }
        }

        return n;
    }

    public static List<String> replace(List<String> n) {
        n = n.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
        n = n.stream().map(s -> s.replace("%prefix%", ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("MessageConfiguration.PluginPrefix")))).collect(Collectors.toList());
        if(player != null) {
            n = n.stream().map(s-> s.replace("%player%", player.getName())).collect(Collectors.toList());
        }
        if(killer != null) {
            n = n.stream().map(s-> s.replace("%killer%", killer.getName())).collect(Collectors.toList());
        }
        if(arena != null) {
            n = n.stream().map(s-> s.replace("%arena%", arena.getArenaName())).collect(Collectors.toList());
            if(arena.getArenaCountdown() != null) {
                n = n.stream().map(s-> s.replace("%countdown-seconds%", String.valueOf(arena.getArenaCountdown().getTime()))).collect(Collectors.toList());
            }
            if(winner != null && arena.getJoinedPlayers().size() == 1) {
                n = n.stream().map(s-> s.replace("%winner%", arena.getJoinedPlayers().get(0).getName())).collect(Collectors.toList());
            }
            if(arena.getArenaTimer() != null) {
                n = n.stream().map(s-> s.replace("%arena-time%", String.valueOf(arena.getArenaTimer().getTime()))).collect(Collectors.toList());
            } else {
                n = n.stream().map(s-> s.replace("%arena-time%", String.valueOf(300))).collect(Collectors.toList());
            }
        }
        return n;
    }


    public static void loadPluginItems()
    {
        selectorAxe = new ItemBuild(Material.GOLDEN_AXE, 1);
        selectorAxe.setEnchant(Enchantment.LUCK, 1000);
        selectorAxe.setName(replace("&6ArenaSelector"));
        selectorAxe.setLore(Utils.replace(Arrays.asList("&aLeftClick -  pos1 select","&aRightClick -  pos2 select", "&dAfter that &c/sd create <arena-name>")));
        selectorAxe.setFlag(Flag.HIDE_ALL_FLAG);
    }

    public static boolean chanceCalculator(double param) {
        Bukkit.getConsoleSender().sendMessage(String.valueOf(randomChanceNumber));
        return randomChanceNumber <= param;
    }

    public static List<ItemStack> possibleItems = Arrays.asList(
            new ItemStack(Material.WOODEN_AXE),
            new ItemStack(Material.WOODEN_SWORD),
            new ItemStack(Material.STONE_AXE),
            new ItemStack(Material.STONE_SWORD),
            new ItemStack(Material.IRON_AXE),
            new ItemStack(Material.IRON_SWORD),
            new ItemStack(Material.IRON_HELMET),
            new ItemStack(Material.IRON_CHESTPLATE),
            new ItemStack(Material.IRON_LEGGINGS),
            new ItemStack(Material.IRON_BOOTS),
            new ItemStack(Material.DIAMOND_AXE),
            new ItemStack(Material.DIAMOND_SWORD),
            new ItemStack(Material.DIAMOND_HELMET),
            new ItemStack(Material.DIAMOND_CHESTPLATE),
            new ItemStack(Material.DIAMOND_LEGGINGS),
            new ItemStack(Material.DIAMOND_BOOTS),
            new ItemStack(Material.GOLDEN_AXE),
            new ItemStack(Material.GOLDEN_SWORD),
            new ItemStack(Material.GOLDEN_HELMET),
            new ItemStack(Material.GOLDEN_CHESTPLATE),
            new ItemStack(Material.GOLDEN_LEGGINGS),
            new ItemStack(Material.GOLDEN_BOOTS),
            new ItemStack(Material.CHAINMAIL_HELMET),
            new ItemStack(Material.CHAINMAIL_CHESTPLATE),
            new ItemStack(Material.CHAINMAIL_LEGGINGS),
            new ItemStack(Material.CHAINMAIL_BOOTS),
            new ItemStack(Material.NETHERITE_AXE),
            new ItemStack(Material.NETHERITE_SWORD),
            new ItemStack(Material.NETHERITE_HELMET),
            new ItemStack(Material.NETHERITE_CHESTPLATE),
            new ItemStack(Material.NETHERITE_LEGGINGS),
            new ItemStack(Material.NETHERITE_BOOTS),
            new ItemStack(Material.TURTLE_HELMET),
            new ItemStack(Material.BOW),
            new ItemStack(Material.ARROW, random.nextInt(33)),
            new ItemStack(Material.LEATHER_HELMET),
            new ItemStack(Material.LEATHER_CHESTPLATE),
            new ItemStack(Material.LEATHER_LEGGINGS),
            new ItemStack(Material.LEATHER_BOOTS),
            new ItemStack(Material.CROSSBOW),
            new ItemStack(Material.TOTEM_OF_UNDYING, random.nextInt(3)),
            new ItemStack(Material.SHIELD),
            new ItemStack(Material.SNOWBALL,random.nextInt(65)),
            new ItemStack(Material.TRIDENT),
            new ItemStack(Material.GOLDEN_APPLE, random.nextInt(16)),
            new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, random.nextInt(5)),
            new ItemStack(Material.COOKED_BEEF, random.nextInt(65)),
            new ItemStack(Material.COOKED_CHICKEN, random.nextInt(65)),
            new ItemStack(Material.COOKED_COD, random.nextInt(65)),
            new ItemStack(Material.COOKED_MUTTON, random.nextInt(65)),
            new ItemStack(Material.COOKED_PORKCHOP, random.nextInt(65)),
            new ItemStack(Material.COOKED_RABBIT, random.nextInt(65)),
            new ItemStack(Material.COOKED_SALMON, random.nextInt(65)),
            new ItemStack(Material.CARROT, random.nextInt(65)),
            new ItemStack(Material.BAKED_POTATO, random.nextInt(65)),
            new ItemStack(Material.BREAD, random.nextInt(65)),
            new ItemStack(Material.GOLDEN_CARROT, random.nextInt(65)),
            new ItemStack(Material.MUSHROOM_STEW, random.nextInt(65)),
            new ItemStack(Material.RABBIT_STEW, random.nextInt(65))
    );


    public static void setPlayer(Player player) {
        Utils.player = player;
    }

    public static Player getPlayer() {
        return player;
    }

    public static void setArena(Arena arena) {
        Utils.arena = arena;
    }

    public static void setKiller(Player killer) {
        Utils.killer = killer;
    }

    public static void setWinner(Player winner) {
        Utils.winner = winner;
    }

    public static void setArenaName(String arenaName) {
        Utils.arenaName = arenaName;
    }

    public static void setSpawnCount(int spawnCount) {
        Utils.spawnCount = spawnCount;
    }
}
