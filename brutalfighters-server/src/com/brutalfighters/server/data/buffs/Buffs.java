package com.brutalfighters.server.data.buffs;

import java.util.ArrayList;
import java.util.Arrays;

import com.brutalfighters.server.data.players.PlayerData;

public class Buffs {
	
	private static ArrayList<BuffData> buffs = new ArrayList<BuffData>();
	
	public static void addBuff(PlayerData p, BuffData buff) {
		setBuffsArray(p.getBuffs());
		add(buff);
		p.setBuffs(getBuffsArray());
	}
	
	private static void add(BuffData buff) {
		buffs.add(buff);
	}
	private static void setBuffsArray(BuffData[] array) {
		buffs = new ArrayList<BuffData>(Arrays.asList(array));
	}
	private static BuffData[] getBuffsArray() {
		return buffs.toArray(new BuffData[buffs.size()]);
	}
}
