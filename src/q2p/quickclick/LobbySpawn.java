package q2p.quickclick;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import q2p.quickclick.client.ClientInfo;
import q2p.quickclick.client.GameState;
import q2p.quickclick.match.LevelList;
import q2p.quickclick.match.level.LevelBase;
import q2p.quickclick.match.level.specifications.SpawnSpecification;

public class LobbySpawn {
	private static LevelBase base = null;
	
	static void load() {
		base = LevelList.lobbyLevel;
		WorldManager.generateSpawnWorld(base);
	}
	
	static void unload() {
		WorldManager.deleteWorld(WorldManager.lobby);
	}

	public static void onJoin(ClientInfo cli) {
		cli.player.setGameMode(GameMode.SURVIVAL);
		if(cli.gameState != GameState.AUTH) cli.player.teleport(getSpawnLocation());
		cli.gameState = GameState.SPAWN;
	}
	
	public static Location getSpawnLocation() {
		return ((SpawnSpecification)base.specification).getSpawnLocation(WorldManager.lobby);
	}
	
	public static void onLeave(ClientInfo cli) {
		Parcour.stopParkour(cli);
	}

	static void respawn(PlayerRespawnEvent event) {
		event.setRespawnLocation(getSpawnLocation());
	}
	
	static void onMove(ClientInfo cli, PlayerMoveEvent event) {
		Parcour.checkLanding(cli);
		if(WorldManager.behinLimit(event.getTo())) event.setTo(getSpawnLocation());
	}
}