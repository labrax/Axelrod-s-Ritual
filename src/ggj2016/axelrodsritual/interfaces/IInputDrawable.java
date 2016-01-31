package ggj2016.axelrodsritual.interfaces;

import ggj2016.axelrodsritual.InputEvent;
import ggj2016.axelrodsritual.InputHandler;

/**
 * Abstract class for drawable and input objects 
 * @author labrax
 *
 */
public abstract class IInputDrawable extends IDrawable
{
	/**
	 * If the object will receive input
	 */
	public boolean active = false;
	
	/**
	 * When the object is created add to the input handler list
	 */
	public IInputDrawable()
	{
		InputHandler.getInputHandler().addElement(this);
	}
	
	/**
	 * When the object ends remove it from the input handler list
	 */
	public void remove()
	{
		InputHandler.getInputHandler().removeElement(this);
		super.remove();
	}
	
	/**
	 * Check if the object accepts the input
	 * @param e is the input
	 * @return true if accepted, false otherwise
	 */
	public abstract boolean testInput(InputEvent e);
}
