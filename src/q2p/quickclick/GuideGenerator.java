package q2p.quickclick;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import q2p.quickclick.client.ClientPool;

public class GuideGenerator {
	static boolean parseCommand(Command command, CommandSender sender, String[] arguments) {
		if(!command.getName().equals("howto")) return false;
		if(arguments.length > 0) {
			if(arguments[0].equals("news") && arguments.length == 1) {
				String message = ChatColor.WHITE + "On our server you can read about stuff happening on our server using news system.\n";
				message += ChatColor.BLUE + "Commands:\n";
				message += ChatColor.YELLOW + "/news <page>" + ChatColor.WHITE + ": returns you a short list of news. If page not specified you will see first one.\n";
				message += ChatColor.YELLOW + "/news read <number>" + ChatColor.WHITE + ": opens up news on specified number.\n";
				if(ClientPool.isAdmin(sender)) {
					message += ChatColor.YELLOW + "/news add <title :=: text>" + ChatColor.WHITE + ": news with specified title and text\n";
					message += ChatColor.YELLOW + "/news remove <number>" + ChatColor.WHITE + ": removes specified news.\n";
					message += ChatColor.YELLOW + "/news edit <number> <title :=: text>" + ChatColor.WHITE + ": changes specifiend news.\n";
				}
				sender.sendMessage(message);
				return true;
			}
		}
		String message = ChatColor.WHITE + "To see specific article type \""+ChatColor.YELLOW+"/howto <article>"+ChatColor.WHITE+"\"\n";
		message += ChatColor.BLUE + "Articles:\n";
		message += ChatColor.YELLOW + "about" + ChatColor.WHITE + ": tells about server.\n";
		message += ChatColor.YELLOW + "acount" + ChatColor.WHITE + ": tells about acount system on a server.\n";
		message += ChatColor.YELLOW + "building" + ChatColor.WHITE + ": tells about building stuff on a server.\n";
		message += ChatColor.YELLOW + "commutication" + ChatColor.WHITE + ": tells about players interactions between them.\n";
		message += ChatColor.YELLOW + "news" + ChatColor.WHITE + ": tells about news system.\n";
		sender.sendMessage(message);
		return true;
	}
}