package com.brutalfighters.server.data.buffs;

import java.util.Iterator;

import com.brutalfighters.server.data.players.fighters.Fighter;

public class Buff_Slow extends Buff {

	private final int slowness_divide;
	
	public Buff_Slow(int MAX_TIME, int slowness_divide) {
		super("Slow", MAX_TIME); //$NON-NLS-1$
		this.slowness_divide = slowness_divide;
	}
	public Buff_Slow(int slowness_divide) {
		this(2000, slowness_divide);
	}
	
	@Override
	public void start(Fighter p, int index) {
		p.getWalkingSpeed().setX(p.getWalkingSpeed().getX()/slowness_divide);
		p.getRunningSpeed().setX(p.getRunningSpeed().getX()/slowness_divide);
	}
	
	@Override
	public void update(Fighter p, Iterator<Buff> iterator) {
		if(isActive(p, iterator)) {
			/*if(buff.time / GameServer.getDelay() % 3 == 0) {
				Champion fighter = Champion.valueOf(p.name);
				p.walking_speed += p.walking_speed < fighter.WALKING_SPEED ? 1 : 0;
				p.running_speed += p.running_speed < fighter.RUNNING_SPEED ? 1 : 0;
			}*/
		}
	}

	@Override
	public void end(Fighter p, Iterator<Buff> iterator) {
		p.resetSpeeds();
		
		iterator.remove();
	}
	
	@Override
	public Buff getNewBuff() {
		return new Buff_Slow(getMAX_TIME(), this.slowness_divide);
	}
	
}
