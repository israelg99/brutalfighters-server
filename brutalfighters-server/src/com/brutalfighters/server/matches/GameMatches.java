package com.brutalfighters.server.matches;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import com.brutalfighters.server.data.maps.MapManager;
import com.brutalfighters.server.data.players.PlayerMap;
import com.brutalfighters.server.data.players.fighters.Fighter;
import com.esotericsoftware.kryonet.Connection;

public class GameMatches<T extends GameMatch> {
	private Class<T> gameMatch;
	private HashMap <String, T> matches;
	private HashMap <Connection, String> players;
	
	public GameMatches(Class<T> gameMatch) {
		this.gameMatch = gameMatch;
		matches = new HashMap<String, T>();
		players = new HashMap<Connection, String>();
	}
	
	/* Getters and Setters */
	public HashMap<String, T> getMatches() {
		return matches;
	}
	public HashMap<Connection, String> getPlayers() {
		return players;
	}
	
	public T getMatch(String id) {
		if(matches.containsKey(id)) {
			return matches.get(id);
		}
		return null;
	}
	public T getMatch(Connection cnct) {
		return getMatch(players.get(cnct));
	}
	
	public void addMatch(String ID, T match) { // It adds the players and the match into the HashMaps.
		for(Entry<Connection, Fighter> entry : match.getPlayers().entrySet()) {
		    players.put(entry.getKey(), ID);
		}

		matches.put(ID, match);
	}
	
	public String setupMatch(String mapName, PlayerMap players, PlayerMap[] teams) { // It setups a match, with a SKID and stuff.
		try {
			String ID = GameMatchManager.uniqueSecureKeyID();
			T match = gameMatch.getConstructor(String.class, String.class, PlayerMap.class, PlayerMap[].class).newInstance(mapName, ID, players, teams);
			match.changeID(ID);
			addMatch(ID, match);
			
			return ID;
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	public String setupMatch(String mapName) { // It setups a match, with a SKID and stuff.
		try {
			String ID = GameMatchManager.uniqueSecureKeyID();
			T match = gameMatch.getConstructor(String.class, String.class).newInstance(mapName, ID);
			match.changeID(ID);
			addMatch(ID, match);
			
			return ID;
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	public String setupMatch() {
		return setupMatch(MapManager.getDefaultMap());
	}
	
	public void removeMatch(String id) {
		matches.remove(id);
		players.values().removeAll(Collections.singleton(id));
	}
	
	public void connectPlayer(Connection cnct, String id, String fighter) {
		System.out.println("Connecting a new player to the SKID: " + id); //$NON-NLS-1$
		
		getMatch(id).addPlayer(cnct, id, fighter);
		players.put(cnct, id);
	}
	public void connectPlayer(Connection cnct, String fighter) {
		connectPlayer(cnct, getAvailableMatch(), fighter);
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
	
	public Fighter getPlayer(Connection cnct) {
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
	
	/* Search Matches */
	public String getAvailableMatch() {
		System.out.println("Searching for an available match.."); //$NON-NLS-1$
		for (Entry<String, T> entry : getMatches().entrySet()) {
		    if(!entry.getValue().isFull() && entry.getValue().isOpen()) {
		    	System.out.println("Found a match!"); //$NON-NLS-1$
		    	return entry.getKey();
		    }
		}
		
		System.out.println("Didn't find, going to setup one.."); //$NON-NLS-1$
		return setupMatch();
	}

	/* Secure Match ID Key */
	public boolean isKey(String ID) {
		System.out.println("at least we are in isKey?"); //$NON-NLS-1$
		for (Entry<String, T> entry : matches.entrySet()) {
		    if(entry.getKey().equals(ID)) {
		    	return true;
		    }
		}
    	System.out.println("and we are returning false i suppose??"); //$NON-NLS-1$
		return false;
	}
}
