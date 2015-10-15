package com.brutalfighters.server.data.buffs;

import java.util.Iterator;

import com.brutalfighters.server.data.players.PlayerData;
import com.brutalfighters.server.data.players.fighters.Fighter;

public class Buff_IceStun extends Buff {

	public Buff_IceStun(int MAX_TIME) {
		super("IceStun", MAX_TIME); //$NON-NLS-1$
	}
	public Buff_IceStun() {
		this(3000);
	}
	
	@Override
	public void start(Fighter p, Iterator<Buff> iterator) {
		PlayerData player = p.getPlayer();
		player.getVel().resetX();
		player.getVel().resetY();
		p.getWalkingSpeed().resetX();
		p.getRunningSpeed().resetX();
		player.setControl(false);
		player.setVulnerable(false);
	}
	
	@Override
	public void update(Fighter p, Iterator<Buff> iterator) {
		if(isActive(p, iterator)) {
			p.applyGravity();
			p.applyWalking();
		}
	}
	
	@Override
	public void end(Fighter p, Iterator<Buff> iterator) {
		p.getPlayer().setControl(true);
		p.getPlayer().setVulnerable(true);
		
		p.resetSpeeds();
		
		iterator.remove();
	}
	
	@Override
	public Buff getNewBuff() {
		return new Buff_IceStun(getMAX_TIME());
	}
	
}
