package q2p.quickclick.client;

import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import q2p.quickclick.HubStatus;

public class CliMeta implements MetadataValue {
	final ClientInfo clientInfo;
	
	public CliMeta(final ClientInfo clientInfo) {
		this.clientInfo = clientInfo;
	}
	
	public Object value() {
		return clientInfo;
	}
	
	public boolean asBoolean() {return false;}
	public byte asByte() {return 0;}
	public double asDouble() {return 0;}
	public float asFloat() {return 0;}
	public int asInt() {return 0;}
	public long asLong() {return 0;}
	public short asShort() {return 0;	}
	public String asString() {return null;}
	public Plugin getOwningPlugin() {return HubStatus.plugin;}
	public void invalidate() {}
}
