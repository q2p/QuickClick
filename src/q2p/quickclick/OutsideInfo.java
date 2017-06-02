package q2p.quickclick;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.bukkit.util.CachedServerIcon;

public class OutsideInfo {
	public static final ArrayList<CachedServerIcon> icons = new ArrayList<CachedServerIcon>();
	public static final byte[][][] SKELETONS = new byte[][][] {{
		{1,1,1,1,1,1,1,1},
		{1,0,0,0,0,0,0,1},
		{1,0,2,2,2,2,0,1},
		{1,0,2,0,2,0,0,1},
		{1,0,2,2,2,2,0,1},
		{1,0,0,0,2,0,0,1},
		{1,0,0,0,0,0,0,1},
		{1,1,1,1,1,1,1,1}
	},{
		{1,1,1,1,1,1,1,1},
		{1,2,2,2,2,2,2,1},
		{1,2,0,0,0,0,2,1},
		{1,2,0,2,0,2,2,1},
		{1,2,0,0,0,0,2,1},
		{1,2,2,2,0,2,2,1},
		{1,2,2,2,2,2,2,1},
		{1,1,1,1,1,1,1,1}
	}};
	public static final short[][] COLORS = new short[][] {
		{0,127,0},{0,127,255},{127,0,255}
	};
	
	public static void reload() {
		generateIcons();
	}
	
	public static void generateIcons() {
		icons.clear();
		BufferedImage bi = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D)bi.getGraphics();
		for(byte s = 0; s < SKELETONS.length; s++) {
			for(byte c = 0; c < COLORS.length; c++) {
				for(byte y = 0; y < 8; y++) {
					for(byte x = 0; x < 8; x++) {
						byte i = SKELETONS[s][y][x];
						if(i == 1) g.setColor(new Color(0,0,0));
						else if(i == 2) g.setColor(new Color(255,255,255));
						else g.setColor(new Color(COLORS[c][0], COLORS[c][1], COLORS[c][2]));
						g.fillRect(x*8, y*8, 8, 8);
					}
				}
				try {
					icons.add(HubStatus.plugin.getServer().loadServerIcon(bi));
				} catch (Exception e) {}
			}
		}
	}
}
