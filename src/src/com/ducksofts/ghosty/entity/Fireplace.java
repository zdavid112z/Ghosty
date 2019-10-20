package com.ducksofts.ghosty.entity;

import com.ducksofts.ducky.gamework.Level;
import com.ducksofts.ducky.graphics.GameRenderer;
import com.ducksofts.ghosty.graphics.Lighting;

public class Fireplace extends Furniture {

	public Fireplace(Level l, Lighting lighting, Player p) {
		super(l, null, Furniture.Fireplace, lighting, p);
	}
	
	public void render(GameRenderer r) {
		if(mOn) {
			r.render(mMultipleImages[0], positionX - mSizeX, positionY - mSizeY, 125, 1, true);
			float t = (float)Math.abs(Math.sin((double)mTimer / (double)mFullCycle * Math.PI));
			r.render(mMultipleImages[1], positionX - mSizeX, positionY - mSizeY, 125, t, true);
		} else r.render(mMultipleImages[2], positionX - mSizeX, positionY - mSizeY, 125, 1, true);
	}

}
