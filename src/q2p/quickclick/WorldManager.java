package q2p.quickclick;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import q2p.quickclick.match.LevelList;
import q2p.quickclick.match.level.LevelBase;

public class WorldManager {
	public static final short OFF_Y = 78;
	public static final short TP_HEIGHT = 64;
	public static World lobby;
	
	//TODO:
	/*public static void generateRespawnBox() {
		for(int x = -3; x <= -1; x++) {
			for(int y = 0; y <= 3; y++) {
				for(int z = 0; z <= 2; z++) {
					HubStatus.world.getBlockAt(x, y, z).setType(Material.GLOWSTONE);
				}
			}
		}
		for(int y = 1; y <= 2; y++) HubStatus.world.getBlockAt(-2, y, 1).setType(Material.AIR);
	}
	public static Location getAuthBoxLocation() {
		return new Location(HubStatus.world, -1.5, 1, 1.5);
	}*/
	
	public static void generateSpawnWorld(LevelBase base) {
		lobby = generateAndFillWorld("lobby", LevelList.lobbyLevel);
	}
	
	public static World generateAndFillWorld(String name, LevelBase base) {
		World ret = Bukkit.createWorld(new Creator(name));
		System.out.println(base.fileName);
		for(short z = 0; z < base.size[2]; z++) {
			for(short y = 0; y < base.size[1]; y++) {
				for(short x = 0; x < base.size[0]; x++) {
					LevelBase.BASES[base.map[z][y][x]].place(ret.getBlockAt(x, OFF_Y+y, z));
				}	
			}
		}
		return ret;
	}
	
	public static void unload() {
		deleteWorld(lobby);
	}
	
	public static void deleteWorld(World world) {
		if(world == null) return;
		File folder = world.getWorldFolder();
		Bukkit.unloadWorld(world, false);
		deleteWorldFolder(folder);
	}
	
	private static void deleteWorldFolder(File dir) {
		File[] files = dir.listFiles();
		for(File f : files) {
			if(f.isDirectory()){
				deleteWorldFolder(f);
				f.delete();
			} else f.delete();
		}
	}

	public static boolean behinLimit(Location location) {
		return location.getY() < TP_HEIGHT;
	}
}