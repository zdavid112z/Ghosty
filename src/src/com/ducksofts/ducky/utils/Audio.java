package com.ducksofts.ducky.utils;

import java.io.BufferedInputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;

public class Audio {

	private Clip mClip;
	
	public Audio(String path) {
		try {
			class AudioListener implements LineListener {
				@Override
				public synchronized void update(LineEvent event) {
					Type eventType = event.getType();
					if (eventType == Type.STOP || eventType == Type.CLOSE) {
						notifyAll();
					}
				}
			}
			AudioListener listener = new AudioListener();
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(new BufferedInputStream(Audio.class.getResourceAsStream(path)));
			Clip clip = AudioSystem.getClip();
			clip.addLineListener(listener);
			clip.open(audioInputStream);
			mClip = clip;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(mClip == null)
			System.err.println("Could not load audio file " + path + "!");
	}
	
	public synchronized void setToStart() {
		mClip.setFramePosition(0);
	}
	
	public synchronized void play() {
		mClip.start();
	}
	
	public synchronized void pause() {
		mClip.stop();
	}
	
	public synchronized void stop() {
		mClip.stop();
		mClip.setFramePosition(0);
	}
	
	public synchronized boolean isPlaying() {
		return mClip.isActive();
	}

}
