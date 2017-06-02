package q2p.quickclick;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public final class ConfigFile {
	private static BufferedWriter bw;
	private static BufferedReader br;
	
	public static final void load(final String relativePath) throws Exception {
		br = new BufferedReader(new FileReader(Assist.getByPath(relativePath, true)));
	}
	public static final String get() throws Exception {
		if(br != null) {
			try {
				return br.readLine();
			} catch(Exception e) {
				try {
					br.close();
				} catch(Exception e2) {}
				throw e;
			}
		}
		return null;
	}
	
	public static final void save(final String relativePath) throws Exception {
			bw = new BufferedWriter(new FileWriter(Assist.getByPath(relativePath, true)));
	}
	public static final void put(final String string) throws Exception {
		if(bw != null) {
			try {
				bw.write(string);
			} catch(Exception e) {
				try {
					bw.close();
				} catch(Exception e2) {}
				throw e;
			}
		}
	}
	public static final void flush() throws Exception {
		if(bw != null) {
			try {
				bw.flush();
			} catch(Exception e) {
				try {
					bw.close();
				} catch(Exception e2) {}
				throw e;
			}
		}
	}
	
	public static final void dispose() {
		if(bw != null) {
			try {
				bw.flush();
			} catch(Exception e) {}
			try {
				bw.close();
			} catch(Exception e) {}
			bw = null;
		}
		if(br != null) {
			try {
				br.close();
			} catch(Exception e) {}
			br = null;
		}
	}
}