package q2p.quickclick.match.level.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Stairs;

public class StoneBrickStairsBase implements BlockBase {
	private BlockFace face;
	private boolean inverted;
	public StoneBrickStairsBase(boolean isInverted, BlockFace face) {
		inverted = isInverted;
		this.face = face;
	}
	public ItemStack getStack() {
		return (face == BlockFace.NORTH && inverted == false)?new Stairs(Material.SMOOTH_STAIRS).toItemStack(1):null;
	}
	public boolean compare(Block block) {
		return (block.getType() == Material.SMOOTH_STAIRS && ((Stairs)block.getState().getData()).getFacing().getOppositeFace() == face && ((Stairs)block.getState().getData()).isInverted() == inverted);
	}
	public void place(Block block) {
		block.setType(Material.SMOOTH_STAIRS);
		BlockState state = block.getState();
		Stairs stairs = new Stairs(Material.SMOOTH_STAIRS);
		stairs.setInverted(inverted);
		stairs.setFacingDirection(face);
		state.setData(stairs);
		state.update();
	}
}