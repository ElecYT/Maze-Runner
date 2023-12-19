package me.elec.mazerunnercore;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

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
                "§f\uE010 §9ᴘʟᴀʏ.ɴᴀᴜᴛɪᴄᴀʟᴍᴄ.ɴᴇᴛ",
                "§f",
                "   §f\uE007 §9ᴘʟᴀʏᴇʀ " + ChatColor.GRAY + player.getName(),
                "   §f\uE009 §9ᴏɴʟɪɴᴇ " + ChatColor.GRAY + Bukkit.getOnlinePlayers().size(),
                " §9§l  ● ꜱᴇʀᴠᴇʀ ɪɴꜰᴏ",
                "   §f\uE008 §6ʟᴇᴠᴇʟ " + ChatColor.GRAY + getPlayerLevel(player),
                "   §f\uE003 §6xᴘ " + ChatColor.GRAY + getPlayerXP(player),
                " §6§l  ● ᴇxᴘᴇʀɪᴇɴᴄᴇ",
                "",
                "§7⌚ " + PlaceholderAPI.setPlaceholders(player, "%localtime_time%")
                );
        updateObjective(lobbyScoreboard, lobbyObjective, "Lobby", mazeRunner(), lobbyLines);
        player.setScoreboard(lobbyScoreboard);
    }

    public void updateGameScoreboard(Player player) {
        // Example: Set player name, level, and XP on the game scoreboard
        List<String> gameLines = Arrays.asList(
                "§f\uE010 §9ᴘʟᴀʏ.ɴᴀᴜᴛɪᴄᴀʟᴍᴄ.ɴᴇᴛ",
                "§f",
                "   §f\uE007 §9ᴘʟᴀʏᴇʀ " + ChatColor.GRAY + player.getName(),
                "   §f\uE009 §9ᴏɴʟɪɴᴇ " + ChatColor.GRAY + Bukkit.getOnlinePlayers().size(),
                " §9§l  ● ꜱᴇʀᴠᴇʀ ɪɴꜰᴏ",
                "   §f\uE008 §6ʟᴇᴠᴇʟ " + ChatColor.GRAY + getPlayerLevel(player),
                "   §f\uE003 §6xᴘ " + ChatColor.GRAY + getPlayerXP(player),
                " §6§l  ● ᴇxᴘᴇʀɪᴇɴᴄᴇ",
                "   §f\uE021 §bᴛɪᴍᴇ " + ChatColor.GRAY + formatTime(getTimer()),
                "   §f\uE022 §bᴍᴀᴘ " + ChatColor.GRAY + player.getWorld().getName(),
                " §b§l  ● ɢᴀᴍᴇ",
                "",
                "§7⌚ " + PlaceholderAPI.setPlaceholders(player, "%localtime_time%")
                // Add more lines for additional information as needed
                // ChatColor.BOLD + "Timer: " + ChatColor.RESET + getTimer()
        );
        updateObjective(gameScoreboard, gameObjective, "Game", mazeRunner(), gameLines);
        player.setScoreboard(gameScoreboard);
    }

    public String mazeRunner() {
        return "  §f\uE001";
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

