package com.ducksofts.ducky.gamework;

import java.util.Random;

import com.ducksofts.ducky.graphics.GameRenderer;

public abstract class Entity {

	public static Random rand = new Random();
	
	public int positionX = 0, positionY = 0;
	protected Level mLevel;
	
	public Entity(Level l) {
		mLevel = l;
		mLevel.mEntities.add(this);
	}
	
	public abstract void update();
	public abstract void render(GameRenderer renderer);
	
}
