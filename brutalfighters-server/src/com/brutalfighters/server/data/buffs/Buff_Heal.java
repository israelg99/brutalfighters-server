package com.brutalfighters.server.data.buffs;

import java.util.Iterator;

import com.brutalfighters.server.data.players.fighters.Fighter;

public class Buff_Heal extends Buff {

	private final int heal;
	
	public Buff_Heal(int MAX_TIME, int heal) {
		super("Healing", MAX_TIME); //$NON-NLS-1$
		this.heal = heal;
	}
	public Buff_Heal(int heal) {
		this(2000, heal);
	}
	
	@Override
	public void update(Fighter p, Iterator<Buff> iterator) {
		if(isActive(p, iterator)) {
			if(isTime(2)) {
				p.applyRandomHP(heal);
			}
		}
	}
	
	@Override
	public Buff getNewBuff() {
		return new Buff_Heal(getMAX_TIME(), this.heal);
	}

}
