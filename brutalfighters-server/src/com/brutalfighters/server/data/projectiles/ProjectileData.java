package com.brutalfighters.server.data.projectiles;

import com.brutalfighters.server.util.Vec2;

public class ProjectileData {
	
	private static final String INIT = "init"; //$NON-NLS-1$
	private static final String EXPLODE = "explode"; //$NON-NLS-1$
	
	private Vec2 pos, vel;
	private Vec2 size;
	private String name;
	private String flip;
	private String mode;
	private int time;
	
	public ProjectileData(String name, String flip, Vec2 pos, Vec2 size) {
		setPos(new Vec2(pos));
		setSize(new Vec2(size));
		setVel(new Vec2());
		setName(name);
		setFlip(flip);
		setInit();
	}
	public ProjectileData() {
		this("dummy", "right", new Vec2(), new Vec2()); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public static String getInit() {
		return INIT;
	}
	public static String getExplode() {
		return EXPLODE;
	}
	public void setInit() {
		setMode(getInit());
	}
	public void setExplode() {
		setMode(getExplode());
	}

	public Vec2 getPos() {
		return pos;
	}
	public void setPos(Vec2 pos) {
		this.pos = new Vec2(pos);
	}
	
	public Vec2 getVel() {
		return vel;
	}
	public void setVel(Vec2 vel) {
		this.vel = new Vec2(vel);
	}
	
	public Vec2 getSize() {
		return size;
	}
	public void setSize(Vec2 size) {
		this.size = new Vec2(size);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getFlip() {
		return flip;
	}
	public boolean isRight() {
		return flip.equals("right"); //$NON-NLS-1$
	}
	public boolean isLeft() {
		return flip.equals("left"); //$NON-NLS-1$
	}
	public void setFlip(String flip) {
		this.flip = flip;
	}
	public void setRight() {
		setFlip("right"); //$NON-NLS-1$
	}
	public void setLeft() {
		setFlip("left"); //$NON-NLS-1$
	}
	
	public String getMode() {
		return mode;
	}
	public boolean isExplode() {
		return getMode().equals(getExplode());
	}
	public boolean isInit() {
		return getMode().equals(getInit());
	}
	private void setMode(String mode) {
		this.mode = mode;
	}
	
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public void addTime(int time) {
		setTime(getTime() + time);
	}
}
