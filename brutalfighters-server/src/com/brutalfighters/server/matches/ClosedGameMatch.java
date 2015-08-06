package com.brutalfighters.server.matches;

import java.util.Iterator;
import java.util.Map;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.players.PlayerMap;

public class ClosedGameMatch extends GameMatch {
	
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
	
	public ClosedGameMatch(String mapName, String ID, PlayerMap players, PlayerMap[] teams) {
		super(mapName, ID, players, teams);
	}

	/* Updates */
	@Override
	public void updateMatch(Iterator<Map.Entry<String,GameMatch>> iter) {
		if(!gameFinished(iter)) { // We can split this method into 2 methods, for more security.
			if(WARMUP.isFinished()) {
				updateGame();
				updateClients();
			} else {
				// Although we update the warmup first and therefore skip the first second of update,
				// the client has the warmup already loaded.
				updateWarmup();
			}
		} else if(FINISH.getCounter() > 0) {
			FINISH.subCounter(GameServer.getDelay());
		} else {
			removeMatch(iter);
		}
	}
}