package com.brutalfighters.server.data.players;

import com.brutalfighters.server.data.buffs.BuffData;

public class PlayerData {
	
	// Basic
	public String name;
	
	public float posx;
	public float posy;
	
	public int width;
	public int height;
	
	public int hp;
	public int maxhp;
	
	public int mana;
	public int maxmana;
	
	// Match ID
	public String m_id;
	
	// Match Team ID
	public int team;
	
	// Connection
	public boolean isConnected;
	
	// CD
	public int AACD;
	public int DCD;
	
	// Buffs
	public BuffData[] buffs;
	
	// Skill Mana
	public int[] skillMana;
	
	// Skill Temp CD
	public int[] skillCD;
	
	// The new velocity way, post processing position.
	public float velx;
	public float vely;
	
	// Movement and Running Speed
	public int walking_speed;
	public int running_speed;
	
	// Flip
	public String flip;
	
	// States
	public boolean onGround;
	public boolean isRunning;
	public boolean isAAttack;
	public boolean isDead;
	public boolean isVulnerable;
	public boolean isFlagged;
	public boolean hasControl;
	
	// Skill States
	public boolean isSkill1;
	public boolean isSkill2;
	public boolean isSkill3;
	public boolean isSkill4;
	public boolean isSkilling;
	
	// Teleport
	public boolean isTeleporting;
	
	// Input
	public boolean isLeft;
	public boolean isRight;
	public boolean isJump;
	
	// Collision
	public boolean collidesLeft;
	public boolean collidesRight;
	public boolean collidesTop;
	public boolean collidesBot;
}