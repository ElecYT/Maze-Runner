package me.elec.mazerunnercore.listeners;

import me.elec.mazerunnercore.MazeRunnerCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoin implements Listener {

    private final MazeRunnerCore plugin;

    public OnPlayerJoin(MazeRunnerCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Actions to execute when a player joins
        // For example, you can set their status, teleport them, etc.
        Player player = event.getPlayer();
        Player player1 = player.getPlayer();

        // Check your conditions and set the player status as in-game or out-of-game
        plugin.setPlayerOutOfGame(player);
    }
}
