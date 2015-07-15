package com.brutalfighters.server.packets;

import com.brutalfighters.server.data.flags.Flag;
import com.brutalfighters.server.data.players.PlayerData;

public class Packet1Connected extends ClosedMatchPacket {
	public String map;
	public PlayerData theClient;
	public PlayerData[] players;
	public Flag[] flags;
}
