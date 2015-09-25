package com.brutalfighters.server.packets;

import com.brutalfighters.server.data.flags.FlagData;
import com.brutalfighters.server.data.players.PlayerData;

public class Packet1Connected extends GameMatchPacket {
	public String map;
	public PlayerData theClient;
	public PlayerData[] players;
	public FlagData[] flags;
	public int warmup;
}
