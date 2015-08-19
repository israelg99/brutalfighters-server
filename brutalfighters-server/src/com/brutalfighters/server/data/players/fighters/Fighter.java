package com.brutalfighters.server.data.players.fighters;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.buffs.BuffData;
import com.brutalfighters.server.data.buffs.Buffs;
import com.brutalfighters.server.data.flags.Flag;
import com.brutalfighters.server.data.flags.FlagHandler;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.maps.GameMap;
import com.brutalfighters.server.data.maps.MapManager;
import com.brutalfighters.server.data.players.PlayerData;
import com.brutalfighters.server.matches.GameMatch;
import com.brutalfighters.server.matches.GameMatchManager;
import com.brutalfighters.server.tiled.Tile;
import com.brutalfighters.server.tiled.Tileset;
import com.brutalfighters.server.util.AOE;
import com.brutalfighters.server.util.CollisionDetection;
import com.brutalfighters.server.util.MathUtil;
import com.esotericsoftware.kryonet.Connection;

abstract public class Fighter {
	
	protected static final int skills = 4;
	protected static final float airForce = 1.4f;
	protected static final int gravityForce = 27;
	protected static final int fallingMomentum = 9;
	
	protected String NAME;
	protected int MAXHP, MAXMANA, MANAREGEN;
	protected int WIDTH, HEIGHT;
	protected int WALKING_SPEED, RUNNING_SPEED, JUMP_HEIGHT;
	protected int AA_CD, AA_X_RANGE, AA_Y_RANGE, AA_DMG;
	
	protected int[] skillMana;
	
	protected final int[] skillTempCD;
	
	protected PlayerData player;
	
	public Fighter(Base base, String m_id, String name, int maxhp, int maxmana, int width, int height, int walking_speed,
				int running_speed, int jump_height, int AACD,
				int AA_X_RANGE, int AA_Y_RANGE, int AA_DMG, int manaRegen,
				int[] skillMana, int[] skillTempCD) {
		
		this.NAME = name;
		
		this.MAXHP = maxhp;
		this.MAXMANA = maxmana;
		this.MANAREGEN = manaRegen;
		
		this.WIDTH = width;
		this.HEIGHT = height;
		
		this.WALKING_SPEED = walking_speed;
		this.RUNNING_SPEED = running_speed;
		this.JUMP_HEIGHT = jump_height;
		
		this.AA_CD = AACD;
		this.AA_X_RANGE = AA_X_RANGE;
		this.AA_Y_RANGE = AA_Y_RANGE;
		this.AA_DMG = AA_DMG;
		
		this.skillMana = skillMana;
		this.skillTempCD = skillTempCD;
		
		setNewPlayer(base, m_id);
		
	}
	
	public final PlayerData getPlayer() {
		return player;
	}
	protected final void setPlayer(PlayerData pdata) {
		this.player = pdata;
	}
	
	public final void update(Connection cnct, GameMap map) {
		
		resetExtrapolation();
		
		if(!applyDeath()) {
			
			if(getPlayer().isSkilling) {
				applySkill(cnct);
			} else if(getPlayer().hasControl) {
				applyVelocity();
				applyAA(cnct);
				applyTeleport(map);
			}
					
			applyCollision(map);
			applyFlag();
			applyRegen();
			updateBuffs();
			applyPosition();

		} else {
			applyBodyGravity(map);
			applyAlive();
		}
	}
	
	protected final void resetExtrapolation() {
		getPlayer().isExtrapolating = true;
	}
	protected final void disableExtrapolation() {
		getPlayer().isExtrapolating = false;
	}

	protected final void updateBuffs() {
		List<BuffData> buffs = new ArrayList<BuffData>(Arrays.asList(getPlayer().buffs));
		Iterator<BuffData> iterator = buffs.iterator();
		while(iterator.hasNext()) {
		    BuffData buff = iterator.next();
		    Buff.valueOf(buff.name).update(this, buff, iterator);
		}
		getPlayer().buffs = buffs.toArray(new BuffData[buffs.size()]);
	}

	protected final void applyTeleport(GameMap map) {
		if(getPlayer().isTeleporting) {
			disableExtrapolation();
			applyTeleporting(map);
		}
	}
	public final boolean applyTeleporting(GameMap map) {
		
		// Resetting the teleporting state, so the player won't have this on true all the time.
		// Note that we do not use the release packet for teleporting, because we can easily just reset it here.
		getPlayer().isTeleporting = false;
		
		// Get the tileset, not the tile itself just tileset to get properties.
		Tileset teleport = map.getTileset(0, getPlayer().posx, getPlayer().posy-getPlayer().height/3);
		
		if(teleport.hasProperty(Tileset.TELEPORT())) { // Are we standing on a teleport?
			// Decoding the coordinates, parsing them, and multiplying them to real game pixel coordinates.
			String[] target = ((String) teleport.getProperty(Tileset.TELEPORT())).split(","); //$NON-NLS-1$
			
			// Setting the coordinates to the player.
			getPlayer().posx = map.toPixelX(Integer.parseInt(target[0]));
			
			// The coordinates in Tiled and the map file are flipped,
			// the (0,0) block tile, is in the top left,
			// but we are going to flip the Y so it will be in the bottom left.
			// We add half of the tile height, so the player won't be stuck in the middle of the block tile.
			getPlayer().posy = (map.getHeightPixels() - map.toPixelY(Integer.parseInt(target[1]))) + map.getTileHeight()/2;
			
			// Returns true, as the player teleported successfully.
			return true;
		}
		// Not the teleport, displays an error and returns false.
		//System.err.println("Not a teleport!");
					
		// Returns false as the player didn't teleport successfully.
		return false;
	}

	protected final void applyFlag() {
		GameMatch match = GameMatchManager.getCurrentMatch();
		String mapName = match.getMapName();
		Flag teamFlag = match.getFlag(getPlayer().team);
		Flag enemyFlag = match.getEnemyFlag(getPlayer().team);
		
		if(!teamFlag.isTaken && !FlagHandler.inBase(teamFlag, mapName, getPlayer().team) && collidesFlag(teamFlag)) {
			FlagHandler.toBase(teamFlag, mapName, getPlayer().team);
		}
		
		if(collidesFlag(enemyFlag) && !enemyFlag.isTaken && !getPlayer().isFlagged) {
			enemyFlag.isTaken = true;
			getPlayer().isFlagged = true;
		}
		
		if(getPlayer().isFlagged && enemyFlag.isTaken && collidesFlagBase(mapName, getPlayer().team) && FlagHandler.inBase(teamFlag, mapName, getPlayer().team)) {
			GameMatchManager.getCurrentMatch().addFlag(getPlayer().team);
			enemyFlag.isTaken = false;
			getPlayer().isFlagged = false;
			FlagHandler.toBase(enemyFlag, mapName, GameMatch.getEnemyTeamID(getPlayer().team));
		}
	}
	public final boolean collidesFlag(Flag flag) {
		return intersects(FlagHandler.getBounds(flag));
	}
	public final boolean collidesFlagBase(String mapName, int team) {
		return collidesFlag(MapManager.getMap(mapName).getFlag(team));
	}

	public final void applyFlip() {
		if(!getPlayer().isRight == getPlayer().isLeft) {
			getPlayer().flip = getPlayer().isRight ? "right" : getPlayer().isLeft ? "left" : getPlayer().flip; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	public final void applyGravity() {
		if(getPlayer().onGround) {
			getPlayer().vely = getPlayer().vely < 0 ? 0 : getPlayer().vely;
		} else {
			applyGravitation();
		}
	}
	public final void applyWalking(int speed) {
		getPlayer().velx = 0; // We must have it here, no worries, we should not apply walking when the X velocity is modified anyway.
		if((getPlayer().isLeft != getPlayer().isRight)) {
			if(getPlayer().isRight) {
				getPlayer().velx = speed;
				getPlayer().flip = "right"; //$NON-NLS-1$
			} else if(getPlayer().isLeft) {
				getPlayer().velx = -speed;
				getPlayer().flip = "left"; //$NON-NLS-1$
			}
		}
	}
	
	
	protected final void applyBodyGravity(GameMap map) {
		if(getPlayer().onGround || collidesBot(map)) {
			getPlayer().vely = 0;
			alignGround(map.getTileHeight());
		} else {
			applyGravitation();
			getPlayer().posy += getPlayer().vely;
		}
		
	}

	protected final void applySkill(Connection cnct) {
		if(getPlayer().isSkill1) {
			skill1(cnct);
		} else if(getPlayer().isSkill2) {
			skill2(cnct);
		} else if(getPlayer().isSkill3) {
			skill3(cnct);
		} else if(getPlayer().isSkill4) {
			skill4(cnct);
		}	
	}

	protected final void applyAlive() {
		if(getPlayer().DCD > 0) {
			getPlayer().DCD -= GameServer.getDelay();
		} else {
			resetPlayer(GameMatchManager.getCurrentMap().getBase(getPlayer().team));
		}
		
	}

	protected final boolean applyDeath() {
		if(!getPlayer().isDead) {
			if(getPlayer().hp <= 0) {
				getPlayer().isDead = true;
				if(getPlayer().isFlagged) {
					getPlayer().isFlagged = false;
					GameMatchManager.getCurrentMatch().getEnemyFlag(getPlayer().team).isTaken = false;
				}
				GameMatchManager.getCurrentMatch().addKill(GameMatch.getEnemyTeamID(getPlayer().team));
				getPlayer().DCD = GameMatchManager.getCurrentMatch().getRespawnTime();
				return true;
			}
			return false;
		}
		return true;
		
	}

	public final void applyVelocity() {
		
		// Getting the champion and the speed
		float speed = getPlayer().isRunning ? getPlayer().running_speed : getPlayer().walking_speed;
		
		if(getPlayer().onGround) {
			
			// Velocity Reset
			getPlayer().vely = getPlayer().vely < 0 ? 0 : getPlayer().vely;
			
			/* JUMP */
			if(getPlayer().isJump && hasFullControl()) {
				if(!getPlayer().collidesTop) {
					getPlayer().vely = JUMP_HEIGHT;
				} else {
					getPlayer().vely = 0;
				}
			}
			
			if(getPlayer().collidesTop && getPlayer().collidesBot) {
				getPlayer().vely = 0;
				getPlayer().isJump = false;
			}
			/* END JUMP */
			
		} else {
			
			// Jump Cut
			if(hasFullControl() && !getPlayer().isJump && getPlayer().vely > JUMP_HEIGHT/2) {
				getPlayer().vely = JUMP_HEIGHT/2;
			}
			
			// Air Momentum
			speed *= airForce;
			
			// Gravity
			applyGravitation();
		}
		
		// Walking (Velocity X reset inside ofc)
		applyWalking((int)speed);
	}
	
	public final boolean hasFullControl() {
		return getPlayer().hasControl && !getPlayer().isSkilling;
	}

	protected final void applyCollision(GameMap map) {
		if(collidesTop(map)) {
			getPlayer().collidesTop = true;
		} else {
			getPlayer().collidesTop = false;
		}
		if(collidesBot(map)) {
			getPlayer().collidesBot = true;
			if(getPlayer().vely <= 0) {
				getPlayer().onGround = true;
				alignGround(map.getTileHeight());
			}
		} else {
			getPlayer().collidesBot = false;
			getPlayer().onGround = false;
		}
		
		if(collidesLeft(map)) {
			getPlayer().collidesLeft = true;
		} else {
			getPlayer().collidesLeft = false;
		}
		
		if(collidesRight(map)) {
			getPlayer().collidesRight = true;
		} else {
			getPlayer().collidesRight = false;
		}
	}
	// Boundary Methods - MUST NOT BE CHANGED!!!!!
	public final float getLeft() {
		return -getPlayer().width/2;
	}
	public final float getRight() {
		return getPlayer().width/2;
	}
	public final float getTop() {
		return getPlayer().height/2;
	}
	public final float getBot() {
		return -getPlayer().height/2;
	}
	
	public final Rectangle getVelocityBounds(boolean velx, boolean vely) {
		Rectangle bounds = getBounds();
		bounds.x += velx ? getPlayer().velx : 0;
		bounds.y += vely ? getPlayer().vely : 0;
		return bounds;
	}
	
	/* We can use Enum of sides(bot,left,right,top) and pass it as a parameter, thus combine those 4 functions into one. */
	public final boolean collidesBot(GameMap map) {
		// BOT!
		return map.intersectsSurroundXBoth("top", getPlayer().posx, getPlayer().posy+getBot()+getPlayer().vely, getVelocityBounds(false, true)) || getPlayer().posy + getPlayer().vely + getBot() < map.getBotBoundary(); //$NON-NLS-1$
	}
	public final boolean collidesLeft(GameMap map) {
		// LEFT!
		return map.intersectsSurroundY(getPlayer().posx+getLeft()+getPlayer().velx, getPlayer().posy, getVelocityBounds(true, false)) ||getPlayer().posx + getPlayer().velx + getLeft() < map.getLeftBoundary();
	}
	public final boolean collidesRight(GameMap map) {
		// RIGHT!
		return map.intersectsSurroundY(getPlayer().posx+getRight()+getPlayer().velx, getPlayer().posy, getVelocityBounds(true, false)) || getPlayer().posx + getPlayer().velx + getRight() > map.getRightBoundary();
	}
	public final boolean collidesTop(GameMap map) {
		// TOP!
		return map.intersectsSurroundX(getPlayer().posx, getPlayer().posy+getTop()+getPlayer().vely, getVelocityBounds(false, true)) || getPlayer().posy + getPlayer().vely + getTop() > map.getTopBoundary();
	}
	
	public final Tile getCellOn(GameMap map) {
		return map.getTile(0, getPlayer().posx, getPlayer().posy + getBot());
	}
	
	public final boolean isFacingCollision() {
		return (getPlayer().flip.equals("right") && getPlayer().collidesRight) || (getPlayer().flip.equals("left") && getPlayer().collidesLeft); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public final Rectangle getBounds() {
		Rectangle bounds = CollisionDetection.getBounds("both", getPlayer().posx, getPlayer().posy, getPlayer().width, getPlayer().height); //$NON-NLS-1$
		return bounds;
	}
	public final boolean intersects(Rectangle rect) {
		return getBounds().intersects(rect);
	}
	public final boolean intersects(Fighter player) {
		return getBounds().intersects(player.getBounds());
	}
	
	protected final void applyAA(Connection cnct) {
		if(getPlayer().isAAttack && isBum()) {
			if(getPlayer().AACD <= 0) {
				
				AAttack(cnct);
				
				getPlayer().AACD = AA_CD;
			} else {
				getPlayer().AACD -= GameServer.getDelay();
			}
		} else {
			getPlayer().AACD = AA_CD;
		}
		
	}
	
	protected final void applyPosition() {
		
		// Apply velocity
		
		// Check collision Y
		if(movingY() && ((getPlayer().vely > 0 && !getPlayer().collidesTop) 
				|| (getPlayer().vely < 0 && !getPlayer().collidesBot))) {
			getPlayer().posy += getPlayer().vely;
		}
		
		// Check collision X
		if(movingX()) {
			if((getPlayer().velx > 0 && !getPlayer().collidesRight)
					|| getPlayer().velx < 0 && !getPlayer().collidesLeft) {
				getPlayer().posx += getPlayer().velx;
			}
		}
		
	}
	
	protected final void applyRegen() {
		healMana(MANAREGEN);
	}
	
	public final void applyGravitation() {
		getPlayer().vely = getPlayer().vely - fallingMomentum < -gravityForce ? -gravityForce : getPlayer().vely - fallingMomentum;	
	}
	public final void alignGround(int ground) {
		getPlayer().posy = (int)(getPlayer().posy / ground) * ground + getPlayer().height/2 - 1;
	}


	// State Method
	public final boolean movingX() {
		return getPlayer().velx != 0;
	}
	public final boolean movingY() {
		return getPlayer().vely != 0;
	}
	
	public final boolean isBum() {
		return getPlayer().hasControl && !getPlayer().isSkilling && getPlayer().onGround && !movingX() && !movingY();
	}
	
	// Buffs
	public final void applyBuffs(BuffData[] buffs) {
		if(getPlayer().isVulnerable) {
			for(int i = 0; i < buffs.length; i++) {
				// Add Buff
				Buffs.addBuff(getPlayer(), Buff.getBuff(buffs[i]));
				
				// We need to get a new buff(), because buffs are passed by ref by value,
				// that's how are objects are treated in Java and we don't want
				// players to refer to the same buff object.
				
				// Start/Initiate the Buff
				Buff.valueOf(getPlayer().buffs[getPlayer().buffs.length-1].name).start(this, getPlayer().buffs.length);
			}
		}
	}
	
	// HP
	public final void applyHP(int hp) {
		if(hp > 0) { 
			healHP(hp);
			return;
		} else if(hp < 0) {
			dealHP(hp);
			return;
		}
	}
	public final void applyRandomHP(int hp) {
		applyHP(hp - MathUtil.nextInt(0, Math.abs((int)(hp*0.1))) + MathUtil.nextInt(0, Math.abs((int)(hp*0.1))));
	}
	protected final void dealHP(int dmg) {
		if(!getPlayer().isDead && getPlayer().hp > 0 && getPlayer().isVulnerable) {
			getPlayer().hp = getPlayer().hp + dmg < 0 ? 0 : getPlayer().hp + dmg; // the dmg itself is negative.
		}
	}
	protected final void healHP(int heal) {
		if(!getPlayer().isDead) {
			getPlayer().hp = getPlayer().hp + heal > MAXHP ?
					MAXHP : getPlayer().hp + heal;
		}
	}
	public final void maxHP() {
		if(!getPlayer().isDead) {
			getPlayer().hp = MAXHP;
		}
	}
	public final void zeroHP() {
		getPlayer().hp =  0;
	}
	
	// MANA
	public final void applyMana(int mana) {
		if(mana > 0) { 
			healMana(mana);
			return;
		} else if(mana < 0) {
			dealMana(mana);
			return;
		}
	}
	public final void applyRandomMana(int mana) {
		applyMana(mana - MathUtil.nextInt(0, (int)(mana*0.1) + MathUtil.nextInt(0, (mana))));
	}
	protected final void dealMana(int dmg) {
		if(!getPlayer().isDead && getPlayer().mana > 0 && getPlayer().isVulnerable) {
			getPlayer().mana = getPlayer().mana + dmg < 0 ? 0 : getPlayer().mana + dmg; // the dmg itself is negative.
		}
	}
	protected final void healMana(int heal) {
		if(!getPlayer().isDead) {
			int newMana = getPlayer().mana + heal;
			getPlayer().mana = newMana > MAXMANA ?
					MAXMANA : newMana;
		}
	}
	public final void MAXMANA() {
		if(!getPlayer().isDead) {
			getPlayer().mana = MAXMANA;
		}
	}
	public final void zeroMana() {
		getPlayer().mana =  0;
	}
	
	// SPEED CONVERTION
	public final int convertSpeed(int speed) {
		return getPlayer().flip.equals("right") ? speed : -speed; //$NON-NLS-1$
	}
	
	public final PlayerData getNewPlayer(Base base, String m_id) {
		
		PlayerData p = new PlayerData();
		
		// Basic
		p.name = Character.toUpperCase(NAME.charAt(0)) + NAME.substring(1);
		
		p.posx = base.getX();
		p.posy = base.getY();
		
		p.width = WIDTH;
		p.height = HEIGHT;
		
		p.flip = base.getFlip();
		
		p.velx = 0;
		p.vely = 0;
		
		p.walking_speed = WALKING_SPEED;
		p.running_speed = RUNNING_SPEED;
		
		p.maxhp = MAXHP;
		p.hp = p.maxhp;
		
		p.maxmana = MAXMANA;
		p.mana = p.maxmana;
		
		// Match ID
		p.m_id = m_id;
		
		// Connection
		p.isConnected = true;
		
		// CD
		p.AACD = AA_CD;
		p.DCD = GameMatch.getDefaultRespawn();
		
		p.buffs = new BuffData[0];
		
		// Skill Mana
		p.skillMana = skillMana.clone();
		
		// Skill Temp CD
		p.skillCD = skillTempCD.clone();
		
		// States
		p.isRunning = false;
		p.onGround = false;
		p.isAAttack = false;
		p.isDead = false;
		p.isVulnerable = true;
		p.isFlagged = false;
		p.hasControl = true;
		
		// Skill States
		p.isSkill1 = false;
		p.isSkill2 = false;
		p.isSkill3 = false;
		p.isSkill4 = false;
		p.isSkilling = false;
		
		// Teleport
		p.isTeleporting = false;
		
		// Movement States
		p.isLeft = false;
		p.isRight = false;
		p.isJump = false;
		p.isRunning = false;
		p.isDead = false;
		
		// Collisions
		p.collidesLeft = false;
		p.collidesRight = false;
		p.collidesTop = false;
		p.collidesBot = false;
		
		return p;
	}
	
	public final void setNewPlayer(Base base, String m_id) {
		setPlayer(getNewPlayer(base, m_id));
	}
	public final void resetPlayer(Base base) {
		setPlayer(getNewPlayer(base, getPlayer().m_id));
	}

	
	protected void defaultUpdate() {
		getPlayer().velx = 0;
		applyGravity();
	}
	
	public void startSkill1(Connection cnct) {
		if(!applySkillMana(0)) {
			endSkill1(cnct);
		}
	}
	public void updateSkill1(Connection cnct) {
		defaultUpdate();
	}
	public void skill1(Connection cnct) {
		
	}
	public void endSkill1(Connection cnct) {
		
	}

	public void startSkill2(Connection cnct) {
		if(!applySkillMana(1)) {
			endSkill2(cnct);
		}
	}
	public void updateSkill2(Connection cnct) {
		defaultUpdate();
	}
	public void skill2(Connection cnct) {
		
	}
	public void endSkill2(Connection cnct) {
		
	}

	public void startSkill3(Connection cnct) {
		if(!applySkillMana(2)) {
			endSkill3(cnct);
		}
	}
	public void updateSkill3(Connection cnct) {
		defaultUpdate();
	}
	public void skill3(Connection cnct) {
		
	}
	public void endSkill3(Connection cnct) {
		
	}
	
	public void startSkill4(Connection cnct) {
		if(!applySkillMana(3)) {
			endSkill4(cnct);
		}
	}
	public void updateSkill4(Connection cnct) {
		defaultUpdate();
	}
	public void skill4(Connection cnct) {
		
	}
	public void endSkill4(Connection cnct) {
		
	}
	
	
	public final float getSpeed() {
		return getPlayer().isRunning ? RUNNING_SPEED : WALKING_SPEED;
	}
	
	public final void assignSpeed() {
		getPlayer().walking_speed = WALKING_SPEED;
		getPlayer().running_speed = RUNNING_SPEED;
	}
	
	public final void AAttack(Connection cnct) {
		AOE.dealAOE_enemy(getPlayer().team, CollisionDetection.getBounds(getPlayer().flip, getPlayer().posx-convertSpeed(getPlayer().width/2), getPlayer().posy, AA_X_RANGE, AA_Y_RANGE), -AA_DMG);
	}
	
	public final boolean applySkillMana(int index) {
		if(getPlayer().mana >= skillMana[index]) {
			applyMana(-skillMana[index]);
			return true;
		}
		return false;
	}
}
