package com.brutalfighters.server.data.players.fighters;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.buffs.BuffData;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.projectiles.Projectiles;
import com.brutalfighters.server.util.AOE;
import com.brutalfighters.server.util.CollisionDetection;
import com.brutalfighters.server.util.MathUtil;
import com.brutalfighters.server.util.Vec2;
import com.esotericsoftware.kryonet.Connection;

public class Blaze extends Fighter {
	
	public Blaze(Base base, String m_id) {
		super(base, m_id, "blaze", 1000, 1000, new Vec2(90,100), 10, //$NON-NLS-1$
				18, 44, 500, new Vec2(110,10), 75, 9,
				new int[] {400,200,950,500}, new int[] {980,580,820,820});
	}
	
	// SKILL 1
	int s1_HEIGHT = 40, s1_WIDTH = 500, dmg = 40; // DEALS AROUND 4 times
	
	@Override
	public void skill1(Connection cnct) {
		updateSkill1(cnct);
		
		if(getPlayer().getSkillCD()[0] > 0) {
			if(getPlayer().getSkillCD()[0] <= max_skillCD[0] - GameServer.getDelay() * 12 && getPlayer().getSkillCD()[0] >= max_skillCD[0] - GameServer.getDelay() * 15) {
				AOE.dealAOE_enemy(getPlayer().getTeam(), CollisionDetection.getBounds(getPlayer().getFlip(), getPlayer().getPos().getX() + convertSpeed(20), getPlayer().getPos().getY(), s1_WIDTH, s1_HEIGHT), -dmg, new BuffData[] {(Buff.getBuff("BIT_SLOW"))}); //$NON-NLS-1$
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
	
	@Override
	public void skill2(Connection cnct) {
		updateSkill2(cnct);
		
		if(getPlayer().getSkillCD()[1] > 0) {
			if(getPlayer().getSkillCD()[1] == max_skillCD[1] - GameServer.getDelay() * 7) {
				float xstart = getPlayer().getPos().getX() + convertSpeed(50);
				Projectiles.addProjectile(cnct, getPlayer().getTeam(), "Blaze_BloodBall", xstart, getPlayer().getPos().getY()+5, getPlayer().getFlip(), "init"); //$NON-NLS-1$ //$NON-NLS-2$
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
	private final int self_hp = 550;
	
	@Override
	public void skill3(Connection cnct) {
		updateSkill3(cnct);
		
		if(getPlayer().getSkillCD()[2] > 0) {
			if(getPlayer().getSkillCD()[2] == max_skillCD[2] - GameServer.getDelay() * 12) {
				Projectiles.addProjectile(cnct, getPlayer().getTeam(), "Blaze_PHEONIX", getPlayer().getPos().getX(), getPlayer().getPos().getY()+getPlayer().getSize().getY()*2, getPlayer().getFlip(), "init"); //$NON-NLS-1$ //$NON-NLS-2$
				applyHP(self_hp);
				AOE.dealAOE_enemy(getPlayer().getTeam(), CollisionDetection.getBounds("both", getPlayer().getPos().getX(), getPlayer().getPos().getY(), 400, 400), 0, new BuffData[] {(Buff.getBuff("SLOW_HEALING"))}); //$NON-NLS-1$ //$NON-NLS-2$
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
	}
	
	
	@Override
	public void skill4(Connection cnct) {
		updateSkill4(cnct);
		
		if(getPlayer().getSkillCD()[3] > 0) {
			if(getPlayer().getSkillCD()[3] / GameServer.getDelay() % 2 == 0) {
				float xstart = getPlayer().getPos().getX() + convertSpeed(MathUtil.nextInt(20,80));
				Projectiles.addProjectile(cnct, getPlayer().getTeam(), "Blaze_SkullFire", xstart, getPlayer().getPos().getY()+MathUtil.nextInt(-50, 10), getPlayer().getFlip(), "init"); //$NON-NLS-1$ //$NON-NLS-2$
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
