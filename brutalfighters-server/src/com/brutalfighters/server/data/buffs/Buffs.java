package com.brutalfighters.server.data.buffs;

import java.util.ArrayList;
import java.util.Arrays;

import com.brutalfighters.server.data.players.PlayerData;

public class Buffs {
	
	private static ArrayList<Buff> buffs = new ArrayList<Buff>();
	
	public static void addBuff(PlayerData p, Buff buff) {
		setBuffsArray(p.getBuffs());
		add(buff);
		p.setBuffs(getBuffsArray());
	}
	
	private static void add(Buff buff) {
		buffs.add(buff);
	}
	private static void setBuffsArray(Buff[] array) {
		buffs = new ArrayList<Buff>(Arrays.asList(array));
	}
	private static Buff[] getBuffsArray() {
		return buffs.toArray(new Buff[buffs.size()]);
	}
}
