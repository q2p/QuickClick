package q2p.quickclick.match;

import java.io.File;
import java.util.ArrayList;
import q2p.quickclick.Assist;
import q2p.quickclick.match.level.LevelBase;
import q2p.quickclick.match.level.specifications.SpawnSpecification;
import q2p.quickclick.match.level.specifications.quake.QuakeSpecification;

public class LevelList {
	static final ArrayList<LevelBase> quakeLevels = new ArrayList<LevelBase>();
	public static LevelBase lobbyLevel = null;
	private static final String LEVELS_DIR = "QuickClickData/levels/";
	
	public static void load() {
		File dir = new File(LEVELS_DIR);
		if(!dir.exists() || !dir.isDirectory()) Assist.abort("Levels folder not found.");
		File[] list = dir.listFiles();
		for(File file : list) {
			if(!file.getName().endsWith(".map") || file.getName().length() < 5) continue;
			sortLevel(new LevelBase(file));
		}
		if(lobbyLevel == null) Assist.abort("Spawn was not loaded.");
	}

	private static void sortLevel(LevelBase base) {
		if(!base.completed) return;
		if(base.specification instanceof QuakeSpecification) quakeLevels.add(base);
		else if(base.specification instanceof SpawnSpecification) lobbyLevel = base;
	}
	
	static LevelBase findLevel(String name) {
		for(int i = 0; i < quakeLevels.size(); i++) if(quakeLevels.get(i).fileName.equalsIgnoreCase(name)) return quakeLevels.get(i);
		return null;
	}
	
	public static void unload() {
		lobbyLevel = null;
		quakeLevels.clear();
	}
}