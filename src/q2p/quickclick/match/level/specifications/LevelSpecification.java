package q2p.quickclick.match.level.specifications;

import java.io.DataInputStream;
import java.io.IOException;

public interface LevelSpecification {
	public boolean readData(DataInputStream dis) throws IOException;
}