package ggj2016.axelrodsritual.util;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import ggj2016.axelrodsritual.interfaces.IDrawable;

/**
 * This is a class for unifying text-only objects
 * @author labrax
 *
 */
public class Text extends IDrawable {
	public TrueTypeFont font = null;
	public String text = "";
	public Color color = Color.black;
	public int x, y;
	
	/**
	 * Creates the text object
	 * @param font
	 * @param color
	 */
	public Text(TrueTypeFont font, Color color)
	{
		this.font = font;
		this.color = color;
		isText = true;
		x = 0;
		y = 0;
	}

	/**
	 * Draw the text on the screen
	 */
	public void draw()
	{
		font.drawString(x, y, text, color);
	}
	
	/**
	 * Sets the object position of its top-right corner
	 * @param x
	 * @param y
	 */
	public void setPos(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}
