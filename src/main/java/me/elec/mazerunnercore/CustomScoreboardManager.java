package me.elec.mazerunnercore;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.scoreboard.*;

public class CustomScoreboardManager implements Listener {

    private final MazeRunnerCore plugin;
    private Scoreboard gameScoreboard;
    private Scoreboard lobbyScoreboard;

    public CustomScoreboardManager(MazeRunnerCore plugin) {
        this.plugin = plugin;

        // Register the listener for PluginEnableEvent
        Bukkit.getPluginManager().registerEvents(this, plugin);

        // Initialize and create the scoreboards when the manager is constructed
        createScoreboards();
    }

    // This method is responsible for creating your scoreboards
    private void createScoreboards() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        gameScoreboard = scoreboardManager.getNewScoreboard();
        lobbyScoreboard = scoreboardManager.getNewScoreboard();

        // Create objectives and add them to the scoreboards
        Objective gameObjective = gameScoreboard.registerNewObjective("game", "dummy", "Game");
        gameObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Objective lobbyObjective = lobbyScoreboard.registerNewObjective("lobby", "dummy", "Lobby");
        lobbyObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Add other scoreboard elements like teams, scores, etc.
        // (Replace with your actual scoreboard setup logic)
    }

    // Method to set the game scoreboard for a player
    public void setGameScoreboard(Player player) {
        player.setScoreboard(gameScoreboard);
    }

    // Method to set the lobby scoreboard for a player
    public void setLobbyScoreboard(Player player) {
        player.setScoreboard(lobbyScoreboard);
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        if (event.getPlugin() == plugin) {
            // Your plugin has been enabled; the scoreboards are already created
        }
    }
}

