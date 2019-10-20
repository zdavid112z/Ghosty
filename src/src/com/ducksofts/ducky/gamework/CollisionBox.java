package com.ducksofts.ducky.gamework;

import java.util.List;

import com.ducksofts.ducky.graphics.GameRenderer;

public class CollisionBox extends Entity {

	//Square size X = 2 * mSizeX
	protected boolean mHasMoved;
	protected int mSizeX, mSizeY;
	protected int mVelX = 0, mVelY = 0;
	
	public CollisionBox(Level l, int sx, int sy) {
		super(l);
		mSizeX = sx;
		mSizeY = sy;
		l.mBoxes.add(this);
	}
	
	public CollisionBox collides() {
		List<CollisionBox> boxes = mLevel.mBoxes;
		for(CollisionBox b : boxes) {
			if(b == this)
				continue;
			int npx = positionX + mVelX;
			int npy = positionY + mVelY;
			int c12x = Math.abs(npx - b.positionX);
			int d12x = mSizeX + b.mSizeX;
			int dx = d12x - c12x;
			int c12y = Math.abs(npy - b.positionY);
			int d12y = mSizeY + b.mSizeY;
			int dy = d12y - c12y;
 			if(dx > 0 && dy > 0) {
 				return b;
 			}
		}
		return null;
	}
	
	public void update() {
		mHasMoved = true;
		if(mVelX == 0 && mVelY == 0)
			return;
		final List<CollisionBox> boxes = mLevel.mBoxes;
		for(final CollisionBox b : boxes) {
			if(b == this)
				continue;
			int npx = positionX + mVelX;
			int npy = positionY + mVelY;
			int c12x = Math.abs(npx - b.positionX);
			int d12x = mSizeX + b.mSizeX;
			int dx = d12x - c12x;
			int c12y = Math.abs(npy - b.positionY);
			int d12y = mSizeY + b.mSizeY;
			int dy = d12y - c12y;
 			if(dx > 0 && dy > 0) {
 				/*if(dy > dx) {
	 				if(npx - b.positionX < 0)
	 					mVelX -= dx;
	 				else mVelX += dx;
 				}
 				else {
 					if(npy - b.positionY < 0)
	 					mVelY -= dy;
	 				else mVelY += dy;
 				}*/
 				mVelX = 0;
 				mVelY = 0;
 				mHasMoved = false;
 				break;
 			}
		}
		positionX += mVelX;
		positionY += mVelY;
		mVelX = 0;
		mVelY = 0;
	}

	@Override
	public void render(GameRenderer renderer) {
		
	}

}
