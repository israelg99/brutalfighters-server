package com.brutalfighters.server.data.maps;

import com.brutalfighters.server.util.Vec2;

public class Base {
	private Vec2 pos;
	private String flip;

	public Base(Vec2 pos, String flip) {
		setPos(pos);
		setFlip(flip);
	}
	
	public Base(int posx, int posy, String flip) {
		this(new Vec2(posx, posy), flip);
	}
	
	public float getX() {
		return getPos().getX();
	}
	public float getY() {
		return getPos().getY();
	}
	public Vec2 getPos() {
		return pos;
	}
	private void setPos(Vec2 pos) {
		this.pos = new Vec2(pos);
	}

	public String getFlip() {
		return flip;
	}
	private void setFlip(String flip) {
		this.flip = flip;
	}
}
