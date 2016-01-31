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

/**
 * This class handles the box for the options on a grid element 
 * @author labrax
 *
 */
public class InteractionScreen extends IInputDrawableUpdatable
{
	public TrueTypeFont font = null;
	public String text = "";
	public Color color = Color.black;
	public int x, y;
	
	private int playerX1, playerY1, playerX2, playerY2;
	private Grid grid = null;
	
	private int selectedX = 0, selectedY = 0;
	
	private boolean keyUp = false, keyDown = false, keyLeft = false, keyRight = false;
	private long lastUp = 0, lastDown = 0, lastLeft = 0, lastRight = 0;
	private float accUp = 0, accDown = 0, accLeft = 0, accRight = 0;
	
	private boolean willRemove = false;
	
	private String[] OPTIONS =
		{
				"Flash",
				"Cast Trait changer",
				"Cast Randomize",
				"Cast Culturalism",
				"Cast Mr. Social",
				"Free dance",
				"Close this window"
		};
	
	private String[] currString = OPTIONS;
	private String[] currStringHorizontal = OPTIONS;

	private INTERACTION_TYPE type = null;
	private int firstOption = 0;
	private int secondOption = 0;
	
	/**
	 * Create an interaction screen with settings
	 * @param font is the font to be used
	 * @param color is the selected element color (the selected tile is used)
	 * @param g is the grid for return
	 * @param playerX1 is the top left corner x position
	 * @param playerY1 is the top left corner y position
	 * @param playerX2 is the bottom right corner x position
	 * @param playerY2 is the bottom right corner y position
	 */
	public InteractionScreen(TrueTypeFont font, Color color, Grid g, int playerX1, int playerY1, int playerX2, int playerY2)
	{
		this.font = font;
		this.color = color;
		isText = false;
		x = 0;
		y = 0;
		
		this.playerX1 = playerX1;
		this.playerX2 = playerX2;
		this.playerY1 = playerY1;
		this.playerY2 = playerY2;
		
		this.grid = g;
		
		active = true;
		visible = true;
		updates = true;
	}

	public boolean testInput(InputEvent e)
	{
		if(e.pressed)
		{
			switch(e.key)
			{
				case Keyboard.KEY_UP:
					keyUp = true;
					lastUp = System.currentTimeMillis();
					return true;
				case Keyboard.KEY_DOWN:
					keyDown = true;
					lastDown = System.currentTimeMillis();
					return true;
				case Keyboard.KEY_LEFT:
					keyLeft = true;
					lastLeft = System.currentTimeMillis();
					return true;
				case Keyboard.KEY_RIGHT:
					keyRight = true;
					lastRight = System.currentTimeMillis();
					return true;
				default:
					break;
			}
		}
		else
		{
			switch(e.key)
			{
				case Keyboard.KEY_UP:
					keyUp = false;
					accUp = 0;
					return true;
				case Keyboard.KEY_DOWN:
					keyDown = false;
					accDown = 0;
					return true;
				case Keyboard.KEY_LEFT:
					keyLeft = false;
					accLeft = 0;
					return true;
				case Keyboard.KEY_RIGHT:
					keyRight = false;
					accRight = 0;
					return true;
				case Keyboard.KEY_SPACE:
					interact();
					return true;
				case Keyboard.KEY_ESCAPE:
					type = null;
					willRemove = true;
					return true;
				default:
					break;
			}
		}
		return false;
	}
	
	/**
	 * When space is pressed interact will be called to pass the result of InteractionScreen
	 */
	public void interact()
	{
		selectedY = selectedY%currString.length;
		if(currString == OPTIONS)
		{
			switch(selectedY)
			{
				case 0:
					type = INTERACTION_TYPE.FLASH;
					willRemove = true;
					break;
				case 1:
					type = INTERACTION_TYPE.SPELL_CHANGETRAIT;
					currString = Config.TRAITS_NAME;
					selectedX = 0;
					selectedY = 0;
					break;
				case 2:
					type = INTERACTION_TYPE.SPELL_RANDOMIZER;
					willRemove = true;
					break;
				case 3:
					type = INTERACTION_TYPE.SPELL_CULTURALISM;
					willRemove = true;
					break;
				case 4:
					type = INTERACTION_TYPE.SPELL_MRSOCIAL;
					willRemove = true;
					break;
				case 5:
					type = INTERACTION_TYPE.JUST_TYPE;
					willRemove = true;
					break;
				default:
					type = null;
					willRemove = true;
					break;
			}
		}
		else if(currString == Config.TRAITS_NAME)
		{
			firstOption = selectedY;
			selectedY = 0;
			currString = Config.TRAITS_ITEMS[firstOption];
		}
		else
		{
			secondOption = selectedY;
			willRemove = true;
		}
	}
	
	/**
	 * Draw the proper boxes and text from InteractionScreen
	 */
	public void draw()
	{
		//preparations
		int textHeight = font.getHeight();
		int biggestWidth = 0;
		for(int i = 0; i < currString.length; i++)
		{
			if(font.getWidth(currString[i])+5 > biggestWidth)
				biggestWidth = font.getWidth(currString[i])+5;
		}
		
		//get a good position
		if(playerX2+biggestWidth < Config.SCREEN_HEIGHT && playerY2+currString.length*textHeight < Config.SCREEN_HEIGHT)
		{
			x = playerX2;
			y = playerY2;
		}
		else if(playerX2+biggestWidth < Config.SCREEN_WIDTH && playerY1-currString.length*textHeight > 0)
		{
			x = playerX2;
			y = playerY1-currString.length*textHeight;
		}
		else if(playerX1-biggestWidth > 0 && playerY1-currString.length*textHeight > 0)
		{
			x = playerX1-biggestWidth;
			y = playerY1-currString.length*textHeight;
		}
		else if(playerX1-biggestWidth > 0 && playerY2+currString.length*textHeight < Config.SCREEN_HEIGHT)
		{
			x = playerX1-biggestWidth;
			y = playerY2;
		}
		
		
		//draw background
		for(int i = 0; i < currString.length; i++)
		{
			if(selectedY%currString.length == i)
			{
				float[] color = {0.5f, 0.5f, 0.5f, 0.5f};
				Draw.drawRectangle(x, y + i*textHeight, x + biggestWidth, y + (i+1)*textHeight, color);
				float[] color2 = {0.4f, 0.4f, 0.4f, 0.8f};
				Draw.drawRectangle(x + 1, y + i*textHeight + 1, x + biggestWidth - 1, y + (i+1)*textHeight - 1, color2);
			}
			else
			{
				float[] color = {1.0f, 1.0f, 1.0f, 0.5f};
				Draw.drawRectangle(x, y + i*textHeight, x + biggestWidth, y + (i+1)*textHeight, color);
				float[] color2 = {1.0f, 1.0f, 1.0f, 0.5f};
				Draw.drawRectangle(x + 1, y + i*textHeight + 1, x + biggestWidth - 1, y + (i+1)*textHeight - 1, color2);
				
			}
		}
		
		//draw text
		GL11.glEnable(GL_TEXTURE_2D);
		for(int i = 0; i < currString.length; i++)
		{
			if(selectedY%currString.length == i)
			{
				font.drawString(x+2, y + i*textHeight, currString[i], this.color);
			}
			else
			{
				font.drawString(x+2, y + i*textHeight, currString[i], Color.black);
			}
		}
		GL11.glDisable(GL_TEXTURE_2D);
	}

	/**
	 * Updates the movement and if the InteractionScreen is over
	 */
	public void update()
	{
		long currTime = System.currentTimeMillis();
		
		if(lastUp == 0)
			lastUp = currTime;
		else if(keyUp)
		{
			float movementsDone = (currTime-lastUp)/1000.0f*Config.MOVEMENTS_PER_SEC + accUp;
			int movementsPossible = (int) ((currTime-lastUp)/1000.0f*Config.MOVEMENTS_PER_SEC + accUp); 
			accUp = movementsDone - movementsPossible;
			selectedY -= movementsPossible;
			
			if(selectedY < 0)
				selectedY = currString.length-1;
			lastUp = currTime;
		}
			
		if(lastDown == 0)
			lastDown = currTime;
		else if(keyDown)
		{
			float movementsDone = (currTime-lastDown)/1000.0f*Config.MOVEMENTS_PER_SEC + accDown;
			int movementsPossible = (int) ((currTime-lastDown)/1000.0f*Config.MOVEMENTS_PER_SEC + accDown); 
			accDown = movementsDone - movementsPossible;
			selectedY += movementsPossible;
			
			/*if(selectedY > OPTIONS.length-1)
				selectedY = OPTIONS.length-1;*/
			lastDown = currTime;
		}
		
		if(lastLeft == 0)
			lastLeft = currTime;
		else if(keyLeft)
		{
			float movementsDone = (currTime-lastLeft)/1000.0f*Config.MOVEMENTS_PER_SEC + accLeft;
			int movementsPossible = (int) ((currTime-lastLeft)/1000.0f*Config.MOVEMENTS_PER_SEC + accLeft); 
			accLeft = movementsDone - movementsPossible;
			selectedX -= movementsPossible;
			
			if(selectedX < 0)
				selectedX = currStringHorizontal.length-1;
			lastLeft = currTime;
		}
		
		if(lastRight == 0)
			lastRight = currTime;
		else if(keyRight)
		{
			float movementsDone = (currTime-lastRight)/1000.0f*Config.MOVEMENTS_PER_SEC + accRight;
			int movementsPossible = (int) ((currTime-lastRight)/1000.0f*Config.MOVEMENTS_PER_SEC + accRight); 
			accRight = movementsDone - movementsPossible;
			selectedX += movementsPossible;
			
			/*if(selectedX > maxX-1)
				selectedX = maxX-1;*/
			lastRight = currTime;
		}
		
		if(willRemove)
		{
			new Ritual(grid, type, firstOption, secondOption);
			remove();
		}
	}

}
