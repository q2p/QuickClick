package q2p.quickclick.match.weapons;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import q2p.quickclick.client.ClientInfo;

public class GunManager {
	public static void give(ClientInfo cli, Gun gun) {
		gun.owner = cli;
		cli.matchInfo.inventory.add(gun);
		ItemStack stack = new ItemStack(gun.material);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(gun.displayName);
		stack.setItemMeta(meta);
		cli.player.getInventory().addItem(stack);
		cli.player.updateInventory();
	}
	
	public static void onDeath(ClientInfo cli) {
		cli.matchInfo.inventory.clear();
	}
	
	public static Gun getByItem(ItemStack stack, ClientInfo owner) {
		for(Gun gun : owner.matchInfo.inventory) if(isThisType(stack, gun)) return gun;
		return null;
	}

	static boolean isThisType(ItemStack stack, Gun gun) {
		if(stack == null) return false;
		return gun.displayName.equals(stack.getItemMeta().getDisplayName());
	}
	
	static ItemStack getStack(ClientInfo cli, RailGun gun) {
		for(ItemStack stack : cli.player.getInventory().getContents()) if(gun.displayName.equals(stack.getItemMeta().getDisplayName())) return stack;
		return null;
	}
}
