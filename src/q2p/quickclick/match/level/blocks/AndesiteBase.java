package q2p.quickclick.match.level.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class AndesiteBase implements BlockBase {
	public ItemStack getStack() {
		return new ItemStack(Material.STONE, 1, (short)1, (byte)2);
	}
	public boolean compare(Block block) {
		return (block.getType() == Material.STONE && block.getData() == 2);
	}
	public void place(Block block) {
		block.setType(Material.STONE);
		block.setData((byte)2);
	}
}