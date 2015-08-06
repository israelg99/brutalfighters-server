package com.brutalfighters.server.base;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.brutalfighters.server.data.buffs.BuffData;
import com.brutalfighters.server.data.flags.Flag;
import com.brutalfighters.server.data.maps.MapManager;
import com.brutalfighters.server.data.players.PlayerData;
import com.brutalfighters.server.data.projectiles.ProjectileData;
import com.brutalfighters.server.matches.GameMatchManager;
import com.brutalfighters.server.packets.ConnectGameMatch;
import com.brutalfighters.server.packets.GameMatchPacket;
import com.brutalfighters.server.packets.Packet;
import com.brutalfighters.server.packets.Packet0ConnectMatch;
import com.brutalfighters.server.packets.Packet1Connected;
import com.brutalfighters.server.packets.Packet2MatchFinished;
import com.brutalfighters.server.packets.Packet2MatchOver;
import com.brutalfighters.server.packets.Packet2Players;
import com.brutalfighters.server.packets.Packet3InputAAttack;
import com.brutalfighters.server.packets.Packet3InputJump;
import com.brutalfighters.server.packets.Packet3InputLeft;
import com.brutalfighters.server.packets.Packet3InputRight;
import com.brutalfighters.server.packets.Packet3InputRun;
import com.brutalfighters.server.packets.Packet3InputSkill1;
import com.brutalfighters.server.packets.Packet3InputSkill2;
import com.brutalfighters.server.packets.Packet3InputSkill3;
import com.brutalfighters.server.packets.Packet3InputSkill4;
import com.brutalfighters.server.packets.Packet3InputTeleport;
import com.brutalfighters.server.packets.Packet4ReleaseAAttack;
import com.brutalfighters.server.packets.Packet4ReleaseJump;
import com.brutalfighters.server.packets.Packet4ReleaseLeft;
import com.brutalfighters.server.packets.Packet4ReleaseRight;
import com.brutalfighters.server.packets.Packet4ReleaseRun;
import com.brutalfighters.server.packets.Packet4ReleaseSkill1;
import com.brutalfighters.server.packets.Packet4ReleaseSkill2;
import com.brutalfighters.server.packets.Packet4ReleaseSkill3;
import com.brutalfighters.server.packets.Packet4ReleaseSkill4;
import com.brutalfighters.server.packets.Packet4ReleaseTeleport;
import com.brutalfighters.server.packets.Packet5EscapeMatch;
import com.brutalfighters.server.util.Score;

public class GameServer {
	private static MPServer server;
	private static Timer timer;
	private static final int DELAY = 50;

	public static void Load() throws IOException {
		server = new MPServer(new NetworkListener());
		registerPackets();

		MapManager.registerMaps();
		GameMatchManager.gameMatchManager();
		
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				GameMatchManager.updateManager();
			}
			
		}, DELAY, DELAY);
		
		MapManager.registerMaps();
		GameMatchManager.gameMatchManager();
		 
	}

	private static void registerPackets() {
		server.getKryo().register(Packet.class);
		server.getKryo().register(GameMatchPacket.class);
		server.getKryo().register(ConnectGameMatch.class);
		
		server.getKryo().register(Packet0ConnectMatch.class);
		server.getKryo().register(PlayerData.class);
		server.getKryo().register(PlayerData[].class);
		server.getKryo().register(BuffData.class);
		server.getKryo().register(BuffData[].class);
		server.getKryo().register(int[].class);
		server.getKryo().register(ProjectileData.class);
		server.getKryo().register(ProjectileData[].class);
		server.getKryo().register(Flag.class);
		server.getKryo().register(Flag[].class);
		server.getKryo().register(Score.class);
		server.getKryo().register(Packet1Connected.class);		
		server.getKryo().register(Packet2Players.class);
		server.getKryo().register(Packet2MatchOver.class);
		server.getKryo().register(Packet2MatchFinished.class);
		
		server.getKryo().register(Packet3InputLeft.class);
		server.getKryo().register(Packet3InputRight.class);
		server.getKryo().register(Packet3InputJump.class);
		server.getKryo().register(Packet3InputRun.class);
		server.getKryo().register(Packet3InputAAttack.class);
		server.getKryo().register(Packet3InputSkill1.class);
		server.getKryo().register(Packet3InputSkill2.class);
		server.getKryo().register(Packet3InputSkill3.class);
		server.getKryo().register(Packet3InputSkill4.class);
		server.getKryo().register(Packet3InputTeleport.class);
		
		server.getKryo().register(Packet4ReleaseLeft.class);
		server.getKryo().register(Packet4ReleaseRight.class);
		server.getKryo().register(Packet4ReleaseJump.class);
		server.getKryo().register(Packet4ReleaseRun.class);
		server.getKryo().register(Packet4ReleaseAAttack.class);
		server.getKryo().register(Packet4ReleaseSkill1.class);
		server.getKryo().register(Packet4ReleaseSkill2.class);
		server.getKryo().register(Packet4ReleaseSkill3.class);
		server.getKryo().register(Packet4ReleaseSkill4.class);
		server.getKryo().register(Packet4ReleaseTeleport.class);
		
		server.getKryo().register(Packet5EscapeMatch.class);
		
	}
	
	public static int getDelay() {
		return DELAY;
	}
}
