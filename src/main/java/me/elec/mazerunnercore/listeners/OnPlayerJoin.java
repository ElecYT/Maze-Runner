package me.elec.mazerunnercore.listeners;

import me.elec.mazerunnercore.CustomScoreboardManager;
import me.elec.mazerunnercore.MazeRunnerCore;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoin implements Listener {

    private final MazeRunnerCore plugin;
    private final CustomScoreboardManager scoreboardManager;

    public OnPlayerJoin(MazeRunnerCore plugin) {
        this.plugin = plugin;
        this.scoreboardManager = new CustomScoreboardManager(plugin); // Initialize CustomScoreboardManager here
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Actions to execute when a player joins
        // For example, you can set their status, teleport them, etc.
        Player player = event.getPlayer();
         plugin.teleportPlayer(player, "game-lobby-1", 21.520, 156, 107.512, 0.215f, 270.163f);
        // Check your conditions and set the player status as in-game or out-of-game
    }
}
