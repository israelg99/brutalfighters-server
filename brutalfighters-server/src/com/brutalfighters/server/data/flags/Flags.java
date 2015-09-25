package com.brutalfighters.server.data.flags;

public class Flags {
	private Flag[] flags;
	
	public Flags(Flag[] flags) {
		setFlags(flags);
	}
	public Flags(int length) {
		this(new Flag[length]);
	}
	
	private Flag[] getFlags() {
		return flags;
	}
	public Flag getFlag(int team) {
		return flags[team];
	}
	private void setFlags(Flag[] flags) {
		this.flags = flags;
	}
	public void setFlag(int team, Flag flag) {
		this.flags[team] = flag;
	}
	
	public void updateFlags() {
		for(int i = 0; i < getFlags().length; i++) {
			getFlag(i).updateFlag(i);
		}
	}
	
	public FlagData[] getData() {
		FlagData[] flags = new FlagData[getFlags().length];
		for(int i = 0; i < flags.length; i++) {
			flags[i] = getFlag(i).getFlag();
		}
		
		return flags;
	}
}
