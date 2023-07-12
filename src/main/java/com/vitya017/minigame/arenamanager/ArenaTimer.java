package com.vitya017.minigame.arenamanager;

import com.vitya017.minigame.Main;
import com.vitya017.minigame.arenastates.ArenaState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ArenaTimer extends BukkitRunnable {

    private final Main plugin = Main.getPlugin(Main.class);
    private final Arena arena;
    private int count;
    private final int cloneCount;
    private final List<Player> arrayList;

    public ArenaTimer(Arena arena, int count, List<Player> arrayList) {
        this.arena = arena;
        this.count = count;
        this.cloneCount = count;
        this.arrayList = arrayList;
    }

    @Override
    public void run() {

        if (arena.getState() == ArenaState.NOT_RUNNING) cancel();

        if (count == 0) {
            for (Player player : arrayList) {
                player.sendMessage("The arena has ended! Thanks for the participation");
                setCount(cloneCount);
            }
            arena.setState(ArenaState.NOT_RUNNING);
            cancel();
        }
        if (count == (cloneCount / 2)) {
            for (Player player : arrayList) {
                player.sendMessage("The arena will end within 2.5 minutes");
            }
        }
        count--;
    }

    public int getTime() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
