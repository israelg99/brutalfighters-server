package com.brutalfighters.server.data.projectiles;

import java.util.Iterator;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.buffs.BuffData;
import com.brutalfighters.server.matches.GameMatchManager;
import com.brutalfighters.server.util.AOE;
import com.brutalfighters.server.util.Vec2;

public enum Projectile {
	
	Blaze_PHEONIX(280, 500, 0, 0) {
		@Override
		public void initialize(ActiveProjectile proj) {
			ProjectileData p = proj.data();
			p.vely = 5;
		}
		
		@Override
		public void update(ActiveProjectile proj, Iterator<ActiveProjectile> iterator) {
			ProjectileData p = proj.data();
			p.vely += 0.2f;
			if(p.vely >= 33.2f) {
				iterator.remove();
				return;
			}
			p.y += p.vely;
		}
	},
	
	Blaze_SkullFire(65, 57, 30, 0, new BuffData[] {(Buff.getBuff("BURN"))}) { //$NON-NLS-1$

	},
	
	Blaze_BloodBall(65, 57, 20, 155) {
		
	},
	
	Dusk_PurpleBat(65, 57, 30, 25) {
		
	},
	
	Dusk_BATS(130, 77, 45, 100) {
		
	},
	
	Dusk_LASER(180, 30, 30, 110) {
		
	},
	
	Dusk_BATZ(180, 90, 17, 0, new BuffData[] {(Buff.getBuff("RED_BATS"))}) { //$NON-NLS-1$
		
	},
	
	Chip_MINE(60, 57, 0, 100, new BuffData[] {(Buff.getBuff("HALF_SLOW"))}) { //$NON-NLS-1$
		@Override
		public void initialize(ActiveProjectile proj) {
			ProjectileData p = proj.data();
			if(!GameMatchManager.getCurrentMap().checkBoundaries(new Vec2(p.x, p.y))) {
				p.mode = explode;
			} else {
				p.velx = p.flip.equals("right") ? speed : -speed; //$NON-NLS-1$
			}
		}
		
		@Override
		public void update(ActiveProjectile proj, Iterator<ActiveProjectile> iterator) {
			ProjectileData p = proj.data();
			p.time += GameServer.getDelay();
			if(p.mode != explode) {
				if(p.time >= 1000 && dealDamage(proj)) { // WE MUST USE `getTeam()` and not `getConnection()` because the fighter may disconnect while the projectile is still on which will cause null.
					p.mode = explode;
					return;
				} else if(!GameMatchManager.getCurrentMap().intersectsSurroundX(p.x, p.y-p.height/2, proj.getBounds())) {
					p.vely = -9;
					p.y += p.vely;
				} else { p.vely = 0; }
			} else {
				iterator.remove();
			}
		}
	},
	
	Chip_TNT(60, 67, 35, 200, new BuffData[] {(Buff.getBuff("HALF_SLOW"))}) { //$NON-NLS-1$
		@Override
		public void update(ActiveProjectile proj, Iterator<ActiveProjectile> iterator) {
			ProjectileData p = proj.data();
			p.time += GameServer.getDelay();
			if(p.mode != explode) {
				if(p.time >= 500) {
					p.mode = explode;
					dealDamage(proj);
				} else if(!isColliding(proj)) {
					p.x += p.velx;
				} else {
					p.mode = explode;
					return;
				}
			} else {
				iterator.remove();
			}
		}
	},
	
	Chip_RPG(60, 57, 50, 170) {

	},
	
	Surge_EnergyWave(185, 90, 17, 200, new BuffData[] {(Buff.getBuff("ICE_STUN"))}) { //$NON-NLS-1$

	},
	
	Surge_DashBall(65, 57, 30, 100) {

	},
	
	Lust_EnergyBall(65, 57, 20, 100, new BuffData[] {(Buff.getBuff("HALF_SLOW"))}) { //$NON-NLS-1$
		
	};
	
	public final int WIDTH, HEIGHT;
	public final int speed, dmg;
	public final BuffData[] buffs;
	
	Projectile(int w, int h, int speedX, int dmg, BuffData[] buffs) {
		this.WIDTH = w;
		this.HEIGHT = h;
		this.speed = speedX;
		this.dmg = dmg;
		this.buffs = buffs;
	}
	
	Projectile(int w, int h, int speedX, int dmg) {
		this(w,h,speedX,dmg, new BuffData[0]);
	}
	
	public void initialize(ActiveProjectile proj) {
		ProjectileData p = proj.data();
		if(isColliding(proj)) {
			p.mode = explode;
		} else {
			p.velx = p.flip.equals("right") ? speed : -speed; //$NON-NLS-1$
		}
	}
	public void update(ActiveProjectile proj, Iterator<ActiveProjectile> iterator) {
		ProjectileData p = proj.data();
		p.time += GameServer.getDelay();
		if(p.mode != explode) {
			if(dealDamage(proj)) {
				p.mode = explode;
			} else if(!isColliding(proj)) {
				p.x += p.velx;
			} else {
				p.mode = explode;
				return;
			}
		} else {
			iterator.remove();
		}
	}
	
	public boolean isColliding(ActiveProjectile proj) { // We may modify it, so it's not static.
		ProjectileData pd = proj.data();
		return !GameMatchManager.getCurrentMap().checkBoundaries(new Vec2(pd.x, pd.y)) || GameMatchManager.getCurrentMap().intersects(pd.x+pd.velx, pd.y+pd.vely, proj.getBounds());
	}
	
	public boolean dealDamage(ActiveProjectile proj) {
		return AOE.dealAOE_enemy(proj.getTeam(), proj.getBounds(), -dmg, buffs);
	}
	
	public static final String explode = "explode"; //$NON-NLS-1$
	
	public static int convertSpeed(ProjectileData p, int speed) {
		return p.flip.equals("right") ? speed : -speed; //$NON-NLS-1$
	}

	public static boolean contains(String projectile) {
	    for (Projectile c : Projectile.values()) {
	        if (c.name().equals(projectile)) {
	            return true;
	        }
	    }

	    return false;
	}
	
	public static void init() {
		values();
	}
}
