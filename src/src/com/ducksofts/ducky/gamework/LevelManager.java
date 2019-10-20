package com.ducksofts.ducky.gamework;

import java.util.ArrayList;
import java.util.List;

import com.ducksofts.ducky.graphics.GameRenderer;

public class LevelManager {

	List<Level> mLevels = new ArrayList<Level>();
	Level mCurrentLevel;
	
	public LevelManager() {
		
	}
	
	public void init() {
		
	}
	
	public void update() {
		if(mCurrentLevel != null)
			mCurrentLevel.update();		
	}
	
	public void render(GameRenderer renderer) {
		if(mCurrentLevel != null)
			mCurrentLevel.render(renderer);
	}
	
	public void addLevel(Level l) {
		mLevels.add(l);
	}
	
	public void setLevel(Level l) {
		mCurrentLevel = l;
	}

	public void setLevel(int index) {
		mCurrentLevel = mLevels.get(index);
	}
	
	public void nextLevel() {
		for(int i = 0; i < mLevels.size(); i++) {
			if(mCurrentLevel == mLevels.get(i)) {
				mCurrentLevel = mLevels.get(i + 1);
				break;
			}
		}
	}
	
}
