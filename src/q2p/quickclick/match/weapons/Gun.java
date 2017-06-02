package q2p.quickclick.match.weapons;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import q2p.quickclick.client.ClientInfo;

public abstract class Gun {
	ClientInfo owner = null;
	Material material = null;
	String displayName = null;
	
	public abstract void onShoot(ClientInfo shooter, PlayerInteractEvent event);
	
	Gun(Material material, String displayName) {
		this.material = material;
		this.displayName = displayName;
	}
}