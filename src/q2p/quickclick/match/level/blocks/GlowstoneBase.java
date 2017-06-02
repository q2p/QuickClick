package q2p.quickclick.match.level.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class GlowstoneBase implements BlockBase {
	public ItemStack getStack() {
		return new ItemStack(Material.GLOWSTONE);
	}
	public boolean compare(Block block) {
		return block.getType() == Material.GLOWSTONE;
	}
	public void place(Block block) {
		block.setType(Material.GLOWSTONE);
	}
}