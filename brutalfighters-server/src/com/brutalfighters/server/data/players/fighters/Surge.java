package com.brutalfighters.server.data.players.fighters;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.buffs.Buff_Slow;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.projectiles.Projectiles;
import com.brutalfighters.server.util.AOE;
import com.brutalfighters.server.util.CollisionDetection;
import com.brutalfighters.server.util.Vec2;
import com.esotericsoftware.kryonet.Connection;

public class Surge extends Fighter {

	public Surge(Base base, String m_id) {
		super(base, m_id, "surge", 1000, 1000, new Vec2(90,100), 8, //$NON-NLS-1$
				16, 44, 500, new Vec2(110,10), 68, 9,
				new int[] {250,400,200,650}, new int[] {500,900,1200,1300});
	}
	
	// SKILLS
	
	// Skill 1
	final int DMG = 20;
	
	@Override
	public void updateSkill1(Connection cnct) {
		defaultUpdate();
		applyFlip();
	}
	
	@Override
	public void skill1(Connection cnct) {
		
		updateSkill1(cnct);
		
		if(getPlayer().getSkillCD()[0] > 0) {
				if(getPlayer().getSkillCD()[0] == max_skillCD[0] - GameServer.getDelay() * 5) {
					float xstart = getPlayer().getPos().getX() + convertSpeed(5);
					Projectiles.addProjectile(cnct, getPlayer().getTeam(), "Surge_DashBall", xstart, getPlayer().getPos().getY(), getPlayer().getFlip(), "init"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				if(getPlayer().getSkillCD()[0] > max_skillCD[0] - GameServer.getDelay() * 5) {
					AOE.dealAOE_enemy(getPlayer().getTeam(), CollisionDetection.getBounds(getPlayer().getFlip(), getPlayer().getPos().getX(), getPlayer().getPos().getY(), 150, 30), -DMG, new Buff[] {(new Buff_Slow(2))});
					getPlayer().getVel().setX(convertSpeed(20));
				}
			getPlayer().getSkillCD()[0] -= GameServer.getDelay();
		} else {
			endSkill1(cnct);
		}
	}
	
	@Override
	public void endSkill1(Connection cnct) {
		getPlayer().getSkillCD()[0] = max_skillCD[0];
		getPlayer().setSkill1(false);
		getPlayer().disableSkilling();
	}

	// Skill 2
	
	@Override
	public void updateSkill2(Connection cnct) {
		defaultUpdate();
	}
	
	@Override
	public void skill2(Connection cnct) {
		
		updateSkill2(cnct);
		
		if(getPlayer().getSkillCD()[1] > 0) {
				if(getPlayer().getSkillCD()[1] == max_skillCD[1] - GameServer.getDelay() * 17) {
					float xstart = getPlayer().getPos().getX() + convertSpeed(getPlayer().getSize().getX()*2);
					Projectiles.addProjectile(cnct, getPlayer().getTeam(), "Surge_EnergyWave", xstart, getPlayer().getPos().getY(), getPlayer().getFlip(), "init"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			getPlayer().getSkillCD()[1] -= GameServer.getDelay();
		} else {
			endSkill2(cnct);
		}
	}
	
	@Override
	public void endSkill2(Connection cnct) {
		getPlayer().getSkillCD()[1] = max_skillCD[1];
		getPlayer().setSkill2(false);
		getPlayer().disableSkilling();
	}
	
	// Skill 3
	
	@Override
	public void updateSkill3(Connection cnct) {
		defaultUpdate();
		applyFlip();
	}
	
	@Override
	public void skill3(Connection cnct) {
		updateSkill3(cnct);
		
		if(getPlayer().getSkillCD()[2] > 0) {
			if(getPlayer().getSkillCD()[2] == max_skillCD[2] - GameServer.getDelay() * 12) {
				getPlayer().setVulnerable(false);
			}
			getPlayer().getSkillCD()[2] -= GameServer.getDelay();
		} else {
			endSkill3(cnct);
		}
	}
	
	@Override
	public void endSkill3(Connection cnct) {
		getPlayer().getSkillCD()[2] = max_skillCD[2];
		getPlayer().setSkill3(false);
		getPlayer().disableSkilling();
		getPlayer().setVulnerable(true);
	}
	
	// Skill 4
	
	final int S4_dmg = 300, S4_X_RANGE = 150, S4_Y_RANGE = 350, DISTANCE = 230;
	
	@Override
	public void skill4(Connection cnct) {
		updateSkill4(cnct);
		
		if(getPlayer().getSkillCD()[3] > 0) {
			if(getPlayer().getSkillCD()[3] == max_skillCD[3] - GameServer.getDelay() * 14) {
//						dealAOE_enemy(getPlayer().getTeam(),"both", getPlayer().getPos().getX()-getPlayer().width-DISTANCE+S4_X_RANGE/2, getPlayer().getPos().getY()-getPlayer().height/2+S4_Y_RANGE, S4_X_RANGE, S4_Y_RANGE, -S4_dmg);
				AOE.dealAOE_enemy(getPlayer().getTeam(), CollisionDetection.getBounds("both", getPlayer().getPos().getX()-getPlayer().getSize().getX()-DISTANCE, getPlayer().getPos().getY(), S4_X_RANGE, S4_Y_RANGE), -S4_dmg); //$NON-NLS-1$
//						dealAOE_enemy(getPlayer().getTeam(),"both", getPlayer().getPos().getX()+getPlayer().width+DISTANCE-S4_X_RANGE/2, getPlayer().getPos().getY()-getPlayer().height/2+S4_Y_RANGE, S4_X_RANGE, S4_Y_RANGE, -S4_dmg);
				AOE.dealAOE_enemy(getPlayer().getTeam(), CollisionDetection.getBounds("both", getPlayer().getPos().getX()+getPlayer().getSize().getX()+DISTANCE, getPlayer().getPos().getY(), S4_X_RANGE, S4_Y_RANGE), -S4_dmg); //$NON-NLS-1$
			}
			getPlayer().getSkillCD()[3] -= GameServer.getDelay();
		} else {
			endSkill4(cnct);
		}
		
	}
	
	@Override
	public void endSkill4(Connection cnct) {
		getPlayer().getSkillCD()[3] = max_skillCD[3];
		getPlayer().setSkill4(false);
		getPlayer().disableSkilling();
	}
}
