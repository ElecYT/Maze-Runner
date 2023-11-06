package me.elec.mazerunnercore.commands;


import me.elec.mazerunnercore.MazeRunnerCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class TpLobbyCommand implements CommandExecutor {

    private final MazeRunnerCore plugin;

    public TpLobbyCommand(MazeRunnerCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            //Call the teleportPlayer method from the plugin instance
            plugin.teleportPlayer(player, "game-lobby-1", 21.409, 156, 017.480, -0.4f, -89.9f);

            return true;
        }
        return false;
    }
}

