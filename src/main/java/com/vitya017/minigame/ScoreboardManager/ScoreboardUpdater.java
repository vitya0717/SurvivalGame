package com.vitya017.minigame.ScoreboardManager;

import com.vitya017.minigame.Main;
import com.vitya017.minigame.ScoreboardManager.BoardManager;
import com.vitya017.minigame.arenamanager.Arena;
import com.vitya017.minigame.arenastates.ArenaState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardUpdater extends BukkitRunnable {

    private final Main plugin = Main.getPlugin(Main.class);
    private final BoardManager boardManager;
    private final Arena arena;

    public ScoreboardUpdater(Arena arena, BoardManager boardManager) {
        this.arena = arena;
        this.boardManager = boardManager;

    }

    @Override
    public void run() {
        for (Player player : arena.getJoinedPlayers()) {
            plugin.arenaManager.createScoreboard(arena, player);
        }
        if(arena.getState() == ArenaState.NOT_RUNNING) cancel();
    }

    public BoardManager getBoardManager() {
        return boardManager;
    }

    public Arena getArena() {
        return arena;
    }
}
