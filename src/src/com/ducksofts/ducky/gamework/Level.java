package com.ducksofts.ducky.gamework;

import java.util.ArrayList;
import java.util.List;

import com.ducksofts.ducky.graphics.GameRenderer;

public class Level {

	protected LevelManager mLevelManager;
	protected List<Entity> mEntities = new ArrayList<Entity>();
	protected List<CollisionBox> mBoxes = new ArrayList<CollisionBox>();
	
	public Level(LevelManager lm) {
		mLevelManager = lm;
		lm.addLevel(this);
	}
	
	public void update() {
		for(int i = 0; i < mEntities.size(); i++)
			mEntities.get(i).update();
	}
	
	public void render(GameRenderer renderer) {
		for(int i = 0; i < mEntities.size(); i++)
			mEntities.get(i).render(renderer);
	}
	 
	public boolean isCurrent() {
		return mLevelManager.mCurrentLevel == this;
	}
	
}
