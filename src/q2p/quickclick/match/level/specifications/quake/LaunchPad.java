package q2p.quickclick.match.level.specifications.quake;

import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import q2p.quickclick.Assist;
import q2p.quickclick.WorldManager;
import q2p.quickclick.client.ClientInfo;
import q2p.quickclick.match.level.MapArea;

public class LaunchPad extends MapArea {
	private byte[] velocity;
	
	LaunchPad(DataInputStream dis) throws IOException {
		super(dis);
		velocity = new byte[]{dis.readByte(), dis.readByte(), dis.readByte()};
	}
	
	public void checkPlayer(ClientInfo cli, PlayerMoveEvent event) {
		Location l = event.getTo();
		if(((Entity)cli.player).isOnGround() && Assist.aabbXaabb(l.getX()-Assist.legsHeadStandRadius, l.getY(), l.getZ()-Assist.legsHeadStandRadius, position[0], WorldManager.OFF_Y+position[1], position[2], Assist.legsHeadStandDiam, Assist.modelHeight, Assist.legsHeadStandDiam, size[0], size[1], size[2])) {
			cli.player.setVelocity(new Vector((float)velocity[0]*0.5f, (float)velocity[1]*0.5f, (float)velocity[2]*0.5f));
		}
	}
}
