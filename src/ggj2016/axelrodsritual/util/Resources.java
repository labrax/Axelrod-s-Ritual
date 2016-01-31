package ggj2016.axelrodsritual.util;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

import ggj2016.axelrodsritual.Config;

/**
 * Resources loader
 * @author labrax
 *
 */
public class Resources
{
	private static Font awtFont = null;
	private static Random random = null;
	
	/**
	 * Load the font from the file
	 */
	private static void loadFont()
	{
		try
		{
	        InputStream inputStream = ResourceLoader.getResourceAsStream(Config.FONT_FILE);
	        awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
	    }
		catch (Exception e)
		{
	        e.printStackTrace();
	        System.out.println("Failed to load font file! Using Times New Roman!");
	        
			awtFont = new Font("Times New Roman", Font.BOLD, 24);
	    }
	}

	/**
	 * Load a sound file
	 * @param file relative to user.dir path, such as "/sounds/acabou.wav"
	 * @return the loaded clip
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 * @throws LineUnavailableException 
	 */
	public static Clip loadSound(String file) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(System.getProperty("user.dir") + file).getAbsoluteFile());
		Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        return clip;
	}
	
	/**
	 * Return the game loaded font
	 * @param size is the font size
	 * @return
	 */
	public static TrueTypeFont getFont(float size)
	{
		if(awtFont == null)
			loadFont();
        return new TrueTypeFont(awtFont.deriveFont(size), false);
	}
	
	/**
	 * 
	 * @return random generator
	 */
	public static Random getRandom()
	{
		if(random == null)
			random = new Random();
		return random;
	}
}
