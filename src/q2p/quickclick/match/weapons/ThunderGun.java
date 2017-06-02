package q2p.quickclick.match.weapons;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
import q2p.quickclick.Assist;
import q2p.quickclick.client.ClientInfo;
import q2p.quickclick.match.Match;
import q2p.quickclick.match.level.LevelBase;

public class ThunderGun extends Gun {
	final static byte HIT_DISTANCE = 24;
	
	public ThunderGun() {
		super(Material.STICK, "ThunderGun");
	}
	
	public void onShoot(ClientInfo shooter, PlayerInteractEvent event) {
		if(owner == null) return;
		BlockIterator bi = new BlockIterator(shooter.player.getEyeLocation(), 0, LevelBase.DIAGONAL_DISTANCE);
		float wallDist = -1;
		while(bi.hasNext()) {
			Block b = bi.next();
			if(b.getType() != Material.AIR) {
				wallDist = Assist.distance(shooter.player.getEyeLocation(), Assist.rayTrace(shooter.player.getEyeLocation(), shooter.player.getEyeLocation().getDirection(), b.getX(), b.getY(), b.getZ(), b.getX()+1, b.getY()+1, b.getZ()+1));
				break;
			}
		}
		if(wallDist < 0) wallDist = LevelBase.DIAGONAL_DISTANCE;
		Match match = shooter.matchInfo.match;
		ClientInfo hited = null;
		for(ClientInfo target : match.players) {
			if(target.name.equals(shooter.name) || target.matchInfo.isDead) continue;
			Location hit = Assist.rayPlayer(shooter.player, target.player);
			if(hit == null) continue;
			float distance = Assist.distance(shooter.player.getEyeLocation(), hit);
			if(distance <= HIT_DISTANCE && distance < wallDist) {
				hited = target;
				wallDist = distance;
			}
		}
		if(hited != null) {
			shooter.matchInfo.match.damage(hited, shooter, 2);
		} 
		drawLightning(wallDist);
	}

	void drawLightning(float distance) {
		Location start = owner.player.getEyeLocation();
		Location pop = owner.player.getEyeLocation();
		Vector dir = start.getDirection().normalize().multiply(0.1);
		while(Assist.distance(start, pop) <= distance) {
			for(ClientInfo p : owner.matchInfo.match.players) {
				p.player.playEffect(pop, Effect.WATERDRIP, null);
				pop.add(dir);
			}
		}
	}
}