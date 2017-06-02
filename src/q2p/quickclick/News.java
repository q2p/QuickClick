package q2p.quickclick;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import q2p.quickclick.client.Authorize;
import q2p.quickclick.client.ClientPool;

public class News {
	//TODO: сделать отдельный класс для инфы о новости
	static ArrayList<String> titles = new ArrayList<String>();
	static ArrayList<String> texts = new ArrayList<String>();
	static ArrayList<Long> dates = new ArrayList<Long>();
		
	private static final byte ARTICLES_ON_PAGE = 8;
	
	static boolean parseCommand(Command command, CommandSender sender, String[] args) {
		if(!command.getName().equals("news")) return false;
		if(args.length == 0) sender.sendMessage(buildNewsList(1));
		else {
			if(args[0].equals("read")) {
				if(args.length != 2) {
					sender.sendMessage(ChatColor.RED + "Usage: /news read <number>");
					return true;
				}
				int id;
				try { id = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					sender.sendMessage(ChatColor.RED + "Usage: /news read <number>");
					return true;
				}
				if(id < 1 || id > titles.size()) {
					sender.sendMessage(ChatColor.RED + "News number "+id+" don't exist.");
					return true;
				}
				sender.sendMessage(buildArticle(id));
			} else if(args[0].equals("add")) {
				if(!ClientPool.isAdmin(sender)) return false;
				if(sender instanceof Player && !ClientPool.logged((Player) sender)) {
					sender.sendMessage(Authorize.LOG_ACT);
					return true;
				}
				String total = "";
				for(int i = 1; i < args.length; i++) total += args[i]+" ";
				if(!total.contains(":=:")) sender.sendMessage(ChatColor.RED + "Usage: /news add <title :=: text>");
				else {
					String title = total.substring(0, total.indexOf(":=:")).trim();
					total = total.substring(total.indexOf(":=:")+3, total.length()).trim();
					addNews(title, total);
					sender.sendMessage("News was added succesfully");
				}
			} else if(args[0].equals("remove")) {
				if(!ClientPool.isAdmin(sender)) return false;
				if(sender instanceof Player && !ClientPool.logged((Player) sender)) {
					sender.sendMessage(Authorize.LOG_ACT);
					return true;
				}
				if(args.length != 2) sender.sendMessage(ChatColor.RED + "Usage: /news remove <number>");
				else {
					int id;
					try { id = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED + "Usage: /news remove <number>");
						return true;
					}
					if(id < 1 || id > titles.size()) sender.sendMessage(ChatColor.RED + "News number "+id+" don't exist.");
					else {
						removeNews(id);
						sender.sendMessage(ChatColor.GOLD + "News was removed succesfully.");
					}
				}
			} else if(args[0].equals("edit")) {
				if(!ClientPool.isAdmin(sender)) return false;
				if(sender instanceof Player && !ClientPool.logged((Player) sender)) {
					sender.sendMessage(Authorize.LOG_ACT);
					return true;
				}
				String total = "";
				if(args.length > 1) {
					int id;
					try { id = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED + "Usage: /news edit <number> <title :=: text>");
						return true;
					}
					if(id < 1 || id > titles.size()) sender.sendMessage(ChatColor.RED + "News number "+id+" don't exist.");
					else {
						for(int i = 2; i < args.length; i++) total += args[i]+" ";
						if(!total.contains(":=:")) sender.sendMessage(ChatColor.RED + "Usage: /news edit <number> <title :=: text>");
						else {
							String title = total.substring(0, total.indexOf(":=:")).trim();
							total = total.substring(total.indexOf(":=:")+3, total.length()).trim();
							editNews(id, title, total);
							sender.sendMessage("News was edited succesfully");
						}
					}
				} else sender.sendMessage(ChatColor.RED + "Usage: /news edit <number> <title :=: text>");
			} else {
				int page;
				try { page = Integer.parseInt(args[0]);
				} catch (NumberFormatException e) {
					sender.sendMessage(ChatColor.RED + "Usage: /news [arguments...]");
					return true;
				}
				if(args.length != 1) sender.sendMessage(ChatColor.RED + "Usage: /news <page>");
				else sender.sendMessage(buildNewsList(page));
			}			
		}
		return true;
	}
	
	static void load() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(Assist.getByPath("/news.txt", true)));
			String line;
			line = br.readLine();
			if(line != null) {
				while(true) {
					line = br.readLine();
					if(line == null) break;
					titles.add(line);
					texts.add(br.readLine());
					dates.add(Long.parseLong(br.readLine()));
				}
			}
			br.close();
		} catch (IOException e) {}
	}

	static String longToDate(long l) {
		Calendar c = Assist.getDefaultCalendar();
		c.setTimeInMillis(l);
		String mounth = ("0"+(c.get(Calendar.MONTH)+1));
		mounth = mounth.substring(mounth.length()-2);
		String day = ("0"+c.get(Calendar.DAY_OF_MONTH));
		day = day.substring(day.length()-2);
		return ""+c.get(Calendar.YEAR)+"."+mounth+"."+day;
	}
	
	static void unload() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(Assist.getByPath("/news.txt", true)));
			while(!titles.isEmpty()) bw.write("\n"+titles.remove(0)+"\n"+texts.remove(0)+"\n"+dates.remove(0));
			bw.flush();
			bw.close();
		} catch (IOException e) {}
	}
	
	static void addNews(String title, String text) {
		titles.add(title);
		texts.add(text);
		Calendar c = Assist.getDefaultCalendar();
		c.setTimeInMillis(new Date().getTime());
		dates.add(c.getTimeInMillis());
	}
		
	private static void editNews(int id, String title, String text) {
		id = titles.size()-id;
		titles.set(id, title);
		texts.set(id, text);
	}

	private static void removeNews(int id) {
		id = titles.size()-id;
		titles.remove(id);
		texts.remove(id);
		dates.remove(id);
	}

	static String buildArticle(int id) {
		String message = ChatColor.GREEN + "Article number "+id+":\n";
		id = titles.size()-id;
		message += ChatColor.BLUE + "Date: " + ChatColor.WHITE + longToDate(dates.get(id)) + "\n";
		message += ChatColor.BLUE + "Title: " + ChatColor.WHITE + titles.get(id) + "\n";
		message += ChatColor.BLUE + "Text: " + ChatColor.WHITE + texts.get(id);
		return message;
	}

	private static String buildNewsList(int page) {
		if(ARTICLES_ON_PAGE*(page-1) >= titles.size() || page < 1) return ChatColor.YELLOW + "No news on page "+page+"."; 
		String message = ChatColor.GREEN + "News on page "+page+":\n";
		page--;
		int top = Math.max(0, (titles.size()-1)-page*ARTICLES_ON_PAGE);
		int bot = Math.max(0, top - (ARTICLES_ON_PAGE-1));
		for(int i = top; i >= bot; i--) {
			message += "" + ChatColor.GREEN + (titles.size()-i) + ". "+ChatColor.BLUE + longToDate(dates.get(i))+ " - " + ChatColor.WHITE + titles.get(i);
			if(i != bot) message += "\n";
		}
		return message;
	}
}
