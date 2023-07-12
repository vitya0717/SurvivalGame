package com.vitya017.minigame.ScoreboardManager;

import com.vitya017.minigame.arenamanager.Arena;
import com.vitya017.minigame.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BoardManager {

    private final ScoreboardManager scoreboardManager;
    private final Scoreboard scoreboard;
    private ScoreboardUpdater scoreboardUpdater;
    private final Objective objective;
    public static String[] colors = {"&1", "&2", "&3", "&4", "&5", "&6", "&7", "&8", "&9", "&0", "&b", "&c", "&a", "&e", "&d"};
    private int lineIndex = 0;

    public BoardManager(Arena arena) {
        this.scoreboardManager = Bukkit.getScoreboardManager();
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = this.scoreboard.registerNewObjective("arenaBoardOf" + arena.getArenaName(), Criteria.DUMMY, arena.getArenaName());
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void create(Player player, String title, List<String> lines) {
        objective.setDisplayName(title);

        Set<String> entry = scoreboard.getEntries();

        for (String ent : entry) {
            scoreboard.resetScores(ent);
        }

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.equals("")) {
                line = line.replace("", colors[i]);
            }
            objective.getScore(Utils.replace(line + colors[i])).setScore(lines.size() - i);
        }

        player.setScoreboard(scoreboard); // Állítsd be a játékos scoreboardját itt, az előző cikluson kívül
    }


    public void remove(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public ScoreboardUpdater getScoreboardUpdater() {
        return scoreboardUpdater;
    }

    public void setScoreboardUpdater(ScoreboardUpdater scoreboardUpdater) {
        this.scoreboardUpdater = scoreboardUpdater;
    }
}
