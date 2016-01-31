package ggj2016.axelrodsritual;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.TrueTypeFont;

import ggj2016.axelrodsritual.InputEvent.TYPE;
import ggj2016.axelrodsritual.interfaces.IInputDrawable;

/**
 * Class to get inputs from lwjgl and pass to elements
 * @author labrax
 *
 */
public class InputHandler {
	private ArrayList<IInputDrawable> elements = new ArrayList<IInputDrawable>();
	
	private static InputHandler instance = null;
	
	TrueTypeFont font;
	
	/**
	 * InputHandler is a singleton
	 * @return InputHandler
	 */
	public static InputHandler getInputHandler()
	{
		if(instance == null)
			instance = new InputHandler();
		return instance;
	}
	
	private InputHandler()
	{
		
	}
	
	/**
	 * Insert an element to be checked for inputs
	 * @param i
	 */
	public void addElement(IInputDrawable i)
	{
		//System.out.println("adding: " + i);
		elements.add(i);
	}
	
	/**
	 * Remove an element from input checkables
	 * @param i
	 */
	public void removeElement(IInputDrawable i)
	{
		//System.out.println("removing: " + i);
		elements.remove(i);
	}
	
	/**
	 * Retrieve inputs from lwjgl and pass to elements
	 */
	public void handle()
	{
		while(Keyboard.next())
		{
			boolean pressed = Keyboard.getEventKeyState();
			int key = Keyboard.getEventKey();
			InputEvent e = new InputEvent(TYPE.KEYBOARD, pressed, key, 0, 0);
			
			for(int j = elements.size()-1; j >= 0; j--)
			{
				IInputDrawable i = elements.get(j);
				//System.out.println("testing: " + i);
				if(i.active == true)
					if(i.testInput(e) == true)
						break;
			}
		}
		
		while(Mouse.next())
		{
			boolean pressed = Mouse.getEventButtonState();
			int key = Mouse.getEventButton();
			
			InputEvent e = new InputEvent(TYPE.MOUSE, pressed, key, Mouse.getX(), Mouse.getY());
			for(IInputDrawable i : elements)
			{
				if(i.active == true)
					if(i.testInput(e) == true)
						break;
			}
		}
	}
}
