package com.brutalfighters.server.matches;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.brutalfighters.server.data.maps.CTFMap;
import com.brutalfighters.server.data.players.Champion;
import com.brutalfighters.server.data.players.fighters.Fighter;
import com.brutalfighters.server.data.projectiles.Projectiles;
import com.esotericsoftware.kryonet.Connection;

public class GameMatchManager {
	private static GameMatches<ClosedGameMatch> closedMatches;
	private static GameMatches<OpenGameMatch> openMatches;
	private static GameMatches<FreestyleGameMatch> freestyleMatches;
	private static GameMatch currentMatch;
	private static SecureRandom matchID;
	
	public static void gameMatchManager() {
		closedMatches = new GameMatches<ClosedGameMatch>(ClosedGameMatch.class);
		openMatches = new GameMatches<OpenGameMatch>(OpenGameMatch.class);
		freestyleMatches = new GameMatches<FreestyleGameMatch>(FreestyleGameMatch.class);
		matchID = new SecureRandom();
	}

	// Getters and Setters
	public static GameMatches<ClosedGameMatch> closedMatches() {
		return closedMatches;
	}
	public static GameMatches<OpenGameMatch> openMatches() {
		return openMatches;
	}
	public static GameMatches<FreestyleGameMatch> freestyleMatches() {
		return freestyleMatches;
	}
	public static HashMap<String, ClosedGameMatch> getClosedMatches() {
		return closedMatches.getMatches();
	}
	public static HashMap<String, OpenGameMatch> getOpenMatches() {
		return openMatches.getMatches();
	}
	public static HashMap<String, FreestyleGameMatch> getFreestyleMatches() {
		return freestyleMatches.getMatches();
	}
	public static GameMatch getClosedMatch(Connection cnct) {
		return closedMatches.getMatch(cnct);
	}
	public static GameMatch getFreestyleMatch(Connection cnct) {
		return freestyleMatches.getMatch(cnct);
	}
	public static Fighter getClosedPlayer(Connection cnct) {
		return closedMatches.getMatch(cnct).getPlayer(cnct);
	}
	public static Fighter getFreestylePlayer(Connection cnct) {
		return freestyleMatches.getMatch(cnct).getPlayer(cnct);
	}
	
	public static Fighter checkPlayer(Connection cnct) {
		Fighter p = openMatches.getPlayer(cnct);
		if(p == null) {
			p = closedMatches.getPlayer(cnct);
			if(p == null) {
				p = freestyleMatches.getPlayer(cnct);
			}
		}
		return p;
	}
	
	// Match Setups
	public static void removeMatch(String ID) {
		System.out.println("MATCH REMOVED"); //$NON-NLS-1$
		openMatches.removeMatch(ID);
		closedMatches.removeMatch(ID);
		System.out.println(closedMatches.getMatchesLength());
		System.out.println(openMatches.getMatchesLength());
	}
	
	// Connect and Disconnect Players to Matches
	public static void connectPlayer(GameMode gamemode, String fighter, Connection cnct) {
		System.out.println("Got a new player: " + cnct.getID() + " | Playing:" + fighter + " | In Game Mode:" + gamemode.name()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		if(Champion.contains(Character.toUpperCase(fighter.charAt(0)) + fighter.substring(1))) {
			if(gamemode.equals(GameMode.MATCH)) {
				matchPlayer(openMatches, fighter, cnct);
			} else if(gamemode.equals(GameMode.FREESTYLE)) {
				matchPlayer(freestyleMatches, fighter, cnct);
			}
		}
	}

	// Player Disconnection Handling
	public static void disconnectPlayer(Connection cnct) {
		closedMatches.removePlayer(cnct);
		openMatches.removePlayer(cnct);
		freestyleMatches.removePlayer(cnct);
		System.out.println("Removed the disconnected player"); //$NON-NLS-1$
	}
	public static void escapePlayer(Connection cnct) {
		disconnectPlayer(cnct);
		System.out.println("Removed the escaped player"); //$NON-NLS-1$
	}

	public static void matchPlayer(GameMatches matches, String fighter, Connection cnct) {
		System.out.println("Matching a player : " + cnct.getID()); //$NON-NLS-1$
		matches.connectPlayer(cnct, fighter);
	}
	
	// Update Matches -- We can't update the matches in the `GameMatches` class because we have to setCurrentMatch(GameMatch);
	private static void updateMatch(Iterator<Map.Entry<String,GameMatch>> iter, GameMatch match) {
		setCurrentMatch(match);
		updateCurrentMatch(iter);
	}
	private static void updateMatches(GameMatches matches) {
		Iterator<Map.Entry<String,GameMatch>> iter = matches.getMatches().entrySet().iterator();
		while (iter.hasNext()) {
		    Map.Entry<String,GameMatch> entry = iter.next();
		    updateMatch(iter, entry.getValue());
		}
	}
	public static void updateManager() {
		updateMatches(closedMatches);
		updateMatches(openMatches);
		updateMatches(freestyleMatches);
	}
	
	// Current Match
	private static void setCurrentMatch(GameMatch match) {
		currentMatch = match;
	}
	public static GameMatch getCurrentMatch() {
		return currentMatch;
	}
	private static void updateCurrentMatch(Iterator<Map.Entry<String,GameMatch>> iter) {
		getCurrentMatch().updateMatch(iter);
	}
	public static Fighter getCurrentPlayer(Connection cnct) {
		return getCurrentMatch().getPlayer(cnct);
	}
	public static CTFMap getCurrentMap() {
		return getCurrentMatch().getMap();
	}
	public static Projectiles getCurrentProjectiles() {
		return getCurrentMatch().getProjectiles();
	}
	
	// Secure Match ID
	public static String nextSecureKeyID() {
		return new BigInteger(130, matchID).toString(32);
	}
	public static String uniqueSecureKeyID() {
		String ID = GameMatchManager.nextSecureKeyID();
		System.out.println("Got key? " + ID); //$NON-NLS-1$
		while(closedMatches.isKey(ID) || openMatches.isKey(ID) || freestyleMatches.isKey(ID)) {
			ID = GameMatchManager.nextSecureKeyID();
		}
		System.out.println("YES THE KEY IS " + ID); //$NON-NLS-1$
		
		return ID;
	}
	
}
