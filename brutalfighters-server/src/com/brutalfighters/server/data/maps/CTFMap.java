package com.brutalfighters.server.data.maps;

import com.brutalfighters.server.data.flags.Flag;
import com.brutalfighters.server.data.flags.FlagHandler;
import com.brutalfighters.server.matches.GameMatch;
import com.brutalfighters.server.tiled.TiledMap;
import com.brutalfighters.server.util.Vec2;

public class CTFMap extends GameMap {

	private Base[] bases;
	private Flag[] flags;
	
	public CTFMap(TiledMap map) {
		super(map);

		this.bases = new Base[GameMatch.TEAM_LENGTH];
		this.flags = new Flag[GameMatch.TEAM_LENGTH];
		
		this.setBase(GameMatch.TEAM1, new Base(new Vec2(leftBoundary+100, 384), "right")); //$NON-NLS-1$
		this.setBase(GameMatch.TEAM2, new Base(new Vec2(rightBoundary-100, 384), "left")); //$NON-NLS-1$

		this.setFlag(GameMatch.TEAM1, new Vec2(leftBoundary+400, FlagHandler.HEIGHT + 291), "right"); //$NON-NLS-1$
		this.setFlag(GameMatch.TEAM2, new Vec2(rightBoundary-400, FlagHandler.HEIGHT + 291), "left"); //$NON-NLS-1$
	}
	
	// It's not the main constructor because the default base and flags values
	// refer to the boundaries values, and we can't include them in the parameters, try for yourself and see.
	public CTFMap(TiledMap map, Base base1, Base base2, Flag flag1, Flag flag2) {
		this(map);
		
		this.setBase(0, base1);
		this.setBase(1, base2);
		this.setFlag(0, flag1); 
		this.setFlag(1, flag2);
	}
	
	// BASES
	public Base getBase(int index) {
		return bases[index];
	}
	public void setBase(int index, Vec2 pos, String flip) {
		bases[index] = new Base(pos, flip);
	}
	public void setBase(int index, Base base) {
		bases[index] = base;
	}
	
	
	// FLAGS
	public Flag getFlag(int index) {
		Flag flag = flags[index];
		return FlagHandler.getFlag(new Vec2(flag.posx, flag.posy), flag.flip);
	}
	public void setFlag(int index, Flag flag) {
		flags[index] = FlagHandler.getFlag(new Vec2(flag.posx, flag.posy), flag.flip);
	}
	public void setFlag(int index, Vec2 pos, String flip) {
		flags[index] = FlagHandler.getFlag(pos, flip);
	}
}
