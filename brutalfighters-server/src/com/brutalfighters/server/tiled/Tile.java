package com.brutalfighters.server.tiled;

import com.brutalfighters.server.data.objects.Collidable;




public class Tile extends Collidable {
	private int id;
	private String blocked;
	private String step;
	private float ratio;

	public Tile(int id, int y, int x, int height, int width, float ratio, String blocked, String step) {
		this.id = id;
		this.blocked = blocked;
		this.step = step;
		this.ratio = ratio;
		Rectangle(x,y,width,height);
	}
	
	public boolean isBlocked(String value) {
		return blocked.equals(value);
	}
	public String getStep() {
		return step;
	}
	public int getID() {
		return id;
	}
	public float getRatio() {
		return ratio;
	}
}
