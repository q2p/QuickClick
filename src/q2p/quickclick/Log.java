package q2p.quickclick;

import java.util.logging.Logger;
import q2p.quickclick.client.ClientPool;

public class Log {
	private static Logger logger;
	
	public static void consoleInfo(String message) {
		logger.info(message);
	}
	public static void consoleWarn(String message) {
		logger.warning(message);
	}
	public static void adminInfo(String message) {
		if(ClientPool.mainAdmin.player != null) ClientPool.mainAdmin.player.sendMessage(message);
	}
	public static void consoleAdminInfo(String message) {
		adminInfo(message);
		consoleInfo(message);
	}
	public static void consoleAdminWarn(String message) {
		adminInfo(message);
		consoleWarn(message);
	}
	
	static void initLog(Logger logger) {
		Log.logger = logger;
	}
}