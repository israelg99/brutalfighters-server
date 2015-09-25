package com.brutalfighters.server.util;

public class Score {
	private int flags[];
	private int kills[];
	
	public Score() {
		setFlags(new int[] {0,0});
		setKills(new int[] {0,0});
	}

	public int[] getFlags() {
		return flags;
	}
	public void setFlags(int[] flags) {
		this.flags = flags;
	}

	public int[] getKills() {
		return kills;
	}
	public void setKills(int[] kills) {
		this.kills = kills;
	}
}
