package q2p.quickclick.match;

import java.util.ArrayList;
import q2p.quickclick.match.weapons.Gun;

public class MatchInfo {
	public Match match = null;
	public long deathTime;
	public boolean isDead = false;
	public ArrayList<Gun> inventory = new ArrayList<Gun>();
}