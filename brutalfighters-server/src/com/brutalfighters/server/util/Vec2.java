package com.brutalfighters.server.util;


public class Vec2 {
	
	private float x, y;
	
	public Vec2(float xa, float ya) {
		setX(xa);
		setY(ya);
	}
	public Vec2() {
		this(0, 0);
	}
	public Vec2(Vec2 vec) {
		this(vec.getX(), vec.getY());
	}
	public Vec2(float xy) {
		this(xy, xy);
	}
	
	public float getX() {
		return this.x;
	}
	public float getY() {
		return this.y;
	}
	
	public void set(float x, float y) {
		setX(x);
		setY(y);
	}
	public void set(float num) {
		set(num,num);
	}
	public void set(Vec2 pos) {
		set(pos.getX(), pos.getY());
	}
	public void setX(float xa) {
		this.x = xa;
	}
	public void setY(float ya) {
		this.y = ya;
	}
	
	public void reset() {
		setX(0);
		setY(0);
	}
	public void resetX() {
		setX(0);
	}
	public void resetY() {
		setY(0);
	}
	
	public void add(float num) {
		addX(num);
		addY(num);
	}
	public void sub(float num) {
		subX(num);
		subY(num);
	}
	public void add(float xa, float ya) {
		addX(xa);
		addY(ya);
	}
	public void sub(float xa, float ya) {
		subX(xa);
		subY(ya);
	}
	public void addX(float num) {
		this.x+=num;
	}
	public void subX(float num) {
		this.x-=num;
	}
	public void addY(float num) {
		this.y+=num;
	}
	public void subY(float num) {
		this.y-=num;
	}
	
	public float getBoth() {
		return getX()+getY();
	}
	public float getMultiplied() {
		return getX()*getY();
	}
	public Vec2 getVec2() {
		return new Vec2(getX(), getY());
	}
}
