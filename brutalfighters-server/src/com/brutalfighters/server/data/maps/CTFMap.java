package com.brutalfighters.server.data.maps;

import com.brutalfighters.server.data.flags.Flag;
import com.brutalfighters.server.data.flags.FlagHandler;
import com.brutalfighters.server.tiled.TiledMap;
import com.brutalfighters.server.util.Vec2;

public class CTFMap extends GameMap {

	private Base[] bases;
	private Flag[] flags;
	
	public CTFMap(TiledMap map) {
		super(map);

		this.bases = new Base[2];
		this.flags = new Flag[2];
		
		this.setBase(0, new Base(new Vec2(leftBoundary+100, 300), "right")); //$NON-NLS-1$
		this.setBase(1, new Base(new Vec2(rightBoundary-100, 300), "left")); //$NON-NLS-1$

		// 284
		this.setFlag(0, FlagHandler.getFlag(leftBoundary+400, 291, "right")); //$NON-NLS-1$
		this.setFlag(1, FlagHandler.getFlag(rightBoundary-400, 291, "left")); //$NON-NLS-1$
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
	public void setBase(int index, Base base) {
		bases[index] = base;
	}
	
	
	// FLAGS
	public Flag getFlag(int index) {
		Flag flag = flags[index];
		return FlagHandler.getFlag(flag.posx, flag.posy, flag.flip);
	}
	public void setFlag(int index, Flag flag) {
		flags[index] = FlagHandler.getFlag(flag.posx, flag.posy, flag.flip);
	}
}
