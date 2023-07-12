package com.vitya017.minigame;

import com.vitya017.minigame.arenamanager.ArenaManager;
import com.vitya017.minigame.commands.Commands;
import com.vitya017.minigame.listeners.*;
import com.vitya017.minigame.utils.ConfigManager;
import com.vitya017.minigame.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.Random;


public final class Main extends JavaPlugin {

    private final ConsoleCommandSender sender = Bukkit.getConsoleSender();
    public ArenaManager arenaManager;
    public ConfigManager arenaConfig;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        generateCommands();
        Utils.loadPluginItems();
        loadEvents();
        arenaManager = new ArenaManager(this);
        arenaConfig = new ConfigManager(this);
        File directory = arenaConfig.findFile("Arenas", true);
        if(directory != null) {
            for (File arena : directory.listFiles()) {
                String arenaName = arena.getName().split("\\.")[0];
                arenaConfig.generateDirectory("Arenas");
                arenaConfig.generateFile(arenaName);
                arenaManager.loadArena(arenaName);
                getLogger().info(arenaName);
                getLogger().info(arenaConfig.getConfig(arenaName).getString("Arena."+arenaName+".min-players"));
            }
        }
        sender.sendMessage("§e[MiniGame] §aA plugin elindult");
    }

    private void loadEvents() {
        Bukkit.getPluginManager().registerEvents(new ChestModeListener(), this);
        Bukkit.getPluginManager().registerEvents(new ArenaCreateSelector(), this);
        Bukkit.getPluginManager().registerEvents(new WallCheckListener(), this);
        Bukkit.getPluginManager().registerEvents(new ArenaJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new ArenaQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryResetListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerKillListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ArenaWaitingMoveListener(this), this);
        Bukkit.getPluginManager().registerEvents(new CommandControllListener(this), this);
    }

    private void generateCommands() {
        Objects.requireNonNull(this.getCommand("event")).setExecutor(new Commands());
    }

    @Override
    public void onDisable() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            arenaManager.restorePlayerInventory(online.getUniqueId());
        }
        sender.sendMessage("§e[MiniGame] §cA plugin leállt");
    }
}
