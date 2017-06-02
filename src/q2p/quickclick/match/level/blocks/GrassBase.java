package q2p.quickclick.match.level.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class GrassBase implements BlockBase {
	public ItemStack getStack() {
		return new ItemStack(Material.GRASS);
	}
	public boolean compare(Block block) {
		return block.getType() == Material.GRASS;
	}
	public void place(Block block) {
		block.setType(Material.GRASS);
	}
}