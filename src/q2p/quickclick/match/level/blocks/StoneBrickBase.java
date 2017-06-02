package q2p.quickclick.match.level.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.SmoothBrick;

@SuppressWarnings("deprecation")
public class StoneBrickBase implements BlockBase {
	public ItemStack getStack() {
		return new SmoothBrick(Material.SMOOTH_BRICK, (byte)0).toItemStack(1);
	}
	public boolean compare(Block block) {
		return (block.getType() == Material.SMOOTH_BRICK && ((SmoothBrick)block.getState().getData()).getData() == 0);
	}
	public void place(Block block) {
		block.setType(Material.SMOOTH_BRICK);
		((SmoothBrick)block.getState().getData()).setData((byte)0);
		block.getState().update();
	}
}