package com.ducksofts.ducky.utils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	public static final int MAX_KEYS = 2000;
	
	private static boolean[] mKeys = new boolean[MAX_KEYS];
	private static int mMouseRawX = 0;
	private static int mMouseRawY = 0;
	private static int mMouseX = 0;
	private static int mMouseY = 0;
	private static int mMouseButton = 0;
	private static int mMouseWheel = 0;
	
	public static void update(float fx, float fy) {
		mMouseX = (int)((float)mMouseRawX * fx);
		mMouseY = (int)((float)mMouseRawY * fy);
	}
	
	public static boolean isKeyDown(int v) {
		return mKeys[v];
	}
	
	public static int getMouseX() {
		return mMouseX;
	}
	
	public static int getMouseY() {
		return mMouseY;
	}
	
	public static int getMouseButton() {
		return mMouseButton;
	}
	
	public static int getMouseWheel() {
		return mMouseWheel;
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		mMouseWheel = e.getWheelRotation();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mMouseRawX = e.getX();
		mMouseRawY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mMouseRawX = e.getX();
		mMouseRawY = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mMouseButton = e.getButton();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mMouseButton = 0;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		mKeys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		mKeys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	public static void resetKey(int mKeycode) {
		mKeys[mKeycode] = false;
	}

}
