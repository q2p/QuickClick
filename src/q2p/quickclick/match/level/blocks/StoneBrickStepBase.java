package q2p.quickclick.match.level.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Step;

public class StoneBrickStepBase implements BlockBase {
	private boolean inverted;
	public StoneBrickStepBase(boolean isInverted) {
		inverted = isInverted;
	}
	public ItemStack getStack() {
		return !inverted?new Step(Material.SMOOTH_BRICK).toItemStack(1):null;
	}
	public boolean compare(Block block) {
		return (block.getType() == Material.STEP && ((Step)block.getState().getData()).getMaterial() == Material.SMOOTH_BRICK && ((Step)block.getState().getData()).isInverted() == inverted);
	}
	public void place(Block block) {
		block.setType(Material.STEP);
		BlockState state = block.getState();
		Step step = new Step(Material.SMOOTH_BRICK);
		step.setInverted(inverted);
		state.setData(step);
		state.update();
	}
}