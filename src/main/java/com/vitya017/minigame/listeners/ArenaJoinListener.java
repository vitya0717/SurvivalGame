package com.vitya017.minigame.listeners;

import com.vitya017.minigame.Main;
import com.vitya017.minigame.events.PlayerJoinArenaEvent;
import com.vitya017.minigame.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArenaJoinListener implements Listener {

    private final Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onJoinArena(PlayerJoinArenaEvent event) {
        event.setJoinMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.JoinArenaMessage")));
        event.setJoinOthersMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.JoinArenaOthersMessage")));
    }

}
