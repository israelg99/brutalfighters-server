package com.brutalfighters.server.data.buffs;

import java.util.Iterator;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.players.fighters.Fighter;

abstract public class Buff {
	
	protected final int MAX_TIME;
	
	protected BuffData buff;
	protected boolean isStarted;
	
	protected Buff(String name, int MAX_TIME) {
		this.MAX_TIME = MAX_TIME;
		setStarted(false);
		setBuff(new BuffData(name, getMAX_TIME()));
	}
	protected Buff(String name) {
		this.MAX_TIME = 0;
		setBuff(new BuffData(name, getMAX_TIME()));
	}
	
	public BuffData getBuff() {
		return buff;
	}
	public void setBuff(BuffData buff) {
		this.buff = buff;
	}
	
	public boolean isStarted() {
		return isStarted;
	}
	protected void setStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}
	public void started() {
		setStarted(false);
	}
	
	public int getMAX_TIME() {
		return MAX_TIME;
	}
	
	public void tick(Fighter p, Iterator<Buff> iterator) {
		if(isStarted()) {
			update(p, iterator);
		} else {
			start(p, iterator);
			started();
		}
	}

	protected void start(Fighter p, Iterator<Buff> iterator) {
		
	}
	
	protected void update(Fighter p, Iterator<Buff> iterator) {
		isActive(p, iterator);
	}
	
	protected void end(Fighter p, Iterator<Buff> iterator) {
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
	
}
