package com.brutalfighters.server.packets;

import com.brutalfighters.server.data.flags.FlagData;
import com.brutalfighters.server.data.players.PlayerData;
import com.brutalfighters.server.data.projectiles.ProjectileData;
import com.brutalfighters.server.util.Score;

public class Packet2Players extends GameMatchPacket {
	public PlayerData theClient;
	public PlayerData[] players;
	public ProjectileData[] projectiles;
	public FlagData[] flags;
	public Score score;
}
