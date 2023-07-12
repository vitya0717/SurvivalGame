package com.vitya017.minigame.arenamanager;

import com.vitya017.minigame.Main;
import com.vitya017.minigame.arenastates.ArenaState;
import com.vitya017.minigame.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ArenaCountdown extends BukkitRunnable {

    private final Arena arena;
    private int time;
    private final Main plugin = Main.getPlugin(Main.class);
    private final List<Player> arrayList;

    public ArenaCountdown(Arena arena, int count, List<Player> arrayList) {
        this.arena = arena;
        this.time = count;
        this.arrayList = arrayList;
    }
    @Override
    public void run() {
        if (time == 0) {
            for(Player player :  getArrayList()) {
                player.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.ArenaStartedMessage")));
            }
            arena.setState(ArenaState.WARMUP);
            if(arena.getArenaWarmup() == null || arena.getArenaWarmup().isCancelled()) {
                arena.setArenaWarmup(new ArenaWarmup(arena, 5, arrayList));
                arena.getArenaWarmup().runTaskTimer(plugin, 0,20L);
            }
            cancel();
        } else {
            for (Player player : getArrayList()) {
                if(time % 10 == 0 || time <= 10) {
                    player.sendMessage(Utils.replace(plugin.getConfig().getString("MessageConfiguration.ArenaCountdown")));
                }
            }
            time--;
        }
    }
    public List<Player> getArrayList() {
        return arrayList;
    }
    public int getTime() {
        return time;
    }
}
