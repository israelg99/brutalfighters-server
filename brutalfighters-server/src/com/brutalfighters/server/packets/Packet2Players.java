package com.brutalfighters.server.packets;

import com.brutalfighters.server.data.flags.Flag;
import com.brutalfighters.server.data.players.PlayerData;
import com.brutalfighters.server.data.projectiles.ProjectileData;
import com.brutalfighters.server.util.Score;

public class Packet2Players extends ClosedMatchPacket {
	public PlayerData theClient;
	public PlayerData[] players;
	public ProjectileData[] projectiles;
	public Flag[] flags;
	public Score score;
}
