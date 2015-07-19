package com.brutalfighters.server.data.players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.buffs.BuffData;
import com.brutalfighters.server.data.buffs.Buffs;
import com.brutalfighters.server.data.flags.Flag;
import com.brutalfighters.server.data.flags.FlagHandler;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.maps.GameMap;
import com.brutalfighters.server.data.maps.Teleport;
import com.brutalfighters.server.matches.GameMatch;
import com.brutalfighters.server.matches.GameMatchManager;
import com.brutalfighters.server.util.CollisionDetection;
import com.brutalfighters.server.util.MathUtil;
import com.esotericsoftware.kryonet.Connection;

public class StaticPlayer {
	
	public static final float airForce = 1.4f;
	public static final int gravityForce = 27;
	public static final int fallingMomentum = 9;
	
	public static void update(Entry<Connection, PlayerData> entry, GameMap map) {
		PlayerData p = entry.getValue();
		Connection cnct = entry.getKey();
		
		if(!applyDeath(p)) {
			
			if(p.isSkilling) {
				applySkill(p, cnct);
			} else if(p.hasControl) {
				applyVelocity(p);
				applyAA(p, cnct);
				applyTeleport(p, map);
			}
					
			applyCollision(p, map);
			applyFlag(p);
			applyRegen(p);
			updateBuffs(p);
			applyPosition(p);

		} else {
			applyBodyGravity(p, map);
			applyAlive(p);
		}
	}
	
	private static void updateBuffs(PlayerData p) {
		List<BuffData> buffs = new ArrayList<BuffData>(Arrays.asList(p.buffs));
		Iterator<BuffData> iterator = buffs.iterator();
		while(iterator.hasNext()) {
		    BuffData buff = iterator.next();
		    Buff.valueOf(buff.name).update(p, buff, iterator);
		}
		p.buffs = buffs.toArray(new BuffData[buffs.size()]);
	}

	private static void applyTeleport(PlayerData p, GameMap map) {
		if(p.isTeleporting) {
			Teleport.applyTeleporting(map, p);
		}
	}

	private static void applyFlag(PlayerData p) {
		GameMatch match = GameMatchManager.getCurrentMatch();
		String mapName = match.getMapName();
		Flag teamFlag = match.getFlag(p.team);
		Flag enemyFlag = match.getEnemyFlag(p.team);
		
		if(!teamFlag.isTaken && !FlagHandler.inBase(teamFlag, mapName, p.team) && FlagHandler.collidesFlag(teamFlag, p)) {
			FlagHandler.toBase(teamFlag, mapName, p.team);
		}
		
		if(FlagHandler.collidesFlag(enemyFlag, p) && !enemyFlag.isTaken && !p.isFlagged) {
			enemyFlag.isTaken = true;
			p.isFlagged = true;
		}
		
		if(p.isFlagged && enemyFlag.isTaken && FlagHandler.collidesBase(p, mapName, p.team) && FlagHandler.inBase(teamFlag, mapName, p.team)) {
			GameMatchManager.getCurrentMatch().addFlag(p.team);
			enemyFlag.isTaken = false;
			p.isFlagged = false;
			FlagHandler.toBase(enemyFlag, mapName, GameMatch.getEnemyTeamID(p.team));
		}
	}

	public static void applyFlip(PlayerData p) {
		if(!p.isRight == p.isLeft) {
			p.flip = p.isRight ? "right" : p.isLeft ? "left" : p.flip; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	public static void applyGravity(PlayerData p) {
		if(p.onGround) {
			p.vely = p.vely < 0 ? 0 : p.vely;
		} else {
			applyGravitation(p);
		}
	}
	public static void applyWalking(PlayerData p, int speed) {
		p.velx = 0;
		if((p.isLeft != p.isRight)) {
			if(p.isRight) {
				p.velx = speed;
				p.flip = "right"; //$NON-NLS-1$
			} else if(p.isLeft) {
				p.velx = -speed;
				p.flip = "left"; //$NON-NLS-1$
			}
		}
	}
	
	
	private static void applyBodyGravity(PlayerData p, GameMap map) {
		if(p.onGround || CollisionDetection.collidesBot(p, map)) {
			p.vely = 0;
			alignGround(p, map.getTileHeight());
		} else {
			applyGravitation(p);
			p.posy += p.vely;
		}
		
	}

	private static void applySkill(PlayerData p, Connection cnct) {
		Champion fighter = Champion.valueOf(p.name);
		if(p.isSkill1) {
			fighter.Skill1(p, cnct);
		} else if(p.isSkill2) {
			fighter.Skill2(p, cnct);
		} else if(p.isSkill3) {
			fighter.Skill3(p, cnct);
		} else if(p.isSkill4) {
			fighter.Skill4(p, cnct);
		}
		
	}

	private static void applyAlive(PlayerData p) {
		if(p.DCD > 0) {
			p.DCD -= GameServer.getDelay();
		} else {
			setNewPlayer(p);
		}
		
	}

	private static boolean applyDeath(PlayerData p) {
		if(!p.isDead) {
			if(p.hp <= 0) {
				p.isDead = true;
				if(p.isFlagged) {
					p.isFlagged = false;
					GameMatchManager.getCurrentMatch().getEnemyFlag(p.team).isTaken = false;
				}
				GameMatchManager.getCurrentMatch().addKill(GameMatch.getEnemyTeamID(p.team));
				p.DCD = GameMatchManager.getCurrentMatch().getRespawnTime();
				return true;
			}
			return false;
		}
		return true;
		
	}

	public static void applyVelocity(PlayerData p) {
		
		Champion fighter = Champion.valueOf(p.name);
		float speed = p.isRunning ? p.running_speed : p.walking_speed;
		
		if(p.onGround) {

			p.vely = p.vely < 0 ? 0 : p.vely;
			
			if(p.isJump && hasFullControl(p)) {
				if(!p.collidesTop) {
					p.vely = fighter.JUMP_HEIGHT;
				} else {
					p.vely = 0;
				}
			}
			
			if(p.collidesTop && p.collidesBot) {
				p.vely = 0;
				p.isJump = false;
			}
			
		} else {
			if(hasFullControl(p) && !p.isJump && p.vely > fighter.JUMP_HEIGHT/2) {
				p.vely = fighter.JUMP_HEIGHT/2;
			}
			speed *= airForce;
			
			// Gravity
			applyGravitation(p);
		}
		
		applyWalking(p, (int)speed);
	}
	
	public static boolean hasFullControl(PlayerData p) {
		return p.hasControl && !p.isSkilling;
	}

	private static void applyCollision(PlayerData p, GameMap map) {
		if(CollisionDetection.collidesTop(p, map)) {
			p.collidesTop = true;
		} else {
			p.collidesTop = false;
		}
		if(CollisionDetection.collidesBot(p, map)) {
			p.collidesBot = true;
			if(p.vely <= 0) {
				p.onGround = true;
				alignGround(p, map.getTileHeight());
			}
		} else {
			p.collidesBot = false;
			p.onGround = false;
		}
		
		if(CollisionDetection.collidesLeft(p,map)) {
			p.collidesLeft = true;
		} else {
			p.collidesLeft = false;
		}
		
		if(CollisionDetection.collidesRight(p,map)) {
			p.collidesRight = true;
		} else {
			p.collidesRight = false;
		}
		
	}
	
	private static void applyAA(PlayerData p, Connection cnct) {
		Champion fighter = Champion.valueOf(p.name);
		if(p.isAAttack && isBum(p) && !p.isFlagged) {
			if(p.AACD <= 0) {
				
				fighter.AAttack(p, cnct);
				
				p.AACD = fighter.AA_CD;
			} else {
				p.AACD -= GameServer.getDelay();
			}
		} else {
			p.AACD = fighter.AA_CD;
		}
		
	}
	
	private static void applyPosition(PlayerData p) {
		
		// Apply velocity
		
		// Check collision Y
		if(movingY(p) && ((p.vely > 0 && !p.collidesTop) 
				|| (p.vely < 0 && !p.collidesBot))) {
			p.posy += p.vely;
		}
		
		// Check collision X
		if(movingX(p)) {
			if((p.velx > 0 && !p.collidesRight)
					|| p.velx < 0 && !p.collidesLeft) {
				p.posx += p.velx;
			}
		}
		
	}
	
	private static void applyRegen(PlayerData p) {
		healMana(p, Champion.valueOf(p.name).MANAREGEN);
	}
	
	public static void applyGravitation(PlayerData p) {
		p.vely = p.vely - fallingMomentum < -gravityForce ? -gravityForce : p.vely - fallingMomentum;	
	}
	public static void alignGround(PlayerData p, int ground) {
		p.posy = (int)(p.posy / ground) * ground + p.height/2 - 1;
	}


	// State Method
	public static boolean movingX(PlayerData p) {
		return p.velx != 0;
	}
	public static boolean movingY(PlayerData p) {
		return p.vely != 0;
	}
	
	public static boolean isBum(PlayerData p) {
		return p.hasControl && !p.isSkilling && p.onGround && !movingX(p) && !movingY(p);
	}
	
	public static PlayerData getNewPlayer(String fighter, float x, float y, String flip, String m_id) {
		PlayerData player = new PlayerData();
		setNewPlayer(player, fighter, x, y, flip, m_id);
		
		return player;
	}
	
	public static void setNewPlayer(PlayerData p) {
		Base base = GameMatchManager.getCurrentMap().getBase(p.team);
		setNewPlayer(p, p.name, base.pos.getX(), base.pos.getY(), base.flip, p.m_id);
	}
	
	public static void setNewPlayer(PlayerData player, String fighter, float x, float y, String flip, String m_id) {
		
		String temp = Character.toUpperCase(fighter.charAt(0)) + fighter.substring(1);
		Champion c;
		if(Champion.contains(temp)) { // Second check our first check is executed in the `connectPlayer()` function in the `GameMatchManager` class.
			c = Champion.valueOf(temp);
		} else {
			return;
		}
		
		// Basic
		player.name = Character.toUpperCase(c.NAME.charAt(0)) + c.NAME.substring(1);
		
		player.posx = x;
		player.posy = y;
		
		player.width = c.WIDTH;
		player.height = c.HEIGHT;
		
		player.flip = flip;
		
		player.velx = 0;
		player.vely = 0;
		
		player.walking_speed = c.WALKING_SPEED;
		player.running_speed = c.RUNNING_SPEED;
		
		player.maxhp = c.MAXHP;
		player.hp = player.maxhp;
		
		player.maxmana = c.MAXMANA;
		player.mana = player.maxmana;
		
		// Match ID
		player.m_id = m_id;
		
		// Connection
		player.isConnected = true;
		
		// CD
		player.AACD = c.AA_CD;
		player.DCD = GameMatch.getDefaultRespawn();
		
		player.buffs = new BuffData[0];
		
		// Skill Mana
		player.skillMana = c.skillMana.clone();
		
		// Skill Temp CD
		player.skillCD = c.skillTempCD.clone();
		
		// States
		player.isRunning = false;
		player.onGround = false;
		player.isAAttack = false;
		player.isDead = false;
		player.isVulnerable = true;
		player.isFlagged = false;
		player.hasControl = true;
		
		// Skill States
		player.isSkill1 = false;
		player.isSkill2 = false;
		player.isSkill3 = false;
		player.isSkill4 = false;
		player.isSkilling = false;
		
		// Teleport
		player.isTeleporting = false;
		
		// Movement States
		player.isLeft = false;
		player.isRight = false;
		player.isJump = false;
		player.isRunning = false;
		player.isDead = false;
		
		// Collisions
		player.collidesLeft = false;
		player.collidesRight = false;
		player.collidesTop = false;
		player.collidesBot = false;
	}
	
	// Buffs
	public static void applyBuffs(PlayerData p, BuffData[] buffs) {
		if(p.isVulnerable) {
			for(int i = 0; i < buffs.length; i++) {
				// Add Buff
				Buffs.addBuff(p, Buff.getBuff(buffs[i]));
				
				// We need to get a new buff(), because buffs are passed by ref by value,
				// that's how are objects are treated in Java and we don't want
				// players to refer to the same buff object.
				
				// Start/Initiate the Buff
				Buff.valueOf(p.buffs[p.buffs.length-1].name).start(p, p.buffs.length);
			}
		}
	}
	
	// HP
	public static void applyHP(PlayerData p, int hp) {
		if(hp > 0) { 
			healHP(p, hp);
			return;
		} else if(hp < 0) {
			dealHP(p, hp);
			return;
		}
	}
	public static void applyRandomHP(PlayerData p, int hp) {
		applyHP(p, hp - MathUtil.nextInt(0, Math.abs((int)(hp*0.1))) + MathUtil.nextInt(0, Math.abs((int)(hp*0.1))));
	}
	private static void dealHP(PlayerData p, int dmg) {
		if(!p.isDead && p.hp > 0 && p.isVulnerable) {
			p.hp = p.hp + dmg < 0 ? 0 : p.hp + dmg; // the dmg itself is negative.
		}
	}
	private static void healHP(PlayerData p, int heal) {
		Champion fighter = Champion.valueOf(p.name);
		if(!p.isDead) {
			p.hp = p.hp + heal > fighter.MAXHP ?
					fighter.MAXHP : p.hp + heal;
		}
	}
	public static void maxHP(PlayerData p) {
		if(!p.isDead) {
			p.hp = Champion.valueOf(p.name).MAXHP;
		}
	}
	public static void zeroHP(PlayerData p) {
		p.hp =  0;
	}
	
	// MANA
	public static void applyMana(PlayerData p, int mana) {
		if(mana > 0) { 
			healMana(p, mana);
			return;
		} else if(mana < 0) {
			dealMana(p, mana);
			return;
		}
	}
	public static void applyRandomMana(PlayerData p, int mana) {
		applyMana(p, mana - MathUtil.nextInt(0, (int)(mana*0.1) + MathUtil.nextInt(0, (mana))));
	}
	private static void dealMana(PlayerData p, int dmg) {
		if(!p.isDead && p.mana > 0 && p.isVulnerable) {
			p.mana = p.mana + dmg < 0 ? 0 : p.mana + dmg; // the dmg itself is negative.
		}
	}
	private static void healMana(PlayerData p, int heal) {
		Champion fighter = Champion.valueOf(p.name);
		if(!p.isDead) {
			p.mana = p.mana + heal > fighter.MAXMANA ?
					fighter.MAXMANA : p.mana + heal;
		}
	}
	public static void MAXMANA(PlayerData p) {
		if(!p.isDead) {
			p.mana = Champion.valueOf(p.name).MAXMANA;
		}
	}
	public static void zeroMana(PlayerData p) {
		p.mana =  0;
	}
	
	// SPEED CONVERTION
	public static int convertSpeed(PlayerData p, int speed) {
		return p.flip.equals("right") ? speed : -speed; //$NON-NLS-1$
	}
}
