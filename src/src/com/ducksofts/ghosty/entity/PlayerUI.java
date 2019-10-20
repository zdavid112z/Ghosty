package com.ducksofts.ghosty.entity;

import java.util.List;

import com.ducksofts.ducky.gamework.Entity;
import com.ducksofts.ducky.gamework.Level;
import com.ducksofts.ducky.graphics.GameRenderer;
import com.ducksofts.ducky.graphics.PFont;
import com.ducksofts.ducky.graphics.PImage;
import com.ducksofts.ducky.utils.Audio;
import com.ducksofts.ghosty.MainGame;
import com.ducksofts.ghosty.levels.DefaultLevel;

public class PlayerUI extends Entity {

	protected Player mPlayer;
	protected PFont mFont;
	protected Audio mAudio;
	protected int mOldMult = 1;
	protected PImage mMap;
	protected List<Entity> mEntities;
	
	private int mOffsetX;
	private int mOffsetY;
	private int mEndTime = 0;
	private int mTime;
	
	public PlayerUI(Level l, Player player, PImage map) {
		super(l);
		mPlayer = player;
		mEntities = mPlayer.mEntities;
		mMap = map;
		mFont = new PFont("/font.fnt", "/font_0.png");
		mAudio = new Audio("/audio.wav");
		mOffsetX = ((DefaultLevel)l).getWindowWidth() - map.getWidth() - 10;
		mOffsetY = 10;
	}

	@Override
	public void update() {
		mTime++;
		if(mEndTime == 1) {
			Player.Win.setToStart();
			Player.Win.play();
		}
		if(((DefaultLevel)mLevel).ending)
			mEndTime++;
		if(mOldMult < mPlayer.mMultiplier)
			mOldMult = mPlayer.mMultiplier;
		if(!mPlayer.isGhost()) {
			if(mPlayer.mTempScore > 0) {
				mPlayer.mMultiplier = 1;
				mPlayer.mScore += mOldMult;
				mPlayer.mTempScore--;
			}
		} else mOldMult = 1;
	}

	public void render(GameRenderer renderer) {
		
	}
	
	public void renderNow(GameRenderer renderer) {
		if(mEndTime > 0) {
			float f = mEndTime / 120.0f;
			((DefaultLevel)mLevel).mFade.fade(0, f < 1 ? f : 1);
			renderer.renderTextNow(mFont, 170, 186, "You won with a score of " + mPlayer.mScore, 0xffffff, -127, 1, false);
			int medal = mPlayer.mScore / 1200;
			if(medal > 2)
				medal = 2;
			renderer.renderImageNow(Player.Medals[medal], 300, 230, -127, 1, false);
			if(mEndTime >= 120) {
				MainGame.mainGame.nextLevel();
			}
			return;
		}
		renderer.renderTextNow(mFont, 0, 0, "Total Score: " + mPlayer.mScore, 0xffffff, 0, 1, false);
		renderer.renderTextNow(mFont, 0, 28, "Multiplier: " + mPlayer.mMultiplier, 0xffffff, 0, 1, false);
		renderer.renderTextNow(mFont, 0, 56, "Score: " + mPlayer.mTempScore, 0xffffff, 0, 1, false);
		
		renderer.renderImageNow(mMap, mOffsetX, mOffsetY, 0, 0.6f, false);
		for(Entity e : mEntities) {
			if(e instanceof NPC && !(e instanceof Player)) {
				NPC n = (NPC)e;
				if(n.mFear < 6) {
					int x = (int) ((n.positionX / 64.0f - 7.0f) * 3.0f);
					x += mOffsetX;
					int y = (int) ((n.positionY / 64.0f - 4.0f) * 3.0f);
					y += mOffsetY;
					renderer.blendPixel(0xff00, x, y, 0.6f);
					//renderer.getPixels()[x + y * renderer.getWidth()] = 0xaa00;
				}
			} else if(e instanceof Furniture) {
				Furniture f = (Furniture)e;
				if(f.mOn) {					
					int x = (int) ((f.positionX / 64.0f - 7.0f) * 3.0f);
					x += mOffsetX;
					int y = (int) ((f.positionY / 64.0f - 4.0f) * 3.0f);
					y += mOffsetY;
					renderer.blendPixel(0xffff00, x, y, 0.6f);
					//renderer.getPixels()[x + y * renderer.getWidth()] = 0xffff00;
				}
			}
		}
		if(mPlayer.isGhost()) {
			int x = (int) ((mPlayer.mGhostPosX / 64.0f - 7.0f) * 3.0f);
			x += mOffsetX;
			int y = (int) ((mPlayer.mGhostPosY / 64.0f - 4.0f) * 3.0f);
			y += mOffsetY;
			renderer.blendPixel(0xff0000, x, y, 0.6f);
			renderer.blendPixel(0xff0000, x + 1, y, 0.6f);
			renderer.blendPixel(0xff0000, x, y + 1, 0.6f);
			renderer.blendPixel(0xff0000, x, y - 1, 0.6f);
			renderer.blendPixel(0xff0000, x - 1, y, 0.6f);
		}
		else {
			int x = (int) ((mPlayer.positionX / 64.0f - 7.0f) * 3.0f);
			x += mOffsetX;
			int y = (int) ((mPlayer.positionY / 64.0f - 4.0f) * 3.0f);
			y += mOffsetY;
			renderer.blendPixel(0xff0000, x, y, 0.6f);
			renderer.blendPixel(0xff0000, x + 1, y, 0.6f);
			renderer.blendPixel(0xff0000, x, y + 1, 0.6f);
			renderer.blendPixel(0xff0000, x, y - 1, 0.6f);
			renderer.blendPixel(0xff0000, x - 1, y, 0.6f);
		}
		if(mPlayer.isGhost()) {
			String time = "Time: ";
			time += mPlayer.mGhostTime / 3600;
			time += ":";
			int t = ((mPlayer.mGhostTime / 60) % 60);
			time += t < 10 ? "0" + t : t + "";
			renderer.renderTextNow(mFont, 0, 84, time, 0xffffff, 0, 1, false);
			if(mPlayer.mQTimer <= 0)
				renderer.renderTextNow(mFont, 10, 360, "Blow ready", 0xff0000, 0, 1, false);
			if(mPlayer.mETimer <= 0)
				renderer.renderTextNow(mFont, 460, 360, "Visibility ready", 0xff0000, 0, 1, false);
		}
		if(mTime < 120) {
			((DefaultLevel)mLevel).mFade.fade(0, 1.0f - mTime / 120.0f);
		}
	}

}
