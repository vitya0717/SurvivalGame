package com.vitya017.minigame.listeners;

import com.vitya017.minigame.Main;
import com.vitya017.minigame.events.PlayerQuitArenaEvent;
import com.vitya017.minigame.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArenaQuitListener implements Listener {

    private final Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onQuitArena(PlayerQuitArenaEvent event) {
        event.setQuitMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.QuitArenaMessage")));
        event.setQuitOthersMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.QuitArenaOthersMessage")));
    }
}
