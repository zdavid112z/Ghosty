package com.ducksofts.ghosty.entity;

import com.ducksofts.ducky.gamework.Entity;
import com.ducksofts.ducky.gamework.Level;
import com.ducksofts.ducky.graphics.GameRenderer;
import com.ducksofts.ducky.utils.Audio;
import com.ducksofts.ghosty.levels.DefaultLevel;

public class Music extends Entity {

	public static Audio mSlowMusic;
	public static Audio mFastMusic;
	
	public static void loadMusic() {
		mSlowMusic = new Audio("/audio/slowLoop.wav");
		mFastMusic = new Audio("/audio/fastLoop.wav");
	}
	
	private Player mPlayer;
	
	public Music(Level l, Player p) {
		super(l);
		mPlayer = p;
	}

	@Override
	public void update() {
		if(((DefaultLevel)mLevel).ending) {
			mFastMusic.stop();
			mSlowMusic.stop();
			return;
		}
		if(mPlayer.isGhost()) {
			mSlowMusic.stop();
			if(!mFastMusic.isPlaying()) {
				mFastMusic.setToStart();
				mFastMusic.play();
			}
		} else {
			mFastMusic.stop();
			if(!mSlowMusic.isPlaying()) {
				mSlowMusic.setToStart();
				mSlowMusic.play();
			}
		}
	}

	@Override
	public void render(GameRenderer renderer) {
		
	}

}
