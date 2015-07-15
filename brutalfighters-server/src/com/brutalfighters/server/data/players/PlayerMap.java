package com.brutalfighters.server.data.players;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.esotericsoftware.kryonet.Connection;

public class PlayerMap {
	public HashMap<Connection, PlayerData> players;
	
	public PlayerMap() {
		players = new HashMap<Connection, PlayerData>();
	}
	public PlayerMap(HashMap<Connection, PlayerData> data) {
		players = new HashMap<Connection, PlayerData>();
		players.putAll(data);
	}
	
	// GETTERS AND SETTERS
	public PlayerData get(Connection connection) {
		return players.get(connection);
	}
	public void put(Connection connection, PlayerData p) {
		players.put(connection, p);
	}
	public void remove(Connection connection) {
		players.remove(connection);
	}
	public int size() {
		return players.size();
	}
	
	public PlayerData[] getPlayers() {
		return getPlayersCollection().toArray(new PlayerData[players.values().size()]);
	}
	public Collection<PlayerData> getPlayersCollection() {
		return getPlayersMap().values();
	}
	public HashMap<Connection, PlayerData> getPlayersMap() {
		return players;
	}
	
	public PlayerData[] getOtherPlayers(Connection connection) {
		Collection<PlayerData> coll = getOtherCollection(connection);
		return coll.toArray(new PlayerData[coll.size()]);
	}
	public Collection<PlayerData> getOtherCollection(Connection connection) {
		return getOtherMap(connection).values();
	}
	public HashMap<Connection, PlayerData> getOtherMap(Connection connection) {
		HashMap<Connection, PlayerData> temp = (HashMap<Connection, PlayerData>) players.clone();
		temp.remove(connection);
		return temp;
	}
	
	public boolean containsKey(Connection connection) {
		return players.containsKey(connection);
	}
	public Set<Map.Entry<Connection, PlayerData>> entrySet() {
		return getPlayersMap().entrySet();
	}
	public Set<Connection> keySet() {
		return getPlayersMap().keySet();
	}
}
