package com.vitya017.minigame.arenamanager;

import com.vitya017.minigame.Main;
import com.vitya017.minigame.arenastates.ArenaState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ArenaWarmup extends BukkitRunnable {

    private final Main plugin = Main.getPlugin(Main.class);
    private final Arena arena;
    private int time;
    private final List<Player> arrayList;

    public ArenaWarmup(Arena arena, int count, List<Player> arrayList) {
        this.arena = arena;
        this.time = count;
        this.arrayList = arrayList;
    }

    @Override
    public void run() {
        if(arena.getState() == ArenaState.NOT_RUNNING) cancel();

        if(time == 0) {
            for (Player player : arrayList) {
                player.sendMessage("§4Warmup has ended! Go win this arena!");
                player.sendMessage("The arena will end within 5 minutes");
            }
            arena.setState(ArenaState.RUNNING);
            if(arena.getArenaTimer() == null || arena.getArenaTimer().isCancelled()) {
                arena.setArenaTimer(new ArenaTimer(arena, 300,arrayList));
                arena.getArenaTimer().runTaskTimer(plugin, 0, 20L);
            }
            cancel();
        }
        time--;
    }

    public int getTime() {
        return time;
    }
}
