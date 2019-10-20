package com.ducksofts.ghosty.entity;

import com.ducksofts.ducky.gamework.CollisionBox;
import com.ducksofts.ducky.gamework.Level;
import com.ducksofts.ducky.graphics.GameRenderer;
import com.ducksofts.ducky.graphics.PImage;

public class NPC extends CollisionBox {
	
	public static enum STATE {
		IDLE, WALKING
	}
	
	public static enum DIRECTION {
		TOP, BOTTOM, RIGHT, LEFT
	}
	
	public final static int[] SkinColors = {
			0xFFA146, 0xFFC37A, 0xD8A468, 0xAF8656, 0xEFB777, 0xEDDD4B, 0xFFEF68
	};
	
	public static PImage[] BodyFront;
	public static PImage BodyBack;
	public static PImage BodyLatheral;
	public static PImage Face;
	public static PImage[] MouthsFront;
	public static PImage[] MouthsLeft;
	public static PImage[] MouthsRight;
	public static PImage EyesFront;
	public static PImage EyesLeft;
	public static PImage EyesRight;
	public static PImage LegsFrontBack;
	public static PImage[] LegsFrontBackWalking;
	public static PImage LegsLatheral;
	public static PImage[] LegsLeftWalking;
	public static PImage[] LegsRightWalking;
	
	public static void loadImages() {
		BodyFront = new PImage[4];
		BodyFront[0] = new PImage("/entity/npc/bf1.png");
		BodyFront[1] = new PImage("/entity/npc/bf2.png");
		BodyFront[2] = new PImage("/entity/npc/bf3.png");
		BodyFront[3] = new PImage("/entity/npc/bf4.png");
		BodyBack = new PImage("/entity/npc/bb.png");
		BodyLatheral = new PImage("/entity/npc/bl.png");
		Face = new PImage("/entity/npc/fe.png");
		MouthsFront = new PImage[6];
		MouthsFront[0] = new PImage("/entity/npc/m1.png");
		MouthsFront[1] = new PImage("/entity/npc/m2.png");
		MouthsFront[2] = new PImage("/entity/npc/m3.png");
		MouthsFront[3] = new PImage("/entity/npc/m4.png");
		MouthsFront[4] = new PImage("/entity/npc/m5.png");
		MouthsFront[5] = new PImage("/entity/npc/m6.png");
		MouthsLeft = new PImage[6];
		MouthsLeft[0] = new PImage("/entity/npc/ml1.png");
		MouthsLeft[1] = new PImage("/entity/npc/ml2.png");
		MouthsLeft[2] = new PImage("/entity/npc/ml3.png");
		MouthsLeft[3] = new PImage("/entity/npc/ml4.png");
		MouthsLeft[4] = new PImage("/entity/npc/ml5.png");
		MouthsLeft[5] = new PImage("/entity/npc/ml6.png");
		MouthsRight = new PImage[6];
		MouthsRight[0] = new PImage("/entity/npc/mr1.png");
		MouthsRight[1] = new PImage("/entity/npc/mr2.png");
		MouthsRight[2] = new PImage("/entity/npc/mr3.png");
		MouthsRight[3] = new PImage("/entity/npc/mr4.png");
		MouthsRight[4] = new PImage("/entity/npc/mr5.png");
		MouthsRight[5] = new PImage("/entity/npc/mr6.png");
		EyesFront = new PImage("/entity/npc/ef.png");
		EyesLeft = new PImage("/entity/npc/el.png");
		EyesRight = new PImage("/entity/npc/er.png");
		LegsFrontBack = new PImage("/entity/npc/lfi.png");
		LegsFrontBackWalking = new PImage[2];
		LegsFrontBackWalking[0] = new PImage("/entity/npc/lw1.png");
		LegsFrontBackWalking[1] = new PImage("/entity/npc/lw2.png");
		LegsLatheral = new PImage("/entity/npc/lli.png");
		LegsLeftWalking = new PImage[2];
		LegsLeftWalking[0] = new PImage("/entity/npc/llw1.png");
		LegsLeftWalking[1] = new PImage("/entity/npc/llw2.png");
		LegsRightWalking = new PImage[2];
		LegsRightWalking[0] = new PImage("/entity/npc/lrw1.png");
		LegsRightWalking[1] = new PImage("/entity/npc/lrw2.png");
	}
	
	protected int mTimeInterval = 15;
	protected int mTime = 0;
	protected float mFear = 0;
	protected int mSpeed = 2;
	
	protected final int mShirtColor;
	protected final int mShirtSecColor;
	protected final int mPantsColor;
	protected int mSkinColor;
	protected final int mShirt;
	
	protected int mLegsOffsetX = -24;
	protected int mLegsOffsetY = 10;
	protected int mHeadOffsetX = -16;
	protected int mHeadOffsetY = -48;
	protected int mBodyOffsetX = -24;
	protected int mBodyOffsetY = -20;
	
	protected STATE mState = STATE.IDLE;
	protected DIRECTION mDirection = DIRECTION.RIGHT;
	
	protected int[] mPantsOrg;
	protected int[] mPantsOther;
	protected int[] mBodyOrg;
	protected int[] mBodyOther;
	protected int[] mFaceOrg;
	protected int[] mFaceOther;
	
	private int mAction = 0;
	private int mTimeAction = 0;
	private int mActVelX = 0;
	private int mActVelY = 0;
	private final int mMaxTime = 300;
	private final int mMinTime = 120;
	
	public NPC(Level l) {
		super(l, 16, 44);
		mShirt = rand.nextInt(BodyFront.length);
		mShirtColor = rand.nextInt(0xffffff);
		mShirtSecColor = rand.nextInt(0xffffff);
		mPantsColor = rand.nextInt(0xffffff);
		mSkinColor = SkinColors[rand.nextInt(SkinColors.length)];
		
		mPantsOrg = new int[2];
		mPantsOrg[0] = 0xFF0000;
		mPantsOrg[1] = 0xFFFF00;
		mPantsOther = new int[2];
		mPantsOther[0] = mPantsColor;
		mPantsOther[1] = mSkinColor;
		mBodyOrg = new int[3];
		mBodyOrg[0] = 0xFF;
		mBodyOrg[1] = 0xFF00;
		mBodyOrg[2] = 0xFFFF00;
		mBodyOther = new int[3];
		mBodyOther[0] = mShirtColor;
		mBodyOther[1] = mShirtSecColor;
		mBodyOther[2] = mSkinColor;
		mFaceOrg = new int[1];
		mFaceOrg[0] = 0xffff00;
		mFaceOther = new int[1];
		mFaceOther[0] = mSkinColor;
		
	}

	public void turnWhite() {
		mSkinColor = 0xffffff;
		mBodyOther[2] = mSkinColor;
		mFaceOther[0] = mSkinColor;
		mPantsOther[1] = mSkinColor;
	}
	
	@Override
	public void update() {
		mTimeAction--;
		noaiUpdate();
		if(!mHasMoved || mTimeAction <= 0) {
			mAction = rand.nextInt(10);
			if(mAction < 9) {
				mActVelX = (mAction % 3 - 1) * mSpeed;
				mActVelY = (mAction / 3 - 1) * mSpeed;
			}
			else {
				mActVelX = 0;
				mActVelY = 0;
			}
			mTimeAction = rand.nextInt(mMaxTime - mMinTime) + mMinTime;
		}
		mState = STATE.IDLE;
		if(mActVelY > 0) {
			mDirection = DIRECTION.BOTTOM;
			mState = STATE.WALKING;
		}
		else if(mActVelY < 0) {
			mDirection = DIRECTION.TOP;
			mState = STATE.WALKING;
		}
		else if(mActVelX > 0) {
			mDirection = DIRECTION.RIGHT;
			mState = STATE.WALKING;
		}
		else if(mActVelX < 0) {
			mDirection = DIRECTION.LEFT;
			mState = STATE.WALKING;
		}
		mVelX = mActVelX;
		mVelY = mActVelY;
	}
	
	protected void noaiUpdate() {
		mTime++;
		super.update();
	}

	@Override
	public void render(GameRenderer renderer) {
		renderer.render(getLegs(), mPantsOrg, mPantsOther, positionX + mLegsOffsetX, positionY + mLegsOffsetY, 0, 1, true);
		renderer.render(getBody(), mBodyOrg, mBodyOther, positionX + mBodyOffsetX, positionY + mBodyOffsetY, 0, 1, true);
		renderer.render(Face, mFaceOrg, mFaceOther, positionX + mHeadOffsetX, positionY + mHeadOffsetY, 0, 1, true);
		if(mDirection != DIRECTION.TOP) {
			renderer.render(getEyes(), positionX + mHeadOffsetX, positionY + mHeadOffsetY, 0, 1, true);
			renderer.render(getMouth(), positionX + mHeadOffsetX, positionY + mHeadOffsetY, 0, 1, true);
		}
	}
	
	private PImage getLegs() {
		switch(mDirection) {
		case BOTTOM:
			if(mState == STATE.IDLE)
				return LegsFrontBack;
			else return LegsFrontBackWalking[(mTime / mTimeInterval) % LegsFrontBackWalking.length];
		case TOP:
			if(mState == STATE.IDLE)
				return LegsFrontBack;
			else return LegsFrontBackWalking[(mTime / mTimeInterval) % LegsFrontBackWalking.length];
		case LEFT:
			if(mState == STATE.IDLE)
				return LegsLatheral;
			else return LegsLeftWalking[(mTime / mTimeInterval) % LegsLeftWalking.length];
		case RIGHT:
			if(mState == STATE.IDLE)
				return LegsLatheral;
			else return LegsRightWalking[(mTime / mTimeInterval) % LegsRightWalking.length];
		}
		return null;
	}

	private PImage getBody() {
		switch(mDirection) {
		case BOTTOM:
			return BodyFront[mShirt];
		case TOP:
			return BodyBack;
		case LEFT:
			return BodyLatheral;
		case RIGHT:
			return BodyLatheral;
		}
		return null;
	}

	private PImage getMouth() {
		switch(mDirection) {
		case BOTTOM:
			return MouthsFront[((int) mFear) < 5 ? (int) mFear : 5];
		case TOP:
			return null;
		case LEFT:
			return MouthsLeft[((int) mFear) < 5 ? (int) mFear : 5];
		case RIGHT:
			return MouthsRight[((int) mFear) < 5 ? (int) mFear : 5];
		}
		return null;
	}
	
	private PImage getEyes() {
		switch(mDirection) {
		case BOTTOM:
			return EyesFront;
		case TOP:
			return null;
		case LEFT:
			return EyesLeft;
		case RIGHT:
			return EyesRight;
		}
		return null;
	}

}
