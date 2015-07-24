package com.brutalfighters.server.matches;

public enum GameMode {
	MATCH, FREESTYLE;
	
	public static boolean contains(String gamemode) {
		for (GameMode gm : GameMode.values()) {
			if (gm.name().equals(gamemode)) {
				return true;
			}
		}
		return false;
	}
}
