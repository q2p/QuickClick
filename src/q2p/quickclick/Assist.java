package q2p.quickclick;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public final class Assist {
	static final String validLetters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static final String validDigits = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	public static final float modelHeight = 1.875f;
	public static final float legsHeadDiam = 0.5625f;
	public static final float legsHeadRadius = legsHeadDiam*0.5f;
	public static final float legsHeadStandDiam = 0.3f;
	public static final float legsHeadStandRadius = legsHeadStandDiam*0.5f;
	
	//TODO: нужно?
	public static int worldTimeFromRealTime() {
		return (int)(getDefaultCalendar().getTimeInMillis()%(24*60*60*1000)/(60*60));
	}
	public static Calendar getDefaultCalendar(){
		return Calendar.getInstance(TimeZone.getTimeZone("GMT"), new Locale("en"));
	}
	public static boolean isValidString(String str, boolean digitsAllowed) {
		String charset = digitsAllowed?validDigits:validLetters;
		for(int i = 0; i < str.length(); i++) if(!charset.contains(""+str.charAt(i))) return false;
		return true;
	}
	public static String readString(DataInputStream dis) throws IOException {
		byte[] b = new byte[dis.readByte()];
		dis.read(b);
		return new String(b, StandardCharsets.UTF_8);
	}
	public static void writeString(DataOutputStream dos, String str) {
		try {
			byte[] b = str.getBytes(StandardCharsets.UTF_8);
			dos.writeInt(b.length);
			dos.write(b);
		} catch (IOException e) {}
	}
	public static File getByPath(String path, boolean isFile) {
		File f = new File("QuickClickData"+path);
		try {
			if(f.exists() && (!f.isFile() == isFile)) f.delete();
			if(!f.exists()) {
				if(isFile) f.createNewFile();
				else f.mkdir();
			}
		} catch (IOException e) {}
		return f;
	}
	public static boolean isArgument(int id, String argument, String[] args){
		if(args.length-1 < id || !args[id].equals(argument)) return false;
		return true;
	}
	public static String generateHash(int length, boolean digitsAllowed) {
		String charset = digitsAllowed?validDigits:validLetters;
		String string = "";
		for(;length != 0; length--) string += charset.charAt(HubStatus.random.nextInt(charset.length()));
		return string;
	}
	public static Location rayTrace(Location start, Vector ray, double startX, double startY, double startZ, double endX, double endY, double endZ) {
		double dirfracx = 1.0 / ray.getX();
		double dirfracy = 1.0 / ray.getY();
		double dirfracz = 1.0 / ray.getZ();
		
		double t1 = (startX - start.getX())*dirfracx;
		double t2 = (endX - start.getX())*dirfracx;
		double t3 = (startY - start.getY())*dirfracy;
		double t4 = (endY - start.getY())*dirfracy;
		double t5 = (startZ - start.getZ())*dirfracz;
		double t6 = (endZ - start.getZ())*dirfracz;
		
		double tmin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));
		double tmax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));
		
		if(tmax >= 0 && tmin <= tmax) return start.add(ray.multiply(tmin));
		return null;
	}
	public static Location rayPlayer(Player source, Player target) {
		return rayTrace(source.getEyeLocation(), source.getEyeLocation().getDirection().normalize(), target.getLocation().getX() - Assist.legsHeadRadius, target.getLocation().getY(), target.getLocation().getZ() - Assist.legsHeadRadius, target.getLocation().getX() + Assist.legsHeadRadius, target.getLocation().getY() + Assist.modelHeight, target.getLocation().getZ() + Assist.legsHeadRadius);
	}
	public static float distanceToAABB(Location start, double startX, double startY, double startZ, double endX, double endY, double endZ) {
		double dx = Math.max(Math.max(startX - start.getX(), start.getX() - endX), 0);
		double dy = Math.max(Math.max(startY - start.getY(), start.getY() - endY), 0);
		double dz = Math.max(Math.max(startZ - start.getZ(), start.getZ() - endZ), 0);
	    return (float) Math.sqrt(dx*dx + dy*dy + dz*dz);
	}
	public static float distanceToPlayer(Location start, Player target) {
		return distanceToAABB(start, target.getLocation().getX() - Assist.legsHeadRadius, target.getLocation().getY(), target.getLocation().getZ() - Assist.legsHeadRadius, target.getLocation().getX() + Assist.legsHeadRadius, target.getLocation().getY() + Assist.modelHeight, target.getLocation().getZ() + Assist.legsHeadRadius);
	}
	public static float distance(Location location1, Location location2) {
		float dx = (float)(location1.getX() - location2.getX());
	    float dy = (float)(location1.getY() - location2.getY());
	    float dz = (float)(location1.getZ() - location2.getZ());

	    return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	public static final void abort(final String reason) {
		System.out.println(reason);
		System.exit(1);
	}
	public static final void abort(final Exception e) {
		e.printStackTrace();
		System.exit(1);
	}
	public static BlockFace byteToDir(byte b) {
		switch(b) {
		case 0: return BlockFace.NORTH;
		case 1: return BlockFace.SOUTH;
		case 2: return BlockFace.WEST;
		default: return BlockFace.EAST;
		}
	}
	public static boolean aabbXaabb(double x1, double y1, double z1, double x2, double y2, double z2, double xSize1, double ySize1, double zSize1, double xSize2, double ySize2, double zSize2) {
		return	(Math.abs(x1 - x2) * 2 < (xSize1 + xSize2)) && (Math.abs(y1 - y2) * 2 < (ySize1 + ySize2)) && (Math.abs(z1 - z2) * 2 < (zSize1 + zSize2));
	}
	public static void sendTitle(Player player, String title, String subTitle, int fadeIn, int hold, int fadeOut) {
		try {
			sendPacket(player, getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class).newInstance(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null), getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\""+title+"\"}"), fadeIn, hold, fadeOut));
			sendPacket(player, getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class).newInstance(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null), getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\""+subTitle+"\"}"), fadeIn, hold, fadeOut));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void sendParticle(Player player, String name, boolean distance, Location location, float offX, float offY, float offZ, float speed, int amount) {
		try {
			sendPacket(player, getNMSClass("PacketPlayOutWorldParticles").getConstructor(getNMSClass("EnumParticle"), boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class, int[].class).newInstance(getNMSClass("EnumParticle").getField(name).get(null), distance, (float)location.getX(), (float)location.getY(), (float)location.getZ(), offX, offY, offZ, speed, amount, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Class<?> getNMSClass(String name) {
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		try { return Class.forName("net.minecraft.server." + version + "." + name); }
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}