package com.ducksofts.ducky;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import com.ducksofts.ducky.gamework.LevelManager;
import com.ducksofts.ducky.graphics.GameRenderer;
import com.ducksofts.ducky.graphics.PFont;
import com.ducksofts.ducky.graphics.PImage;
import com.ducksofts.ducky.utils.Input;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static Game mainGame;
	
	public static final String TITLE = "Game";
	public static final int WIDTH = 320;
	public static final int HEIGHT = 180;
	public static final int SCALE = 4;
	public static final int UPS = 60;
	
	private JFrame mFrame;
	private Thread mThread;
	private Input mInput;
	private LevelManager mLevelManager;
	private GameRenderer mRenderer;
	private boolean mRunning = false;
	
	public static void main(String[] args) {
		mainGame = new Game();
		mainGame.mFrame.add(mainGame);
		mainGame.mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainGame.mFrame.pack();
		mainGame.mFrame.setLocationRelativeTo(null);
		mainGame.mFrame.setTitle(TITLE);
		mainGame.mFrame.setVisible(true);
		mainGame.mFrame.setEnabled(true);
		mainGame.start();
	}

	public Game() {
		mFrame = new JFrame();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		mInput = new Input();
		addKeyListener(mInput);
		addMouseListener(mInput);
		addMouseMotionListener(mInput);
		addMouseWheelListener(mInput);
		mRenderer = new GameRenderer(WIDTH, HEIGHT);
		mRenderer.setBlending(true);
		mLevelManager = new LevelManager();
	}
	
	public synchronized void start() {
		if(mRunning)
			return;
		mRunning = true;
		mThread = new Thread(this, "Display");
		mThread.start();
	}
	
	public synchronized void stop() {
		if(!mRunning)
			return;
		mRunning = false;
		try {
			mThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	PImage ship;
	PImage trail;
	PFont fnt;
	int fps = 0;
	
	public void run() {
		ship = new PImage("/ship.png");
		trail = new PImage("/trail.png");
		fnt = new PFont("/font.fnt", "/font_0.png");
		int tfps = 0;
		long time = System.nanoTime();
		while(mRunning) {
			update();
			render();
			tfps++;
			if(System.nanoTime() - time >= 1000000000) {
				time += 1000000000;
				fps = tfps;
				tfps = 0;
			}
		}
	}
	
	private void update() {
		mLevelManager.update();
	}
	
	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		mRenderer.clearAll();
		mRenderer.render(fnt, 0, 0, "FPS: " + fps, 0xffffff, -1, 1, false);
		mRenderer.render(trail, (int)((float)Input.getMouseX() / (float)getWidth() * WIDTH) - trail.getWidth() / 2, (int)((float)Input.getMouseY() / (float)getHeight() * HEIGHT) - trail.getHeight() / 2, 0, 1, false);
		mRenderer.render(ship, 0, 0, 1, 1, false);
		mLevelManager.render(mRenderer);
		mRenderer.renderAll();
		g.drawImage(mRenderer.getImage(), 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}
}
