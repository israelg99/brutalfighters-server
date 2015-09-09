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
import com.brutalfighters.server.util.Vec2;
import com.esotericsoftware.kryonet.Connection;

abstract public class Fighter {
	
	// Finals
	protected static final int SKILLS = 4;
	protected static final float AIR_FORCE = 1.4f;
	protected static final int GRAVITY_FORCE = 27;
	protected static final int FALLING_MOMENTUM = 9;
	
	
	// Mana
	protected int manaRegen;
	
	// Skills
	protected final int[] max_skillCD;
	protected int[] skillMana;
	
	// Match ID
	protected String m_id;
	
	// Connection
	protected boolean isConnected;
	
	// AA
	protected int AA_dmg;
	protected Vec2 AA_range;
	protected Vec2 AA_CD;
	
	// Movement and Jumping speed
	protected Vec2 walking_speed;
	protected Vec2 running_speed;
	protected Vec2 jump_height;
	
	// Max Size
	protected Vec2 max_size;
	
	// The PlayerData
	protected PlayerData player;
	
	public Fighter(Base base, String m_id, String name, int maxhp, int maxmana, Vec2 size, int walking_speed,
				int running_speed, int jump_height, int AA_CD,
				Vec2 AA_range, int AA_DMG, int manaRegen,
				int[] skillMana, int[] max_skillCD) {
		
		// Skill Temp CD
		this.max_skillCD = max_skillCD.clone();
		
		// Constructing a new fighter/player data
		setPlayer(new PlayerData(base, name, maxhp, maxmana, size));

		// Mana
		setManaRegen(manaRegen);
		
		// Max Size
		max_size = new Vec2(size);
		
		// Movement Speed
		setWalkingSpeed(new Vec2(walking_speed));
		setRunningSpeed(new Vec2(running_speed));
		setJumpHeight(new Vec2(jump_height));

		// Match ID
		setMatchID(m_id);
		
		// Connection
		setConnected(true);
		
		// Auto Attack
		setAA_CD(new Vec2(AA_CD, AA_CD));
		setAA_Range(AA_range);
		setAA_Dmg(AA_DMG);
		
		// Skill Mana
		setSkillMana(skillMana.clone());

	}
	
	public static int getSkills() {
		return SKILLS;
	}
	public static float getAirForce() {
		return AIR_FORCE;
	}
	public static int getGravityForce() {
		return GRAVITY_FORCE;
	}
	public static int getFallingMomentum() {
		return FALLING_MOMENTUM;
	}
	
	public int getManaRegen() {
		return manaRegen;
	}
	public void setManaRegen(int manaRegen) {
		this.manaRegen = manaRegen;
	}

	public int[] getSkillMana() {
		return skillMana;
	}
	public void setSkillMana(int[] skillMana) {
		this.skillMana = skillMana;
	}

	public String getMatchID() {
		return m_id;
	}
	public void setMatchID(String m_id) {
		this.m_id = m_id;
	}

	public boolean isConnected() {
		return isConnected;
	}
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public int getAA_Dmg() {
		return AA_dmg;
	}
	public void setAA_Dmg(int aA_dmg) {
		AA_dmg = aA_dmg;
	}

	public Vec2 getAA_Range() {
		return AA_range;
	}
	public void setAA_Range(Vec2 aA_range) {
		AA_range = aA_range;
	}

	public Vec2 getAA_CD() {
		return AA_CD;
	}
	public void setAA_CD(Vec2 aA_CD) {
		AA_CD = aA_CD;
	}
	public void resetAA_CD() {
		getAA_CD().setX(getAA_CD().getY());
	}

	public Vec2 getWalkingSpeed() {
		return walking_speed;
	}
	public void setWalkingSpeed(Vec2 walking_speed) {
		this.walking_speed = walking_speed;
	}

	public Vec2 getRunningSpeed() {
		return running_speed;
	}
	public void setRunningSpeed(Vec2 running_speed) {
		this.running_speed = running_speed;
	}

	public Vec2 getJumpHeight() {
		return jump_height;
	}
	public void setJumpHeight(Vec2 jump_height) {
		this.jump_height = jump_height;
	}

	public int[] getMaxSkillCD() {
		return max_skillCD;
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
			
			if(getPlayer().isSkilling()) {
				applySkill(cnct);
			} else if(getPlayer().hasControl()) {
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
		getPlayer().enableExtrapolating();
	}
	protected final void disableExtrapolation() {
		getPlayer().disableExtrapolating();
	}

	protected final void updateBuffs() {
		List<BuffData> buffs = new ArrayList<BuffData>(Arrays.asList(getPlayer().getBuffs()));
		Iterator<BuffData> iterator = buffs.iterator();
		while(iterator.hasNext()) {
		    BuffData buff = iterator.next();
		    Buff.valueOf(buff.name).update(this, buff, iterator);
		}
		getPlayer().setBuffs(buffs.toArray(new BuffData[buffs.size()]));
	}

	protected final void applyTeleport(GameMap map) {
		if(getPlayer().isTeleporting()) {
			disableExtrapolation();
			applyTeleporting(map);
		}
	}
	public final boolean applyTeleporting(GameMap map) {
		
		// Resetting the teleporting state, so the player won't have this on true all the time.
		// Note that we do not use the release packet for teleporting, because we can easily just reset it here.
		getPlayer().disableTeleporting();
		
		// Get the tileset, not the tile itself just tileset to get properties.
		Tileset teleport = map.getTileset(0, getPlayer().getPos().getX(), getPlayer().getPos().getY()-getPlayer().getSize().getY()/3);
		
		if(teleport.hasProperty(Tileset.TELEPORT())) { // Are we standing on a teleport?
			// Decoding the coordinates, parsing them, and multiplying them to real game pixel coordinates.
			String[] target = ((String) teleport.getProperty(Tileset.TELEPORT())).split(","); //$NON-NLS-1$
			
			// Setting the coordinates to the player.
			getPlayer().getPos().setX(map.toPixelX(Integer.parseInt(target[0])));
			
			// The coordinates in Tiled and the map file are flipped,
			// the (0,0) block tile, is in the top left,
			// but we are going to flip the Y so it will be in the bottom left.
			// We add half of the tile height, so the player won't be stuck in the middle of the block tile.
			getPlayer().getPos().setY((map.getHeightPixels() - map.toPixelY(Integer.parseInt(target[1]))) + map.getTileHeight()/2);
			
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
		Flag teamFlag = match.getFlag(getPlayer().getTeam());
		Flag enemyFlag = match.getEnemyFlag(getPlayer().getTeam());
		
		if(!teamFlag.getFlag().isTaken() && !teamFlag.inBase(mapName, getPlayer().getTeam()) && collidesFlag(teamFlag)) {
			teamFlag = Flag.getFlag(mapName, getPlayer().getTeam());
		}
		
		if(collidesFlag(enemyFlag) && !enemyFlag.getFlag().isTaken() && !getPlayer().isHoldingFlag()) {
			enemyFlag.getFlag().gotStolen();
			getPlayer().stoleFlag();
		}
		
		if(getPlayer().isHoldingFlag() && enemyFlag.getFlag().isTaken() && collidesFlagBase(mapName, getPlayer().getTeam()) && teamFlag.inBase(mapName, getPlayer().getTeam())) {
			GameMatchManager.getCurrentMatch().addFlag(getPlayer().getTeam());
			enemyFlag.getFlag().gotDropped();
			getPlayer().droppedFlag();
			enemyFlag = Flag.getFlag(mapName, getPlayer().getTeam());
		}
	}
	public final boolean collidesFlag(Flag flag) {
		return intersects(flag.getBounds());
	}
	public final boolean collidesFlagBase(String mapName, int team) {
		return collidesFlag(MapManager.getMap(mapName).getFlag(team));
	}

	public final void applyFlip() {
		if(!getPlayer().isRight() == getPlayer().isLeft()) {
			if(getPlayer().isRight()) {
				getPlayer().flipRight();
			} else {
				getPlayer().flipLeft();
			}
		}
	}
	public final void applyGravity() {
		if(getPlayer().onGround()) {
			gravityVelocityReset();
		} else {
			applyGravitation();
		}
	}
	public final void gravityVelocityReset() {
		if(getPlayer().getVel().getY() < 0) {
			getPlayer().getVel().resetY();
		}
	}
	public final void applyWalking(int speed) {
		getPlayer().getVel().resetX(); // We must have it here, no worries, we should not apply walking when the X velocity is modified anyway.
		if((getPlayer().isLeft() != getPlayer().isRight())) {
			if(getPlayer().isRight()) {
				getPlayer().getVel().setX(speed);
				getPlayer().flipRight();
			} else if(getPlayer().isLeft()) {
				getPlayer().getVel().setX(-speed);
				getPlayer().flipLeft();
			}
		}
	}
	
	
	protected final void applyBodyGravity(GameMap map) {
		if(getPlayer().onGround() || collidesBot(map)) {
			getPlayer().getVel().resetY();
			alignGround(map.getTileHeight());
		} else {
			applyGravitation();
			getPlayer().getPos().addY(getPlayer().getVel().getY());
		}
		
	}

	protected final void applySkill(Connection cnct) {
		if(getPlayer().isSkill1()) {
			skill1(cnct);
		} else if(getPlayer().isSkill2()) {
			skill2(cnct);
		} else if(getPlayer().isSkill3()) {
			skill3(cnct);
		} else if(getPlayer().isSkill4()) {
			skill4(cnct);
		}	
	}

	protected final void applyAlive() {
		if(getPlayer().isDCD()) {
			getPlayer().subDCD();
		} else {
			getPlayer().reset((GameMatchManager.getCurrentMap().getBase(getPlayer().getTeam())));
		}
		
	}

	protected final boolean applyDeath() {
		if(!getPlayer().isDead()) {
			if(getPlayer().hasNoHP()) {
				getPlayer().died();
				if(getPlayer().isHoldingFlag()) {
					getPlayer().droppedFlag();
					GameMatchManager.getCurrentMatch().getEnemyFlag(getPlayer().getTeam()).getFlag().gotDropped();
				}
				GameMatchManager.getCurrentMatch().addKill(GameMatch.getEnemyTeamID(getPlayer().getTeam()));
				getPlayer().setDCD(GameMatchManager.getCurrentMatch().getRespawnTime());
				return true;
			}
			return false;
		}
		return true;
		
	}

	public final void applyVelocity() {
		
		// Getting the champion and the speed
		float speed = getSpeed();
		
		if(getPlayer().onGround()) {
			
			// Velocity Reset
			gravityVelocityReset();
			
			/* JUMP */
			if(getPlayer().isJump() && hasFullControl()) {
				if(!getPlayer().isCollidingTop()) {
					getPlayer().getVel().setY(getJumpHeight().getX());
				} else {
					getPlayer().getVel().resetY();
				}
			}
			
			if(getPlayer().isCollidingTop() && getPlayer().isCollidingBot()) {
				getPlayer().getVel().resetY();
				getPlayer().setJump(false);
			}
			/* END JUMP */
			
		} else {
			
			// Jump Cut
			if(hasFullControl() && !getPlayer().isJump() && getPlayer().getVel().getY() > getJumpHeight().getX()/2) {
				getPlayer().getVel().setY(getJumpHeight().getX()/2);
			}
			
			// Air Momentum
			speed *= getAirForce();
			
			// Gravity
			applyGravitation();
		}
		
		// Walking (Velocity X reset inside ofc)
		applyWalking((int)speed);
	}
	
	public final boolean hasFullControl() {
		return getPlayer().hasControl() && !getPlayer().isSkilling();
	}

	protected final void applyCollision(GameMap map) {
		if(collidesTop(map)) {
			getPlayer().isCollidingTop(true);
		} else {
			getPlayer().isCollidingTop(false);
		}
		if(collidesBot(map)) {
			getPlayer().isCollidingBot(true);
			if(getPlayer().getVel().getY() <= 0) {
				getPlayer().isOnGround(true);
				alignGround(map.getTileHeight());
			}
		} else {
			getPlayer().isCollidingBot(false);
			getPlayer().isOnGround(true);
		}
		
		if(collidesLeft(map)) {
			getPlayer().isCollidingLeft(true);
		} else {
			getPlayer().isCollidingLeft(false);
		}
		
		if(collidesRight(map)) {
			getPlayer().isCollidingRight(true);
		} else {
			getPlayer().isCollidingRight(false);
		}
	}
	// Boundary Methods - MUST NOT BE CHANGED!!!!!
	public final float getLeft() {
		return -getPlayer().getSize().getX()/2;
	}
	public final float getRight() {
		return getPlayer().getSize().getX()/2;
	}
	public final float getTop() {
		return getPlayer().getSize().getY()/2;
	}
	public final float getBot() {
		return -getPlayer().getSize().getY()/2;
	}
	
	public final Rectangle getVelocityBounds(boolean velx, boolean vely) {
		Rectangle bounds = getBounds();
		bounds.x += velx ? getPlayer().getVel().getX() : 0;
		bounds.y += vely ? getPlayer().getVel().getY() : 0;
		return bounds;
	}
	
	/* We can use Enum of sides(bot,left,right,top) and pass it as a parameter, thus combine those 4 functions into one. */
	public final boolean collidesBot(GameMap map) {
		// BOT!
		return map.intersectsSurroundXBoth("top", getPlayer().getPos().getX(), getPlayer().getPos().getY()+getBot()+getPlayer().getVel().getY(), getVelocityBounds(false, true)) || getPlayer().getPos().getY() + getPlayer().getVel().getY() + getBot() < map.getBotBoundary(); //$NON-NLS-1$
	}
	public final boolean collidesLeft(GameMap map) {
		// LEFT!
		return map.intersectsSurroundY(getPlayer().getPos().getX()+getLeft()+getPlayer().getVel().getX(), getPlayer().getPos().getY(), getVelocityBounds(true, false)) ||getPlayer().getPos().getX() + getPlayer().getVel().getX() + getLeft() < map.getLeftBoundary();
	}
	public final boolean collidesRight(GameMap map) {
		// RIGHT!
		return map.intersectsSurroundY(getPlayer().getPos().getX()+getRight()+getPlayer().getVel().getX(), getPlayer().getPos().getY(), getVelocityBounds(true, false)) || getPlayer().getPos().getX() + getPlayer().getVel().getX() + getRight() > map.getRightBoundary();
	}
	public final boolean collidesTop(GameMap map) {
		// TOP!
		return map.intersectsSurroundX(getPlayer().getPos().getX(), getPlayer().getPos().getY()+getTop()+getPlayer().getVel().getY(), getVelocityBounds(false, true)) || getPlayer().getPos().getY() + getPlayer().getVel().getY() + getTop() > map.getTopBoundary();
	}
	
	public final Tile getCellOn(GameMap map) {
		return map.getTile(0, getPlayer().getPos().getX(), getPlayer().getPos().getY() + getBot());
	}
	
	public final boolean isFacingCollision() {
		return (getPlayer().getFlip().equals(GameMatch.RIGHT) && getPlayer().isCollidingRight()) || (getPlayer().getFlip().equals(GameMatch.LEFT) && getPlayer().isCollidingLeft()); 
	}
	
	public final Rectangle getBounds() {
		Rectangle bounds = CollisionDetection.getBounds("both", getPlayer().getPos().getX(), getPlayer().getPos().getY(), getPlayer().getSize().getX(), getPlayer().getSize().getY()); //$NON-NLS-1$
		return bounds;
	}
	public final boolean intersects(Rectangle rect) {
		return getBounds().intersects(rect);
	}
	public final boolean intersects(Fighter player) {
		return getBounds().intersects(player.getBounds());
	}
	
	protected final void applyAA(Connection cnct) {
		if(getPlayer().isAAttack() && isBum()) {
			if(getAA_CD().getX() <= 0) {
				
				AAttack(cnct);
				
				resetAA_CD();
			} else {
				getAA_CD().subX(GameServer.getDelay());
			}
		} else {
			resetAA_CD();
		}
		
	}
	
	protected final void applyPosition() {
		
		// Apply velocity
		
		// Check collision Y
		if(movingY() && ((getPlayer().getVel().getY() > 0 && !getPlayer().isCollidingTop()) 
				|| (getPlayer().getVel().getY() < 0 && !getPlayer().isCollidingBot()))) {
			getPlayer().getPos().addY(getPlayer().getVel().getY());
		}
		
		// Check collision X
		if(movingX()) {
			if((getPlayer().getVel().getX() > 0 && !getPlayer().isCollidingRight())
					|| getPlayer().getVel().getX() < 0 && !getPlayer().isCollidingLeft()) {
				getPlayer().getPos().addX(getPlayer().getVel().getX());
			}
		}
		
	}
	
	protected final void applyRegen() {
		healMana(getManaRegen());
	}
	
	public final void applyGravitation() {
		getPlayer().getVel().setY(getPlayer().getVel().getY() - getFallingMomentum() < -getGravityForce() ? -getGravityForce() : getPlayer().getVel().getY() - getFallingMomentum());	
	}
	public final void alignGround(int ground) {
		getPlayer().getPos().setY((int)(getPlayer().getPos().getY() / ground) * ground + getPlayer().getSize().getY()/2 - 1);
	}


	// State Method
	public final boolean movingX() {
		return getPlayer().getVel().getX() != 0;
	}
	public final boolean movingY() {
		return getPlayer().getVel().getY() != 0;
	}
	
	public final boolean isBum() {
		return getPlayer().hasControl() && !getPlayer().isSkilling() && getPlayer().onGround() && !movingX() && !movingY();
	}
	
	// Buffs
	public final void applyBuffs(BuffData[] buffs) {
		if(getPlayer().isVulnerable()) {
			for(int i = 0; i < buffs.length; i++) {
				// Add Buff
				Buffs.addBuff(getPlayer(), Buff.getBuff(buffs[i]));
				
				// We need to get a new buff(), because buffs are passed by ref by value,
				// that's how are objects are treated in Java and we don't want
				// players to refer to the same buff object.
				
				// Start/Initiate the Buff
				Buff.valueOf(getPlayer().getBuffs()[getPlayer().getBuffs().length-1].name).start(this, getPlayer().getBuffs().length);
			}
		}
	}
	
	// HP
	public final void applyHP(float hp) {
		if(hp > 0) { 
			healHP(hp);
			return;
		} else if(hp < 0) {
			dealHP(hp);
			return;
		}
	}
	public final void applyRandomHP(float hp) {
		applyHP(hp - MathUtil.nextInt(0, Math.abs((int)(hp*0.1))) + MathUtil.nextInt(0, Math.abs((int)(hp*0.1))));
	}
	protected final void dealHP(float dmg) {
		if(!getPlayer().isDead() && getPlayer().hasHP() && getPlayer().isVulnerable()) {
			getPlayer().getHP().setX(getPlayer().getHP().getX() + dmg < 0 ? 0 : getPlayer().getHP().getX() + dmg); // the dmg itself is negative.
		}
	}
	protected final void healHP(float heal) {
		if(!getPlayer().isDead()) {
			getPlayer().getHP().setX(getPlayer().getHP().getX() + heal > getPlayer().getHP().getY() ?
					getPlayer().getHP().getY() : getPlayer().getHP().getX() + heal);
		}
	}
	
	// MANA
	public final void applyMana(float mana) {
		if(mana > 0) { 
			healMana(mana);
			return;
		} else if(mana < 0) {
			dealMana(mana);
			return;
		}
	}
	public final void applyRandomMana(float mana) {
		applyMana(mana - MathUtil.nextFloat(0, (int)(mana*0.1) + MathUtil.nextFloat(0, (mana))));
	}
	protected final void dealMana(float dmg) {
		if(!getPlayer().isDead() && getPlayer().hasMana() && getPlayer().isVulnerable()) {
			getPlayer().getMana().setX((getPlayer().getMana().getX() + dmg < 0 ? 0 : getPlayer().getMana().getX())); // the dmg itself is negative.
		}
	}
	protected final void healMana(float heal) {
		if(!getPlayer().isDead()) {
			float newMana = getPlayer().getMana().getX() + heal;
			getPlayer().getMana().setX(newMana > getPlayer().getMana().getY() ?
					getPlayer().getMana().getY() : newMana);
		}
	}
	
	// SPEED CONVERTION
	public final float convertSpeed(float speed) {
		return getPlayer().getFlip().equals(GameMatch.RIGHT) ? speed : -speed; 
	}
	
	protected void defaultUpdate() {
		getPlayer().getVel().resetX();
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
		return getPlayer().isRunning() ? getRunningSpeed().getX() : getWalkingSpeed().getX();
	}
	
	public final void resetSpeeds() {
		resetWalkingSpeed();
		resetWalkingSpeed();
		resetJumpHeight();
	}
	
	public final void resetWalkingSpeed() {
		getWalkingSpeed().setX(getWalkingSpeed().getY());
	}
	public final void resetRunningSpeed() {
		getRunningSpeed().setX(getRunningSpeed().getY());
	}
	public final void resetJumpHeight() {
		getJumpHeight().setX(getJumpHeight().getY());
	}
	
	public final void AAttack(Connection cnct) {
		AOE.dealAOE_enemy(getPlayer().getTeam(), CollisionDetection.getBounds(getPlayer().getFlip(), getPlayer().getPos().getX()-convertSpeed(getPlayer().getSize().getX()/2), getPlayer().getPos().getY(), getAA_Range().getX(), getAA_Range().getY()), -getAA_Dmg());
	}
	
	public final boolean applySkillMana(int index) {
		if(getPlayer().getMana().getX() >= skillMana[index]) {
			applyMana(-skillMana[index]);
			return true;
		}
		return false;
	}
}

