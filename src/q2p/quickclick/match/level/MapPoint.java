package q2p.quickclick.match.level;

import java.io.DataInputStream;
import java.io.IOException;

class MapPoint {
	protected byte[] position;
	protected byte[] offset; /* ... -1.5 -1 -0.5 0 0.5 1 1.5 equals -3 -2 -1 0 1 2 3 ...*/
		
	MapPoint(DataInputStream dis) throws IOException {
		position = new byte[]{dis.readByte(), dis.readByte(), dis.readByte()};
		offset = new byte[]{dis.readByte(), dis.readByte(), dis.readByte()};
	}
}
