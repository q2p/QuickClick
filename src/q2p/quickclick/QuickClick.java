package q2p.quickclick;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import q2p.quickclick.client.Authorize;
import q2p.quickclick.client.ClientInfo;
import q2p.quickclick.client.ClientPool;
import q2p.quickclick.client.GameState;
import q2p.quickclick.match.MatchMaking;

/*
TODO:
/vovanlost
блокировать ip которые не смогли за логинится на 10 минут
замениеть System.out.println() на встроенную функцию bukkit'а (logger)
*/

public class QuickClick extends JavaPlugin implements Listener {
	private static final String[] blockCommands = new String[] {
			"ban",
			"banlist",
			"help",
			"list",
			"deop",
			"op",
			"pardon",
			"restart",
			"save-all",
			"reload",
			"stop",
			"achievement",
			"ban-ip",
			"banlist",
			"blockdata",
			"clear",
			"clone",
			"debug",
			"defaultgamemode",
			"difficulty",
			"effect",
			"enchant",
			"entitydata",
			"execute",
			"fill",
			"gamemode",
			"gamerule",
			"give",
			"kick",
			"kill",
			"me",
			"pardon-ip",
			"particle",
			"playsound",
			"plugins",
			"replaceitem",
			"save-off",
			"save-on",
			"say",
			"scoreboard",
			"seed",
			"setblock",
			"setidletimeout",
			"setworldspawn",
			"spawnpoint",
			"spreadplayers",
			"stats",
			"stopsound",
			"summon",
			"tell",
			"tellraw",
			"testfor",
			"testforblock",
			"testforblocks",
			"time",
			"timings",
			"title",
			"toggledownfall",
			"tp",
			"tps",
			"trigger",
			"version",
			"weather",
			"whitelist",
			"worldborder",
			"xp"
	};
	private static final String unknownCmd = "Unknown command. Type \"/howto\" for guide.";
	
	public void onEnable() {
		HubStatus.initilize(this);
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable() {
		HubStatus.deInitilize();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(GuideGenerator.parseCommand(command, sender, args)) return true;
		else if(News.parseCommand(command, sender, args)) return true;
		else if(ClientPool.checkVacationCommand(command, sender, args)) return true;
		else if(Authorize.checkCommand(command, sender, args)) return true;
		else if(Parcour.checkCommand(command, sender, args)) return true;
		else if(Modifications.checkCommand(command, sender, args)) return true;
		else if(HubStatus.logicTick.benchmarkCommand(command, sender, args)) return true;
		else if(MatchMaking.checkCommand(command, sender, args)) return true;
		else if(command.getLabel().equals("rail")) {
			if(args.length == 1 && sender instanceof Player) {
				Effect eff = Effect.getByName(args[0]);
				
				if(eff != null) {
					Player player = (Player) sender;
					Location start = player.getEyeLocation();
					Vector dir = start.getDirection().normalize().multiply(0.5);
					for(int i = 0; i < 128; i++) {
						player.playEffect(start, eff, null);
						start.add(dir);
					}
				}
			} else for(Effect e : Effect.values()) sender.sendMessage(e.getName());
		}
		else sender.sendMessage(unknownCmd);
		return true;
	}
	
	// Восстановление здоровья
	@EventHandler
	public void onRegainHealth(EntityRegainHealthEvent event) {
		event.setCancelled(true);
	}
	// Изменение уровня
	@EventHandler
	public void onLevelChange(PlayerLevelChangeEvent event) {
		if(event.getNewLevel() != 0) {
			event.getPlayer().setLevel(0);
		}
	}
	// MOTD
	@EventHandler
	public void onPing(ServerListPingEvent event) {
		event.setServerIcon(OutsideInfo.icons.get(HubStatus.random.nextInt(OutsideInfo.icons.size())));
		if(Modifications.isModified) event.setMotd("QuickClick: Server in modification mode.");
		else event.setMotd("QuickClick");
	}
	// Нажатие лкм/пкм
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		ClientInfo cli = ClientPool.getClient(event.getPlayer());
		HubStatus.plugin.getServer().broadcastMessage(cli.gameState.name());
		if(cli.gameState == GameState.MATCH) {
			if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				cli.matchInfo.match.onShoot(cli, event);
			}
		}
	}
	// При подкулючении к серверу
	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		ClientPool.acceptPlayer(event);
	}
	// Передвижение
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		ClientInfo cli = ClientPool.getClient(event.getPlayer());
		if(!ClientPool.logged(event.getPlayer())) Authorize.onMove(cli, event);
		if(cli.gameState == GameState.MATCH) MatchMaking.onMove(cli, event);
		if(cli.gameState == GameState.SPAWN) LobbySpawn.onMove(cli, event);
		WorldBoxes.checkMovement(event);
	}
	// При входе в игру (без логина)
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Authorize.onJoin(event);
	}
	// Отключение от сервера
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		ClientPool.onExit(event);
	}
	// Запрет крафта
	@EventHandler
	public void onCraft(CraftItemEvent event) {
		event.setCancelled(true);
	}
	// Уничтожение блока
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		event.setCancelled(!LobbySpawn.allowPlaceBreakBlock(ClientPool.getClient(event.getPlayer()), event.getBlock()));
	}
	@EventHandler
	public void onBlockDamage(BlockDamageEvent event) {
		event.setCancelled(!LobbySpawn.allowPlaceBreakBlock(ClientPool.getClient(event.getPlayer()), event.getBlock()));
	}
	// Постановка блока
	@EventHandler 
	public void onBlockPlace(BlockPlaceEvent event) {
		boolean allowed = LobbySpawn.allowPlaceBreakBlock(ClientPool.getClient(event.getPlayer()), event.getBlock());
		event.setBuild(allowed);
		event.setCancelled(!allowed);
	}
	// Декоративный огонь
	@EventHandler
	public void onBurn(BlockBurnEvent event) {
		event.setCancelled(true);
	}
	@EventHandler
	public void onIgnite(BlockIgniteEvent event) {
		if(event.getCause() != IgniteCause.FLINT_AND_STEEL || event.getPlayer() == null || !LobbySpawn.allowPlaceBreakBlock(ClientPool.getClient(event.getPlayer()), event.getBlock())) event.setCancelled(true);
	}
	@EventHandler
	public void onFade(BlockFadeEvent event) {
		if(event.getBlock().getType() == Material.FIRE) event.setCancelled(true);
	}
	// Смерть
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		event.setDroppedExp(0);
		event.setKeepInventory(false);
		event.setKeepLevel(false);
		event.setDeathMessage(null);
		ClientInfo cli = ClientPool.getClient(event.getEntity());
		if(cli.gameState == GameState.MATCH) MatchMaking.onDeath(cli);
	}
	// Возрождение
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		ClientInfo cli = ClientPool.getClient(event.getPlayer());
		switch(cli.gameState) {
		case AUTH:
		case OFFLINE:
			Authorize.onRespawn(event);
			break;
		case MATCH: MatchMaking.onRespawn(cli, event); break;
		case SPAWN: LobbySpawn.respawn(event); break;
		}
		Bukkit.createWorld(new Creator("pos"));
	}
	// Урон
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if(!(event.getEntity() instanceof Player)) return;
		ClientInfo cli = ClientPool.getClient((Player) event.getEntity());
		if(cli.gameState == GameState.MATCH) MatchMaking.onDamage(cli, event);
		else event.setCancelled(true);
	}
	// Никогда не голоден
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		event.setFoodLevel(20);
	}
	// Заглушка стандартных команд
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onCommandPreProcess(PlayerCommandPreprocessEvent pcpe) {
		String ref = pcpe.getMessage();
		if(ref.startsWith("/")) {
			int idx = pcpe.getMessage().indexOf(" ");
			if(idx == -1) idx = pcpe.getMessage().length();
			ref = ref.substring(1, idx).toLowerCase();
			for(int i = 0; i < blockCommands.length; i++) {
				if(ref.equals(blockCommands[i])) {
					pcpe.setCancelled(true);
					pcpe.getRecipients().clear();
					pcpe.getPlayer().sendMessage(unknownCmd);
					break;
				}
			}
		}
	}
	// Сообщение в чат
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		//TODO:
	}
	// Сброс предмета
	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}
	// Избавление от мусора
	@EventHandler
	public void onItemSpawn(ItemSpawnEvent event) {
		event.getEntity().remove();
	}
	// Постоянная не дождливая погода
	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		if(event.toWeatherState()){
			event.getWorld().setStorm(false);
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onThunerChange(ThunderChangeEvent event) {
		if(event.toThunderState()){
			event.getWorld().setStorm(false);
			event.setCancelled(true);
		}
	}
	// Молния
	public void onLightning(LightningStrikeEvent event) {
		event.setCancelled(true);
	}
	// Перетекание воды и лавы
	public void onFromTo(BlockFromToEvent event) {
		event.setCancelled(true);
	}
}
