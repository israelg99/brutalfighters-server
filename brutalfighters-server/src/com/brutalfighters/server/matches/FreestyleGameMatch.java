package com.brutalfighters.server.matches;

import java.util.Iterator;
import java.util.Map;

import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.players.Champion;
import com.brutalfighters.server.data.players.PlayerMap;
import com.brutalfighters.server.data.players.fighters.Fighter;
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

		fighter = Character.toUpperCase(fighter.charAt(0)) + fighter.substring(1);
		
		// Checking if the fighter name the client passed does in fact exist
		if(!Champion.contains(fighter)) {
			return;
		}
		
		// Setting up the information needed for getting the fighter
		int team = teams[0].size() < teams[1].size() ? TEAM1 : TEAM2;
		Base base = getMap().getBase(team);
		
		// Getting the fighter
		Fighter player = Champion.valueOf(fighter).getNew(base, m_id);
		player.getPlayer().team = team;
		
		// Adding the fighter into the data arrays
		teams[team].put(connection, player);
		players.put(connection, teams[team].get(connection));
		
		// In freestyle we send resources immediately
		approveResources(connection);
	}
}