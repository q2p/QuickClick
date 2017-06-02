package q2p.quickclick.match.level.specifications.quake;

import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.World;
import q2p.quickclick.WorldManager;

public class SpawnPoint extends MapPoint {
	private byte rotation; /* -135 -90 -45 0 45 90 135 180 equals 0 1 2 3 4 5 6 7 */
	
	SpawnPoint(DataInputStream dis) throws IOException {
		super(dis);
		rotation = dis.readByte();
	}
	
	int getRotation() {
		return (rotation-3)*45;
	}

	public Location getLocation(World world) {
		return new Location(world, position[0]+(float)offset[0]/2f+0.5, WorldManager.OFF_Y+position[1]+(float)offset[1]/2f, position[2]+(float)offset[2]/2f+0.5, getRotation(), 0);
	}
}