package com.ducksofts.ghosty.entity;

import com.ducksofts.ducky.gamework.CollisionBox;
import com.ducksofts.ducky.gamework.Level;
import com.ducksofts.ducky.graphics.GameRenderer;
import com.ducksofts.ducky.graphics.PImage;
import com.ducksofts.ghosty.graphics.LightData;
import com.ducksofts.ghosty.graphics.Lighting;

public class Furniture extends CollisionBox {

	public static PImage Chair;
	public static PImage Couch;
	public static PImage Cupboard;
	public static PImage[] LightCupboard;
	public static PImage[] Fireplace;
	public static PImage[] Lamp;
	
	public static void loadImages() {
		Chair = new PImage("/level/chair.png");
		Couch = new PImage("/level/couch.png");
		Cupboard = new PImage("/level/cupboard.png");
		LightCupboard = new PImage[2];
		LightCupboard[0]= new PImage("/level/cupboardOn.png");
		LightCupboard[1]= new PImage("/level/cupboardOff.png");
		Fireplace = new PImage[3];
		Fireplace[0] = new PImage("/level/fireplace1.png");
		Fireplace[1] = new PImage("/level/fireplace2.png");
		Fireplace[2] = new PImage("/level/fireplaceOff.png");
		Lamp = new PImage[2];
		Lamp[0] = new PImage("/level/lampOn.png");
		Lamp[1] = new PImage("/level/lampOff.png");
	}
	
	protected LightData mLight;
	protected PImage mSingleImage;
	protected PImage[] mMultipleImages;
	protected boolean mSingle;
	protected boolean mOn;
	protected int mTimer = 0;
	protected int mFullCycle = 40;
	public int minLightRadius = 200;
	public int maxLightRadius = 250;
	protected Player mPlayer;
	
	public Furniture(Level l, PImage single, PImage[] multiple, Lighting lighting, Player player) {
		super(l, multiple == null ? single.getWidth() / 2 : multiple[0].getWidth() / 2, multiple == null ? single.getHeight() / 2 : multiple[0].getHeight() / 2);
		mSingle = multiple == null;
		mSingleImage = single;
		mMultipleImages = multiple;
		mLight = new LightData();
		mLight.positionX = positionX;
		mLight.positionY = positionY;
		if(!mSingle) {
			mOn = true;
			mLight.colorR = 1;
			mLight.colorG = 0.75f;
			mLight.colorB = 0.75f;			
			lighting.add(mLight);
		} else mOn = false;
		mTimer = rand.nextInt(mFullCycle);
		mPlayer = player;
	}
	
	public LightData getLight() {
		return mLight;
	}
	
	public void setOn(boolean state) {
		mOn = state;
		if(state) {
			mLight.colorR = 1;
			mLight.colorG = 0.75f;
			mLight.colorB = 0.75f;
		}
		else {
			mLight.colorR = 0;
			mLight.colorG = 0;
			mLight.colorB = 0;
		}
	}
	
	public void update() {
		mTimer++;
		double rad = Math.abs(Math.sin((double)mTimer / (double)mFullCycle * Math.PI)) * (maxLightRadius - minLightRadius) + minLightRadius;
		mLight.radius = (int)rad;
		mLight.positionX = positionX;
		mLight.positionY = positionY;
	}
	
	public void render(GameRenderer renderer) {
		if(mSingle) {
			renderer.render(mSingleImage, positionX - mSizeX, positionY - mSizeY, 10, 1, true);
		} else if(mOn) {
			renderer.render(mMultipleImages[0], positionX - mSizeX, positionY - mSizeY, 125, 1, true);
		} else {
			renderer.render(mMultipleImages[1], positionX - mSizeX, positionY - mSizeY, 125, 1, true);
		}
	}
	
	

}
