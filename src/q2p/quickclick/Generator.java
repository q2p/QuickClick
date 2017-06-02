package q2p.quickclick;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

public class Generator extends ChunkGenerator {
	public ChunkData generateChunkData(World world, Random random, int x, int z, ChunkGenerator.BiomeGrid biome) {
		return createChunkData(world);
	}
	
	public List<BlockPopulator> getDefaultPopulators(World world) {
		return new ArrayList<BlockPopulator>();
	}
}
