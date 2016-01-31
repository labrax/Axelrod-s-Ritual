package ggj2016.axelrodsritual.screens;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import ggj2016.axelrodsritual.Config;
import ggj2016.axelrodsritual.InputEvent;
import ggj2016.axelrodsritual.interfaces.IInputDrawable;
import ggj2016.axelrodsritual.util.Resources;

/**
 * Class for the start title
 * @author labrax
 *
 */
public class Title extends IInputDrawable
{
	private TrueTypeFont font;
	
	/**
	 * The initial Title creator
	 */
	public Title()
	{
		font = Resources.getFont(64f);
		
		visible = true;
		active = true;
		isText = true;
	}
	
	public void draw()
	{
		font.drawString(Config.SCREEN_WIDTH/2-font.getWidth(Config.TITLE)/2, Config.SCREEN_HEIGHT/2-font.getHeight()/2, Config.TITLE, Color.yellow);
	}

	public boolean testInput(InputEvent e) {
		if(e.type == InputEvent.TYPE.KEYBOARD)
		{
			visible = false;
			active = false;
			
			new Grid();
			remove();
		}
		return true;
	}
}
