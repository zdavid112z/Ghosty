package com.ducksofts.ghosty.levels;

import com.ducksofts.ducky.gamework.Level;
import com.ducksofts.ducky.gamework.LevelManager;
import com.ducksofts.ducky.graphics.GameRenderer;
import com.ducksofts.ducky.graphics.PImage;
import com.ducksofts.ducky.utils.Audio;
import com.ducksofts.ducky.utils.Input;
import com.ducksofts.ghosty.MainGame;
import com.ducksofts.ghosty.entity.Music;

public class ImageLevel extends Level {

	private Audio mMusic = Music.mSlowMusic;
	private PImage mImage;
	private int mKeycode;
	
	public ImageLevel(LevelManager lm, PImage image, int keycode) {
		super(lm);
		mImage = image;
		mKeycode = keycode;
	}
	
	public void update() {
		if(!mMusic.isPlaying()) {
			mMusic.setToStart();
			mMusic.play();
		}
		if(Input.isKeyDown(mKeycode)) {
			MainGame.mainGame.nextLevel();
			Input.resetKey(mKeycode);
		}
	}
	
	public void render(GameRenderer renderer) {
		renderer.render(mImage, 0, 0, 0, 1, false);
		renderer.renderAll();
	}

}
