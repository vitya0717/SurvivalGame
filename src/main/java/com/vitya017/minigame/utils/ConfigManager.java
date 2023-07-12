package com.vitya017.minigame.utils;

import com.vitya017.minigame.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("ALL")
public class ConfigManager {

    private String fileName;

    private FileConfiguration fileConfiguration;
    private File file;

    private File directory;
    private String directoryName;

    private Main plugin;

    /**
     * @param fileName The configuration file
     */
    public ConfigManager(String fileName) {
        setFileName(fileName);
        generateFile(fileName);
    }

    public ConfigManager(String directoryName, String fileName) {
        setFileName(fileName);
        setDirectoryName(directoryName);

        generateDirectory(directoryName);
        generateFile(fileName);
    }

    public ConfigManager(Main instance) {
        this.plugin = instance;
    }
    public FileConfiguration getConfig(String fileName) {
        for (File file : plugin.getDataFolder().listFiles()) {
            if (file.getName().equals(fileName)) {
                try {
                    fileConfiguration.load(fileName);
                } catch (IOException | InvalidConfigurationException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileConfiguration;
    }

    public void generateDirectory(String directoryName) {
        setDirectoryName(directoryName);
        directory = new File(plugin.getDataFolder()+"/"+getDirectoryName());
        if(!directory.exists()) {
            directory.mkdir();
        }
    }
    public void generateFile(String fileName) {
        setFileName(fileName);
        file = (getDirectoryName() == null ? new File(plugin.getDataFolder(), getFileName()+".yml") : new File(plugin.getDataFolder()+"/"+getDirectoryName()+"/"+getFileName()+".yml"));

        fileConfiguration = new YamlConfiguration();
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
                fileConfiguration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            fileConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public File findFile(String fileName, boolean isDirectory) {
        for (File file : plugin.getDataFolder().listFiles()) {
            if(file.getName().equals(fileName) && file.isDirectory() == isDirectory) {
                return file;
            }
        }
        return null;
    }

    public void removeFile(String fileName) {
        for (File file : findFile("Arenas", true).listFiles()) {
            if(file.getName().equals(fileName)) {
                file.delete();
                break;
            }
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void saveConfig() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }
}
