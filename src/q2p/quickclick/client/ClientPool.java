package q2p.quickclick.client;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import q2p.quickclick.Assist;
import q2p.quickclick.ConfigFile;
import q2p.quickclick.HubStatus;
import q2p.quickclick.LobbySpawn;
import q2p.quickclick.match.MatchMaking;
import org.bukkit.event.player.PlayerQuitEvent;

public final class ClientPool {
	private static final String BANNED_MSG = "You were banned on QuickClick :(";
	static final ArrayList<ClientInfo> clients = new ArrayList<ClientInfo>();
	public static final ArrayList<ClientInfo> online = new ArrayList<ClientInfo>();
	static final ArrayList<ClientInfo> offline = new ArrayList<ClientInfo>();
	public static ClientInfo mainAdmin = null;
	
	public static final ClientInfo findClient(final String name, final boolean onlineFirst) {
		ArrayList<ClientInfo> s1 = online;
		ArrayList<ClientInfo> s2 = offline;
		while(s1 != null){
			for(int i = s1.size()-1; i != -1; i--) {
				if(s1.get(i).name.equalsIgnoreCase(name)) {
					return s1.get(i);
				}
			}
			s1 = s2;
			s2 = null;
		}
		return null;
	}

	public static final ClientInfo getClient(final Player player) {
		if(!player.hasMetadata("cli")) return null;
		return (ClientInfo)player.getMetadata("cli").get(0).value();
	}
	
	static final ClientInfo initClient(final Player player) {
		ClientInfo cli = findClient(player.getName(), false);
		if(cli == null) {
			cli = new ClientInfo();
			cli.name = player.getName().toLowerCase();
			clients.add(cli);
			offline.add(cli);
		}
		cli.player = player;
		if(player.hasMetadata("cli"))
			player.removeMetadata("cli", HubStatus.plugin);
		if(!player.hasMetadata("cli"))
			player.setMetadata("cli", new CliMeta(cli));
		cli.auth.logged = false;
		cli.auth.loginTries = 0;
		cli.auth.changeTries = 0;
		cli.player.teleport(LobbySpawn.getSpawnLocation());
		offline.remove(cli);
		online.add(cli);
		return cli;
	}
	
	public static final void initilize() {
		try {
			ConfigFile.save("/clients.txt");
			String line;
			ClientInfo cli;
			while((line = ConfigFile.get()) != null) {
				cli = new ClientInfo();
				cli.name = line;
				cli.auth.registered = true;
				cli.auth.password = ConfigFile.get();
				cli.auth.secretWord = ConfigFile.get();
				cli.banned = ConfigFile.get().equals("1")?true:false;
				clients.add(cli);
				offline.add(cli);
			}
			ConfigFile.dispose();
		} catch (Exception e) {
			Assist.abort(e);
		}
		try {
			ConfigFile.save("/meta.txt");
			final String line = ConfigFile.get();
			if(line != null && !line.equals("")) mainAdmin = findClient(line, false);
			else mainAdmin = null;
			ConfigFile.dispose();
		} catch (Exception e) {
			Assist.abort(e);
		}
		for(Player pl : HubStatus.plugin.getServer().getOnlinePlayers())
			initClient(pl);
	}
	
	public static void deInitilize() {
		try {
			ConfigFile.load("/clients.txt");
			while(!clients.isEmpty()){
				ClientInfo cli = clients.remove(0);
				// Не записывать не зарегестрированных пользователей
				if(!cli.auth.registered) continue;
				ConfigFile.put(cli.name + "\n");
				ConfigFile.put(cli.auth.password + "\n");
				ConfigFile.put(cli.auth.secretWord+"\n");
				ConfigFile.put(cli.banned?"1":"0");
				if(!clients.isEmpty())
					ConfigFile.put("\n");
				ConfigFile.flush();
			}
			ConfigFile.dispose();
		} catch (Exception e) {
			Assist.abort(e);
		}
		try {
			ConfigFile.load("/meta.txt");
			ConfigFile.put(mainAdmin == null ? "" : mainAdmin.name);
			ConfigFile.dispose();
		} catch (Exception e) {
			Assist.abort(e);
		}
		mainAdmin = null;
	}
	// Авторизировался ли игрок
	public static boolean logged(Player player) {
		return getClient(player).auth.logged;
	}
	// Является ли игрок главным админом или сервером
	public static boolean isAdmin(ClientInfo cli) {
		if(mainAdmin != null && cli.name.equalsIgnoreCase(mainAdmin.name)) return true;
		return false;
	}
	public static boolean isAdmin(CommandSender sender) {
		if(sender instanceof Player) {
			if(mainAdmin != null && sender.getName().equalsIgnoreCase(mainAdmin.name)) return true;
		} else if(sender.isOp()) return true;
		return false;
	}
	// Разрешить игроку подключится?
	public static void acceptPlayer(PlayerLoginEvent event) {
		ClientInfo cli = findClient(event.getPlayer().getName(), true);
		if(cli != null && cli.player != null) {
			event.disallow(Result.KICK_OTHER, "Player with name \""+event.getPlayer().getName()+"\" is already on server.");
			return;
		}
		
		cli = initClient(event.getPlayer());
		if(cli.banned) event.disallow(Result.KICK_BANNED, BANNED_MSG);
	}
	// Альтернатива kick
	public static void throwOut(final ClientInfo cli, final String reason) {
		if(cli.player != null)
			cli.player.kickPlayer(reason);
	}
	// Блокировка игрока
	static String banPlayer(String name) {
		ClientInfo cli = findClient(name,true);
		if(cli == null) return "Player " + name + " not in database.";
		if(cli.banned) return "Player " + name + " banned already.";
		cli.banned = true;
		if(cli.player != null) cli.player.kickPlayer(BANNED_MSG);
		return "Player " + name + " was banned.";
	}
	static String unbanPlayer(String name) {
		ClientInfo cli = findClient(name,false);
		if(cli == null) return "Player " + name + " not in database.";
		if(!cli.banned) return "Player " + name + " not banned.";
		cli.banned = false;
		return "Player " + name + " was unbanned.";
	}
	static String isBanned(String name) {
		ClientInfo cli = null;
		cli = findClient(name,false);
		if(cli == null) return "Player " + name + " not in database.";
		return "Player " + name + (cli.banned?"":" not") +" banned.";
	}
	public static boolean checkVacationCommand(Command command, CommandSender sender, String[] args) {
		if(!isAdmin(sender)) return false;
		if(command.getName().equals("vacation")) {
			if((sender instanceof Player) && !ClientPool.logged((Player) sender)) {
				sender.sendMessage(Authorize.LOG_ACT);
				return false;
			}
			if(args.length == 1) sender.sendMessage(ClientPool.banPlayer(args[0]));
			else sender.sendMessage(ChatColor.RED + "Usage: /vacation <name>");
		} else if(command.getName().equals("endvacation")) {
			if((sender instanceof Player) && !ClientPool.logged((Player) sender)) {
				sender.sendMessage(Authorize.LOG_ACT);
				return false;
			}
			if(args.length == 1) sender.sendMessage(ClientPool.unbanPlayer(args[0]));
			else sender.sendMessage(ChatColor.RED + "Usage: /endvacation <name>");
		} else if(command.getName().equals("onvacation")) {
			if((sender instanceof Player) && !ClientPool.logged((Player) sender)) {
				sender.sendMessage(Authorize.LOG_ACT);
				return false;
			}
			if(args.length == 1) sender.sendMessage(ClientPool.isBanned(args[0]));
			else sender.sendMessage(ChatColor.RED + "Usage: /onvacation <name>");
		} else return false;
		return true;
	}
	// Отключение игрока
	public static void onExit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		ClientInfo cli = ClientPool.getClient(event.getPlayer());
		if(cli.gameState == GameState.MATCH) MatchMaking.onLeave(cli);
		if(cli.gameState == GameState.SPAWN) LobbySpawn.onLeave(cli);
		Authorize.onDeLogin(cli);
		cli.gameState = GameState.OFFLINE;
		cli.player.setGameMode(GameMode.SURVIVAL);
		cli.player = null;
		online.remove(cli);
		offline.add(cli);
	}
}