package me.elec.mazerunnercore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.bukkit.util.ChatPaginator;

import java.util.Arrays;
import java.util.List;

public class CustomScoreboardManager implements Listener {

    private final MazeRunnerCore plugin;
    private Scoreboard gameScoreboard;
    private Scoreboard lobbyScoreboard;
    private Objective gameObjective;
    private Objective lobbyObjective;
    private boolean initialized = false;
    private boolean isInGame = false;

    public CustomScoreboardManager(MazeRunnerCore plugin) {
        this.plugin = plugin;
        this.initialized = false;
    }

    private void initializeScoreboards() {
        gameScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        lobbyScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        // Set up objectives for Game scoreboard
        gameObjective = gameScoreboard.registerNewObjective("Game", "dummy", ChatColor.GOLD + "Maze Runner Game");
        gameObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Set up objectives for Lobby scoreboard
        lobbyObjective = lobbyScoreboard.registerNewObjective("Lobby", "dummy", ChatColor.BLUE + "Maze Runner Lobby");
        lobbyObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        initialized = true;

        // Schedule a task to update the scoreboard every 20 ticks
        int delay = 0; // Delay before the first update
        int period = 10; // Period (in ticks) between updates

        new BukkitRunnable() {
            @Override
            public void run() {
                updateScoreboards();
            }
        }.runTaskTimer(plugin, delay, period);
    }

    public void setGameScoreboard(Player player) {
        if (!initialized) {
            initializeScoreboards();
        }
        player.setScoreboard(gameScoreboard);
        isInGame = true;
    }

    public void setLobbyScoreboard(Player player) {
        if (!initialized) {
            initializeScoreboards();
        }
        player.setScoreboard(lobbyScoreboard);
        isInGame = false;
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        if (event.getPlugin() == plugin && !initialized) {
            initializeScoreboards();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (initialized) {
            // Update lobby scoreboard information on player join
            Player player = event.getPlayer();
            updateLobbyScoreboard(player);
            // You might want to teleport players or perform other actions on join
            // plugin.teleportPlayer(player, "lobby", x, y, z, yaw, pitch);
        }
    }

    private void updateScoreboards() {
        // Clear scores from both lobby and game scoreboards for all online players

        // Update both lobby and game scoreboards for all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isInGame) {
                updateGameScoreboard(player);
            } else {
                updateLobbyScoreboard(player);
            }
        }
    }

    private void updateLobbyScoreboard(Player player) {
        // Example: Set player name, level, and XP on the lobby scoreboard
        List<String> lobbyLines = Arrays.asList(
                ChatColor.GRAY + "play.nauticalmc.net",
                ChatColor.GRAY + "",
                ChatColor.GREEN + "| Player " + ChatColor.BLUE + player.getName(),
                ChatColor.GOLD + "| Online " + ChatColor.GOLD + Bukkit.getOnlinePlayers().size(),
                ChatColor.AQUA + "| Level " + ChatColor.BLUE + getPlayerLevel(player),
                ChatColor.GOLD + "| XP " + ChatColor.GOLD + getPlayerXP(player),
                ""
        );
        updateObjective(lobbyScoreboard, lobbyObjective, "Lobby", mazeRunner(), lobbyLines);
        player.setScoreboard(lobbyScoreboard);
    }

    public void updateGameScoreboard(Player player) {
        // Example: Set player name, level, and XP on the game scoreboard
        List<String> gameLines = Arrays.asList(
                ChatColor.GRAY + "play.nauticalmc.net",
                ChatColor.GRAY + "",
                ChatColor.GREEN + "| Player " + ChatColor.GOLD + player.getName(),
                ChatColor.AQUA + "| Level " + ChatColor.BLUE + getPlayerLevel(player),
                ChatColor.GOLD + "| XP " + ChatColor.GOLD + getPlayerXP(player),
                ChatColor.BLUE + "| Time " + ChatColor.BLUE + formatTime(getTimer()),
                ChatColor.BLUE + "| Map " + ChatColor.GOLD + player.getWorld().getName(),
                ""
                // Add more lines for additional information as needed
                // ChatColor.BOLD + "Timer: " + ChatColor.RESET + getTimer()
        );
        updateObjective(gameScoreboard, gameObjective, "Game", mazeRunner(), gameLines);
        player.setScoreboard(gameScoreboard);
    }

    public String mazeRunner() {
        return "   §x§3§C§0§0§F§F§lM§x§5§2§1§4§E§3§la§x§6§7§2§8§C§6§lz§x§7§D§3§C§A§A§le §x§9§3§5§0§8§E§lR§x§A§8§6§5§7§1§lu§x§B§E§7§9§5§5§ln§x§D§4§8§D§3§9§ln§x§E§9§A§1§1§C§le§x§F§F§B§5§0§0§lr";
    }


    private void updateObjective(Scoreboard scoreboard, Objective oldObjective, String newObjectiveName, String displayName, List<String> lines) {
        if (oldObjective != null) {
            // Remove the existing objective
            oldObjective.unregister();
        }

        // Set up the new objective
        Objective newObjective = scoreboard.registerNewObjective(newObjectiveName, "dummy", displayName);
        newObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Set the new scores
        int scoreValue = 0;
        for (String line : lines) {
            Score score = newObjective.getScore(line);
            score.setScore(scoreValue++);
        }
    }

    // Replace these methods with your actual methods to get player level and XP
    private String getPlayerLevel(Player player) {
        // Example method, replace with your logic
        return String.valueOf(player.getLevel());
    }

    private String getPlayerXP(Player player) {
        // Example method, replace with your logic
        return String.valueOf(player.getTotalExperience());
    }

    private String formatTime(long seconds) {
        // Example method, replace with your logic to format time
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    private long getTimer() {
        // Example method, replace with your logic to get time in seconds
        return (long) plugin.getElapsedTimeSeconds();
    }
}

