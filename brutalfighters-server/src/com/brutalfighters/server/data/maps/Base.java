package com.brutalfighters.server.data.maps;

import com.brutalfighters.server.util.Vec2;

public class Base {
	public Vec2 pos;
	public String flip;
	
	public Base(Vec2 pos, String flip) {
		this.pos = pos;
		this.flip = flip;
	}
	
	public Base(int posx, int posy, String flip) {
		this(new Vec2(posx, posy), flip);
	}
}
