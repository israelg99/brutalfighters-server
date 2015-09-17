package com.brutalfighters.server.data.buffs;

import java.util.Iterator;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.players.fighters.Fighter;

abstract public class Buff {
	
	protected final int MAX_TIME;
	
	protected BuffData buff;
	
	protected Buff(String name, int MAX_TIME) {
		this.MAX_TIME = MAX_TIME;
		setBuff(new BuffData(name, getMAX_TIME()));
	}
	
	public BuffData getBuff() {
		return buff;
	}
	public void setBuff(BuffData buff) {
		this.buff = buff;
	}
	
	public int getMAX_TIME() {
		return MAX_TIME;
	}

	public void start(Fighter p, int index) {
		
	}
	
	public void update(Fighter p, Iterator<Buff> iterator) {
		isActive(p, iterator);
	}
	
	public void end(Fighter p, Iterator<Buff> iterator) {
		iterator.remove();
	}
	
	protected final boolean isActive(Fighter p, Iterator<Buff> iterator) {
		if(getBuff().getTime() > 0) {
			getBuff().setTime(getBuff().getTime()-GameServer.getDelay());
			return true;
		}
		end(p, iterator);
		return false;
	}
	protected final boolean isTime(int number) {
		return getBuff().getTime() / GameServer.getDelay() % number == 0;
	}
	
	abstract public Buff getNewBuff();
	
//	public final BuffData getBuff() {
//		BuffData buff = new BuffData();
//		buff.name = Buffd.valueOf(name).NAME;
//		buff.time = time;
//		
//		return buff;
//	}
//	public final BuffData getBuff() {
//		BuffData buff = new BuffData();
//		buff.name = Buffd.valueOf(name).NAME;
//		buff.time = Buffd.valueOf(name).TIME;
//		
//		return buff;
//	}
//	public final BuffData getBuff(BuffData ref) {
//		return getBuff(ref.name, ref.time);
//	}
	
}
