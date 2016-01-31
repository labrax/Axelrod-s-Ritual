package ggj2016.axelrodsritual;

import java.io.IOException;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import ggj2016.axelrodsritual.util.Resources;

/**
 * Class for handling sound, not used
 * @author labrax
 *
 */
public class Sound
{
	public enum SOUND_TYPE {START, BACKGROUND};
	private static boolean loaded = false;
	private static Clip startSound = null;
	private static Clip backgroundSound = null;
	private static Clip placeholderSound = null;
	
	/**
	 * Load all sounds
	 */
	private static void load()
	{
		loaded = true;
		try {
			startSound = Resources.loadSound("/sounds/start.wav");
			backgroundSound = Resources.loadSound("/sounds/background.wav");
			placeholderSound = Resources.loadSound("/sounds/placeholder.wav");
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Play a sound
	 * @param type is the sound
	 */
	public static void playSound(SOUND_TYPE type)
	{
		if(loaded == false)
			load();
		Clip sound = null;
		switch(type)
		{
			case START:
				sound = startSound;
				break;
			case BACKGROUND:
				sound = backgroundSound;
				break;
			default:
				sound = placeholderSound;
				break;
		}
		if(sound != null)
			sound.start();
	}
}
