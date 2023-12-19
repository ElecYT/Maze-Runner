package me.elec.mazerunnercore.listeners;

import me.elec.mazerunnercore.CustomScoreboardManager;
import me.elec.mazerunnercore.DataManager;
import me.elec.mazerunnercore.GameBarrier;
import me.elec.mazerunnercore.MazeRunnerCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerLeave implements Listener {

    private final MazeRunnerCore plugin;
    private final CustomScoreboardManager scoreboardManager;
    private final GameBarrier gameBarrier;
    private final DataManager dataManager;

    public OnPlayerLeave(MazeRunnerCore plugin, CustomScoreboardManager scoreboardManager, GameBarrier gameBarrier, DataManager dataManager) {
        this.plugin = plugin;
        this.scoreboardManager = scoreboardManager; // Use the provided CustomScoreboardManager
        this.gameBarrier = gameBarrier; // Use the provided GameBarrier instance
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        dataManager.savePlayerData(player.getUniqueId(), player);
    }
}
