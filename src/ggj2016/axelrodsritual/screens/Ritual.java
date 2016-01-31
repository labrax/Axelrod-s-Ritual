package ggj2016.axelrodsritual.screens;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import ggj2016.axelrodsritual.Config;
import ggj2016.axelrodsritual.InputEvent;
import ggj2016.axelrodsritual.interfaces.IInputDrawableUpdatable;
import ggj2016.axelrodsritual.screens.Grid.INTERACTION_TYPE;
import ggj2016.axelrodsritual.util.Draw;
import ggj2016.axelrodsritual.util.Resources;

/**
 * Class for handling the ritual, sequences of key presses
 * @author labrax
 *
 */
public class Ritual extends IInputDrawableUpdatable
{
	private long startTime = 0;
	private long keyTime = 0;
	private int size = 0;
	
	private int amountOk = 0;
	private int amountFail = 0;
	private int amountTotal = 0;
	
	public TrueTypeFont font = null;
	public Color color = null;
	
	float[] barColor = {1.0f, 0.0f, 0.0f, 1.0f};
	
	private int posX, posY;
	private int randKey = 0;
	
	private String[] KEYS_MAPPING = {"UP", "DOWN", "LEFT", "RIGHT", "A", "B", "X", "Y"};
	private int[] KEYS_MAPPING_NUM = {Keyboard.KEY_UP, Keyboard.KEY_DOWN, Keyboard.KEY_LEFT, Keyboard.KEY_RIGHT, Keyboard.KEY_A, Keyboard.KEY_B, Keyboard.KEY_X, Keyboard.KEY_Y}; 
	
	private int AMOUNT_KEYS = KEYS_MAPPING.length;
	
	private boolean end = false;
	private boolean isNull = false;
	
	private Grid grid = null;
	private Grid.INTERACTION_TYPE type = null;
	private int firstOption = 0;
	private int secondOption = 0;
	
	private int minTime = 1000;
	
	/**
	 * Creates the Ritual
	 * @param g is the grid for return
	 * @param type is the interaction type for return
	 * @param firstOption is for return
	 * @param secondOption is for return
	 */
	public Ritual(Grid g, Grid.INTERACTION_TYPE type, int firstOption, int secondOption)
	{
		font = Resources.getFont(60f);
		color = Color.white;
		
		visible = true;
		updates = true;
		active = true;
		
		amountTotal = Resources.getRandom().nextInt(10) + 10;
		
		grid = g;
		this.type = type;
		this.firstOption = firstOption;
		this.secondOption = secondOption;
		
		nextKey();
		updatePos();
		
		if(type == null)
		{
			isNull = true;
			end = true;
		}
	}
	
	/**
	 * Updates the key position relative to its size
	 */
	public void updatePos()
	{
		this.posX = Config.SCREEN_WIDTH/2-font.getWidth(KEYS_MAPPING[randKey])/2;
		this.posY = Config.SCREEN_HEIGHT/2-font.getHeight()/2;
	}
	
	/**
	 * Selects the next key from the pool of keys
	 */
	public void nextKey()
	{
		if(amountOk + amountFail >= amountTotal)
		{
			end = true;
		}
		else
		{
			int previousKey = randKey;
			boolean ok = false;
			while(!ok)
			{
				randKey = Resources.getRandom().nextInt(AMOUNT_KEYS);
				if(randKey != previousKey)
					ok = true;
			}
			keyTime = Resources.getRandom().nextInt(1000)+minTime;
			startTime = System.currentTimeMillis();
			updatePos();
		}
	}

	public void update()
	{
		long currTime = System.currentTimeMillis();
		//System.err.println(currTime + " " + startTime + " " + keyTime + " " + Config.SCREEN_WIDTH);
		size = (int) Math.floor(Config.SCREEN_WIDTH - (currTime-startTime)/(float)keyTime*Config.SCREEN_WIDTH);
		if(size < 0)
		{
			minTime+=200;
			amountFail++;
			nextKey();
		}
		
		if(end)
		{
			if(isNull)
				grid.interact(type, firstOption, secondOption);
			
			if(amountOk < amountFail)
			{
				grid.interact(INTERACTION_TYPE.FAIL, firstOption, secondOption);
			}
			else
			{
				if(type != INTERACTION_TYPE.SPELL_CHANGETRAIT)
					grid.interact(type, amountOk, amountTotal);
				else
					grid.interact(type, firstOption, secondOption);
			}
			remove();
		}
	}

	public boolean testInput(InputEvent e)
	{
		if(e.key == KEYS_MAPPING_NUM[randKey])
		{
			amountOk++;
			minTime -= 100;
			nextKey();
		}
		else if(e.key == Keyboard.KEY_ESCAPE)
		{
			amountTotal = 0;
			amountOk = 0;
			amountFail = 1;
			end = true;
		}
		return true;
	}

	public void draw()
	{
		if(!isNull)
		{
			Draw.drawRectangle(0, Config.SCREEN_HEIGHT-2, (int) size, Config.SCREEN_HEIGHT, barColor);
			
			float[] colorBackground = {0f, 0f, 0f, 1f};
			Draw.drawRectangle(posX-2, posY, posX + font.getWidth(KEYS_MAPPING[randKey])+3, posY + font.getHeight(), colorBackground);
			GL11.glEnable(GL_TEXTURE_2D);
			font.drawString(posX, posY, KEYS_MAPPING[randKey], color);
			GL11.glDisable(GL_TEXTURE_2D);
		}
	}

}
