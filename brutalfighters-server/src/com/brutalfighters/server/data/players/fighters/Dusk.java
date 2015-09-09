package com.brutalfighters.server.data.players.fighters;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.projectiles.Projectiles;
import com.brutalfighters.server.util.MathUtil;
import com.brutalfighters.server.util.Vec2;
import com.esotericsoftware.kryonet.Connection;

public class Dusk extends Fighter {
	
	public Dusk(Base base, String m_id) {
		super(base, m_id, "dusk", 900, 1000, new Vec2(90,100), 10, //$NON-NLS-1$
				21, 44, 500, new Vec2(110,10), 68, 9,
				new int[] {300,300,300,500}, new int[] {740,820,580,500});
	}
	
	@Override
	public void updateSkill1(Connection cnct) {
		defaultUpdate();
		applyFlip();
	}
	
	@Override
	public void skill1(Connection cnct) {
		
		updateSkill1(cnct);
		
		if(getPlayer().getSkillCD()[0] > 0) {
				if(getPlayer().getSkillCD()[0] == max_skillCD[0] - GameServer.getDelay() * 12) {
					float xstart = getPlayer().getPos().getX() + convertSpeed(getPlayer().getSize().getX()*2);
					Projectiles.addProjectile(cnct, getPlayer().getTeam(), "Dusk_BATZ", xstart, getPlayer().getPos().getY(), getPlayer().getFlip(), "init"); //$NON-NLS-1$ //$NON-NLS-2$
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
			if(getPlayer().getSkillCD()[1] == max_skillCD[1] - GameServer.getDelay() * 13) {
				float xstart = getPlayer().getPos().getX() + convertSpeed(getPlayer().getSize().getX());
				Projectiles.addProjectile(cnct, getPlayer().getTeam(), "Dusk_BATS", xstart, getPlayer().getPos().getY(), getPlayer().getFlip(), "init"); //$NON-NLS-1$ //$NON-NLS-2$
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
	
	@Override
	public void skill3(Connection cnct) {
		updateSkill3(cnct);
		
		if(getPlayer().getSkillCD()[2] > 0) {
			if(getPlayer().getSkillCD()[2] == max_skillCD[2] - GameServer.getDelay() * 10) {
				float xstart = getPlayer().getPos().getX() + convertSpeed(getPlayer().getSize().getX()*2);
				Projectiles.addProjectile(cnct, getPlayer().getTeam(), "Dusk_LASER", xstart, getPlayer().getPos().getY()+8, getPlayer().getFlip(), "init"); //$NON-NLS-1$ //$NON-NLS-2$
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
			float xstart = getPlayer().getPos().getX() + convertSpeed(MathUtil.nextInt(20,80));
			Projectiles.addProjectile(cnct, getPlayer().getTeam(), "Dusk_PurpleBat", xstart, getPlayer().getPos().getY()+MathUtil.nextInt(-40, +30), getPlayer().getFlip(), "init"); //$NON-NLS-1$ //$NON-NLS-2$
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
