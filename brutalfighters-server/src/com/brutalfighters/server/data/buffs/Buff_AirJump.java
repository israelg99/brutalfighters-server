package com.brutalfighters.server.data.buffs;

import java.util.Iterator;

import com.brutalfighters.server.data.players.PlayerData;
import com.brutalfighters.server.data.players.fighters.Fighter;

public class Buff_AirJump extends Buff {
	
	private float height_vel;
	
	public Buff_AirJump(float height_vel) {
		super("AirJump", 1000); //$NON-NLS-1$
		setHeightVel(height_vel);
	}
	public Buff_AirJump() {
		this(50);
	}
	
	public float getHeightVel() {
		return height_vel;
	}
	public void setHeightVel(float height_vel) {
		this.height_vel = height_vel;
	}
	
	@Override
	public void start(Fighter p, int index) {
		PlayerData player = p.getPlayer();
		player.getVel().resetX();
		player.getVel().setY(getHeightVel());
		p.getWalkingSpeed().resetX();
		p.getRunningSpeed().resetX();
		player.setControl(false);
		player.setVulnerable(false);
	}
	
	@Override
	public void update(Fighter p, Iterator<Buff> iterator) {
		if(!p.getPlayer().isMidAir() && isActive(p, iterator)) {
			p.applyGravity();
		} else {
			end(p, iterator);
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
		return new Buff_AirJump(getHeightVel());
	}
	
}