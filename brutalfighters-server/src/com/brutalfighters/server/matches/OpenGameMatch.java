package com.brutalfighters.server.matches;

import java.util.Iterator;
import java.util.Map;

public class OpenGameMatch extends GameMatch {
	
	/*
	 * 
	 * Warmup: 20sec
	 * Players: 6
	 * Win State: 3 flags
	 * Respawn: 6sec
	 * Match Finish: 6sec
	 * 
	 */
	
	/* Constructors */
	public OpenGameMatch(String mapName, String ID) {
		super(mapName, ID);
	}

	/* Updates */
	@Override
	public void updateMatch(Iterator<Map.Entry<String,GameMatch>> iter) {
		if(isFull() && isOpen()) {
			System.out.println("Yay! an open match is ready to be closed! :-)"); //$NON-NLS-1$
			close();
			GameMatchManager.closedMatches().getMatch(GameMatchManager.closedMatches().setupMatch(getMapName(), getPlayers(), teams)).startMatch();
			iter.remove();
		}
	}
}