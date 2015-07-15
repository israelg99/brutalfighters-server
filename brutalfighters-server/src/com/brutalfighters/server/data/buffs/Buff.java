package com.brutalfighters.server.data.buffs;

import java.util.Iterator;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.players.Champion;
import com.brutalfighters.server.data.players.PlayerData;
import com.brutalfighters.server.data.players.StaticPlayer;


public enum Buff {
	HALF_SLOW("HALF_SLOW", 2000) { //$NON-NLS-1$

		@Override
		public void start(PlayerData p, int index) {
			p.walking_speed /= 2;
			p.running_speed /= 2;
		}
		
		@Override
		public void update(PlayerData p, BuffData buff, Iterator<BuffData> iterator) {
			if(Buff.isActive(p, buff, iterator)) {
				/*if(buff.time / GameServer.getDelay() % 3 == 0) {
					Champion fighter = Champion.valueOf(p.name);
					p.walking_speed += p.walking_speed < fighter.WALKING_SPEED ? 1 : 0;
					p.running_speed += p.running_speed < fighter.RUNNING_SPEED ? 1 : 0;
				}*/
			}
		}

		@Override
		public void end(PlayerData p, BuffData buff, Iterator<BuffData> iterator) {
			Champion.valueOf(p.name).assignSpeed(p);
			
			iterator.remove();
		}
		
	},
	
	BIT_SLOW("BIT_SLOW", 2000) { //$NON-NLS-1$
		
		@Override
		public void start(PlayerData p, int index) {
			p.walking_speed -= p.walking_speed/5;
			p.running_speed -= p.walking_speed/5;
		}
		
		@Override
		public void end(PlayerData p, BuffData buff, Iterator<BuffData> iterator) {
			Champion.valueOf(p.name).assignSpeed(p);
			
			iterator.remove();
		}
		
	},
	
	ICE_STUN("ICE_STUN", 3000) { //$NON-NLS-1$
		
		@Override
		public void start(PlayerData p, int index) {
			p.velx = 0;
			p.vely = 0;
			p.walking_speed = 0;
			p.running_speed = 0;
			p.hasControl = false;
			p.isVulnerable = false;
		}
		
		@Override
		public void update(PlayerData p, BuffData buff, Iterator<BuffData> iterator) {
			if(Buff.isActive(p, buff, iterator)) {
				StaticPlayer.applyGravity(p);
			}
		}
		
		@Override
		public void end(PlayerData p, BuffData buff, Iterator<BuffData> iterator) {
			p.hasControl = true;
			p.isVulnerable = true;
			
			Champion.valueOf(p.name).assignSpeed(p);
			
			iterator.remove();
		}
		
	},
	
	SLOW_HEALING("SLOW_HEALING", 2000) { //$NON-NLS-1$
		
		private final int heal = 10;
		
		@Override
		public void update(PlayerData p, BuffData buff, Iterator<BuffData> iterator) {
			if(Buff.isActive(p, buff, iterator)) {
				if(Buff.isTime(buff, 2)) {
					StaticPlayer.applyRandomHP(p, heal);
				}
			}
		}
		
	},
	
	RED_BATS("RED_BATS", 3000) { //$NON-NLS-1$
		
		private final int dmg = 50;
		
		@Override
		public void update(PlayerData p, BuffData buff, Iterator<BuffData> iterator) {
			if(Buff.isActive(p, buff, iterator)) {
				if(Buff.isTime(buff, 10)) {
					StaticPlayer.applyRandomHP(p, -dmg);
				}
			}
		}
		
	},
	
	BURN("BURN", 3000) { //$NON-NLS-1$
		
		private final int dmg = 2;
		
		@Override
		public void update(PlayerData p, BuffData buff, Iterator<BuffData> iterator) {
			if(Buff.isActive(p, buff, iterator)) {
				if(Buff.isTime(buff, 3)) {
					StaticPlayer.applyRandomHP(p, -dmg);
				}
			}
		}
		
	};
	
	public final String NAME;
	public final int TIME;
	
	Buff(String name, int time) {
		this.NAME = name;
		this.TIME = time;
	}
	
	
	public void start(PlayerData p, int index) {
		
	}
	
	public void update(PlayerData p, BuffData buff, Iterator<BuffData> iterator) {
		Buff.isActive(p, buff, iterator);
	}
	
	public void end(PlayerData p, BuffData buff, Iterator<BuffData> iterator) {
		iterator.remove();
	}
	
	private static boolean isActive(PlayerData p, BuffData buff, Iterator<BuffData> iterator) {
		if(buff.time > 0) {
			buff.time -= GameServer.getDelay();
			return true;
		}
		Buff.valueOf(buff.name).end(p, buff, iterator);
		return false;
	}
	private static boolean isTime(BuffData buff, int number) {
		return buff.time / GameServer.getDelay() % number == 0;
	}
	
	public static BuffData getBuff(String name, int time) {
		BuffData buff = new BuffData();
		buff.name = Buff.valueOf(name).NAME;
		buff.time = time;
		
		return buff;
	}
	public static BuffData getBuff(String name) {
		BuffData buff = new BuffData();
		buff.name = Buff.valueOf(name).NAME;
		buff.time = Buff.valueOf(name).TIME;
		
		return buff;
	}
	public static BuffData getBuff(BuffData ref) {
		return getBuff(ref.name, ref.time);
	}
}
