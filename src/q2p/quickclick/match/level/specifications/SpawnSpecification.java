package q2p.quickclick.match.level.specifications;

import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.World;
import q2p.quickclick.WorldManager;

public class SpawnSpecification implements LevelSpecification {
	short[] spawnPosition = null;
	public boolean readData(DataInputStream dis) throws IOException {
		spawnPosition = new short[]{dis.readShort(), dis.readShort(), dis.readShort()};
		return true;
	}
	public Location getSpawnLocation(World world) {
		return new Location(world, spawnPosition[0], WorldManager.OFF_Y+spawnPosition[1], spawnPosition[2], 0, 0);
	}
}
