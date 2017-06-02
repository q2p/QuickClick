package q2p.quickclick.match.level.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class LavaBase implements BlockBase {
	public void place(Block block) {
		block.setType(Material.LAVA);
		block.getState().update();
	}
}