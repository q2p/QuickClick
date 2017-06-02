package q2p.quickclick;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import q2p.quickclick.client.Authorize;
import q2p.quickclick.client.ClientInfo;
import q2p.quickclick.client.ClientPool;

public class Modifications {
	public static boolean checkCommand(Command command, CommandSender sender, String[] args) {
		if(!command.getName().equals("modifie")) return false;
		if(sender instanceof Player && !ClientPool.logged((Player) sender)) {
			sender.sendMessage(Authorize.LOG_ACT);
			return true;
		}
		
		if(Assist.isArgument(0, "admin", args) && sender.isOp() && !(sender instanceof Player)) {
			if(args.length != 2) sender.sendMessage("Usage: /modifie admin <admin name>");
			ClientInfo cli = ClientPool.findClient(args[1], true);
			if(cli == null) sender.sendMessage("Player " + args[1] + " not in database.");
			else {
				ClientPool.mainAdmin = cli;
				if(cli.player != null) cli.player.sendMessage("You are admin now.");
				sender.sendMessage("Player " + args[1] + " now is main admin.");
			}
		} else if(Assist.isArgument(0, "noadmin", args) && sender.isOp() && !(sender instanceof Player)) {
			if(ClientPool.mainAdmin != null && ClientPool.mainAdmin.player != null) ClientPool.mainAdmin.player.sendMessage("You are not admin any more.");
			ClientPool.mainAdmin = null;
			sender.sendMessage("Server no longer have admin.");
		} else if(!ClientPool.isAdmin(sender)) return false;
		else sender.sendMessage(ChatColor.RED+"Usage: /modifie <admin|noadmin> [arguments...]");
		return true;
	}
}
