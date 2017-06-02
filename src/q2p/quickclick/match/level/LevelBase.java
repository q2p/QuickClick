package q2p.quickclick.match.level;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import q2p.quickclick.Assist;
import q2p.quickclick.Log;
import q2p.quickclick.match.level.blocks.AirBase;
import q2p.quickclick.match.level.blocks.AndesiteBase;
import q2p.quickclick.match.level.blocks.BlockBase;
import q2p.quickclick.match.level.blocks.DirtBase;
import q2p.quickclick.match.level.blocks.GlowstoneBase;
import q2p.quickclick.match.level.blocks.GoldBlockBase;
import q2p.quickclick.match.level.blocks.GrassBase;
import q2p.quickclick.match.level.blocks.LavaBase;
import q2p.quickclick.match.level.blocks.StoneBase;
import q2p.quickclick.match.level.blocks.StoneBrickBase;
import q2p.quickclick.match.level.blocks.StoneBrickStairsBase;
import q2p.quickclick.match.level.blocks.StoneBrickStepBase;
import q2p.quickclick.match.level.specifications.LevelSpecification;
import q2p.quickclick.match.level.specifications.SpawnSpecification;
import q2p.quickclick.match.level.specifications.quake.QuakeSpecification;

public class LevelBase {
	public static final BlockBase[] BASES = new BlockBase[] {
			new AirBase(),
			new StoneBase(),
			new DirtBase(),
			new GrassBase(),
			new AndesiteBase(),
			new GoldBlockBase(),
			new GlowstoneBase(),
			new LavaBase(),
			new StoneBrickBase(),
			new StoneBrickStepBase(false),
			new StoneBrickStepBase(true),
			new StoneBrickStairsBase(false, BlockFace.NORTH),
			new StoneBrickStairsBase(false, BlockFace.SOUTH),
			new StoneBrickStairsBase(false, BlockFace.WEST),
			new StoneBrickStairsBase(false, BlockFace.EAST),
			new StoneBrickStairsBase(true, BlockFace.NORTH),
			new StoneBrickStairsBase(true, BlockFace.SOUTH),
			new StoneBrickStairsBase(true, BlockFace.WEST),
			new StoneBrickStairsBase(true, BlockFace.EAST)
	};
	public static final int DIAGONAL_DISTANCE = (int)Math.ceil(Assist.distance(new Location(null, 0, 0, 0), new Location(null, 128, 48, 128)));
	public boolean completed = false;
	public String fileName;
	public LevelSpecification specification;
	public short[] size; 
	public byte[][][] map;
	
	public LevelBase(File mapFile) {
		fileName = mapFile.getName().substring(0, mapFile.getName().length()-".map".length());
		DataInputStream dis = null;
		try { dis = new DataInputStream(new FileInputStream(mapFile));
		} catch (FileNotFoundException e) {
			Log.consoleWarn("Can't read from file " + mapFile.getAbsolutePath());
			return;
		}
		try {
			/*
			spec byte
			info many
			size[3] short
			blocks[xyz] byte
			*/
			specification = getSpecififcation(dis.readByte());
			if(!specification.readData(dis)) throw new IOException();
			System.out.println(mapFile.getName()+" 2");
			size = new short[]{dis.readShort(), dis.readShort(), dis.readShort()};
			map = new byte[size[2]][size[1]][size[0]];
			System.out.println(size[0]+" "+size[1]+" "+size[2]);
			for(short z = 0; z < size[2]; z++) {
				for(short y = 0; y < size[1]; y++) {
					for(short x = 0; x < size[0]; x++) {
						map[z][y][x] = dis.readByte();
						System.out.println((specification instanceof QuakeSpecification?"q":"s")+" "+map[z][y][x]);
					}
				}
			}
			System.out.println(mapFile.getName()+" 4");
			completed = true;
		} catch (IOException e) {
			try {dis.close();} catch (IOException e1) {}
			Log.consoleWarn("Can't read from file " + mapFile.getAbsolutePath());
			return;
		}
		try {dis.close();} catch (IOException e1) {}
	}
	
	private static LevelSpecification getSpecififcation(byte b) {
		switch(b) {
		case 0: return new SpawnSpecification();
		case 1: return new QuakeSpecification();
		default: return null;
		}
	}
}