package q2p.quickclick.match.level.specifications.quake;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class MapPoint {
	public short[] position;
	public byte[] offset; /* ... -1.5 -1 -0.5 0 0.5 1 1.5 equals -3 -2 -1 0 1 2 3 ...*/
	
	MapPoint(short position[], float offset[]) {
		this.position = position;
		this.offset = new byte[]{(byte)Math.round(offset[0]*2), (byte)Math.round(offset[1]*2), (byte)Math.round(offset[2]*2)};
	}
	
	MapPoint(DataInputStream dis) throws IOException {
		position = new short[]{dis.readShort(), dis.readShort(), dis.readShort()};
		offset = new byte[]{dis.readByte(), dis.readByte(), dis.readByte()};
	}

	void write(DataOutputStream dos) throws IOException {
		for(byte i = 0; i != 3; i++) dos.writeShort(position[i]);
		for(byte i = 0; i != 3; i++) dos.writeByte(offset[i]);
	}
}
