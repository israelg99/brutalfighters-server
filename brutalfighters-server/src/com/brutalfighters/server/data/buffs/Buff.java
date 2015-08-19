package com.brutalfighters.server.data.buffs;

import java.util.Iterator;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.players.PlayerData;
import com.brutalfighters.server.data.players.fighters.Fighter;


public enum Buff {
	HALF_SLOW("HALF_SLOW", 2000) { //$NON-NLS-1$

		@Override
		public void start(Fighter p, int index) {
			p.getPlayer().walking_speed /= 2;
			p.getPlayer().running_speed /= 2;
		}
		
		@Override
		public void update(Fighter p, BuffData buff, Iterator<BuffData> iterator) {
			if(Buff.isActive(p, buff, iterator)) {
				/*if(buff.time / GameServer.getDelay() % 3 == 0) {
					Champion fighter = Champion.valueOf(p.name);
					p.walking_speed += p.walking_speed < fighter.WALKING_SPEED ? 1 : 0;
					p.running_speed += p.running_speed < fighter.RUNNING_SPEED ? 1 : 0;
				}*/
			}
		}

		@Override
		public void end(Fighter p, BuffData buff, Iterator<BuffData> iterator) {
			p.assignSpeed();
			
			iterator.remove();
		}
		
	},
	
	BIT_SLOW("BIT_SLOW", 2000) { //$NON-NLS-1$
		
		@Override
		public void start(Fighter p, int index) {
			p.getPlayer().walking_speed -= p.getPlayer().walking_speed/5;
			p.getPlayer().running_speed -= p.getPlayer().walking_speed/5;
		}
		
		@Override
		public void end(Fighter p, BuffData buff, Iterator<BuffData> iterator) {
			p.assignSpeed();
			
			iterator.remove();
		}
		
	},
	
	ICE_STUN("ICE_STUN", 3000) { //$NON-NLS-1$
		
		@Override
		public void start(Fighter p, int index) {
			PlayerData player = p.getPlayer();
			player.velx = 0;
			player.vely = 0;
			player.walking_speed = 0;
			player.running_speed = 0;
			player.hasControl = false;
			player.isVulnerable = false;
		}
		
		@Override
		public void update(Fighter p, BuffData buff, Iterator<BuffData> iterator) {
			if(Buff.isActive(p, buff, iterator)) {
				p.applyGravity();
			}
		}
		
		@Override
		public void end(Fighter p, BuffData buff, Iterator<BuffData> iterator) {
			p.getPlayer().hasControl = true;
			p.getPlayer().isVulnerable = true;
			
			p.assignSpeed();
			
			iterator.remove();
		}
		
	},
	
	SLOW_HEALING("SLOW_HEALING", 2000) { //$NON-NLS-1$
		
		private final int heal = 10;
		
		@Override
		public void update(Fighter p, BuffData buff, Iterator<BuffData> iterator) {
			if(Buff.isActive(p, buff, iterator)) {
				if(Buff.isTime(buff, 2)) {
					p.applyRandomHP(heal);
				}
			}
		}
		
	},
	
	RED_BATS("RED_BATS", 3000) { //$NON-NLS-1$
		
		private final int dmg = 50;
		
		@Override
		public void update(Fighter p, BuffData buff, Iterator<BuffData> iterator) {
			if(Buff.isActive(p, buff, iterator)) {
				if(Buff.isTime(buff, 10)) {
					p.applyRandomHP(-dmg);
				}
			}
		}
		
	},
	
	BURN("BURN", 3000) { //$NON-NLS-1$
		
		private final int dmg = 2;
		
		@Override
		public void update(Fighter p, BuffData buff, Iterator<BuffData> iterator) {
			if(Buff.isActive(p, buff, iterator)) {
				if(Buff.isTime(buff, 3)) {
					p.applyRandomHP(-dmg);
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
	
	
	public void start(Fighter p, int index) {
		
	}
	
	public void update(Fighter p, BuffData buff, Iterator<BuffData> iterator) {
		Buff.isActive(p, buff, iterator);
	}
	
	public void end(Fighter p, BuffData buff, Iterator<BuffData> iterator) {
		iterator.remove();
	}
	
	private static boolean isActive(Fighter p, BuffData buff, Iterator<BuffData> iterator) {
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
