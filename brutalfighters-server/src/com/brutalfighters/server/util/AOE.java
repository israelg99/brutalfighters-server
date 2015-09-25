package com.brutalfighters.server.util;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Map;

import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.players.fighters.Fighter;
import com.brutalfighters.server.matches.GameMatch;
import com.brutalfighters.server.matches.GameMatchManager;
import com.esotericsoftware.kryonet.Connection;

public class AOE {
	
	// With BUFFS
	public static boolean dealAOE_players(int team, Rectangle bounds, float aoe, Buff[] buffs) {
		return dealAOE_players(getAOE_Players(team, bounds), aoe, buffs);
	}
	public static boolean dealAOE_players(Rectangle bounds, float aoe, Buff[] buffs) {
		return dealAOE_players(getAOE_Players(bounds), aoe, buffs);
	}
	public static boolean dealAOE_enemy(int team, Rectangle bounds, float aoe, Buff[] buffs) {
		return dealAOE_players(GameMatch.getEnemyTeamID(team), bounds, aoe, buffs);
	}
	
	// WITHOUT BUFFS (Feeding the "with buffs" functions with empty buff arrays).
	public static boolean dealAOE_players(int team, Rectangle bounds, float aoe) {
		return dealAOE_players(team, bounds, aoe, new Buff[0]);
	}
	public static boolean dealAOE_players(Rectangle bounds, float aoe) {
		return dealAOE_players(bounds, aoe, new Buff[0]);
	}
	public static boolean dealAOE_enemy(int team, Rectangle bounds, float aoe) {
		return dealAOE_enemy(team, bounds, aoe, new Buff[0]);
	}
	
	// Main Deal AOE
	public static boolean dealAOE_players(ArrayList<Fighter> players, float aoe, Buff[] buffs) {
		if(players.size() > 0) {
			
			boolean areBuffs = buffs.length > 0;
			boolean isDmg = aoe != 0;
			
			for(int i = 0; i < players.size(); i++) {
				Fighter p = players.get(i);
				
				if(p.getPlayer().isVulnerable()) {
					// Apply HP
					if(isDmg) { // In order no to call the function for nothing.
						p.applyRandomHP(aoe);
					}	
					
					if(areBuffs) { // In order no to call the function for nothing.
						p.applyBuffs(buffs);
					}
				}
				
			}
			return true;
		}
		return false;
	}
	
	// Get players that are impacted by the AOE
	public static ArrayList<Fighter> getAOE_Players(int team, Rectangle bounds) {
		ArrayList<Fighter> players = new ArrayList<Fighter>();
		for(Map.Entry<Connection, Fighter> entry : GameMatchManager.getCurrentMatch().getTeam(team).entrySet()) {
			if(!entry.getValue().getPlayer().isDead() && entry.getValue().intersects(bounds)) {
				players.add(entry.getValue());
			}
		}
		return players;
	}
	public static ArrayList<Fighter> getAOE_Players(Rectangle bounds) {
		ArrayList<Fighter> both = new ArrayList<Fighter>();
		both.addAll(getAOE_Players(GameMatch.getTEAM1(), bounds));
		both.addAll(getAOE_Players(GameMatch.getTEAM1(), bounds));
		return both;
	}
}
