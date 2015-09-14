package com.brutalfighters.server.data.buffs;

import java.util.Iterator;

import com.brutalfighters.server.data.players.fighters.Fighter;

public class Buff_Burn extends Buff {

	private final int dmg;
	
	public Buff_Burn(int MAX_TIME, int dmg) {
		super("Burn", MAX_TIME); //$NON-NLS-1$
		this.dmg = dmg;
	}
	public Buff_Burn(int dmg) {
		this(3000, dmg);
	}
	
	@Override
	public void update(Fighter p, Iterator<Buff> iterator) {
		if(isActive(p, iterator)) {
			if(isTime(3)) {
				p.applyRandomHP(-dmg);
			}
		}
	}
	
	@Override
	public Buff getNewBuff() {
		return new Buff_Burn(getMAX_TIME(), this.dmg);
	}


}
