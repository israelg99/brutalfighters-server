package com.brutalfighters.server.util;

import java.io.Serializable;

public class Vec2 implements Serializable {

	private static final long serialVersionUID = 1L;
	public float x, y;
	
	public Vec2(float xa, float ya) {
		this.x = xa;
		this.y = ya;
	}
	public Vec2() {
		this(0, 0);
	}
	public Vec2(Vec2 vec) {
		this(vec.x, vec.y);
	}
	
	public float getX() {
		return this.x;
	}
	public float getY() {
		return this.y;
	}
	
	public void set(int num) {
		setX(num);
		setY(num);
	}
	public void set(int xa, int ya) {
		setX(xa);
		setY(ya);
	}
	public void setX(int xa) {
		this.x = xa;
	}
	public void setY(int ya) {
		this.y = ya;
	}
	
	public void add(int num) {
		addX(num);
		addY(num);
	}
	public void sub(int num) {
		subX(num);
		subY(num);
	}
	public void add(int xa, int ya) {
		addX(xa);
		addY(ya);
	}
	public void sub(int xa, int ya) {
		subX(xa);
		subY(ya);
	}
	public void addX(int num) {
		this.x+=num;
	}
	public void subX(int num) {
		this.x-=num;
	}
	public void addY(int num) {
		this.y+=num;
	}
	public void subY(int num) {
		this.y-=num;
	}
	
	public float getBoth() {
		return getX()+getY();
	}
	public float getMultiplied() {
		return getX()*getY();
	}
}
