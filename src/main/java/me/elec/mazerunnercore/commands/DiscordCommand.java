package me.elec.mazerunnercore.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class DiscordCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            //Call the teleportPlayer method from the plugin instance

            player.sendMessage("\uE002 §dJoin the discord -> dsc.gg/nautical");
            return true;
        }
        return false;
    }
}
