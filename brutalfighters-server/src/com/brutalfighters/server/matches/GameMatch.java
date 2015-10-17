package com.brutalfighters.server.matches;

import java.util.Iterator;
import java.util.Map;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.flags.Flag;
import com.brutalfighters.server.data.flags.Flags;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.maps.CTFMap;
import com.brutalfighters.server.data.maps.MapManager;
import com.brutalfighters.server.data.players.PlayerData;
import com.brutalfighters.server.data.players.PlayerMap;
import com.brutalfighters.server.data.players.fighters.Fighter;
import com.brutalfighters.server.data.players.fighters.FighterFactory;
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
	
	/* DON'T TOUCH IT */
	protected static final int TEAM_LENGTH = 2;
	protected static final int TEAM1 = 0;
	protected static final int TEAM2 = 1;
	
	/* Configurable */
	protected static final int DEFAULT_PLAYER_LIMIT = 6;
	protected static final int WIN_STATE = 3;

	/* Configurable */
	protected static final int DEFAULT_WARMUP = 20000;
	protected static final int DEFAULT_RESPAWN = 6000;
	protected static final int DEFAULT_FINISH = 6000;
			
	protected PlayerMap players;
	protected PlayerMap[] teams;
	
	protected Flags flags;
	
	protected Projectiles projectiles;
	
	protected final String mapName;
	
	protected int playerLimit;
	
	protected boolean isOpen;
	
	protected String ID;
	
	protected Score score;
	
	protected int respawn;
	
	protected int teamWon;
	
	protected Counter warmup, finish;
	
	public GameMatch(String mapName, String ID, PlayerMap players, PlayerMap[] teams) {
		this.mapName = mapName;
		
		changeID(ID);
		
		setScore(new Score());
		
		setPlayers(players);
		
		setTeams(teams);
		
		setFlags(new Flags(new Flag[] { Flag.getFlag(mapName, getTEAM1()), Flag.getFlag(mapName, getTEAM2()) }));
		
		setProjectiles(new Projectiles());
		
		setPlayerLimit(getDefaultPlayerLimit());
		
		open();
		
		setWarmup(new Counter(getDefaultWarmup()));
		setFinish(new Counter(getDefaultFinish()));
		
		setRespawnTime(getDefaultRespawn());
		
		setTeamWon(-1);
	}
	
	public GameMatch(String mapName, String ID) {
		this(mapName, ID, new PlayerMap(), new PlayerMap[]{new PlayerMap(),new PlayerMap()});
	}
	
	public static int getTeamLength() {
		return TEAM_LENGTH;
	}
	public static int getDefaultPlayerLimit() {
		return DEFAULT_PLAYER_LIMIT;
	}
	public static int getWinState() {
		return WIN_STATE;
	}
	public static int getDefaultWarmup() {
		return DEFAULT_WARMUP;
	}
	public static int getDefaultFinish() {
		return DEFAULT_FINISH;
	}
	public static int getDefaultRespawn() {
		return DEFAULT_RESPAWN;
	}
	
	public static int getTEAM1() {
		return TEAM1;
	}
	public static int getTEAM2() {
		return TEAM2;
	}
	
	public PlayerMap[] getTeams() {
		return teams;
	}
	public void setTeams(PlayerMap[] teams) {
		this.teams = teams;
	}
	
	public CTFMap getMap() {
		return MapManager.getMap(mapName);
	}
	public String getMapName() {
		return mapName;
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
	
	public Flags getFlags() {
		return flags;
	}
	public void setFlags(Flags flags) {
		this.flags = flags;
	}
	
	public String getID() {
		return getID();
	}
	public void changeID(String ID) {
		this.ID = ID;
	}
	
	public boolean isFull() {
		return getPlayerLimit() == getPlayers().size();
	}
	public boolean isOpen() {
		return isOpen;
	}
	public void open() {
		isOpen = true;
	}
	public void close() {
		isOpen = false;
	}
	
	public Score getScore() {
		return score;
	}
	public void setScore(Score score) {
		this.score = score;
	}
	public void addKill(int team) {
		getScore().getKills()[team]++;
	}
	public void addFlag(int team) {
		getScore().getFlags()[team]++;
	}
	
	public int getRespawnTime() {
		return respawn;
	}
	public void setRespawnTime(int RESPAWN) {
		this.respawn = RESPAWN;
	}
	
	public Counter getWarmup() {
		return warmup;
	}
	public void setWarmup(Counter WARMUP) {
		this.warmup = WARMUP;
	}
	public boolean isWarmup() {
		return !getWarmup().isFinished();
	}
	
	public Counter getFinish() {
		return finish;
	}
	public void setFinish(Counter FINISH) {
		this.finish = FINISH;
	}
	
	public PlayerMap getPlayers() {
		return players;
	}
	public void setPlayers(PlayerMap players) {
		this.players = players;
	}
	
	public void addPlayer(Connection connection, String m_id, String fighter) {
		
		fighter = Character.toUpperCase(fighter.charAt(0)) + fighter.substring(1);
		
		// Checking if the fighter name the client passed does in fact exist
		if(!FighterFactory.contains(fighter)) {
			return;
		}
		
		// Setting up the information needed for getting the fighter
		int team = teams[0].size() < teams[1].size() ? getTEAM1() : getTEAM2();
		Base base = getMap().getBase(team);
		
		// Getting the fighter
		Fighter player = FighterFactory.valueOf(fighter).getNew(connection, team, base, m_id);
		
		// Adding the fighter into the data arrays
		teams[team].put(connection, player);
		getPlayers().put(connection, teams[team].get(connection));
		
	}
	public Fighter getPlayer(Connection connection) {
		return getPlayers().get(connection);
	}
	public void removePlayer(Connection connection) {
		getTeam1().remove(connection);
		getTeam2().remove(connection);
		getPlayers().remove(connection);
	}

	public PlayerMap getTeam1() {
		return getTeam(getTEAM1());
	}
	public PlayerMap getTeam2() {
		return getTeam(getTEAM2());
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
		if(team == getTEAM1()) {
			return getTeam2();
		} else if(team == getTEAM2()) {
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
		if(team == getTEAM1()) {
			return getTEAM2();
		} else if(team == getTEAM2()) {
			return getTEAM1();
		} else {
			return -1;
		}
	}
	public int getEnemyTeamID(Connection cnct) {
		if(getTeam1().containsKey(cnct)) {
			return getTEAM2();
		} else if(getTeam2().containsKey(cnct)) {
			return getTEAM1();
		} else {
			return -1;
		}
	}
	public int getTeamID(Connection cnct) {
		if(getTeam1().containsKey(cnct)) {
			return getTEAM1();
		} else if(getTeam2().containsKey(cnct)) {
			return getTEAM2();
		} else {
			return -1;
		}
	}
	
	public Projectiles getProjectiles() {
		return projectiles;
	}
	public void setProjectiles(Projectiles projectiles) {
		this.projectiles = projectiles;
	}
	public ProjectileData[] getProjectilesArray() {
		ProjectileData[] array = new ProjectileData[getProjectiles().getAll().size()];
		
		for(int i = 0; i < array.length; i++) {
			array[i] = getProjectiles().get(i).getProjectile();
		}
		
		return array;
	}
	
	public int getPlayerLimit() {
		return playerLimit;
	}
	public void setPlayerLimit(int playerLimit) {
		this.playerLimit = playerLimit;
	}
	
	
	// UPDATES
	public abstract void updateMatch(Iterator<Map.Entry<String,GameMatch>> iter);
	
	protected boolean gameFinished(Iterator<Map.Entry<String,GameMatch>> iter) {
		return getTeamWon() != -1 || checkEmpty(iter) || checkGameState();
	}

	protected boolean checkGameState() {
		for(int i = 0; i < getScore().getFlags().length; i++) {
			if(getScore().getFlags()[i] >= WIN_STATE) {
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
		getFinish().resetCounter();
	}
	
	protected boolean checkEmpty(Iterator<Map.Entry<String,GameMatch>> iter) {
		if(getPlayers().size() <= 0) {
			removeMatch(iter);
			return true;
		}
		return false;
	}
	protected void updateWarmup() {
		getWarmup().subCounter(GameServer.getDelay());
	}
	protected void updateGame() {
		updatePlayers();
		updateProjectiles();
		updateFlags();
	}
	
	protected void updateFlags() {
		getFlags().updateFlags();
	}
	protected void updateProjectiles() {
		getProjectiles().update();
	}	
	protected void updatePlayers() {
		for(Map.Entry<Connection, Fighter> entry : getPlayers().entrySet()) {
			entry.getValue().update();
		}
	}
	
	protected void updateClients() {
		Packet2Players packet = new Packet2Players();
		
		for(Connection cnct : getPlayers().keySet()) {
			packet = new Packet2Players();
			packet.theClient = getPlayers().get(cnct).getPlayer();
			packet.players = getPlayers().getOtherPlayersData(cnct);
			packet.projectiles = getProjectilesArray();
			packet.flags = getFlags().getData();
			packet.score = getScore();
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
		getScore().setKills(new int[] {MathUtil.nextInt(20, 40),MathUtil.nextInt(20, 40)});
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
		for(Connection cnct : getPlayers().keySet()) {
			approveResources(cnct);
		}
	}
	protected void approveResources(Connection connection) {
		if(getPlayers().containsKey(connection)) {
			Packet1Connected res = new Packet1Connected();
			res = new Packet1Connected();
			res.map = MapManager.getDefaultMap();
			res.theClient = getPlayers().get(connection).getPlayer();
			res.players = getPlayers().getOtherPlayersData(connection);
			res.flags = getFlags().getData();
			res.warmup = getWarmup().getCounter();
			connection.sendTCP(res);
		}
	}
	
	protected void updateClients(Packet packet) {
		for(Connection cnct : getPlayers().keySet()) {
			cnct.sendUDP(packet);
		}
	}
	protected void updateClientsTCP(Packet packet) {
		for(Connection cnct : getPlayers().keySet()) {
			cnct.sendTCP(packet);
		}
	}
	
	// Remove Match
	protected void removeMatch() {
		updateClientsTCP(new Packet2MatchOver());
		GameMatchManager.removeMatch(getID());
	}
	protected void removeMatch(Iterator<Map.Entry<String,GameMatch>> iter) {
		updateClientsTCP(new Packet2MatchOver());
		System.out.println("MATCH REMOVED"); //$NON-NLS-1$
		iter.remove();
	}
	
	// Map
	
}
