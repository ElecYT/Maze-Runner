package me.elec.mazerunnercore;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class CustomScoreboardManager implements Listener {
    private final MazeRunnerCore plugin;

    public CustomScoreboardManager(MazeRunnerCore plugin) {
        this.plugin = plugin;

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

    private void updateScoreboards() {
        // Update both lobby and game scoreboards for all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (plugin.isStopwatchRunning(player)) {
                setGameScoreboard(player);
            } else {
                setLobbyScoreboard(player);
            }
        }
    }

    public void switchLobbyScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("lobby", "dummy", ChatColor.translateAlternateColorCodes('&', "&6&lMaze &9&lRunner"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Set the game scoreboard
        Score date = objective.getScore("§7⌚ " + PlaceholderAPI.setPlaceholders(player, "%localtime_time_MMM d, yyy%"));
        Score xpFiller = objective.getScore("§6§l  ● ᴇxᴘᴇʀɪᴇɴᴄᴇ");
        Score xp = objective.getScore("   §6xᴘ " + ChatColor.GRAY + getPlayerXP(player));
        Score level = objective.getScore("   §6ʟᴇᴠᴇʟ " + ChatColor.GRAY + getPlayerLevel(player));
        Score infoFiller = objective.getScore("§9§l  ● ꜱᴇʀᴠᴇʀ ɪɴꜰᴏ");
        Score name = objective.getScore("   §9ᴘʟᴀʏᴇʀ " + ChatColor.GRAY + player.getName());
        Score online = objective.getScore("   §9ᴏɴʟɪɴᴇ " + ChatColor.GRAY + Bukkit.getOnlinePlayers().size());
        Score ip = objective.getScore("§7ᴘʟᴀʏ.ʙɪᴏᴍᴇꜱᴍᴄ.ɴᴇᴛ");
        Score blank = objective.getScore("§f");
        Score blankTwo = objective.getScore("§6");

        date.setScore(9);
        blankTwo.setScore(8);
        xpFiller.setScore(7);
        xp.setScore(6);
        level.setScore(5);
        infoFiller.setScore(4);
        name.setScore(3);
        online.setScore(2);
        blank.setScore(1);
        ip.setScore(0);

        player.setScoreboard(board);
    }

    public void switchGameScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("game", "dummy", ChatColor.translateAlternateColorCodes('&', "&6&lMaze &9&lRunner"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Set the game scoreboard
        Score date = objective.getScore("§7⌚ " + PlaceholderAPI.setPlaceholders(player, "%localtime_time_MMM d, yyy%"));
        Score gameFiller = objective.getScore("§b§l  ● ɢᴀᴍᴇ");
        Score map = objective.getScore("   §bᴍᴀᴘ " + ChatColor.GRAY + player.getWorld().getName());
        Score time = objective.getScore("   §bᴛɪᴍᴇ " + ChatColor.GRAY + formatTime(getTimer(player)));
        Score xpFiller = objective.getScore("§6§l  ● ᴇxᴘᴇʀɪᴇɴᴄᴇ");
        Score xp = objective.getScore("   §6xᴘ " + ChatColor.GRAY + getPlayerXP(player));
        Score level = objective.getScore("   §6ʟᴇᴠᴇʟ " + ChatColor.GRAY + getPlayerLevel(player));
        Score infoFiller = objective.getScore("§9§l  ● ꜱᴇʀᴠᴇʀ ɪɴꜰᴏ");
        Score name = objective.getScore("   §9ᴘʟᴀʏᴇʀ " + ChatColor.GRAY + player.getName());
        Score online = objective.getScore("   §9ᴏɴʟɪɴᴇ " + ChatColor.GRAY + Bukkit.getOnlinePlayers().size());
        Score ip = objective.getScore("§7ᴘʟᴀʏ.ʙɪᴏᴍᴇꜱᴍᴄ.ɴᴇᴛ");
        Score blank = objective.getScore("§f");
        Score blankTwo = objective.getScore("§6");

        date.setScore(12);
        blankTwo.setScore(11);
        gameFiller.setScore(10);
        map.setScore(9);
        time.setScore(8);
        xpFiller.setScore(7);
        xp.setScore(6);
        level.setScore(5);
        infoFiller.setScore(4);
        name.setScore(3);
        online.setScore(2);
        blank.setScore(1);
        ip.setScore(0);

        player.setScoreboard(board);
    }

    public void setLobbyScoreboard(Player player) {
        switchLobbyScoreboard(player);
    }

    public void setGameScoreboard(Player player) {
        switchGameScoreboard(player);
    }

    private String formatTime(long seconds) {
        // Example method, replace with your logic to format time
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    private long getTimer(Player player) {
        // Example method, replace with your logic to get time in seconds
        return (long) ((long) plugin.getElapsedTimeSeconds(player) + .5);
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
}
