package com.vitya017.minigame.arenamanager;

import com.vitya017.minigame.Main;
import com.vitya017.minigame.ScoreboardManager.BoardManager;
import com.vitya017.minigame.ScoreboardManager.ScoreboardUpdater;
import com.vitya017.minigame.arenastates.ArenaState;
import com.vitya017.minigame.events.PlayerJoinArenaEvent;
import com.vitya017.minigame.events.PlayerQuitArenaEvent;
import com.vitya017.minigame.lootchest.LootChest;
import com.vitya017.minigame.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@SuppressWarnings("ALL")
public class ArenaManager {

    private final Random random = new Random();
    public final List<Arena> arenaList = new ArrayList<>();
    private final Main plugin;
    private Arena currentArena;

    //Arena spawn index
    private int index = 0;
    private final HashMap<UUID, Integer> onePlayerteleportDelay = new HashMap<>();
    private final HashMap<UUID, ItemStack[]> playerInventories = new HashMap<>();

    private Integer taskIndex = 0;

    public ArenaManager(Main instance) {
        this.plugin = instance;
    }

    public void createArena(String arenaName, List<Location> locations, int minPlayers, int maxPlayers) {
        if (minPlayers <= 1 || maxPlayers > 64) {
            return;
        }
        setCurrentArena(new Arena(arenaName, locations.get(0), locations.get(1), minPlayers, maxPlayers));
        currentArena.setCountDown(60);
        arenaList.add(currentArena);
        saveArena(currentArena);
    }

    public void createScoreboard(Arena arena, Player player) {
        if (arena.getBoardManager() == null) {
            arena.setBoardManager(new BoardManager(arena));
        }
        arena.getBoardManager().create(player,
                Utils.replace(plugin.arenaConfig.getConfig(arena.getArenaName()+".yml").getString("Arena."+arena.getArenaName()+".scoreboard-title")),
                Utils.replace(plugin.arenaConfig.getConfig(arena.getArenaName()+".yml").getStringList("Arena."+arena.getArenaName()+".scoreboard")));
    }

    public void removeArena(String arena) {
        plugin.arenaConfig.removeFile(arena + ".yml");
        arenaList.remove(getCurrentArena(arena));
    }

    //Belépés az arénába
    public void joinArena(UUID uuid, String arena) {
        Player player = Bukkit.getPlayer(uuid);
        Utils.setPlayer(player);
        Arena a = getCurrentArena(arena);
        PlayerJoinArenaEvent joinArenaEvent = new PlayerJoinArenaEvent(getCurrentArena(arena), player,
                plugin.getConfig().getString("MessageConfiguration.JoinArenaMessage"),
                plugin.getConfig().getString("MessageConfiguration.JoinArenaOthersMessage"));
        Bukkit.getPluginManager().callEvent(joinArenaEvent);


        if (a.isPlayableArena()) {
            if (a.getState() == ArenaState.NOT_RUNNING) {
                a.setState(ArenaState.WAITING_FOR_PLAYERS);
            }
            if (!a.getJoinedPlayers().contains(player) && !onePlayerteleportDelay.containsKey(uuid)) {
                onePlayerteleportDelay.put(uuid, taskIndex);
                taskIndex++;
            }
            if (!a.getJoinedPlayers().contains(player)) {
                a.getJoinedPlayers().add(player);
                movePlayerToArena(uuid, arena);
                savePlayerInventoryToHash(player.getUniqueId());
                //joinmessage
                player.sendMessage(Utils.replace(joinArenaEvent.getJoinMessage()));

                createScoreboard(a, player);

                if (a.getBoardManager().getScoreboardUpdater() == null || a.getBoardManager().getScoreboardUpdater().isCancelled()) {
                    a.getBoardManager().setScoreboardUpdater(new ScoreboardUpdater(a, a.getBoardManager()));
                    a.getBoardManager().getScoreboardUpdater().runTaskTimer(plugin, 0,20L);
                }

                for (Player others : a.getJoinedPlayers()) {
                    if (player != others) {
                        others.sendMessage(Utils.replace(joinArenaEvent.getJoinOthersMessage()));
                    }
                }
                if (a.getMinPlayers() == a.getJoinedPlayers().size()) {
                    start(arena);
                }
            }
        } else {
            player.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.ArenaCurrentlyNotPlayableOrRunning")));
        }
        // player.sendMessage(getCurrentArena(arena).getState().toString());
    }

    //Kilépés az arénából
    public void leaveArena(UUID uuid, String arena) {
        Player player = Bukkit.getPlayer(uuid);
        Utils.setPlayer(player);
        PlayerQuitArenaEvent quitArenaEvent = new PlayerQuitArenaEvent(getCurrentArena(arena), player,
                plugin.getConfig().getString("MessageConfiguration.QuitArenaMessage"),
                plugin.getConfig().getString("MessageConfiguration.QuitArenaOthersMessage"));
        Bukkit.getPluginManager().callEvent(quitArenaEvent);

        if (getCurrentArena(arena).getJoinedPlayers().contains(player)) {
            getCurrentArena(arena).getJoinedPlayers().remove(player);
            restorePlayerInventory(uuid);
        }
        if (getCurrentArena(arena).getMinPlayers() > getCurrentArena(arena).getJoinedPlayers().size() && getCurrentArena(arena).getState() == ArenaState.COUNTDOWN) {
            restorePlayerInventory(uuid);
            getCurrentArena(arena).setState(ArenaState.WAITING_FOR_PLAYERS);
            if (!getCurrentArena(arena).getArenaCountdown().isCancelled()) {
                getCurrentArena(arena).getArenaCountdown().cancel();
            }
        }
        if (getCurrentArena(arena).getJoinedPlayers().size() == 0 && getCurrentArena(arena).getState() == ArenaState.WAITING_FOR_PLAYERS) {
            getCurrentArena(arena).setState(ArenaState.NOT_RUNNING);
            index = 0;
        }
        if (getCurrentArena(arena).getJoinedPlayers().size() == 1 && getCurrentArena(arena).getState() == ArenaState.RUNNING) {
            end(getCurrentArena(arena));
            index = 0;
        }
        if (getCurrentArena(arena).getJoinedPlayers().size() == 1 && getCurrentArena(arena).getState() == ArenaState.WARMUP) {
            end(getCurrentArena(arena));
            index = 0;
        }
        player.sendMessage(quitArenaEvent.getQuitMessage());
        for (Player others : getCurrentArena(arena).getJoinedPlayers()) {
            if (player != others) {
                others.sendMessage(Utils.replace(quitArenaEvent.getQuitOthersMessage()));
            }
        }
        getCurrentArena(arena).getBoardManager().remove(player);
    }

    //Aréna keresés játékosnév alapján!
    public Arena findArenaByPlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        Arena found = null;
        for (Arena arena : plugin.arenaManager.arenaList) {
            if (arena.isPlayableArena() && (arena.getJoinedPlayers().contains(player) || arena.getDiedPlayers().contains(player))) {
                found = arena;
            }
        }
        return found;
    }

    //Aréna indító
    private void start(String arena) {
        if (getCurrentArena(arena).getState() != ArenaState.COUNTDOWN) {
            getCurrentArena(arena).setState(ArenaState.COUNTDOWN);
        }
        if (getCurrentArena(arena).getArenaCountdown() == null || getCurrentArena(arena).getArenaCountdown().isCancelled()) {
            fillLootChest(arena);
            for (Player p : getCurrentArena(arena).getJoinedPlayers()) {
                p.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.EnoughPlayerJoined")));
                getCurrentArena(arena).setState(ArenaState.COUNTDOWN);
            }
            getCurrentArena(arena).setArenaCountdown(new ArenaCountdown(getCurrentArena(arena), getCurrentArena(arena).getCountDown(), getCurrentArena(arena).getJoinedPlayers()));
            getCurrentArena(arena).getArenaCountdown().runTaskTimer(plugin, 0L, 20L);
        }
    }

    //Aréna lezáró
    public void end(Arena currentArena) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            for (Player winner : currentArena.getJoinedPlayers()) {
                currentArena.getBoardManager().remove(winner);
                Utils.setWinner(winner);
                winner.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.WinnerMessage")));
                restorePlayerInventory(winner.getUniqueId());
                winner.teleport(currentArena.getLobbyLocation());
            }
            Bukkit.getConsoleSender().sendMessage(currentArena.getArenaName());
            for (Player died : currentArena.getDiedPlayers()) {
                currentArena.getBoardManager().remove(died);
                died.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.WinnerMessageOthers")));
                restorePlayerInventory(died.getUniqueId());
                died.teleport(currentArena.getLobbyLocation());
                died.setGameMode(GameMode.SURVIVAL);
                died.updateInventory();
            }

            if (currentArena.getArenaTimer() != null && !currentArena.getArenaTimer().isCancelled()) {
                currentArena.getArenaTimer().cancel();
            }
            currentArena.getJoinedPlayers().clear();
            currentArena.getDiedPlayers().clear();
            currentArena.setState(ArenaState.NOT_RUNNING);
        }, 40);
    }


    //Az arénában megtalálható chestek feltöltése random tárgyakkal
    private void fillLootChest(String arena) {
        for (Map.Entry<Integer, LootChest> lc : getCurrentArena(arena).getLootChests().entrySet()) {
            LootChest chest = lc.getValue();
            //DEBUG
            //Bukkit.getConsoleSender().sendMessage(chest.getLocation() + "");
            //ha nem megy akkor ez 1, 4
            int randomItemNumber = random.nextInt(1, 4);
            int inventoryPos = random.nextInt(0, 27);
            Directional directional = null;

            if (!(chest.getLocation().getBlock() instanceof Chest)) {
                chest.getLocation().getBlock().setType(Material.CHEST);
                directional = (Directional) chest.getLocation().getBlock().getBlockData();
                directional.setFacing(chest.getChestFacing());
            }
            Chest c = (Chest) chest.getLocation().getBlock().getState();
            c.setBlockData(directional);
            c.update();
            Inventory inv = c.getInventory();
            inv.clear();
            for (int i = 0; i < randomItemNumber; i++) {
                while (inv.getItem(inventoryPos) != null) {
                    inventoryPos = random.nextInt(0, 27);
                }
                inv.setItem(inventoryPos, (ItemStack) Utils.randomChoice(Utils.possibleItems));
            }
        }
    }

    public void movePlayerToArena(UUID uuid, String arena) {
        Arena a = getCurrentArena(arena);
        Player player = Bukkit.getPlayer(uuid);
        int task = onePlayerteleportDelay.get(uuid);
        if (!Bukkit.getScheduler().isCurrentlyRunning(task)) {
            task = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                player.teleport(a.getSpawnLocations().get(index));
                onePlayerteleportDelay.remove(uuid);
                index++;
                if (index == a.getSpawnLocations().size()) {
                    index = 0;
                }
            }, 20);
        }
    }

    public void savePlayerInventoryToHash(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (!playerInventories.containsKey(uuid)) {
            playerInventories.put(uuid, player.getInventory().getContents());
        }
        player.getInventory().clear();
    }

    public void restorePlayerInventory(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (playerInventories.get(uuid) != null) {
            player.getInventory().clear();
            player.getInventory().setContents(playerInventories.get(uuid));
            playerInventories.remove(uuid);
        }
    }

    public Arena getCurrentArena(String name) {
        for (Arena arena : arenaList) {
            if (arena.getArenaName().equals(name)) {
                return arena;
            }
        }
        return null;
    }

    public void setCurrentArena(Arena currentArena) {
        this.currentArena = currentArena;
    }

    public void saveArena(Arena arena) {

        plugin.arenaConfig.generateDirectory("Arenas");
        plugin.arenaConfig.generateFile(arena.getArenaName());

        plugin.arenaConfig.getConfig(arena.getArenaName()).set("Arena." + arena.getArenaName() + ".min-players", arena.getMinPlayers());
        plugin.arenaConfig.getConfig(arena.getArenaName()).set("Arena." + arena.getArenaName() + ".max-players", arena.getMaxPlayers());
        plugin.arenaConfig.getConfig(arena.getArenaName()).set("Arena." + arena.getArenaName() + ".countdown", arena.getCountDown());
        plugin.arenaConfig.getConfig(arena.getArenaName()).set("Arena." + arena.getArenaName() + ".scoreboard-title", "&a&lSurvivalDomination &7- &a%arena%");
        plugin.arenaConfig.getConfig(arena.getArenaName()).set("Arena." + arena.getArenaName() + ".scoreboard", Arrays.asList(
                "",
                "&aWelcome to the game &2%player%",
                "",
                "&ePlaytime &7≫ &c%arena-time%",
                "&ePlayers alive &7≫ &c%alive%",
                "&ePlayers died &7≫ &c%died%",
                "",
                "&eKills &7≫ &c%kills%"));

        if (arena.getPos1() != null) {
            plugin.arenaConfig.getConfig(arena.getArenaName()).set("Arena." + arena.getArenaName() + ".arenaMinWallRadius", arena.getPos1());
        } else {
            plugin.arenaConfig.getConfig(arena.getArenaName()).set("Arena." + arena.getArenaName() + ".arenaMinWallRadius", "Not set yet");
        }
        if (arena.getPos2() != null) {
            plugin.arenaConfig.getConfig(arena.getArenaName()).set("Arena." + arena.getArenaName() + ".arenaMaxWallRadius", arena.getPos2());
        } else {
            plugin.arenaConfig.getConfig(arena.getArenaName()).set("Arena." + arena.getArenaName() + ".arenaMaxWallRadius", "Not set yet");
        }
        if (arena.getLobbyLocation() != null) {
            plugin.arenaConfig.getConfig(arena.getArenaName()).set("Arena." + arena.getArenaName() + ".lobby-location", arena.getLobbyLocation());
        } else {
            plugin.arenaConfig.getConfig(arena.getArenaName()).set("Arena." + arena.getArenaName() + ".lobby-location", "Not set yet");
        }
        if (arena.getSpawnLocations() != null) {
            plugin.arenaConfig.getConfig(arena.getArenaName()).set("Arena." + arena.getArenaName() + ".spawn-locations", arena.getSpawnLocations());
        } else {
            plugin.arenaConfig.getConfig(arena.getArenaName()).set("Arena." + arena.getArenaName() + ".spawn-locations", "Not set yet");
        }

        plugin.arenaConfig.saveConfig();
    }

    public void loadArena(String name) {
        int minPlayers = Integer.parseInt(plugin.arenaConfig.getConfig(name).getString("Arena." + name + ".min-players"));
        int maxPlayers = Integer.parseInt(plugin.arenaConfig.getConfig(name).getString("Arena." + name + ".max-players"));
        if (minPlayers <= 1 || minPlayers >= maxPlayers) {
            minPlayers = 2;
        } else if (maxPlayers > 64 || maxPlayers < minPlayers) {
            maxPlayers = 64;
        }
        Arena newArena = new Arena(name, minPlayers, maxPlayers);
        newArena.setCountDown(plugin.arenaConfig.getConfig(name).getInt("Arena." + name + ".countdown"));
        newArena.setPos1(plugin.arenaConfig.getConfig(name).getLocation("Arena." + name + ".arenaMinWallRadius"));
        newArena.setPos2(plugin.arenaConfig.getConfig(name).getLocation("Arena." + name + ".arenaMaxWallRadius"));
        newArena.setLobbyLocation(plugin.arenaConfig.getConfig(name).getLocation("Arena." + name + ".lobby-location"));
        newArena.setSpawnLocations((List<Location>) plugin.arenaConfig.getConfig(name).getList("Arena." + name + ".spawn-locations"));
        arenaList.add(newArena);
        loadLootChests(name);
    }

    public void saveLootChest(Arena arena, String display, World world, Location loc, Directional chestFace) {
        plugin.arenaConfig.generateFile(arena.getArenaName());
        if (arena.getLootChests() != null) {
            plugin.arenaConfig.getConfig(arena.getArenaName()).set("Arena." + arena.getArenaName() + "." + ".lootChests." + display + ".location", "world=" + world.getName() + ", x=" + loc.getX() + ", y=" + loc.getY() + ", z=" + loc.getZ() + ", direction=" + chestFace.getFacing().name());
        }
        plugin.arenaConfig.saveConfig();
    }

    private void loadLootChests(String name) {
        if (plugin.arenaConfig.getConfig(name).getConfigurationSection("Arena." + name + ".lootChests") != null) {
            for (String lootChestName : plugin.arenaConfig.getConfig(name).getConfigurationSection("Arena." + name + ".lootChests").getKeys(false)) {
                String[] coordSplit = plugin.arenaConfig.getConfig(name).getString("Arena." + name + ".lootChests." + "." + lootChestName + ".location").split(",");
                World world = Bukkit.getWorld(coordSplit[0].split("=")[1]);
                double x = Double.parseDouble(coordSplit[1].split("=")[1]);
                double y = Double.parseDouble(coordSplit[2].split("=")[1]);
                double z = Double.parseDouble(coordSplit[3].split("=")[1]);
                BlockFace chestFace = BlockFace.valueOf(coordSplit[4].split("=")[1]);
                int index = Integer.parseInt(lootChestName);
                Location loc = new Location(world, x, y, z);
                LootChest lootChest = new LootChest(lootChestName, plugin.arenaManager.getCurrentArena(name), loc, chestFace.name());
                plugin.arenaManager.getCurrentArena(name).getLootChests().put(index, lootChest);
            }
        }
    }
}