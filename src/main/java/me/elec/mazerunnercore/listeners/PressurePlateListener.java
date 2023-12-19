package me.elec.mazerunnercore.listeners;

import me.elec.mazerunnercore.DataManager;
import me.elec.mazerunnercore.GameEndings;
import me.elec.mazerunnercore.MazeRunnerCore;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PressurePlateListener implements Listener {
    private final MazeRunnerCore plugin;
    private final GameEndings gameEndings;
    private final DataManager dataManager; // Initialize DataManager

    public PressurePlateListener(MazeRunnerCore plugin, GameEndings gameEndings, DataManager dataManager) {
        this.plugin = plugin;
        this.gameEndings = gameEndings;
        this.dataManager = dataManager; // Initialize DataManager in the constructor
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Assuming you have access to mazeName here

        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.OAK_PRESSURE_PLATE) {
            if (plugin.isPlayerInGame(player.getUniqueId())) {
                // Player has won and is in the game, reward with XP
                dataManager.givePlayerXP(player, plugin.difficulty);

                plugin.playerIsWinner();
                plugin.stopStopwatch(player);
            }
        }
    }
}
