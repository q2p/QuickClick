package q2p.quickclick.match;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import q2p.quickclick.HubStatus;
import q2p.quickclick.WorldManager;
import q2p.quickclick.client.ClientInfo;
import q2p.quickclick.client.ClientPool;
import q2p.quickclick.client.GameState;
import q2p.quickclick.match.level.LevelBase;
import q2p.quickclick.match.level.specifications.quake.QuakeSpecification;
import q2p.quickclick.match.weapons.Gun;
import q2p.quickclick.match.weapons.GunManager;
import q2p.quickclick.match.weapons.RailGun;
import q2p.quickclick.match.weapons.ThunderGun;

public class Match implements Runnable {
	public ArrayList<ClientInfo> players = new ArrayList<ClientInfo>();
	MatchState matchState = MatchState.PREPARE;
	public LevelBase base;
	private boolean isEnding = false;
	private int runId;
	static final long RESPAWN_TIME = 5000;
	static World world;
	
	Match(ClientInfo cli1, ClientInfo cli2, LevelBase levelBase) {
		base = levelBase;
		world = WorldManager.generateAndFillWorld("match"+MatchMaking.liveMatches.size(), levelBase);
		matchState = MatchState.LIVE;
		MatchMaking.onJoin(cli1, this);
		MatchMaking.onJoin(cli2, this);
        runId = Bukkit.getScheduler().scheduleSyncRepeatingTask(HubStatus.plugin, this, 1, 1);
	}
	
	void leave(ClientInfo cli) {
		players.remove(cli);
		if(!isEnding) endMatch();
	}

	private void endMatch() {
		isEnding = true;
		Bukkit.getScheduler().cancelTask(runId);
		while(!players.isEmpty()) MatchMaking.onLeave(players.remove(0));
		WorldManager.deleteWorld(world);
		MatchMaking.liveMatches.remove(this);
	}

	void join(ClientInfo cli) {
		players.add(cli);
		cli.gameState = GameState.MATCH;
		cli.matchInfo.match = this;
		spawn(cli);
	}

	private void spawn(ClientInfo cli) {
		cli.matchInfo.isDead = false;
		cli.player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 50000, 0, true, false));
		cli.player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 50000, 1, true, false));
		cli.player.setHealthScale(40);
		cli.player.setMaxHealth(40);
		cli.player.setHealth(20);
		GunManager.give(cli, new RailGun());
		GunManager.give(cli, new ThunderGun());
		cli.player.teleport(getSpawnLocation());
	}
	
	public void damage(ClientInfo target, ClientInfo damager, double damage) {
		double newHealth = target.player.getHealth() - damage;
		if(newHealth <= 0) onDeath(target);
		else target.player.setHealth(newHealth);
	}

	private Location getSpawnLocation() {
		return ((QuakeSpecification)base.specification).spawns.get(HubStatus.random.nextInt(((QuakeSpecification)base.specification).spawns.size())).getLocation(world);
	}

	public void onShoot(ClientInfo shooter, PlayerInteractEvent event) {
		Gun gun = GunManager.getByItem(event.getItem(), shooter);
		if(gun != null) gun.onShoot(shooter, event);
	}

	void respawn(ClientInfo cli, PlayerRespawnEvent event) {
		event.setRespawnLocation(getRespawnRoom());
	}

	private Location getRespawnRoom() {
		// TODO:
		return new Location(world, 0.5, 128, 0.5);
	}
	
	void onDeath(ClientInfo cli) {
		cli.matchInfo.deathTime = System.currentTimeMillis();
		cli.matchInfo.isDead = true;
		GunManager.onDeath(cli);
		cli.player.getInventory().clear();
		cli.player.updateInventory();
		cli.player.setHealthScale(20);
		cli.player.setMaxHealth(20);
		cli.player.setHealth(20);
		cli.player.teleport(getRespawnRoom());
	}

	void onDamage(ClientInfo cli, EntityDamageEvent event) {
		event.setCancelled(true);
		if(event.getCause() == DamageCause.FALL || event.getCause() == DamageCause.ENTITY_ATTACK) return;
		damage(ClientPool.getClient((Player)event.getEntity()), null, event.getDamage());
	}

	public void run() {
		checkRespawn();
	}

	public void checkPortals() {
		
	}

	private void checkRespawn() {
		for(ClientInfo cli : players) {
			if(cli.matchInfo.isDead && System.currentTimeMillis() - cli.matchInfo.deathTime >= RESPAWN_TIME && !cli.player.isDead()) {
				spawn(cli);
			}
		}
	}

	void onMove(ClientInfo cli, PlayerMoveEvent event) {
		if(cli.matchInfo.isDead) {
			event.setTo(getRespawnRoom());
			return;
		}
		if(WorldManager.behinLimit(event.getPlayer().getLocation())) onDeath(cli);
		QuakeSpecification qs = (QuakeSpecification)base.specification;
		for(int i = 0; i < qs.portals.size(); i++) qs.portals.get(i).checkPlayer(cli, event);
		for(int i = 0; i < qs.launchPads.size(); i++) qs.launchPads.get(i).checkPlayer(cli, event);
	}
}