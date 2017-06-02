package q2p.quickclick.match;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import q2p.quickclick.LobbySpawn;
import q2p.quickclick.Parcour;
import q2p.quickclick.client.Authorize;
import q2p.quickclick.client.ClientInfo;
import q2p.quickclick.client.ClientPool;
import q2p.quickclick.client.GameState;
import q2p.quickclick.match.level.LevelBase;

public class MatchMaking {
	static final ArrayList<Match> liveMatches = new ArrayList<Match>();
	static final byte MAX_MATCHES = 1;
	static final boolean[] boxes = new boolean[MAX_MATCHES];
	
	static void initilize() {
		for(byte i = 0; i < MAX_MATCHES; i++) boxes[i] = false;
	}
	
	public static boolean checkCommand(Command command, CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) return true;
		if(command.getName().equals("duel")) {
			ClientInfo cli = ClientPool.getClient((Player)sender);
			if(!ClientPool.logged(cli.player)) {
				sender.sendMessage(Authorize.LOG_ACT);
				return true;
			}
			if(cli.gameState != GameState.SPAWN) {
				sender.sendMessage("You must be on spawn to start a duel.");
				return true;
			}
			if(liveMatches.size() >= MAX_MATCHES) {
				sender.sendMessage("No avilable rooms");
				return true;
			}
			if(args.length != 2) {
				sender.sendMessage("Usage: /duel <opponent name> <level name>");
				return true;
			}
			ClientInfo opp = ClientPool.findClient(args[0], true);
			if(opp == null) {
				sender.sendMessage("Player " + args[0] + " not in database.");
				return true;
			}
			if(opp.name.equals(cli.name)) {
				sender.sendMessage("You can't start duel with yourself.");
				return true;
			}
			if(opp.player == null) {
				sender.sendMessage("Player " + opp.name + " is offline.");
				return true;
			}
			if(opp.gameState != GameState.SPAWN) {
				sender.sendMessage("Player " + opp.name + " can't play this match right now.");
				return true;
			}
			LevelBase level = LevelList.findLevel(args[1]);
			if(level == null) {
				sender.sendMessage("Level " + args[1] + " not found.");
				return true;
			}
			beginDuel(cli, opp, level);
			return true;
		}
		return false;
	}

	private static void beginDuel(ClientInfo cli1, ClientInfo cli2, LevelBase level) {
		Match match = new Match(cli1, cli2, level);
		liveMatches.add(match);
	}

	public static void onJoin(ClientInfo cli, Match match) {
		Parcour.stopParkour(cli);
		match.join(cli);
	}

	public static void onLeave(ClientInfo cli) {
		cli.matchInfo.match.leave(cli);
		LobbySpawn.onJoin(cli);
	}

	public static void onDeath(ClientInfo cli) {
		cli.matchInfo.match.onDeath(cli);
	}

	public static void onRespawn(ClientInfo cli, PlayerRespawnEvent event) {
		cli.matchInfo.match.respawn(cli, event);
	}

	public static void onDamage(ClientInfo cli, EntityDamageEvent event) {
		cli.matchInfo.match.onDamage(cli, event);
	}
	
	public static void onMove(ClientInfo cli, PlayerMoveEvent event) {
		cli.matchInfo.match.onMove(cli, event);
	}
}
