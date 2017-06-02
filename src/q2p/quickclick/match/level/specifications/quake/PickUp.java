package q2p.quickclick.match.level.specifications.quake;

import java.io.DataInputStream;
import java.io.IOException;

class PickUp extends MapPoint {
	static final byte TYPE_HEALTH = 0;
	static final byte TYPE_ARMOR = 1;
	static final byte TYPE_GUN = 2;
	static final byte TYPE_AMMO = 3;

	static final byte HEALTH_ARMOR_5 = 0;
	static final byte HEALTH_ARMOR_25 = 1;
	static final byte HEALTH_ARMOR_50 = 2;
	static final byte HEALTH_ARMOR_100 = 3;
	
	static final byte GUN_AMMO_MACHINE = 0;
	static final byte GUN_AMMO_SHOTGUN = 1;
	static final byte GUN_AMMO_ROCKET = 2;
	static final byte GUN_AMMO_PLASMA = 3;
	static final byte GUN_AMMO_THUNDER = 4;
	static final byte GUN_AMMO_RAIL = 5;
	
	byte type = 0;
	byte subType = 0;
		
	PickUp(DataInputStream dis) throws IOException {
		super(dis);
		type = dis.readByte();
		subType = dis.readByte();
	}
}