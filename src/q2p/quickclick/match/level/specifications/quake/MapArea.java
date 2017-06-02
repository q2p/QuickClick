package q2p.quickclick.match.level.specifications.quake;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MapArea {
	public short[] position;
	public byte[] size;
	
	MapArea(short[] position, byte[] size) {
		this.position = position;
		this.size = size;
	}
	
	MapArea(DataInputStream dis) throws IOException {
		position = new short[]{dis.readShort(), dis.readShort(), dis.readShort()};
		size = new byte[]{dis.readByte(), dis.readByte(), dis.readByte()};
	}

	void write(DataOutputStream dos) throws IOException {
		for(byte i = 0; i != 3; i++) dos.writeShort(position[i]);
		for(byte i = 0; i != 3; i++) dos.writeByte(size[i]);
	}
}
