package com.brutalfighters.server.data.players.fighters;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.buffs.Buff_AirJump;
import com.brutalfighters.server.data.buffs.Buff_Slow;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.projectiles.types.RedEnergyBall;
import com.brutalfighters.server.matches.GameMatchManager;
import com.brutalfighters.server.util.AOE;
import com.brutalfighters.server.util.CollisionDetection;
import com.brutalfighters.server.util.Vec2;
import com.esotericsoftware.kryonet.Connection;

public class Lust extends Fighter {

	public Lust(Connection connection, int team, Base base, String m_id) {
		super(connection, team, base, m_id, "Lust", 800, 1000, new Vec2(90,100), //$NON-NLS-1$
				14, 26, 48, 500, new Vec2(200,50), 75, 9,
				new int[] {250,200,400,300}, new int[] {600,380,0,450});
	}
	
	// SKILLS

	// Skill 1
	
	// Variables
	public final float S1_DMG = 200, S1_HEIGHT = getPlayer().getSize().getY()*2, S1_WIDTH = 100, S1_JUMP_HEIGHT = 55;
	
	@Override
	public void startSkill1() {
		if(!getPlayer().isCollidingTop() && applySkillMana(0)) {
			getPlayer().getVel().setY(S1_JUMP_HEIGHT);
		} else {
			endSkill1();
		}
	}
	
	@Override
	public void updateSkill1() {
		applyVelocity();
	}

	@Override
	public void skill1() {
		
		updateSkill1();
		
		if(getPlayer().getSkillCD()[0] > 0 && getPlayer().isMidAir()) {
			if(getPlayer().getSkillCD()[0] == getMaxSkillCD()[0] - GameServer.getDelay() * 3) {
				AOE.dealAOE_enemy(getPlayer().getTeam(), CollisionDetection.getBounds(getPlayer().getFlip(), getPlayer().getPos().getX(), getPlayer().getPos().getY(), S1_WIDTH, S1_HEIGHT), -S1_DMG, new Buff[] {new Buff_AirJump()});
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
	@Override
	public void startSkill2() {
		if(applySkillMana(1)) {
			getRunningSpeed().setX(getWalkingSpeed().getX());
		} else {
			endSkill1();
		}
	}
	
	@Override
	public void updateSkill2() {
		applyVelocity();
	}
	
	@Override
	public void skill2() {
		
		updateSkill2();
		
		if(getPlayer().getSkillCD()[1] > 0) {
			if(getPlayer().getSkillCD()[1] == getMaxSkillCD()[1] - GameServer.getDelay() * 3) {
				float xstart = getPlayer().getPos().getX() + convertSpeed(10);
				GameMatchManager.getCurrentProjectiles().add(new RedEnergyBall(this, getPlayer().getFlip(), new Vec2(xstart, getPlayer().getPos().getY()-3), 20, 100, new Buff[] {new Buff_Slow(2)}));
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
		resetSpeeds();
	}
	
	
	
	// Skill 3
	
	public final int S3_HP = 200;
	
	@Override
	public void startSkill3() {
		if(getPlayer().getHP().getX() < getPlayer().getHP().getY() && applySkillMana(2)) {
			applyHP(S3_HP);
			endSkill3();
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
	public void startSkill4() {
		if(applySkillMana(3)) {
			getPlayer().setVulnerable(false);
		} else {
			endSkill4();
		}
	}
	
	@Override
	public void updateSkill4() {
		defaultUpdate();
		applyFlip();
	}
	
	@Override
	public void skill4() {
		updateSkill4();
		
		getPlayer().getSkillCD()[3] -= GameServer.getDelay();
		if(getPlayer().getSkillCD()[3] <= 0) {
			endSkill4();
		}
	}
	
	@Override
	public void endSkill4() {
		getPlayer().getSkillCD()[3] = getMaxSkillCD()[3];
		getPlayer().setSkill4(false);
		getPlayer().disableSkilling();
		getPlayer().setVulnerable(true);
	}
}
