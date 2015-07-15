package com.brutalfighters.server.matches;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import com.brutalfighters.server.data.maps.MapManager;
import com.brutalfighters.server.data.players.PlayerData;
import com.esotericsoftware.kryonet.Connection;

public class GameMatches {
	private HashMap <String, GameMatch> matches;
	private HashMap <Connection, String> players;
	
	public GameMatches() {
		matches = new HashMap<String, GameMatch>();
		players = new HashMap<Connection, String>();
	}
	
	// Getters and Setters
	public HashMap<String, GameMatch> getMatches() {
		return matches;
	}
	public HashMap<Connection, String> getPlayers() {
		return players;
	}
	
	public GameMatch getMatch(String id) {
		if(matches.containsKey(id)) {
			return matches.get(id);
		}
		return null;
	}
	public GameMatch getMatch(Connection cnct) {
		return getMatch(players.get(cnct));
	}
	
	public void addMatch(String ID, GameMatch match) { // It adds the players and the match into the HashMaps.
		for(Entry<Connection, PlayerData> entry : match.getPlayers().entrySet()) {
		    players.put(entry.getKey(), ID);
		}

		matches.put(ID, match);
	}
	public String setupMatch(String ID, GameMatch match) { // It setups a match, with a SKID and stuff.
		System.out.println("Got key? " + ID); //$NON-NLS-1$
		while(isKey(ID)) {
			ID = GameMatchManager.nextSecureKeyID();
		}
		System.out.println("YES THE KEY IS " + ID); //$NON-NLS-1$
		match.changeID(ID);
		addMatch(ID, match);
		
		return ID;
	}
	public String setupMatch(Entry<String, GameMatch> match) {
		return setupMatch(match.getKey(), match.getValue());
	}
	public String setupMatch(String ID) {
		return setupMatch(ID, new GameMatch(MapManager.getDefaultMap()));
	}
	public String setupMatch(GameMatch match) {
		return setupMatch(GameMatchManager.nextSecureKeyID(), match);
	}
	public String setupMatch() {
		System.out.println("Setuping a match with a new SKID! and a new GameMatch!"); //$NON-NLS-1$
		return setupMatch(GameMatchManager.nextSecureKeyID(), new GameMatch(MapManager.getDefaultMap()));
	}
	
	public void removeMatch(String id) {
		matches.remove(id);
		players.values().removeAll(Collections.singleton(id));
	}
	
	public void connectPlayer(Connection cnct, String id, String fighter) {
		System.out.println("Connection a new player to the SKID: " + id); //$NON-NLS-1$
		getMatch(id).addPlayer(cnct, id, fighter);
		players.put(cnct, id);
	}
	public void removePlayer(Connection cnct) {
		System.out.println("Player Removed!"); //$NON-NLS-1$
		if(getMatch(cnct) != null) {
			getMatch(cnct).removePlayer(cnct);
			players.remove(cnct);
		}
	}
	
	public String getMapName(String id) {
		return getMatch(id).getMapName();
	}
	
	public PlayerData getPlayer(Connection cnct) {
		if(getMatch(cnct) != null) {
			return getMatch(cnct).getPlayer(cnct);
		}
		return null;
	}
	
	public int getMatchesLength() {
		return matches.size();
	}
	public int getPlayersPlaying() {
		return players.size();
	}
	
	// Secure Match ID Key
	public boolean isKey(String ID) {
		System.out.println("at least we are in isKey?"); //$NON-NLS-1$
		for (Entry<String, GameMatch> entry : matches.entrySet()) {
		    if(entry.getKey().equals(ID)) {
		    	return true;
		    }
		}
    	System.out.println("and we are returning false i suppose??"); //$NON-NLS-1$
		return false;
	}
}
