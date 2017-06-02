package q2p.quickclick.match.level.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class DirtBase implements BlockBase {
	public ItemStack getStack() {
		return new ItemStack(Material.DIRT);
	}
	public boolean compare(Block block) {
		return block.getType() == Material.DIRT;
	}
	public void place(Block block) {
		block.setType(Material.DIRT);
	}
}