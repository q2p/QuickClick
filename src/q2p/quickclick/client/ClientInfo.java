package q2p.quickclick.client;

import org.bukkit.entity.Player;
import q2p.quickclick.ParcourInfo;
import q2p.quickclick.match.MatchInfo;

public class ClientInfo {
	public String name = "";
	String about = "";
	boolean banned = false;
	public Player player = null;
	public ParcourInfo parcour = new ParcourInfo();
	AuthorizeInfo auth = new AuthorizeInfo();
	public MatchInfo matchInfo = new MatchInfo();
	public GameState gameState = GameState.OFFLINE;
}
