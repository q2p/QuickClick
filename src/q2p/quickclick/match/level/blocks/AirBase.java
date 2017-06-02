package q2p.quickclick.match.level.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class AirBase implements BlockBase {
	public ItemStack getStack() {
		return null;
	}
	public boolean compare(Block block) {
		return (block.getType() == Material.AIR);
	}
	public void place(Block block) {
		block.setType(Material.AIR);
	}
}