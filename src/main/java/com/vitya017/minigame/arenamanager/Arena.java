package com.vitya017.minigame.arenamanager;

import com.vitya017.minigame.ScoreboardManager.BoardManager;
import com.vitya017.minigame.arenastates.ArenaState;
import com.vitya017.minigame.lootchest.LootChest;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Arena {

    //Aréna paraméterek
    private String arenaName;
    private int minPlayers;
    private int maxPlayers;
    //Setup paraméterek, amiket később kell beállítani
    private Location lobbyLocation;
    private List<Location> spawnLocations = new ArrayList<>();
    private final HashMap<Integer, LootChest> lootChests = new HashMap<>();
    //setup utána beállítások
    private final List<Player> joinedPlayers = new ArrayList<>();
    private final List<Player> diedPlayers = new ArrayList<>();
    private ArenaState arenaState = ArenaState.NOT_RUNNING;
    private Location pos1;
    private Location pos2;
    private boolean chestMode = false;
    private int countDown;
    private int warmupTime;
    private int arenaPlayTime;

    private BoardManager boardManager;

    private ArenaCountdown arenaCountdown;

    private ArenaWarmup arenaWarmup;

    private ArenaTimer arenaTimer;


    public Arena() {

    }

    public Arena(String arenaName, int minPlayers, int maxPlayers) {
        setArenaName(arenaName);
        setMinPlayers(minPlayers);
        setMaxPlayers(maxPlayers);
    }

    public Arena(String arenaName, Location location, Location location1, int minPlayers, int maxPlayers) {
        setArenaName(arenaName);
        setMinPlayers(minPlayers);
        setMaxPlayers(maxPlayers);
        setPos1(location);
        setPos2(location1);
    }

    public String getArenaName() {
        return arenaName;
    }

    public void setArenaName(String arenaName) {
        this.arenaName = arenaName;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public void setLobbyLocation(Location lobbyLocation) {
        this.lobbyLocation = lobbyLocation;
    }

    public List<Location> getSpawnLocations() {
        return spawnLocations;
    }

    public void setSpawnLocations(List<Location> spawnLocations) {
        this.spawnLocations = spawnLocations;
    }

    public List<Player> getJoinedPlayers() {
        return joinedPlayers;
    }

    public boolean isPlayableArena() {
        return lobbyLocation != null && spawnLocations != null && Objects.requireNonNull(spawnLocations).size() >= 1;
    }

    public HashMap<Integer, LootChest> getLootChests() {
        return lootChests;
    }

    public ArenaState getState() {
        return arenaState;
    }

    public void setState(ArenaState state) {
        this.arenaState = state;
    }

    public List<Player> getDiedPlayers() {
        return diedPlayers;
    }

    public boolean isChestMode() {
        return chestMode;
    }

    public void setChestMode(boolean value) {
        this.chestMode = value;
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }


    public ArenaTimer getArenaTimer() {
        return arenaTimer;
    }

    public void setArenaTimer(ArenaTimer arenaTimer) {
        this.arenaTimer = arenaTimer;
    }


    public ArenaWarmup getArenaWarmup() {
        return arenaWarmup;
    }

    public void setArenaWarmup(ArenaWarmup arenaWarmup) {
        this.arenaWarmup = arenaWarmup;
    }


    public ArenaCountdown getArenaCountdown() {
        return arenaCountdown;
    }

    public void setArenaCountdown(ArenaCountdown arenaCountdown) {
        this.arenaCountdown = arenaCountdown;
    }


    //Aréna visszaszámláló
    public void setCountDown(int countDown) {
        this.countDown = countDown;
    }

    public int getCountDown() {
        return countDown;
    }

    //Aréna felkészülési idő visszaszámláló
    public int getWarmupTime() {
        return warmupTime;
    }

    public void setWarmupTime(int warmupTime) {
        this.warmupTime = warmupTime;
    }

    //Aréna játék idejének hossza!
    public int getArenaPlayTime() {
        return arenaPlayTime;
    }

    public void setArenaPlayTime(int arenaPlayTime) {
        this.arenaPlayTime = arenaPlayTime;
    }

    public BoardManager getBoardManager() {
        return boardManager;
    }

    public void setBoardManager(BoardManager boardManager) {
        this.boardManager = boardManager;
    }
}
