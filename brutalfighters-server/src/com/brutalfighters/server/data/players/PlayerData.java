package com.brutalfighters.server.data.players;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.buffs.BuffData;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.matches.GameMatch;
import com.brutalfighters.server.util.Vec2;

public class PlayerData {
	
	// Basic
	private String name;
	
	private Vec2 pos;
	
	private Vec2 size;
	
	private Vec2 hp;
	
	private Vec2 mana;
	
	// The new velocity way, post processing position.
	private Vec2 vel;
	
	// Match Team ID
	private int team;
	
	// Death
	private int DCD;
	
	// Buffs
	private BuffData[] buffs;
	
	// Skills
	private int[] skillCD;
	
	// Flip
	private String flip;
	
	// States
	private boolean onGround;
	private boolean isRunning;
	private boolean isAAttack;
	private boolean isDead;
	private boolean isVulnerable;
	private boolean isFlagged;
	private boolean hasControl;
	
	// Skill States
	private boolean isSkill1;
	private boolean isSkill2;
	private boolean isSkill3;
	private boolean isSkill4;
	private boolean isSkilling;
	
	// Teleport
	private boolean isTeleporting;
	
	// Input
	private boolean isLeft;
	private boolean isRight;
	private boolean isJump;
	
	// Collision
	private boolean collidesLeft;
	private boolean collidesRight;
	private boolean collidesTop;
	private boolean collidesBot;
	
	// Extrapolation
	private boolean isExtrapolating;

	
	public PlayerData(Base base, String name, float maxhp, float maxmana, Vec2 size) {
		
		// Basic
		setName(Character.toUpperCase(name.charAt(0)) + name.substring(1));
		setPos(base.getPos());
		setFlip(base.getFlip());
		setVel(new Vec2(0,0));
		setSize(size);
		
		// Health and Mana
		setHP(new Vec2(maxhp));
		setMana(new Vec2(maxmana));
		
		// CD
		setDCD(GameMatch.getDefaultRespawn());
		
		// Buffs
		setBuffs(new BuffData[0]);
		
		// States
		setRunning(false);
		isOnGround(false);
		setAAttack(false);
		setDead(false);
		setVulnerable(true);
		setFlagged(false);
		setControl(true);
		enableExtrapolating();
		
		// Skill States
		setSkill1(false);
		setSkill2(false);
		setSkill3(false);
		setSkill4(false);
		setSkilling(false);
		
		// Teleport
		disableTeleporting();
		
		// Movement States
		setLeft(false);
		setRight(false);
		setJump(false);
		setRunning(false);
		setDead(false);
		
		// Collisions
		isCollidingLeft(false);
		isCollidingRight(false);
		isCollidingTop(false);
		isCollidingBot(false);
	}
	
	public void reset(Base base) {
	
		// Basic
		setPos(base.getPos());
		setFlip(base.getFlip());
		setVel(new Vec2(0,0));
		
		// Health and Mana
		maxHP();
		maxMana();
		
		// CD
		setDCD(GameMatch.getDefaultRespawn());
		
		// Buffs
		setBuffs(new BuffData[0]);
		
		// States
		setRunning(false);
		isOnGround(false);
		setAAttack(false);
		setDead(false);
		setVulnerable(true);
		setFlagged(false);
		setControl(true);
		enableExtrapolating();
		
		// Skill States
		setSkill1(false);
		setSkill2(false);
		setSkill3(false);
		setSkill4(false);
		setSkilling(false);
		
		// Teleport
		disableTeleporting();
		
		// Movement States
		setLeft(false);
		setRight(false);
		setJump(false);
		setRunning(false);
		setDead(false);
		
		// Collisions
		isCollidingLeft(false);
		isCollidingRight(false);
		isCollidingTop(false);
		isCollidingBot(false);
	}
	
	
	public String getName() {
		return name;
	}
	private void setName(String name) {
		this.name = name;
	}

	public Vec2 getPos() {
		return pos;
	}
	public void setPos(Vec2 pos) {
		this.pos = pos;
	}

	public Vec2 getSize() {
		return size;
	}
	public void setSize(Vec2 size) {
		this.size = size;
	}

	public Vec2 getHP() {
		return hp;
	}
	public boolean hasNoHP() {
		return hp.getX() <= 0;
	}
	public boolean hasHP() {
		return !hasNoHP();
	}
	public void setHP(Vec2 hp) {
		this.hp = hp;
	}
	public void maxHP() {
		if(!isDead()) {
			getHP().setX(getHP().getY());
		}
	}
	public void zeroHP() {
		getHP().resetX();
	}

	public Vec2 getMana() {
		return mana;
	}
	public boolean hasNoMana() {
		return mana.getX() <= 0;
	}
	public boolean hasMana() {
		return !hasNoMana();
	}
	public void setMana(Vec2 mana) {
		this.mana = mana;
	}
	public void maxMana() {
		if(!isDead()) {
			getMana().setX(getMana().getY());
		}
	}
	public void zeroMana() {
		getMana().resetX();
	}

	public Vec2 getVel() {
		return vel;
	}
	public void setVel(Vec2 vel) {
		this.vel = vel;
	}

	public int getTeam() {
		return team;
	}
	public void setTeam(int team) {
		this.team = team;
	}

	public int getDCD() {
		return DCD;
	}
	public boolean isDCD() {
		return DCD > 0;
	}
	public void subDCD() {
		setDCD(getDCD()-GameServer.getDelay());
	}
	public void setDCD(int dCD) {
		DCD = dCD;
	}

	public BuffData[] getBuffs() {
		return buffs;
	}
	public void setBuffs(BuffData[] buffs) {
		this.buffs = buffs;
	}

	public int[] getSkillCD() {
		return skillCD;
	}
	public void setSkillCD(int[] skillCD) {
		this.skillCD = skillCD;
	}

	public String getFlip() {
		return flip;
	}
	public void setFlip(String flip) {
		this.flip = flip;
	}
	public void flipRight() {
		flip = GameMatch.RIGHT;
	}
	public void flipLeft() {
		flip = GameMatch.LEFT;
	}
	

	public boolean onGround() {
		return onGround;
	}
	public void isOnGround(boolean onGround) {
		this.onGround = onGround;
	}

	public boolean isRunning() {
		return isRunning;
	}
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public boolean isAAttack() {
		return isAAttack;
	}
	public void setAAttack(boolean isAAttack) {
		this.isAAttack = isAAttack;
	}

	public boolean isDead() {
		return isDead;
	}
	private void setDead(boolean isDead) {
		this.isDead = isDead;
	}
	public void died() {
		this.isDead = true;
	}
	public void alive() {
		this.isDead = false;
	}

	public boolean isVulnerable() {
		return isVulnerable;
	}
	public void setVulnerable(boolean isVulnerable) {
		this.isVulnerable = isVulnerable;
	}

	public boolean isHoldingFlag() {
		return isFlagged;
	}
	private void setFlagged(boolean isFlagged) {
		this.isFlagged = isFlagged;
	}
	public void stoleFlag() {
		this.isFlagged = true;
	}
	public void droppedFlag() {
		this.isFlagged = false;
	}

	public boolean hasControl() {
		return hasControl;
	}
	public void setControl(boolean hasControl) {
		this.hasControl = hasControl;
	}

	public boolean isSkill1() {
		return isSkill1;
	}
	public void setSkill1(boolean isSkill1) {
		this.isSkill1 = isSkill1;
	}

	public boolean isSkill2() {
		return isSkill2;
	}
	public void setSkill2(boolean isSkill2) {
		this.isSkill2 = isSkill2;
	}

	public boolean isSkill3() {
		return isSkill3;
	}
	public void setSkill3(boolean isSkill3) {
		this.isSkill3 = isSkill3;
	}

	public boolean isSkill4() {
		return isSkill4;
	}
	public void setSkill4(boolean isSkill4) {
		this.isSkill4 = isSkill4;
	}

	public boolean isSkilling() {
		return isSkilling;
	}
	private void setSkilling(boolean isSkilling) {
		this.isSkilling = isSkilling;
	}
	public void disableSkilling() {
		setSkilling(false);
	}
	public void enableSkilling() {
		setSkilling(true);
	}

	public boolean isTeleporting() {
		return isTeleporting;
	}
	public void enableTeleporting() {
		this.isTeleporting = true;
	}
	public void disableTeleporting() {
		this.isTeleporting = false;
	}

	public boolean isLeft() {
		return isLeft;
	}
	public void setLeft(boolean isLeft) {
		this.isLeft = isLeft;
	}

	public boolean isRight() {
		return isRight;
	}
	public void setRight(boolean isRight) {
		this.isRight = isRight;
	}

	public boolean isJump() {
		return isJump;
	}
	public void setJump(boolean isJump) {
		this.isJump = isJump;
	}

	public boolean isCollidingLeft() {
		return collidesLeft;
	}
	public void isCollidingLeft(boolean collidesLeft) {
		this.collidesLeft = collidesLeft;
	}

	public boolean isCollidingRight() {
		return collidesRight;
	}
	public void isCollidingRight(boolean collidesRight) {
		this.collidesRight = collidesRight;
	}

	public boolean isCollidingTop() {
		return collidesTop;
	}
	public void isCollidingTop(boolean collidesTop) {
		this.collidesTop = collidesTop;
	}

	public boolean isCollidingBot() {
		return collidesBot;
	}
	public void isCollidingBot(boolean collidesBot) {
		this.collidesBot = collidesBot;
	}

	public boolean isExtrapolating() {
		return isExtrapolating;
	}
	public void disableExtrapolating() {
		this.isExtrapolating = false;
	}
	public void enableExtrapolating() {
		this.isExtrapolating = true;
	}
}