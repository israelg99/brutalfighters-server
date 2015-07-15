package com.brutalfighters.server.matches;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.brutalfighters.server.data.maps.CTFMap;
import com.brutalfighters.server.data.players.Champion;
import com.brutalfighters.server.data.players.PlayerData;
import com.brutalfighters.server.data.projectiles.Projectile;
import com.brutalfighters.server.data.projectiles.Projectiles;
import com.esotericsoftware.kryonet.Connection;

public class GameMatchManager {
	private static GameMatches closedMatches;
	private static GameMatches openMatches;
	private static GameMatch currentMatch;
	private static SecureRandom matchID;
	
	public static void gameMatchManager() {
		closedMatches = new GameMatches();
		openMatches = new GameMatches();
		matchID = new SecureRandom();
		
		gameResources();
		
	}
	
	private static void gameResources() {
		// Loading the Champion Enum
		System.err.println("Initialize the Champion Enum!"); //$NON-NLS-1$
		Champion.init();
		System.out.println("Finished Initializing the Champion Enum!"); //$NON-NLS-1$
		
		// Loading the Projectile Enum
		System.err.println("Initialize the Projectile Enum!"); //$NON-NLS-1$
		Projectile.init();
		System.out.println("Finished Initializing the Projectile Enum!"); //$NON-NLS-1$
	}

	// Getters and Setters
	public static GameMatches closedMatches() {
		return closedMatches;
	}
	public static GameMatches openMatches() {
		return openMatches;
	}
	public static HashMap<String, GameMatch> getClosedMatches() {
		return closedMatches.getMatches();
	}
	public static HashMap<String, GameMatch> getOpenMatches() {
		return openMatches.getMatches();
	}
	public static GameMatch getClosedMatch(Connection cnct) {
		return closedMatches.getMatch(cnct);
	}
	public static PlayerData getClosedPlayer(Connection cnct) {
		return closedMatches.getMatch(cnct).getPlayer(cnct);
	}
	
	// Match Setups
	public static void setupOpenMatch() {
		openMatches.setupMatch();
	}
	public static void removeMatch(String ID) {
		System.out.println("MATCH REMOVED"); //$NON-NLS-1$
		openMatches.removeMatch(ID);
		closedMatches.removeMatch(ID);
		System.out.println(closedMatches.getMatchesLength());
		System.out.println(openMatches.getMatchesLength());
	}
	
	// Connect and Disconnect Players to Matches
	public static void connectPlayer(String fighter, Connection cnct) {
		System.out.println("Got a new player : " + cnct.getID() + " Playing: " + fighter); //$NON-NLS-1$ //$NON-NLS-2$
		if(Champion.contains(Character.toUpperCase(fighter.charAt(0)) + fighter.substring(1))) {
			matchPlayer(fighter, cnct);
		}
	}
	
	// Player Disconnection Handling
	public static void disconnectPlayer(Connection cnct) {
		closedMatches.removePlayer(cnct);
		openMatches.removePlayer(cnct);
		System.out.println("Removed the disconnected player"); //$NON-NLS-1$
	}
	public static void escapePlayer(Connection cnct) {
		closedMatches.removePlayer(cnct);
		openMatches.removePlayer(cnct);
		System.out.println("Removed the escaped player"); //$NON-NLS-1$
	}

	public static void matchPlayer(String fighter, Connection cnct) {
		System.out.println("Matching a player : " + cnct.getID()); //$NON-NLS-1$
		openMatches.connectPlayer(cnct, getAvailableMatch(), fighter);
	}
	
	// Update Matches
	private static void updateMatch(Iterator<Map.Entry<String,GameMatch>> iter, String id) {
		setCurrentMatch(id);
		updateCurrentMatch(iter);
	}
	private static void updateMatches() {
		Iterator<Map.Entry<String,GameMatch>> iter = closedMatches.getMatches().entrySet().iterator();
		while (iter.hasNext()) {
		    Map.Entry<String,GameMatch> entry = iter.next();
		    updateMatch(iter, entry.getKey());
		}
	}
	public static void updateManager() {
		updateMatches();
		updateOpenMatches();
	}
	
	// Current Match
	private static void setCurrentMatch(String id) {
		currentMatch = closedMatches.getMatch(id);
	}
	public static GameMatch getCurrentMatch() {
		return currentMatch;
	}
	private static void updateCurrentMatch(Iterator<Map.Entry<String,GameMatch>> iter) {
		getCurrentMatch().updateMatch(iter);
	}
	public static PlayerData getCurrentPlayer(Connection cnct) {
		return getCurrentMatch().getPlayer(cnct);
	}
	public static CTFMap getCurrentMap() {
		return getCurrentMatch().getMap();
	}
	public static Projectiles getCurrentProjectiles() {
		return getCurrentMatch().getProjectiles();
	}
	
	// Open Matches Update
	public static void updateOpenMatches() {
		Iterator<Map.Entry<String,GameMatch>> iter = openMatches.getMatches().entrySet().iterator();
		while (iter.hasNext()) {
		    Map.Entry<String,GameMatch> entry = iter.next();
			if(entry.getValue().isFull()) {
				System.out.println("Yay! an open match is ready to be closed! :-)"); //$NON-NLS-1$
				entry.getValue().close();
				closedMatches.getMatch(closedMatches.setupMatch(entry)).startMatch();
				iter.remove();
			}
		}
	}
	
	// Search Matches
	private static String getAvailableMatch() {
		System.out.println("Searching for an available match.."); //$NON-NLS-1$
		for (Entry<String, GameMatch> entry : openMatches.getMatches().entrySet()) {
		    if(!entry.getValue().isFull() && entry.getValue().isOpen()) {
		    	System.out.println("Found a match!"); //$NON-NLS-1$
		    	return entry.getKey();
		    }
		}
		
		System.out.println("Didn't find, going to setup one.."); //$NON-NLS-1$
		return openMatches.setupMatch();
	}
	
	// Secure Match ID
	public static String nextSecureKeyID() {
		return new BigInteger(130, matchID).toString(32);
	}
	
}
