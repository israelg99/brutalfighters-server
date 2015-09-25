package com.brutalfighters.server.data.buffs;

public class BuffData {
	private String name;
	private int time;
	
	public BuffData(String name, int time) {
		setName(name);
		setTime(time);
	}
	public BuffData() {
		this("dummy", 0); //$NON-NLS-1$
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
}
