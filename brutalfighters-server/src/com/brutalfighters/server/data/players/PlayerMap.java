package com.brutalfighters.server.data.players;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.brutalfighters.server.data.players.fighters.Fighter;
import com.esotericsoftware.kryonet.Connection;

public class PlayerMap {
	private HashMap<Connection, Fighter> players;
	
	public PlayerMap() {
		players = new HashMap<Connection, Fighter>();
	}
	public PlayerMap(HashMap<Connection, Fighter> data) {
		players = new HashMap<Connection, Fighter>();
		players.putAll(data);
	}
	
	// GETTERS AND SETTERS
	public Fighter get(Connection connection) {
		return players.get(connection);
	}
	public void put(Connection connection, Fighter p) {
		players.put(connection, p);
	}
	public void remove(Connection connection) {
		players.remove(connection);
	}
	public int size() {
		return players.size();
	}
	
	public Fighter[] getPlayers() {
		return getPlayersCollection().toArray(new Fighter[players.values().size()]);
	}
	public Collection<Fighter> getPlayersCollection() {
		return getPlayersMap().values();
	}
	public HashMap<Connection, Fighter> getPlayersMap() {
		return players;
	}
	
	public PlayerData[] getOtherPlayersData(Connection connection) {
		Fighter[] players = getOtherPlayers(connection);
		PlayerData[] pdatas = new PlayerData[players.length];
		
		for(int i = 0; i < pdatas.length; i++) {
			pdatas[i] = players[i].getPlayer();
		}
		
		return pdatas;
	}
	public Fighter[] getOtherPlayers(Connection connection) {
		Collection<Fighter> coll = getOtherCollection(connection);
		return coll.toArray(new Fighter[coll.size()]);
	}
	public Collection<Fighter> getOtherCollection(Connection connection) {
		return getOtherMap(connection).values();
	}
	public HashMap<Connection, Fighter> getOtherMap(Connection connection) {
		HashMap<Connection, Fighter> temp = new HashMap<Connection, Fighter>();
		temp.putAll(players);
		temp.remove(connection);
		return temp;
	}
	
	public boolean containsKey(Connection connection) {
		return players.containsKey(connection);
	}
	public Set<Map.Entry<Connection, Fighter>> entrySet() {
		return getPlayersMap().entrySet();
	}
	public Set<Connection> keySet() {
		return getPlayersMap().keySet();
	}
}
