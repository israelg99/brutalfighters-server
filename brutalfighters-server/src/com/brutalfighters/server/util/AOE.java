package com.brutalfighters.server.util;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Map;

import com.brutalfighters.server.data.buffs.BuffData;
import com.brutalfighters.server.data.players.PlayerData;
import com.brutalfighters.server.data.players.StaticPlayer;
import com.brutalfighters.server.matches.GameMatch;
import com.brutalfighters.server.matches.GameMatchManager;
import com.esotericsoftware.kryonet.Connection;

public class AOE {
	
	// With BUFFS
	public static boolean dealAOE_players(int team, Rectangle bounds, int aoe, BuffData[] buffs) {
		return dealAOE_players(getAOE_Players(team, bounds), aoe, buffs);
	}
	public static boolean dealAOE_players(Rectangle bounds, int aoe, BuffData[] buffs) {
		return dealAOE_players(getAOE_Players(bounds), aoe, buffs);
	}
	public static boolean dealAOE_enemy(int team, Rectangle bounds, int aoe, BuffData[] buffs) {
		return dealAOE_players(GameMatch.getEnemyTeamID(team), bounds, aoe, buffs);
	}
	
	// WITHOUT BUFFS (Feeding the "with buffs" functions with empty buff arrays).
	public static boolean dealAOE_players(int team, Rectangle bounds, int aoe) {
		return dealAOE_players(team, bounds, aoe, new BuffData[0]);
	}
	public static boolean dealAOE_players(Rectangle bounds, int aoe) {
		return dealAOE_players(bounds, aoe, new BuffData[0]);
	}
	public static boolean dealAOE_enemy(int team, Rectangle bounds, int aoe) {
		return dealAOE_enemy(team, bounds, aoe, new BuffData[0]);
	}
	
	// Main Deal AOE
	public static boolean dealAOE_players(ArrayList<PlayerData> players, int aoe, BuffData[] buffs) {
		if(players.size() > 0) {
			
			boolean areBuffs = buffs.length > 0;
			boolean isDmg = aoe != 0;
			
			for(int i = 0; i < players.size(); i++) {
				PlayerData p = players.get(i);
				
				if(p.isVulnerable) {
					// Apply HP
					if(isDmg) { // In order no to call the function for nothing.
						StaticPlayer.applyRandomHP(p, aoe);
					}	
					
					if(areBuffs) { // In order no to call the function for nothing.
						StaticPlayer.applyBuffs(p, buffs);
					}
				}
				
			}
			return true;
		}
		return false;
	}
	
	// Get players that are impacted by the AOE
	public static ArrayList<PlayerData> getAOE_Players(int team, Rectangle bounds) {
		ArrayList<PlayerData> players = new ArrayList<PlayerData>();
		for(Map.Entry<Connection, PlayerData> entry : GameMatchManager.getCurrentMatch().getTeam(team).entrySet()) {
			if(!entry.getValue().isDead && CollisionDetection.intersects(entry.getValue(), bounds)) {
				players.add(entry.getValue());
			}
		}
		return players;
	}
	public static ArrayList<PlayerData> getAOE_Players(Rectangle bounds) {
		ArrayList<PlayerData> both = new ArrayList<PlayerData>();
		both.addAll(getAOE_Players(GameMatch.TEAM1, bounds));
		both.addAll(getAOE_Players(GameMatch.TEAM2, bounds));
		return both;
	}
}
