package q2p.quickclick;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import q2p.quickclick.client.Authorize;
import q2p.quickclick.client.ClientPool;

public class LogicTick implements Runnable {
	private long last = 0;
	private long tps = 0;
	private boolean needBenchmark = false;
	public void run() {
		if(needBenchmark) {
			if(System.currentTimeMillis()-last > 1000) {
				last = System.currentTimeMillis();
				Log.adminInfo("Benchmark " + tps+" tps");
				tps = 0;
			}
			tps++;
		}
		WorldManager.lobby.setFullTime(6000);
		//TODO:
		//WorldManager.lobby.setFullTime(Assist.worldTimeFromRealTime());
		Parcour.tick();
	}
	
	public boolean benchmarkCommand(Command command, CommandSender sender, String[] args) {
		if(!command.getName().equals("benchmark") || !ClientPool.isAdmin(sender)) return false;
		if(sender instanceof Player && !ClientPool.logged((Player) sender)) sender.sendMessage(Authorize.LOG_ACT);
		else {
			needBenchmark = !needBenchmark;
			tps = 0;
			last = System.currentTimeMillis();
			Log.consoleAdminInfo("Benchmark turned "+(needBenchmark?"on":"off")+".");
		}
		return true;
	}
}