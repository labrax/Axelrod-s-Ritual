package ggj2016.axelrodsritual;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import java.util.Stack;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;

import ggj2016.axelrodsritual.interfaces.IDrawable;

/**
 * Class to load display and handle drawable objects
 * @author labrax
 *
 */
public class Screen {
	private Stack<IDrawable> elements = new Stack<IDrawable>();
	private static Screen instance = null;
	
	TrueTypeFont font;
	
	/**
	 * Screen is a singleton
	 * @return Screen
	 */
	public static Screen getScreen()
	{
		if(instance == null)
			instance = new Screen();
		return instance;
	}
	
	/**
	 * Initializes the Screen with Config's settings
	 */
	private Screen()
	{
	    try
	    {
	        Display.setTitle(Config.TITLE);
	        
	        if(Config.FULLSCREEN)
	        	Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
	        else
	        	Display.setDisplayMode(new DisplayMode(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT));
	        
	        Config.SCREEN_WIDTH = Display.getDisplayMode().getWidth();
	        Config.SCREEN_HEIGHT = Display.getDisplayMode().getHeight();
	        
	        Display.create();
	    }
	    catch(LWJGLException e)
	    {
	        e.printStackTrace();
	        System.exit(-1);
	    }
	    
	    Display.setVSyncEnabled(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);        
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);                    
  
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);                
        GL11.glClearDepth(1);
  
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
  
        GL11.glViewport(0, 0, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
  
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public void close()
	{
		Display.destroy();
	}
	
	/**
	 * Checks if the window is closed
	 * @return true if closed
	 */
	public boolean isClosed()
	{
		return Display.isCloseRequested();
	}
	
	/**
	 * Add drawable object
	 * @param i is the object
	 */
	public void addElement(IDrawable i)
	{
		elements.add(i);
	}
	
	/**
	 * Remove drawable object
	 * @param i is the object
	 */
	public void removeElement(IDrawable i)
	{
		elements.remove(i);
	}
	
	/**
	 * Draw every object that is visible
	 */
	public void draw()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		for(IDrawable i : elements)
		{
			if(i.visible == true)
			{
				if(i.isText == false)
					GL11.glDisable(GL_TEXTURE_2D);
				i.draw();
				if(i.isText == false)
					GL11.glEnable(GL_TEXTURE_2D);
			}
		}
		
	    
        Display.update();
        Display.sync(60);
	}
}
