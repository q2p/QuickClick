package q2p.quickclick;

import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class Creator extends WorldCreator {
	public Creator(String name) {
		super(name);
		environment(Environment.NORMAL);
		generateStructures(false);
		generatorSettings("");
		seed(0);
		type(WorldType.CUSTOMIZED);
		generator(HubStatus.chunkGenerator);
		World world = createWorld();
		world.setDifficulty(Difficulty.NORMAL);
		world.setAutoSave(false);
	}
}