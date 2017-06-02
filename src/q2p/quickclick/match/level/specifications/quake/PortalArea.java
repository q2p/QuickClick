package q2p.quickclick.match.level.specifications.quake;

import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;
import q2p.quickclick.Assist;
import q2p.quickclick.WorldManager;
import q2p.quickclick.client.ClientInfo;
import q2p.quickclick.match.level.MapArea;

public class PortalArea extends MapArea {
	byte destinationId;
	
	PortalArea(DataInputStream dis) throws IOException {
		super(dis);
		destinationId = dis.readByte();
	}

	public void checkPlayer(ClientInfo cli, PlayerMoveEvent event) {
		Location l = event.getTo();
		if(Assist.aabbXaabb(l.getX()-Assist.legsHeadStandRadius, l.getY(), l.getZ()-Assist.legsHeadStandRadius, position[0], WorldManager.OFF_Y+position[1], position[2], Assist.legsHeadStandDiam, Assist.modelHeight, Assist.legsHeadStandDiam, size[0], size[1], size[2])) {
			event.setTo(((QuakeSpecification)cli.matchInfo.match.base.specification).destinations.get(destinationId).teleport(cli));
		}
	}
}