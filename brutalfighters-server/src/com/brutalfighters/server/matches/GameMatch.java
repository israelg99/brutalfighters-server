package com.brutalfighters.server.matches;

import java.util.Iterator;
import java.util.Map;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.flags.Flag;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.maps.CTFMap;
import com.brutalfighters.server.data.maps.MapManager;
import com.brutalfighters.server.data.players.Champion;
import com.brutalfighters.server.data.players.PlayerData;
import com.brutalfighters.server.data.players.PlayerMap;
import com.brutalfighters.server.data.players.fighters.Fighter;
import com.brutalfighters.server.data.projectiles.ProjectileData;
import com.brutalfighters.server.data.projectiles.Projectiles;
import com.brutalfighters.server.packets.Packet;
import com.brutalfighters.server.packets.Packet1Connected;
import com.brutalfighters.server.packets.Packet2MatchFinished;
import com.brutalfighters.server.packets.Packet2MatchOver;
import com.brutalfighters.server.packets.Packet2Players;
import com.brutalfighters.server.util.Counter;
import com.brutalfighters.server.util.MathUtil;
import com.brutalfighters.server.util.Score;
import com.esotericsoftware.kryonet.Connection;

abstract public class GameMatch {
	
	/*
	 * 
	 * Warmup: 20sec
	 * Players: 6
	 * Win State: 3 flags
	 * Respawn: 6sec
	 * Match Finish: 6sec
	 * 
	 */
	
	public static final int TEAM_LENGTH = 2;
	public static final int TEAM1 = 0;
	public static final int TEAM2 = 1;
	
	protected static final int DEFAULT_PLAYER_LIMIT = 2;
	protected static final int WIN_STATE = 3;

	protected static final int DEFAULT_WARMUP = 20000;
	protected static final int DEFAULT_RESPAWN = 6000;
	protected static final int DEFAULT_FINISH = 6000;
			
	protected PlayerMap players;
	protected PlayerMap[] teams;
	
	protected Flag[] flags;
	
	protected Projectiles projectiles;
	
	protected final String mapName;
	
	protected int playerLimit;
	
	protected boolean isOpen;
	
	protected String ID;
	
	protected Score score;
	
	protected int RESPAWN;
	
	protected int teamWon;
	
	protected Counter WARMUP, FINISH;
	
	public GameMatch(String mapName, String ID, PlayerMap players, PlayerMap[] teams) {
		this.ID = ID;
		
		this.score = new Score();
		this.score.flags = new int[] {0,0};
		this.score.kills = new int[] {0,0};
		
		this.players = players;
		
		this.teams = teams;
		
		flags = new Flag[2];
		setFlag(TEAM1, Flag.getFlag(mapName, TEAM1));
		setFlag(TEAM2, Flag.getFlag(mapName, TEAM2));
		
		projectiles = new Projectiles();
		
		this.mapName = mapName;
		
		this.playerLimit = DEFAULT_PLAYER_LIMIT;
		
		this.isOpen = true;
		
		this.WARMUP = new Counter(DEFAULT_WARMUP);
		this.FINISH = new Counter(DEFAULT_FINISH);
		
		this.RESPAWN = getDefaultRespawn();
		
		setTeamWon(-1);
	}
	public GameMatch(String mapName, String ID) {
		this(mapName, ID, new PlayerMap(), new PlayerMap[]{new PlayerMap(),new PlayerMap()});
	}
	
	// UPDATES
	public abstract void updateMatch(Iterator<Map.Entry<String,GameMatch>> iter);
	
	protected boolean gameFinished(Iterator<Map.Entry<String,GameMatch>> iter) {
		return getTeamWon() != -1 || checkEmpty(iter) || checkGameState();
	}

	protected boolean checkGameState() {
		for(int i = 0; i < score.flags.length; i++) {
			if(score.flags[i] >= WIN_STATE) {
				setTeamWon(i);
				resetFinish();
				
				Packet2MatchFinished packet = new Packet2MatchFinished();
				packet.teamWon = getTeamWon();
				updateClients(packet);
				return true;
			}
		}
		return false;
	}
	protected void resetFinish() {
		FINISH.resetCounter();
	}
	
	protected boolean checkEmpty(Iterator<Map.Entry<String,GameMatch>> iter) {
		if(players.size() <= 0) {
			removeMatch(iter);
			return true;
		}
		return false;
	}
	protected void updateWarmup() {
		WARMUP.subCounter(GameServer.getDelay());
	}
	protected void updateGame() {
		updatePlayers();
		updateProjectiles();
		updateFlags();
	}
	
	protected void updateFlags() {
		Flag.updateFlags(flags);
	}
	protected void updateProjectiles() {
		projectiles.update();
	}	
	protected void updatePlayers() {
		for(Map.Entry<Connection, Fighter> entry : players.entrySet()) {
			entry.getValue().update(getMap());
		}
	}
	
	protected void updateClients() {
		Packet2Players packet = new Packet2Players();
		
		for(Connection cnct : players.keySet()) {
			packet = new Packet2Players();
			packet.theClient = players.get(cnct).getPlayer();
			packet.players = players.getOtherPlayersData(cnct);
			packet.projectiles = getProjectilesArray();
			packet.flags = flags;
			packet.score = score;
			cnct.sendUDP(packet);
		}
	}
	
	public void womboCombo(Connection connection) {
		for(Map.Entry<Connection, Fighter> entry : getPlayers().entrySet()) {
			
			Fighter fighter = entry.getValue();
			PlayerData pd = fighter.getPlayer();
			
			switch(pd.getName()) {
				case "Blaze": //$NON-NLS-1$
					pd.setLeft(false);
					pd.setRunning( false);
					pd.setAAttack(true);
					pd.enableSkilling();
					pd.setSkill3(true);
					fighter.startSkill3();
				break;
				
				case "Dusk": //$NON-NLS-1$
					pd.setAAttack(true);
					pd.enableSkilling();
					pd.setSkill1(true);
					fighter.startSkill1();
				break;
				
				case "Chip": //$NON-NLS-1$
					pd.setAAttack(true);
					pd.enableSkilling();
					pd.setSkill2(true);
					fighter.startSkill2();
				break;
				
				case "Surge": //$NON-NLS-1$
					pd.setAAttack(true);
					pd.enableSkilling();
					pd.setSkill4(true);
					fighter.startSkill4();
				break;
				
				case "Lust": //$NON-NLS-1$
					pd.setAAttack(true);
					pd.enableSkilling();
					pd.setSkill1(true);
					fighter.startSkill1();
				break;
			}
		}
	}
	
	public void moveCombo(Connection connection) {
		for(Map.Entry<Connection, Fighter> entry : getPlayers().entrySet()) {

			Fighter fighter = entry.getValue();
			PlayerData pd = fighter.getPlayer();
			
			switch(pd.getName()) {
				case "Blaze": //$NON-NLS-1$
					pd.enableSkilling();
					pd.setSkill1(true);
					fighter.startSkill1();
				break;
				
				case "Dusk": //$NON-NLS-1$
					pd.enableSkilling();
					pd.setSkill4(true);
					fighter.startSkill4();
				break;
				
				case "Chip": //$NON-NLS-1$
					pd.setLeft(true);
					pd.setRunning(true);
					pd.enableSkilling();
					pd.setSkill2(true);
					fighter.startSkill2();
				break;
				
				case "Surge": //$NON-NLS-1$
					pd.enableSkilling();
					pd.setSkill2(true);
					fighter.startSkill2();
				break;
				
				case "Lust": //$NON-NLS-1$
					
				break;
			}
		}
	}
	public void stopCombo() {
		this.score.kills = new int[] {MathUtil.nextInt(20, 40),MathUtil.nextInt(20, 40)};
		for(Map.Entry<Connection, Fighter> entry : getPlayers().entrySet()) {
			PlayerData pd = entry.getValue().getPlayer();
			pd.maxHP();
			pd.getHP().subX(200);
			pd.setLeft(false);
			pd.setRight(false);
			pd.setRunning(false);
			pd.setAAttack(false);
			pd.setJump(false);
		}
	}
	
	// Approve Resources
	public void startMatch() {
		for(Connection cnct : players.keySet()) {
			approveResources(cnct);
		}
	}
	protected void approveResources(Connection connection) {
		if(players.containsKey(connection)) {
			Packet1Connected res = new Packet1Connected();
			res = new Packet1Connected();
			res.map = MapManager.getDefaultMap();
			res.theClient = players.get(connection).getPlayer();
			res.players = players.getOtherPlayersData(connection);
			res.flags = flags;
			res.warmup = getWarmup();
			connection.sendTCP(res);
		}
	}
	
	protected void updateClients(Packet packet) {
		for(Connection cnct : players.keySet()) {
			cnct.sendUDP(packet);
		}
	}
	protected void updateClientsTCP(Packet packet) {
		for(Connection cnct : players.keySet()) {
			cnct.sendTCP(packet);
		}
	}
	
	// Remove Match
	protected void removeMatch() {
		updateClientsTCP(new Packet2MatchOver());
		GameMatchManager.removeMatch(ID);
	}
	protected void removeMatch(Iterator<Map.Entry<String,GameMatch>> iter) {
		updateClientsTCP(new Packet2MatchOver());
		System.out.println("MATCH REMOVED"); //$NON-NLS-1$
		iter.remove();
	}
	
	// Map
	public CTFMap getMap() {
		return MapManager.getMap(mapName);
	}
	public String getMapName() {
		return mapName;
	}
	
	// Players / players Control
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
		Fighter player = Champion.valueOf(fighter).getNew(connection, base, m_id);
		player.getPlayer().setTeam(team);
		
		// Adding the fighter into the data arrays
		teams[team].put(connection, player);
		players.put(connection, teams[team].get(connection));
		
	}
	public Fighter getPlayer(Connection connection) {
		return players.get(connection);
	}
	public void removePlayer(Connection connection) {
		teams[0].remove(connection);
		teams[1].remove(connection);
		players.remove(connection);
	}
	
	// Get ALL Players
	public PlayerMap getPlayers() {
		return players;
	}
	public PlayerMap getTeam1() {
		return teams[0];
	}
	public PlayerMap getTeam2() {
		return teams[1];
	}
	public PlayerMap getTeam(int team) {
		return teams[team];
	}
	public PlayerMap getTeam(Connection cnct) {
		if(getTeam1().containsKey(cnct)) {
			return getTeam1();
		} else if(getTeam2().containsKey(cnct)) {
			return getTeam2();
		} else {
			return null;
		}
	}
	public PlayerMap getEnemyTeam(int team) {
		if(team == TEAM1) {
			return getTeam2();
		} else if(team == TEAM2) {
			return getTeam1();
		} else {
			return null;
		}
	}
	public PlayerMap getEnemyTeam(Connection cnct) {
		if(getTeam1().containsKey(cnct)) {
			return getTeam1();
		} else if(getTeam2().containsKey(cnct)) {
			return getTeam2();
		} else {
			return null;
		}
	}
	public static int getEnemyTeamID(int team) {
		if(team == TEAM1) {
			return TEAM2;
		} else if(team == TEAM2) {
			return TEAM1;
		} else {
			return -1;
		}
	}
	public int getEnemyTeamID(Connection cnct) {
		if(getTeam1().containsKey(cnct)) {
			return TEAM2;
		} else if(getTeam2().containsKey(cnct)) {
			return TEAM1;
		} else {
			return -1;
		}
	}
	public int getTeamID(Connection cnct) {
		if(getTeam1().containsKey(cnct)) {
			return TEAM1;
		} else if(getTeam2().containsKey(cnct)) {
			return TEAM2;
		} else {
			return -1;
		}
	}
	
	// Flags
	public Flag getFlag(int index) {
		return flags[index];
	}
	protected void setFlag(int index, Flag flag) {
		flags[index] = flag;
	}
	public Flag getEnemyFlag(int team) {
		return flags[team == TEAM1? TEAM2 : TEAM1];
	}
	
	// Projectiles
	public Projectiles getProjectiles() {
		return projectiles;
	}
	public ProjectileData[] getProjectilesArray() {
		ProjectileData[] array = new ProjectileData[getProjectiles().getAll().size()];
		
		for(int i = 0; i < array.length; i++) {
			array[i] = getProjectiles().get(i).getProjectile();
		}
		
		return array;
	}
	
	// Match Limits and States
	protected int getPlayerLimit() {
		return playerLimit;
	}
	public boolean isFull() {
		return (playerLimit == players.size());
	}
	public boolean isOpen() {
		return isOpen;
	}
	public void close() {
		isOpen = false;
	}
	
	// Set ID
	public void changeID(String ID) {
		this.ID = ID;
	}
	
	// Get Score
	public void addKill(int team) {
		score.kills[team]++;
	}
	public void addFlag(int team) {
		score.flags[team]++;
	}
	
	// Get Respawn Time
	public static int getDefaultRespawn() {
		return DEFAULT_RESPAWN;
	}
	public int getRespawnTime() {
		return RESPAWN;
	}
	
	// Team Won / Finish Screen
	public int getTeamWon() {
		return teamWon;
	}
	public void setTeamWon(int i) {
		this.teamWon = i;
	}
	public void resetTeamWon() {
		this.teamWon = -1;
	}
	
	protected int getWarmup() {
		return WARMUP.getCounter();
	}
	public boolean isWarmup() {
		return !WARMUP.isFinished();
	}
}
