package q2p.quickclick;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import q2p.quickclick.client.Authorize;
import q2p.quickclick.client.ClientInfo;
import q2p.quickclick.client.ClientPool;

public class Parcour {
	private static final int MAX_MILIS = 60*60*1000;
	static long[] startPoint;
	static long[] endPoint;
	static long landingLevel;

	static ArrayList<ClientInfo> clis = new ArrayList<ClientInfo>();
	
	static boolean checkCommand(Command command, CommandSender sender, String[] args) {
		if(!command.getName().equals("parcour")) return false;
		if(args.length == 0) {
			if(!(sender instanceof Player)) return true;
			if(!ClientPool.logged((Player) sender)) {
				sender.sendMessage(Authorize.LOG_ACT);
				return true;
			}
			ClientInfo cli = ClientPool.getClient((Player) sender);
			if(cli.parcour.record == -1) sender.sendMessage("You never complited a run :(");
			else sender.sendMessage(ChatColor.WHITE + "Your record: " + ChatColor.GOLD + (int)(cli.parcour.record/1000)+"."+(cli.parcour.record%1000)+"sec"+ChatColor.WHITE+".");
		} else if(args.length == 1) {
			if(sender instanceof Player && !ClientPool.logged((Player) sender)) {
				sender.sendMessage(Authorize.LOG_ACT);
				return true;
			}
			ClientInfo cli = ClientPool.findClient(args[0], true);
			if(cli == null) sender.sendMessage("Player " + args[0]+" not in database.");
			else if(cli.parcour.record == -1) sender.sendMessage(cli.name+" never complited a run :(");
			else sender.sendMessage(ChatColor.WHITE + cli.name+"'s record: " + ChatColor.GOLD + (int)(cli.parcour.record/1000)+"."+(cli.parcour.record%1000)+"sec"+ChatColor.WHITE+".");
		} else {
			sender.sendMessage("Usage: /parcour [name]");
		}
		return true;
	}
	
	public static void stopParkour(ClientInfo cli) {
		if(cli.parcour.onRun) {
			cli.parcour.onRun = false;
			cli.parcour.started = -1;
			clis.remove(cli);
		}
	}
	
	static void tick() {
		for(int i = 0; i < clis.size(); i++) {
			ClientInfo cli = clis.get(i);
			long milis = System.currentTimeMillis() - cli.parcour.started;
			if(milis > MAX_MILIS) {
				stopParkour(cli);
				i--;
			}
		}
	}
	
	static void checkLanding(ClientInfo cli) {
		if(((Entity)cli.player).isOnGround()) {
			Location l = cli.player.getLocation();
			l.setY(l.getY() - 0.5);
			Block b = l.getBlock();
			long x = b.getX();
			long y = b.getY();
			long z = b.getZ();
			if(!cli.parcour.onRun) {
				if(x == startPoint[0] && y == startPoint[1] && z == startPoint[2]) {
					cli.parcour.onRun = true;
					cli.parcour.started = System.currentTimeMillis();
					clis.add(cli);
				}
			} else {
				if(x == endPoint[0] && y == endPoint[1] && z == endPoint[2]) {
					cli.parcour.onRun = false;
					long milis = System.currentTimeMillis() - cli.parcour.started;
					if(milis < cli.parcour.record || cli.parcour.record == -1) {
						cli.parcour.record = milis;
						cli.player.sendMessage(ChatColor.GREEN + "Yay! New record: " + ChatColor.GOLD + (int)(cli.parcour.record/1000)+"."+(cli.parcour.record%1000)+"sec"+ChatColor.GREEN+"!");
					} else cli.player.sendMessage(ChatColor.GREEN + "You complited a run with " + ChatColor.GOLD + (int)(milis/1000)+"."+(milis%1000)+"sec"+ChatColor.GREEN+"!");
					cli.parcour.started = -1;
					clis.remove(cli);
				} else if(y == landingLevel) {
					stopParkour(cli);
				}
			}
		}
	}
	
	static void unload() {
		while(!clis.isEmpty()) stopParkour(clis.get(0));
		startPoint = null;
		endPoint = null;
		landingLevel = 0;
	}
	
	static void load() {
		clis.clear();
		startPoint = new long[]{-1,-1,-1};
		endPoint = new long[]{-1,-1,-1};
		landingLevel = -1;
	}
}
