package q2p.quickclick;

import java.util.Random;

import org.bukkit.plugin.java.JavaPlugin;
import q2p.quickclick.client.ClientPool;
import q2p.quickclick.match.LevelList;

public class HubStatus {
	static LogicTick logicTick = null;
	private static int logicTickId = -1;
	
	public static JavaPlugin plugin = null;
	
	public static Random random = new Random();
	
	public static Generator chunkGenerator = new Generator();
		
	static void initilize(JavaPlugin plugin) {
		HubStatus.plugin = plugin;
		
		Log.initLog(plugin.getLogger());
		
		LevelList.load();
		LobbySpawn.load();
		ClientPool.initilize();
		News.load();
		Parcour.load();
		OutsideInfo.reload();
				
		logicTick = new LogicTick();
        logicTickId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, logicTick, 0, 1);
	}
	
	static void deInitilize() {
		News.unload();
		Parcour.unload();
		ClientPool.deInitilize();
		LobbySpawn.unload();
		LevelList.unload();
		plugin.getServer().getScheduler().cancelTask(logicTickId);
		logicTickId = -1;
		logicTick = null;
		plugin = null;
	}
}
