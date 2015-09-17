package com.brutalfighters.server.data.projectiles.types;

import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.players.fighters.Fighter;
import com.brutalfighters.server.data.projectiles.Projectile;
import com.brutalfighters.server.util.Vec2;

public class RPG extends Projectile {

	public RPG(Fighter fighter, String flip, Vec2 pos, float speed, float dmg, Buff[] buffs) {
		super("RPG", fighter, flip, pos, new Vec2(60,57), speed, dmg, buffs); //$NON-NLS-1$
	}
}
