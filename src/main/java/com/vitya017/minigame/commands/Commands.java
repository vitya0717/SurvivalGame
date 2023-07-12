package com.vitya017.minigame.commands;

import com.vitya017.minigame.Main;
import com.vitya017.minigame.arenamanager.Arena;
import com.vitya017.minigame.arenastates.ArenaState;
import com.vitya017.minigame.listeners.ArenaCreateSelector;
import com.vitya017.minigame.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.vitya017.minigame.utils.Utils.chanceCalculator;
import static com.vitya017.minigame.utils.Utils.chest;


public class Commands implements CommandExecutor {

    private final Main plugin = Main.getPlugin(Main.class);
    int minPlayers = 0;
    int maxPlayers = 0;

    List<Location> locations = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            /*Utils.setRandomChanceNumber(plugin.random.nextDouble() * 99.9 + 0.1);
            //50%
            ItemStack kenyer = new ItemStack(Material.BREAD, 1);
            //45%
            ItemStack vas = new ItemStack(Material.IRON_INGOT, 1);
            //5%
            ItemStack gyemant = new ItemStack(Material.DIAMOND, 1);
            HashMap<Integer, ItemStack> targyak = new HashMap<>();

            targyak.put(99, kenyer);
            targyak.put(10, vas);
            targyak.put(1, gyemant);

            List<Integer> keys = new ArrayList<>(targyak.keySet());
            Collections.sort(keys);

            for (Integer key : keys) {
                Bukkit.getConsoleSender().sendMessage(key.toString());
            }

            ItemStack dropped = null;
            /* *
             *
             * HA NINCS BENNA AZ INTERVALLUMBAN AKKOR A legrosszabbat adja oda
             * 100-as tesztet akarok generálni
             *
             *
            if (Utils.getRandomChanceNumber() > Collections.max(keys)) {
                dropped = targyak.get(Collections.max(keys));
                sender.sendMessage(Utils.getRandomChanceNumber() + "");
                sender.sendMessage("Max:" + Collections.max(keys));
                sender.sendMessage(dropped.getType().name());
            }
            for (Integer key : keys) {
                if (chanceCalculator(key)) {
                    dropped = targyak.get(key);
                    sender.sendMessage(Utils.getRandomChanceNumber() + "");
                    sender.sendMessage("Max:" + Collections.max(keys));
                    sender.sendMessage(dropped.getType().name());
                    break;
                }
            }
            Player player = (Player) sender;
            if (dropped != null) {
                player.getInventory().addItem(dropped);
            }*/
            sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.HelpMessage")));
            return true;
        }
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("help")) {
                if (sender.hasPermission("domination.use")) {
                    LinkedHashMap<String, String> help = new LinkedHashMap<String, String>() {{
                        put("0;domination.use", "§a/sd help - Helps about the plugin");
                        put("1;domination.use", "§a/sd join <arena> - Join the arena");
                        put("2;domination.use", "§a/sd leave <arena> - Quit the arena");
                        put("3;domination.use", "§a/sd info <arena> - Information about an arena");
                        put("4;domination.use", "§a/sd list - List of arenas");
                        put("5;domination.admin", "§fAdmin commands:");
                        put("6;domination.admin", "§c/sd create <name> <min-player> <max-player> - Create arena");
                        put("7;domination.admin", "§c/sd edit <arena> - Ingame arena editor GUI");
                        put("8;domination.admin", "§c/sd remove <arena> - Delete arena");
                        put("9;domination.admin", "§c/sd setlobby <arena> - Place lobby location for your arena");
                        put("10;domination.admin", "§c/sd setspawn <arena> - Place spawnpoints on your position");
                        put("11;domination.admin", "§c/sd chestmode <arena> - In chestmode, you can place loot chests for arenas.");
                        put("12;domination.admin", "§c/sd selector - Arena selector axe");
                    }};
                    for (Map.Entry<String, String> line : help.entrySet()) {
                        if (sender.hasPermission(line.getKey().split(";")[1])) {
                            sender.sendMessage(line.getValue());
                        }
                    }
                    return true;
                }
                sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.InsufficientPermission")));
                return true;
            }
            if (args[0].equalsIgnoreCase("create")) {
                if (sender.hasPermission("domination.admin")) {
                    if (ArenaCreateSelector.pos1 == null && ArenaCreateSelector.pos2 == null) {
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.BeforeCreateArenaSelectTheArea")));
                        return true;
                    }
                    locations.add(ArenaCreateSelector.pos1);
                    locations.add(ArenaCreateSelector.pos2);

                    if (args.length == 1) {
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.UsageMessageArenaName")));
                        return true;
                    }
                    String arenaName = args[1];
                    Utils.setArenaName(arenaName);

                    if (args.length == 2) {
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.UsageMessageArenaMinPlayers")));
                        return true;
                    }
                    minPlayers = Integer.parseInt(args[2]);

                    if (minPlayers <= 1 || minPlayers >= 64) {
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.AtLeastTwoMinPlayer")));
                        return true;
                    }


                    if (args.length == 3) {
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.UsageMessageArenaMaxPlayers")));
                        return true;
                    }
                    if (maxPlayers > 64 && maxPlayers < minPlayers) {
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.MaximumPlayersInAnArena")));
                        return true;
                    }
                    maxPlayers = Integer.parseInt(args[3]);

                    if (plugin.arenaManager.getCurrentArena(arenaName) == null || !plugin.arenaManager.arenaList.contains(plugin.arenaManager.getCurrentArena(arenaName))) {
                        Utils.setArena(plugin.arenaManager.getCurrentArena(arenaName));
                        plugin.arenaManager.createArena(arenaName, locations, minPlayers, maxPlayers);
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.ArenaCreate")));
                        locations.clear();
                        ArenaCreateSelector.pos1 = null;
                        ArenaCreateSelector.pos2 = null;
                        return true;
                    }
                    sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.ArenaAlreadyExists")));
                    return true;
                }
                sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.InsufficientPermission")));
                return true;
            }
            if (args[0].equalsIgnoreCase("remove")) {
                if (sender.hasPermission("domination.admin")) {
                    if (args.length == 1) {
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.UsageMessageArenaRemove")));
                        return true;
                    }
                    String arenaName = args[1];
                    Utils.setArenaName(arenaName);
                    if (plugin.arenaManager.getCurrentArena(arenaName) != null || plugin.arenaManager.arenaList.contains(plugin.arenaManager.getCurrentArena(arenaName))) {
                        Utils.setArena(plugin.arenaManager.getCurrentArena(arenaName));
                        plugin.arenaManager.removeArena(arenaName);
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.ArenaRemove")));
                        return true;
                    } else {
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.ArenaNotFound")));
                    }
                    return true;
                }
                sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.InsufficientPermission")));
                return true;

            }
            if (args[0].equalsIgnoreCase("setlobby")) {
                if (sender.hasPermission("domination.admin")) {
                    if (args.length == 1) {
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.UsageMessageSetLobby")));
                        return true;
                    }
                    if (plugin.arenaManager.getCurrentArena(args[1]) != null) {
                        plugin.arenaManager.getCurrentArena(args[1]).setLobbyLocation(((Player) sender).getLocation());
                        Utils.setArena(plugin.arenaManager.getCurrentArena(args[1]));
                        plugin.arenaManager.saveArena(plugin.arenaManager.getCurrentArena(args[1]));
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.LobbyLocationSet")));
                    } else {
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.ArenaNotFound")));
                    }
                    return true;
                }
                sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.InsufficientPermission")));
                return true;
            }

            if (args[0].equalsIgnoreCase("setspawn")) {
                if (sender.hasPermission("domination.admin")) {
                    if (args.length == 1) {
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.UsageMessageSetSpawn")));
                        return true;
                    }
                    if (plugin.arenaManager.getCurrentArena(args[1]) != null) {
                        Utils.setArena(plugin.arenaManager.getCurrentArena(args[1]));
                        plugin.arenaManager.getCurrentArena(args[1]).getSpawnLocations().add(((Player) sender).getLocation());
                        plugin.arenaManager.saveArena(plugin.arenaManager.getCurrentArena(args[1]));
                        Utils.setSpawnCount(plugin.arenaManager.getCurrentArena(args[1]).getSpawnLocations().size());
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.SpawnLocationSet")));
                    } else {
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.ArenaNotFound")));
                    }
                    return true;
                }
                sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.InsufficientPermission")));
                return true;
            }

            if (args[0].equalsIgnoreCase("info")) {
                if (sender.hasPermission("domination.use")) {

                    if (args.length == 1) {
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.UsageMessageArenaInfo")));
                        return true;
                    }
                    sender.sendMessage("Arena Info:");
                    if (plugin.arenaManager.getCurrentArena(args[1]) != null) {
                        Utils.setArena(plugin.arenaManager.getCurrentArena(args[1]));
                        sender.sendMessage("§eName: §a" + plugin.arenaManager.getCurrentArena(args[1]).getArenaName());
                        sender.sendMessage("§eminPlayers: §a" + plugin.arenaManager.getCurrentArena(args[1]).getMinPlayers());
                        sender.sendMessage("§emaxPlayers: §a" + plugin.arenaManager.getCurrentArena(args[1]).getMaxPlayers());
                        sender.sendMessage("§ePlayableArena: " + (plugin.arenaManager.getCurrentArena(args[1]).isPlayableArena() ? "§aYes" : "§cNo"));
                    } else {
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.ArenaNotFound")));
                    }
                    return true;
                }
                sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.InsufficientPermission")));
                return true;
            }
            if (args[0].equalsIgnoreCase("join")) {
                if (sender.hasPermission("domination.use")) {
                    if (args.length == 1) {
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.UsageMessageJoinArena")));
                        return true;
                    }
                    if (plugin.arenaManager.getCurrentArena(args[1]) != null) {
                        Arena ar = plugin.arenaManager.getCurrentArena(args[1]);
                        if(ar.getState() == ArenaState.NOT_RUNNING || ar.getState() == ArenaState.WAITING_FOR_PLAYERS || ar.getState() == ArenaState.COUNTDOWN) {
                            Utils.setArena(plugin.arenaManager.getCurrentArena(args[1]));
                            plugin.arenaManager.joinArena(((Player) sender).getUniqueId(), args[1]);
                        } else {
                            sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.ArenaCurrentlyNotPlayableOrRunning")));
                        }
                        return true;
                    } else {
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.ArenaNotFound")));
                    }
                    return true;
                }
                sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.InsufficientPermission")));
                return true;
            }

            if (args[0].equalsIgnoreCase("leave")) {
                if (sender.hasPermission("domination.use")) {
                    if (args.length == 1) {
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.UsageMessageLeaveArena")));
                        return true;
                    }
                    if (plugin.arenaManager.getCurrentArena(args[1]) != null) {
                        if (plugin.arenaManager.getCurrentArena(args[1]).getJoinedPlayers().contains((Player) sender)) {
                            Utils.setArena(plugin.arenaManager.getCurrentArena(args[1]));
                            plugin.arenaManager.leaveArena(((Player) sender).getUniqueId(), args[1]);
                        } else {
                            sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.NotInArena")));
                        }
                    } else {
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.ArenaNotFound")));
                    }
                    return true;
                }
                sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.InsufficientPermission")));
                return true;

            }

            if (args[0].equalsIgnoreCase("list")) {
                if (sender.hasPermission("domination.use")) {

                    Player player = (Player) sender;
                    Inventory arenaInventory = Bukkit.createInventory(null, 9);

                    for (Arena arena : plugin.arenaManager.arenaList) {
                        ItemStack map = new ItemStack(Material.SAND, 1);
                        ItemMeta meta = map.getItemMeta();
                        List<String> lore = new ArrayList<>();
                        lore.add("§7§m-------------");
                        lore.add("§eMinimum játékosok: §a" + arena.getMinPlayers());
                        lore.add("§eMaximum játékosok: §a" + arena.getMaxPlayers());
                        lore.add("§eLootchestek száma: §a" + arena.getLootChests().size());
                        lore.add("");
                        lore.add("§6Játékosok az arénában 0/");
                        lore.add("");
                        lore.add("§7§m-------------");
                        assert meta != null;
                        meta.setDisplayName(arena.getArenaName());
                        meta.setLore(lore);
                        map.setItemMeta(meta);
                        arenaInventory.addItem(map);
                    }
                    player.openInventory(arenaInventory);
                    player.sendMessage("Láthatod az arénákat");
                    return true;
                }
                sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.InsufficientPermission")));
                return true;
            }
            if (args[0].equalsIgnoreCase("chestmode")) {
                if (sender.hasPermission("domination.admin")) {
                    if (args.length == 1) {
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.UsageMessageChestmode")));
                        return true;
                    }
                    if (plugin.arenaManager.getCurrentArena(args[1]) != null) {
                        if (!plugin.arenaManager.getCurrentArena(args[1]).isChestMode()) {
                            plugin.arenaManager.getCurrentArena(args[1]).setChestMode(true);
                            chest = new ItemStack(Material.CHEST, 1);
                            sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.EnterChestmode")));
                            ItemMeta meta = chest.getItemMeta();
                            assert meta != null;
                            meta.setDisplayName(args[1]);
                            meta.setLore(Collections.singletonList("Lootchest"));
                            chest.setItemMeta(meta);
                            ((Player) sender).getInventory().addItem(chest);
                        } else {
                            plugin.arenaManager.getCurrentArena(args[1]).setChestMode(false);
                            sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.ExitChestmode")));
                            ((Player) sender).getInventory().remove(chest);
                        }
                    } else {
                        sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.ArenaNotFound")));
                        return true;
                    }
                }
                sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.InsufficientPermission")));
                return true;
            }
            if (args[0].equalsIgnoreCase("selector")) {
                if (sender.hasPermission("domination.admin")) {
                    ((Player) sender).getInventory().addItem(Utils.selectorAxe.convertToItemStack());
                }
                sender.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.InsufficientPermission")));
                return true;
            }


        }

        return true;
    }
}
