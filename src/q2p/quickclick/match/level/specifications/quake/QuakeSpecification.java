package q2p.quickclick.match.level.specifications.quake;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import q2p.quickclick.Assist;
import q2p.quickclick.match.level.specifications.LevelSpecification;

public class QuakeSpecification implements LevelSpecification {
	public String displayName = "";
	public ArrayList<SpawnPoint> spawns = new ArrayList<SpawnPoint>();
	public ArrayList<PickUp> pickUps = new ArrayList<PickUp>();
	public ArrayList<PortalArea> portals = new ArrayList<PortalArea>();
	public ArrayList<PortalDestination> destinations = new ArrayList<PortalDestination>();
	public ArrayList<LaunchPad> launchPads = new ArrayList<LaunchPad>();
	
	public boolean readData(DataInputStream dis) throws IOException {
		displayName = Assist.readString(dis);
		
		for(int i = dis.readByte(); i != 0; i--) spawns.add(new SpawnPoint(dis));
		for(int i = dis.readByte(); i != 0; i--) pickUps.add(new PickUp(dis));
		for(int i = dis.readByte(); i != 0; i--) portals.add(new PortalArea(dis));
		for(int i = dis.readByte(); i != 0; i--) destinations.add(new PortalDestination(dis));
		for(int i = dis.readByte(); i != 0; i--) launchPads.add(new LaunchPad(dis));
		return true;
	}
}
