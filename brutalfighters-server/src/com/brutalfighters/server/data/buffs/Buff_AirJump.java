package com.brutalfighters.server.data.buffs;

import java.util.Iterator;

import com.brutalfighters.server.data.players.fighters.Fighter;

public class Buff_AirJump extends Buff {
	
	private float height_vel;
	
	public Buff_AirJump(float height_vel) {
		super("AirJump", 1000); //$NON-NLS-1$
		setHeightVel(height_vel);
	}
	public Buff_AirJump() {
		this(40);
	}
	
	public float getHeightVel() {
		return height_vel;
	}
	public void setHeightVel(float height_vel) {
		this.height_vel = height_vel;
	}
	
	@Override
	public void start(Fighter p, Iterator<Buff> iterator) {
		p.getPlayer().getVel().addY(getHeightVel());
		p.getPlayer().setJump(false);
		iterator.remove();
	}
	
	@Override
	public Buff getNewBuff() {
		return new Buff_AirJump(getHeightVel());
	}
	
}