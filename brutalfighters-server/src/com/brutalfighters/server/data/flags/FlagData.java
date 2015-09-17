package com.brutalfighters.server.data.flags;

import com.brutalfighters.server.util.Vec2;

public class FlagData {
	
	private static final Vec2 SIZE = new Vec2(20,155);
	private static final int GRAVITY = 12;
	
	private Vec2 pos, vel;
	private boolean isTaken;
	private String flip;
	
	public FlagData(Vec2 pos, Vec2 vel, boolean isTaken, String flip) {
		setPos(pos);
		setVel(vel);
		setFlip(flip.toLowerCase());
		setTaken(isTaken);
	}
	public FlagData(Vec2 pos, String flip) {
		this(pos, new Vec2(0,0), false, flip);
	}
	
	
	public static Vec2 getSize() {
		return SIZE;
	}
	public static int getGravity() {
		return GRAVITY;
	}
	
	
	public Vec2 getPos() {
		return pos;
	}
	public void setPos(Vec2 pos) {
		this.pos = pos;
	}
	
	public Vec2 getVel() {
		return vel;
	}
	public void setVel(Vec2 vel) {
		this.vel = vel;
	}
	
	public boolean isTaken() {
		return isTaken;
	}
	private void setTaken(boolean isTaken) {
		this.isTaken = isTaken;
	}
	public void gotStolen() {
		this.isTaken = true;
	}
	public void gotDropped() {
		this.isTaken = false;
	}
	
	public String getFlip() {
		return flip;
	}
	private void setFlip(String flip) {
		this.flip = flip;
	}
	public void flipRight() {
		this.flip = "right"; //$NON-NLS-1$
	}
	public void flipLeft() {
		this.flip = "left"; //$NON-NLS-1$
	}
	
	
}
