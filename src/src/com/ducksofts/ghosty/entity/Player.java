package com.ducksofts.ghosty.entity;

import java.awt.event.KeyEvent;
import java.util.List;

import com.ducksofts.ducky.gamework.Entity;
import com.ducksofts.ducky.gamework.Level;
import com.ducksofts.ducky.graphics.GameRenderer;
import com.ducksofts.ducky.graphics.PImage;
import com.ducksofts.ducky.utils.Audio;
import com.ducksofts.ducky.utils.Input;
import com.ducksofts.ghosty.graphics.LightData;
import com.ducksofts.ghosty.levels.DefaultLevel;

public class Player extends NPC {
	
	public static Audio Win;
	public static Audio GhostBlow;
	public static Audio GhostFade;
	public static Audio GhostLaugh;
	
	public static PImage[] Medals;
	public static PImage GhostFront;
	public static PImage GhostBack;
	public static PImage GhostLeft;
	public static PImage GhostRight;
	
	public static void loadImages() {
		Medals = new PImage[3];
		Medals[0] = new PImage("/misc/bronze.png");
		Medals[1] = new PImage("/misc/silver.png");
		Medals[2] = new PImage("/misc/gold.png");
		GhostFront = new PImage("/entity/ghost/gf.png");
		GhostBack= new PImage("/entity/ghost/gb.png");
		GhostLeft = new PImage("/entity/ghost/gl.png");
		GhostRight = new PImage("/entity/ghost/gr.png");
		
		Win = new Audio("/audio/win.wav");
		GhostBlow = new Audio("/audio/blow.wav");
		GhostFade = new Audio("/audio/ghostFade.wav");
		GhostLaugh = new Audio("/audio/laugh.wav");
	}
	
	private boolean mIsGhost = false;
	private int mWidth, mHeight;
	private float mRandomMove = 0;
	private LightData mLight;
	
	private final int mEAttackCharge = 240;
	private float mEFear = 2f;
	private int mETimeBonus = 100;
	int mETimer = 0;
	private final int mQAttackCharge = 120;
	private final int mQTimeBonus = 70;
	private final double mAttackMaxRange = 150;
	int mQTimer = 0;
	
	protected int mTempScore = 0;
	protected int mScore = 0;
	protected int mMultiplier = 1;
	
	private static final int mMaxGhostTime = 11 * 60 + 30;
	protected int mGhostTime = 0;
	protected int mGhostPosX, mGhostPosY;
	protected DIRECTION mGhostDir;
	
	protected List<Entity> mEntities;
	
	public Player(Level l, int w, int h, List<Entity> ents) {
		super(l);
		turnWhite();
		mEntities = ents;
		mSpeed = 3;
		mWidth = w;
		mHeight = h;
		mTimeInterval = 10;
		mLight = new LightData();
		mLight.radius = 250;
		mLight.colorG = 0.1f;
		mLight.colorR = 0.1f;
		mLight.colorB = 0.1f;
		((DefaultLevel)l).mLighting.add(mLight);
	}

	public int getOffsetX() {
		if(mIsGhost)
			return mWidth / 2 - mGhostPosX;
		return mWidth / 2 - positionX;
	}
	
	public int getOffsetY() {
		if(mIsGhost)
			return mHeight / 2 - mGhostPosY;
		return mHeight / 2 - positionY;
	}
	
	public boolean isGhost() {
		return mIsGhost;
	}
		
	@Override
	public void update() {
		mState = STATE.IDLE;
		if(Input.isKeyDown(KeyEvent.VK_W)) {
			mVelY -= mSpeed;
			mDirection = DIRECTION.TOP;
		}
		if(Input.isKeyDown(KeyEvent.VK_S)) {
			mVelY += mSpeed;
			mDirection = DIRECTION.BOTTOM;
		}
		if(Input.isKeyDown(KeyEvent.VK_D)) {
			mVelX += mSpeed;
			mDirection = DIRECTION.RIGHT;
		}
		if(Input.isKeyDown(KeyEvent.VK_A)) {
			mVelX -= mSpeed;
			mDirection = DIRECTION.LEFT;
		}
		if(mVelX != 0 || mVelY != 0)
			mState = STATE.WALKING;
		
		if(Input.isKeyDown(KeyEvent.VK_ENTER))
			endGame();
		
		if(Input.isKeyDown(KeyEvent.VK_SPACE)) {
			if(!mIsGhost) {
				mGhostTime = mMaxGhostTime;
				mIsGhost = true;
				mLight.radius = 150;
				mLight.colorG = 0.6f;
				mLight.colorR = 0.3f;
				mLight.colorB = 0.3f;
				mETimer = 0;
				mQTimer = 0;
				mGhostDir = DIRECTION.BOTTOM;
				mGhostPosX = positionX;
				mGhostPosY = positionY;
			}
		}
		
		if(mGhostTime <= 0 && mIsGhost) {
			mIsGhost = false;
			mLight.radius = 250;
			mLight.colorG = 0.1f;
			mLight.colorR = 0.1f;
			mLight.colorB = 0.1f;
		}
		
		if(mIsGhost) {
			mGhostTime--;
			mQTimer--;
			mETimer--;
			if(Input.isKeyDown(KeyEvent.VK_Q) && mQTimer <= 0) {
				GhostBlow.setToStart();
				GhostBlow.play();
				boolean hit = false;
				boolean allHit = true;
				for(Entity e : mEntities) {
					if(e instanceof Furniture) {
						Furniture f = (Furniture)e;
						int dx = mGhostPosX - f.positionX;
						int dy = mGhostPosY - f.positionY;
						if(Math.sqrt(dx * dx + dy * dy) <= mAttackMaxRange && f.mOn) {
							f.setOn(false);
							mGhostTime += mQTimeBonus;
							mMultiplier++;
							mTempScore++;
						}
					} else if (e instanceof NPC && e != this) {
						NPC n = (NPC)e;
						int dx = mGhostPosX - n.positionX;
						int dy = mGhostPosY - n.positionY;
						if(Math.sqrt(dx * dx + dy * dy) <= mAttackMaxRange && n.mFear < 6) {
							n.mFear += mEFear;
							if(n.mFear >= 6) {
								hit = true;
								n.turnWhite();
								mTempScore += 10;
							}
						}
						if(n.mFear < 6)
							allHit = false;
					}
				}
				if(hit) {
					GhostLaugh.setToStart();
					GhostLaugh.play();
				}
				if(allHit)
					endGame();
				mQTimer = mQAttackCharge;
			}
			if(Input.isKeyDown(KeyEvent.VK_E) && mETimer <= 0) {
				GhostFade.setToStart();
				GhostFade.play();
				boolean hit = false;
				boolean allHit = true;
				for(Entity e : mEntities) {
					if(e instanceof NPC && e != this) {
						NPC n = (NPC)e;
						int dx = mGhostPosX - n.positionX;
						int dy = mGhostPosY - n.positionY;
						if(Math.sqrt(dx * dx + dy * dy) <= mAttackMaxRange && n.mFear < 6) {
							n.mFear += mEFear;
							mGhostTime += mETimeBonus;
							if(n.mFear >= 6) {
								hit = true;
								n.turnWhite();
								mTempScore += 10;
							}
						}
						if(n.mFear < 6)
							allHit = false;
					}
				}
				if(hit) {
					GhostLaugh.setToStart();
					GhostLaugh.play();
				}
				if(allHit)
					endGame();
				mETimer = mEAttackCharge;
			}
			mState = STATE.IDLE;
			mGhostDir = mDirection;
			mDirection = DIRECTION.TOP;
			mGhostPosX += mVelX;
			mGhostPosY += mVelY;
			if(mGhostPosX < 544)
				mGhostPosX = 544;
			if(mGhostPosY < 352)
				mGhostPosY = 352;
			if(mGhostPosX > (((DefaultLevel)mLevel).getLevelWidth() - 8) * 64 - 32)
				mGhostPosX = (((DefaultLevel)mLevel).getLevelWidth() - 8) * 64 - 32;
			if(mGhostPosY > (((DefaultLevel)mLevel).getLevelHeight() - 5) * 64 - 32)
				mGhostPosY = (((DefaultLevel)mLevel).getLevelHeight() - 5) * 64 - 32;
			mVelX = 0;
			mVelY = 0;
			mLight.positionX = mGhostPosX;
			mLight.positionY = mGhostPosY;
		}
		else {
			noaiUpdate();
			mLight.positionX = positionX;
			mLight.positionY = positionY;
			
		}
		
		mRandomMove += 0.05f;
	}

	@Override
	public void render(GameRenderer renderer) {
		if(mIsGhost)
			renderer.render(getGhost(), mGhostPosX + (int)(Math.cos(mRandomMove) * 5.0f) - 24, mGhostPosY + (int)(Math.sin(mRandomMove) * 5.0f) - 32, -1, mEAttackCharge - mETimer <= 60 ? 1 : 0.1f, true);
		super.render(renderer);
	}
	
	protected PImage getGhost() {
		switch(mGhostDir) {
		case TOP:
			return GhostBack;
		case BOTTOM:
			return GhostFront;
		case RIGHT:
			return GhostRight;
		case LEFT:
			return GhostLeft;
		}
		return null;
	}
	
	private void endGame() {
		((DefaultLevel)mLevel).ending = true;
	}

}
