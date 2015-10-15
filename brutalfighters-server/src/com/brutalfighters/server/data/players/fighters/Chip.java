package com.brutalfighters.server.data.players.fighters;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.buffs.Buff_Slow;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.projectiles.types.Mine;
import com.brutalfighters.server.data.projectiles.types.RPG;
import com.brutalfighters.server.data.projectiles.types.TNT;
import com.brutalfighters.server.matches.GameMatchManager;
import com.brutalfighters.server.util.Vec2;
import com.esotericsoftware.kryonet.Connection;

public class Chip extends Fighter {
	
	public Chip(Connection connection, int team, Base base, String m_id) {
		super(connection, team, base, m_id, "Chip", 600, 1000, new Vec2(90,100), 12, //$NON-NLS-1$
				21, 52, 500, new Vec2(200,50), 68, 9,
				new int[] {300,200,300,600}, new int[] {900,820,560,440});
	}
	
	// SKILLS
	
	// Skill 1
	
	@Override
	public void updateSkill1() {
		defaultUpdate();
		applyFlip();
	}
	
	@Override
	public void skill1() {
		
		updateSkill1();
		
		if(getPlayer().getSkillCD()[0] > 0) {
				if(getPlayer().getSkillCD()[0] == getMaxSkillCD()[0] - GameServer.getDelay() * 14) {
					float xstart = getPlayer().getPos().getX() + convertSpeed(getPlayer().getSize().getX()/2);
					GameMatchManager.getCurrentProjectiles().add(new RPG(this, getPlayer().getFlip(), new Vec2(xstart, getPlayer().getPos().getY()-3), 50, 170, new Buff[0]));
				}
			getPlayer().getSkillCD()[0] -= GameServer.getDelay();
		} else {
			endSkill1();
		}
	}
	
	@Override
	public void endSkill1() {
		getPlayer().getSkillCD()[0] = getMaxSkillCD()[0];
		getPlayer().setSkill1(false);
		getPlayer().disableSkilling();
	}

	
	// Skill 2

	// Variables
	public final int S2_Velocity = 5;
	
	@Override
	public void startSkill2() {
		if(!getPlayer().isCollidingTop() && applySkillMana(1)) {
			getPlayer().getVel().setY(getJumpHeight().getX());
			
		} else {
			endSkill2();
		}
	}
	
	@Override
	public void updateSkill2() {
		applyJump();
		applyGravity();
		applyWalking();
	}
	
	@Override
	public void skill2() {
		
		updateSkill2();
		
		if(getPlayer().getSkillCD()[1] > 0) {
			if(getPlayer().getSkillCD()[1] >= getMaxSkillCD()[1] - GameServer.getDelay() * 10) {
				getPlayer().getVel().addY(S2_Velocity);
			}
			getPlayer().getSkillCD()[1] -= GameServer.getDelay();
		} else {
			endSkill2();
		}
	}
	
	@Override
	public void endSkill2() {
		getPlayer().getSkillCD()[1] = getMaxSkillCD()[1];
		getPlayer().setSkill2(false);
		getPlayer().disableSkilling();
	}
	
	
	// Skill 3
	
	@Override
	public void skill3() {
		updateSkill3();
		
		if(getPlayer().getSkillCD()[2] > 0) {
			if(getPlayer().getSkillCD()[2] == getMaxSkillCD()[2] - GameServer.getDelay() * 9) {
				float xstart = getPlayer().getPos().getX() + convertSpeed(getPlayer().getSize().getX()-10);
				GameMatchManager.getCurrentProjectiles().add(new TNT(this, getPlayer().getFlip(), new Vec2(xstart, getPlayer().getPos().getY()-8), 35, 200, new Buff[] {new Buff_Slow(2)}));
			}
			getPlayer().getSkillCD()[2] -= GameServer.getDelay();
		} else {
			endSkill3();
		}
		
	}
	
	@Override
	public void endSkill3() {
		getPlayer().getSkillCD()[2] = getMaxSkillCD()[2];
		getPlayer().setSkill3(false);
		getPlayer().disableSkilling();
	}
	
	
	// Skill 4
	
	@Override
	public void skill4() {
		updateSkill4();
		
		if(getPlayer().getSkillCD()[3] > 0) {
			if(getPlayer().getSkillCD()[3] == getMaxSkillCD()[3] - GameServer.getDelay() * 5) {
				float xstart = getPlayer().getPos().getX() + convertSpeed(getPlayer().getSize().getX()/2+5);
				GameMatchManager.getCurrentProjectiles().add(new Mine(this, getPlayer().getFlip(), new Vec2(xstart, getPlayer().getPos().getY()-getPlayer().getSize().getY()/2-10), 100, new Buff[] {new Buff_Slow(2)}));
			}
			getPlayer().getSkillCD()[3] -= GameServer.getDelay();
		} else {
			endSkill4();
		}
		
	}
	
	@Override
	public void endSkill4() {
		getPlayer().getSkillCD()[3] = getMaxSkillCD()[3];
		getPlayer().setSkill4(false);
		getPlayer().disableSkilling();
	}
}
