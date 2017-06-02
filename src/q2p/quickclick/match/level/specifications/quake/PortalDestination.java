package q2p.quickclick.match.level.specifications.quake;

import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import q2p.quickclick.client.ClientInfo;

public class PortalDestination extends SpawnPoint {
	private byte[] velocity;
	
	PortalDestination(DataInputStream dis) throws IOException {
		super(dis);
		velocity = new byte[]{dis.readByte(), dis.readByte(), dis.readByte()};
	}
	
	Location teleport(ClientInfo cli) {
		cli.player.setVelocity(new Vector((float)velocity[0]*0.5f, (float)velocity[0]*0.5f, (float)velocity[0]*0.5f));
		return getLocation(cli.player.getWorld());
	}
}
