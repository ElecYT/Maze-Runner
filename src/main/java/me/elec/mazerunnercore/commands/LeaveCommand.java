package me.elec.mazerunnercore.commands;

import me.elec.mazerunnercore.MazeRunnerCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor {
    private final MazeRunnerCore plugin;

    public LeaveCommand(MazeRunnerCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getGradientPrefix() + "§cThis command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;

        // Open the Leave Game GUI for the player
        plugin.openLeaveGameGUI(player);

        return true;
    }
}
