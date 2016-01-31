package ggj2016.axelrodsritual;

import java.io.File;
import java.util.ArrayList;

import ggj2016.axelrodsritual.interfaces.IInputDrawableUpdatable;
import ggj2016.axelrodsritual.screens.Title;

/**
 * Main game class with updatable objects control
 * @author labrax
 *
 */
public class Game {
	private ArrayList<IInputDrawableUpdatable> updatables = new ArrayList<IInputDrawableUpdatable>();
	private ArrayList<IInputDrawableUpdatable> addCache = new ArrayList<IInputDrawableUpdatable>();
	private ArrayList<IInputDrawableUpdatable> removeCache = new ArrayList<IInputDrawableUpdatable>();
	private boolean updatablesRunning = false;
	private static Game instance = null;
	
	private Screen screen;
	private long timeSinceLastUpdate = 0;
	
	/**
	 * Setup required libraries
	 */
	public static void start_native_libraries()
	{
		if(System.getProperty("os.name").startsWith("Windows"))
			System.setProperty("org.lwjgl.librarypath", new File("lib/native/windows").getAbsolutePath());
		else if(System.getProperty("os.name").startsWith("Linux"))
			System.setProperty("org.lwjgl.librarypath", new File("lib/native/linux").getAbsolutePath());
		else if(System.getProperty("os.name").startsWith("Mac"))
			System.setProperty("org.lwjgl.librarypath", new File("lib/native/macosx").getAbsolutePath());
	}
	
	public static void main(String[] args) {
		start_native_libraries();
		
		Game g = Game.getGame();
		g.run();
	}
	
	private Game()
	{
		screen = Screen.getScreen();
		//start the game with Title class
		new Title();
	}
	
	/**
	 * Game is a singleton
	 * @return Game
	 */
	public static Game getGame()
	{
		if(instance == null)
			instance = new Game();
		return instance;
	}
	
	/**
	 * Add updatable object
	 * @param e is the object
	 */
	public void addElement(IInputDrawableUpdatable e)
	{
		if(updatablesRunning)
			addCache.add(e);
		else
			updatables.add(e);
	}
	
	/**
	 * Remove updatable object
	 * @param e is the object
	 */
	public void removeElement(IInputDrawableUpdatable e)
	{
		if(updatablesRunning)
			removeCache.add(e);
		else
			updatables.remove(e);
	}
	
	/**
	 * The game loop
	 */
	public void run()
	{
		boolean running = true;
		while(running)
		{
			InputHandler.getInputHandler().handle();
			
			long currTime = System.currentTimeMillis();
			if(currTime - timeSinceLastUpdate > 10)
			{
				updatablesRunning = true;
				for(IInputDrawableUpdatable u : updatables)
				{
					if(u.updates)
						u.update();
				}
				updatablesRunning = false;
			}
			for(IInputDrawableUpdatable u : addCache)
			{
				//System.out.println("adding: " + u);
				updatables.add(u);
			}
			for(IInputDrawableUpdatable u : removeCache)
			{
				//System.out.println("removing: " + u);
				updatables.remove(u);
			}
			addCache.clear();
			removeCache.clear();
			
			screen.draw();
			if(screen.isClosed())
				running = false;
		}
	}

}
