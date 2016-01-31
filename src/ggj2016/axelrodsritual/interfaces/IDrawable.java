package ggj2016.axelrodsritual.interfaces;

import ggj2016.axelrodsritual.Screen;

/**
 * Abstract class for drawable objects
 * @author labrax
 *
 */
public abstract class IDrawable
{
	/**
	 * If the object will be draw
	 */
	public boolean visible = false;
	/**
	 * If the object is text
	 */
	public boolean isText = false;
	
	/**
	 * When the object is created add to the drawables list
	 */
	public IDrawable()
	{
		Screen.getScreen().addElement(this);
	}
	
	/**
	 * When the object ends remove it from the drawables list
	 */
	public void remove()
	{
		Screen.getScreen().removeElement(this);
	}
	
	/**
	 * Method called to draw
	 */
	public abstract void draw();
}
