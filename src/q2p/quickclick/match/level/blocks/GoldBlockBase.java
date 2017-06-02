package q2p.quickclick.match.level.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class GoldBlockBase implements BlockBase {
	public ItemStack getStack() {
		return new ItemStack(Material.GOLD_BLOCK);
	}
	public boolean compare(Block block) {
		return block.getType() == Material.GOLD_BLOCK;
	}
	public void place(Block block) {
		block.setType(Material.GOLD_BLOCK);
	}
}