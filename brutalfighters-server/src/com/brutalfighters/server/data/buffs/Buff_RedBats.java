package com.brutalfighters.server.data.buffs;

import java.util.Iterator;

import com.brutalfighters.server.data.players.fighters.Fighter;

public class Buff_RedBats extends Buff {
	
	private final int dmg = 50;
	
	public Buff_RedBats(int MAX_TIME) {
		super("RedBats", MAX_TIME); //$NON-NLS-1$
	}
	public Buff_RedBats() {
		this(3000);
	}
	
	@Override
	public void update(Fighter p, Iterator<Buff> iterator) {
		if(isActive(p, iterator)) {
			if(isTime(10)) {
				p.applyRandomHP(-dmg);
			}
		}
	}

	@Override
	public Buff getNewBuff() {
		return new Buff_RedBats(getMAX_TIME());
	}
	
}
