package q2p.quickclick.client;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import q2p.quickclick.Assist;
import q2p.quickclick.LobbySpawn;

public class Authorize {
	public static final String LOG_ACT = ChatColor.RED + "You must log in or register to perform this action.";
	static final String SIZE_LIMIT_STR = "must be at least 6 symbols long and less then 17 symbols long.";
	static final String INVALID_LETTERS_STR = "can contain only lower and upper case latin letters and digits.";
	
	public static boolean checkCommand(Command command, CommandSender sender, String[] arguments) {
		if(command.getName().equals("register")) {
			if(!(sender instanceof Player)) return true;
			Player p = (Player) sender;
			ClientInfo cli = ClientPool.getClient(p);
			if(cli.auth.registered) sender.sendMessage(ChatColor.GREEN + "You already registered.");
			else if(arguments.length != 4) sender.sendMessage(ChatColor.RED + "To register you must type /register <password> <password> <secret word> <secret word>");
			else if(!arguments[0].equals(arguments[1])) sender.sendMessage(ChatColor.RED + "Passwords you entered dont match.");
			else if(arguments[0].length() < 6 || arguments[0].length() > 16) sender.sendMessage(ChatColor.RED + "Password "+SIZE_LIMIT_STR);
			else if(!Assist.isValidString(arguments[0], true)) sender.sendMessage(ChatColor.RED + "Password "+INVALID_LETTERS_STR);
			else if(!arguments[2].equals(arguments[3])) sender.sendMessage(ChatColor.RED + "Secret words you entered dont match.");
			else if(arguments[2].length() < 6 || arguments[2].length() > 16) sender.sendMessage(ChatColor.RED + "Secret word "+SIZE_LIMIT_STR);
			else if(!Assist.isValidString(arguments[2], true)) sender.sendMessage(ChatColor.RED + "Secret word "+INVALID_LETTERS_STR);
			else {
				cli.auth.password = arguments[0];
				cli.auth.secretWord = arguments[2];
				cli.auth.registered = true;
				onLogin(cli);
				sender.sendMessage(ChatColor.GREEN + "You registered succesfully!");
			}
			return true;
		}
		if(command.getName().equals("login")) {
			if(!(sender instanceof Player)) return true;
			Player p = (Player) sender;
			ClientInfo cli = ClientPool.getClient(p);
			if(arguments.length != 1) sender.sendMessage(ChatColor.RED + "Usage: /login <password>");
			else if(cli.auth.logged) sender.sendMessage(ChatColor.GREEN + "You already logged in.");
			else if(!cli.auth.registered) sender.sendMessage(ChatColor.RED + "You are not registered.");
			else if(!cli.auth.password.equals(arguments[0])) {
				cli.auth.loginTries++;
				if(cli.auth.loginTries != 4) sender.sendMessage(ChatColor.RED + "Incorrect password!");
				else ClientPool.throwOut(cli, "You entered incorrect password 4 times.");
			} else {
				onLogin(cli);
				sender.sendMessage(ChatColor.GREEN + "Welcome back "+p.getName()+"!");
			}
			return true;
		}
		if(command.getName().equals("changepassword")){
			if(!(sender instanceof Player)) return true;
			Player p = (Player) sender;
			ClientInfo cli = ClientPool.getClient(p);
			if(!cli.auth.registered) sender.sendMessage(ChatColor.RED + "You are not registered.");
			else if(arguments.length != 3) sender.sendMessage(ChatColor.RED + "Usage /changepassword <old password> <new password> <new password>");
			else if(!arguments[1].equals(arguments[2])) sender.sendMessage(ChatColor.RED + "New passwords you entered dont match.");
			else if(arguments[1].length() < 6 || arguments[1].length() > 16) sender.sendMessage(ChatColor.RED + "New password "+SIZE_LIMIT_STR);
			else if(!Assist.isValidString(arguments[1], true)) sender.sendMessage(ChatColor.RED + "New password "+INVALID_LETTERS_STR);
			else if(!cli.auth.password.equals(arguments[0])){
				cli.auth.changeTries++;
				if(cli.auth.changeTries != 4) sender.sendMessage(ChatColor.RED + "Incorrect old password!");
				else ClientPool.throwOut(cli, "You failed to change password 4 times.");
			} else {
				cli.auth.password = arguments[1];
				sender.sendMessage(ChatColor.GREEN + "You changed your password succesfully!");
			}
			return true;
		}
		if(command.getName().equals("resetpassword")){
			System.out.println("hello");
			if(!(sender instanceof Player)) return true;
			Player p = (Player) sender;
			ClientInfo cli = ClientPool.getClient(p);
			if(!cli.auth.registered) sender.sendMessage(ChatColor.RED + "You are not registered.");
			else if(arguments.length != 1) sender.sendMessage(ChatColor.RED + "Usage: /resetpassword <secret word>");
			else if(!cli.auth.secretWord.equals(arguments[0])) {
				cli.auth.changeTries++;
				if(cli.auth.changeTries != 4) sender.sendMessage(ChatColor.RED + "Incorrect secret word!");
				else ClientPool.throwOut(cli, "You failed to change password 4 times.");
			} else {
				cli.auth.password = Assist.generateHash(12, true);
				sender.sendMessage(ChatColor.GREEN + "Your new password: \""+cli.auth.password+"\".");
			}
			return true;
		}
		if(command.getName().equals("authfix")){
			if(sender instanceof Player || !sender.isOp()) return false;
			if(arguments.length == 1 || arguments.length == 3) {
				ClientInfo cli = ClientPool.findClient(arguments[0], false);
				if(cli == null) {
					sender.sendMessage("Player " + arguments[0] + " not in database.");
					return true;
				}
				ClientPool.throwOut(cli, "Your authorize data was changed by admin. If you own this account then contact with admin or moderators to get your authorize data.");
				cli.auth.registered = true;
				if(arguments.length == 1) {
					cli.auth.password = Assist.generateHash(8, true);
					cli.auth.secretWord = Assist.generateHash(8, true);
				} else {
					if(arguments[0].length() < 6 || arguments[0].length() > 16) {
						sender.sendMessage("Password "+SIZE_LIMIT_STR);
						return true;
					}
					if(!Assist.isValidString(arguments[1], true)) {
						sender.sendMessage("Password "+INVALID_LETTERS_STR);
						return true;
					}
					if(arguments[2].length() < 6 || arguments[2].length() > 16) {
						sender.sendMessage("Secret word "+SIZE_LIMIT_STR);
						return true;
					}
					if(!Assist.isValidString(arguments[2], true)) {
						sender.sendMessage("Secret word "+INVALID_LETTERS_STR);
						return true;
					}
					cli.auth.password = arguments[1];
					cli.auth.secretWord = arguments[2];
				}
				sender.sendMessage("New password and secret word of "+cli.name+" : " + cli.auth.password + " | " + cli.auth.secretWord);
			} else {
				sender.sendMessage("Usage: /autfix <name> [new password] [new secret word]");
			}
			return true;
		}
		return false;
	}
	
	static void onLogin(ClientInfo cli) {
		cli.auth.logged = true;
		cli.auth.loginTries = 0;
		LobbySpawn.onJoin(cli);
	}
	
	static void onDeLogin(ClientInfo cli) {
		cli.auth.logged = false;
		cli.auth.loginTries = 0;
		cli.auth.changeTries = 0;
		cli.gameState = GameState.AUTH;
	}
	
	public static void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		ClientPool.getClient(player).gameState = GameState.AUTH;
		player.teleport(LobbySpawn.getSpawnLocation());
		player.setGameMode(GameMode.SURVIVAL);
		player.getInventory().clear();
		player.setCanPickupItems(false);
		event.setJoinMessage(null);
	}
}
