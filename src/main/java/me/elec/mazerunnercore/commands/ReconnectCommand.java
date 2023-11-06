package me.elec.mazerunnercore.commands;

import me.elec.mazerunnercore.AutoReconnectManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReconnectCommand implements CommandExecutor {
    private AutoReconnectManager reconnectManager;

    public ReconnectCommand(AutoReconnectManager reconnectManager) {
        this.reconnectManager = reconnectManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            reconnectManager.toggleReconnect(player);
            return true;
        } else {
            sender.sendMessage("§cThis command can only be executed by a player.");
            return false;
        }
    }
}
