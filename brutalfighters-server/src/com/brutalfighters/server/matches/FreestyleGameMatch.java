package com.brutalfighters.server.matches;

import java.util.Iterator;
import java.util.Map;

import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.players.PlayerData;
import com.brutalfighters.server.data.players.PlayerMap;
import com.brutalfighters.server.data.players.StaticPlayer;
import com.brutalfighters.server.util.Counter;
import com.esotericsoftware.kryonet.Connection;

public class FreestyleGameMatch extends GameMatch {
	
	/*
	 * 
	 * Warmup: 20sec
	 * Players: 6
	 * Win State: 3 flags
	 * Respawn: 6sec
	 * Match Finish: 6sec
	 * 
	 */
	
	protected static final int DEFAULT_PLAYER_LIMIT = 50;
	protected static final int DEFAULT_WARMUP = 0;
	
	/* Constructors */
	public FreestyleGameMatch(String mapName, String ID, PlayerMap players, PlayerMap[] teams) {
		super(mapName, ID, players, teams);
		setupFreestyle();
	}
	public FreestyleGameMatch(String mapName, String ID) {
		super(mapName, ID);
		setupFreestyle();
	}
	public void setupFreestyle() {
		this.WARMUP = new Counter(DEFAULT_WARMUP);
		this.playerLimit = DEFAULT_PLAYER_LIMIT;
	}
	
	/* Updates */
	@Override
	public void updateMatch(Iterator<Map.Entry<String,GameMatch>> iter) {
		if(!gameFinished(iter)) {
			updateGame();
			updateClients();
		}
	}
	
	@Override
	protected boolean gameFinished(Iterator<Map.Entry<String,GameMatch>> iter) {
		return checkEmpty(iter);
	}
	
	// Players / players Control
	@Override
	public void addPlayer(Connection connection, String m_id, String fighter) {
		PlayerData player;
		
		int team = teams[0].size() < teams[1].size() ? TEAM1 : TEAM2;
		Base base = getMap().getBase(team);
		player = StaticPlayer.getNewPlayer(fighter, base.pos.x, base.pos.y, base.flip, m_id);
		player.team = team;
		teams[team].put(connection, player);
		
		players.put(connection, player);
		
		approveResources(connection);
	}
}