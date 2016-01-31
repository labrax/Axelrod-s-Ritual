package ggj2016.axelrodsritual.interfaces;

import ggj2016.axelrodsritual.Game;

/**
 * Abstract class for drawable, updatable and input objects
 * @author labrax
 *
 */
public abstract class IInputDrawableUpdatable extends IInputDrawable
{
	/**
	 * If the object will be updatable
	 */
	public boolean updates = false;
	
	/**
	 * When the object is created add to the updatables list
	 */
	public IInputDrawableUpdatable()
	{
		Game.getGame().addElement(this);
	}
	
	/**
	 * When the object ends remove it from the updatables list
	 */
	public void remove()
	{
		Game.getGame().removeElement(this);
		super.remove();
	}
	
	/**
	 * Calls the object updater
	 */
	public abstract void update();
}
