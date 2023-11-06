package me.elec.mazerunnercore.listeners;

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

    public PressurePlateListener(MazeRunnerCore plugin, GameEndings gameEndings) {
        this.plugin = plugin;
        this.gameEndings = gameEndings;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Assuming you have access to mazeName here

        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.OAK_PRESSURE_PLATE) {
            // Player has won, reward with XP
            plugin.rewardPlayerWithXP(player, 5);

            plugin.playerIsWinner();
            plugin.stopStopwatch(player);


        }
    }
}






