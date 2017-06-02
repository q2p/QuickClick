package q2p.quickclick.match.level;

import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.Bukkit;

public class MapArea {
	protected short[] position;
	protected byte[] size;
	
	protected MapArea(DataInputStream dis) throws IOException {
		position = new short[]{dis.readShort(), dis.readShort(), dis.readShort()};
		size = new byte[]{dis.readByte(), dis.readByte(), dis.readByte()};
		//TODO:
		Bukkit.getLogger().info(size[0] + " " + size[1] + " " + size[2]);
	}
}
