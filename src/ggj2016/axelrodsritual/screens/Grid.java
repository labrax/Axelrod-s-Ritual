package ggj2016.axelrodsritual.screens;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import ggj2016.axelrodsritual.Config;
import ggj2016.axelrodsritual.GridElement;
import ggj2016.axelrodsritual.InputEvent;
import ggj2016.axelrodsritual.interfaces.IInputDrawableUpdatable;
import ggj2016.axelrodsritual.util.Draw;
import ggj2016.axelrodsritual.util.Resources;
import ggj2016.axelrodsritual.util.Text;

/**
 * This class controls the grid and information for the game
 * @author labrax
 *
 */
public class Grid extends IInputDrawableUpdatable{
	private Integer sizeX, sizeY;
	private Integer refX, refY;
	
	private Integer playerX, playerY;
	private boolean keyUp = false, keyDown = false, keyLeft = false, keyRight = false;
	private long lastUp = 0, lastDown = 0, lastLeft = 0, lastRight = 0;
	private float accUp = 0, accDown = 0, accLeft = 0, accRight = 0;
	
	private GridElement[][] grid;
	private int speed = 0;
	private int previousSpeed = 0;
	private Integer iteration = 0;
	
	private long lastUpdate = 0;
	private double accumulated_update = 0.0f;
	
	private Text iterationText = null;
	private Text speedText = null;
	private Text cultureText = null;
	private boolean cultureTextDone = true;
	private int cultureTextRef = 0;
	private long cultureTextStart = 0;
	
	private boolean insertInteractionScreen = false;
	private boolean insertedInteractionScreen = false;
	public enum INTERACTION_TYPE {FAIL, FLASH, JUST_TYPE, SPELL_CULTURALISM, SPELL_RANDOMIZER, SPELL_CHANGETRAIT, SPELL_MRSOCIAL};
	
	private int culturalism = 0;
	private int[] culturalismTraits = new int[Config.AMOUNT_TRAITS];
	
	private int mrsocial = 0;
	private int mrX = 0, mrY = 0;
	
	private boolean background = false;
	private boolean feedback = false;
	private String feedbackString = "";
	
	private HashMap<String, Integer> culturesRanking = new HashMap<String, Integer>();
	int lastCultureIteration = 0;
	
	/**
	 * Create the grid with Config class settings
	 */
	protected Grid()
	{
		this.sizeX = Config.GRID_X;
		this.sizeY = Config.GRID_Y;
		
		this.playerX = sizeX/2;
		this.playerY = sizeY/2;
		
		grid = new GridElement[sizeY][];
		for(int i = 0; i < sizeY; i++)
		{
			grid[i] = new GridElement[sizeX];
			for(int j = 0; j < sizeX; j++)
			{
				grid[i][j] = new GridElement();
			}
		}
		
		for(int i = 0; i < Config.PRE_GAME_ITERATIONS; i++)
		{
			runIteration();
			iteration = 0;
		}
		
		visible = true;
		active = true;
		updates = true;
		cultureText = new Text(Resources.getFont(24f), Color.white);
		speedText = new Text(Resources.getFont(40f), Color.blue);
		iterationText = new Text(Resources.getFont(24f), Color.yellow);
	}

	public void draw()
	{
		refX = -sizeX*Config.SIZE_ELEM/2+Config.SCREEN_WIDTH/2;
		refY = -sizeY*Config.SIZE_ELEM/2+Config.SCREEN_HEIGHT/2;
		
		long currTime = System.currentTimeMillis();
		for(int i = 0; i < sizeY; i++)
		{
			for(int j = 0; j < sizeX; j++)
			{
				float[] color = grid[i][j].getColor();
				Draw.drawRectangle(j*Config.SIZE_ELEM + refX, i*Config.SIZE_ELEM + refY, (j+1)*Config.SIZE_ELEM + refX, (i+1)*Config.SIZE_ELEM + refY, color);
				
				//modification flash on spot
				grid[i][j].updateFlash();
				long modifiedTime = grid[i][j].getModifiedTime();
				if(currTime - modifiedTime < Config.FLASH_TIME)
				{
					float[] flashColor = {1.0f, 1.0f, 1.0f, 0.3f - 0.3f*(currTime - modifiedTime)/Config.FLASH_TIME};
					Draw.drawRectangle(j*Config.SIZE_ELEM + refX, i*Config.SIZE_ELEM + refY, (j+1)*Config.SIZE_ELEM + refX, (i+1)*Config.SIZE_ELEM + refY, flashColor);
				}
				
				//player position
				if(!background)
				{
					if(playerY == i && playerX == j)
					{
						float[] playerColor = {0.0f, 0.0f, 0.0f, 0.8f};
						Draw.drawRectangle(j*Config.SIZE_ELEM + refX, i*Config.SIZE_ELEM + refY, (j+1)*Config.SIZE_ELEM + refX, (i+1)*Config.SIZE_ELEM + refY, playerColor);
					}
				}
			}
		}
		
		iterationText.text = "" + "iteration: " + iteration;
		if(iterationText.visible == false)
			iterationText.setPos(Config.SCREEN_WIDTH - iterationText.font.getWidth(iterationText.text), Config.SCREEN_HEIGHT-iterationText.font.getHeight(iterationText.text));
		else if(iterationText.x > Config.SCREEN_WIDTH - iterationText.font.getWidth(iterationText.text))
			iterationText.setPos(Config.SCREEN_WIDTH - iterationText.font.getWidth(iterationText.text), Config.SCREEN_HEIGHT-iterationText.font.getHeight(iterationText.text));
		iterationText.visible = true;
		
		updateCultureText();
		
		speedText.text = "";
		if(speed == 0)
			speedText.text = "||";
		for(int i = 0; i < speed && i < 3; i++)
			speedText.text += ">";
		if(speed >= 4)
			speedText.text += " x" + (speed);
		speedText.setPos(0, 0);
		speedText.visible = true;
	}

	/**
	 * Put a new text on the upper part of the screen if the other is done.
	 */
	private void updateCultureText()
	{
		if(cultureTextDone == true)
		{
			if(feedback)
			{
				cultureText.color = new Color(grid[playerY][playerX].getColor()[0], grid[playerY][playerX].getColor()[1], grid[playerY][playerX].getColor()[2], grid[playerY][playerX].getColor()[3]);
				cultureText.text = feedbackString;
				cultureTextDone = false;
				cultureTextStart = System.currentTimeMillis();
				feedback = false;
			}
			else if(culturesRanking.size() > 0 && culturesRanking.size() < 20)
			{
				int topAmount = 0;
				String topCulture = "";
				for(Map.Entry<String, Integer> i : culturesRanking.entrySet())
				{
					if(i.getValue() > topAmount)
					{
						topAmount = i.getValue();
						topCulture = i.getKey();
					}
				}
				
				cultureText.text = "The current top culture has " + topAmount  + " individuals! They like ";
				for(int i = 0; i < Config.AMOUNT_TRAITS; i++)
				{
					//TODO: fix bug on the line bellow
					cultureText.text += Config.TRAITS_ITEMS[i][Integer.parseInt("" + topCulture.charAt(i))] + " as ";
					cultureText.text += Config.TRAITS_NAME[i] + "; "; 
				}
				GridElement ge = new GridElement(topCulture);
				float[] color = ge.getColor();
				cultureText.color = new Color(color[0], color[1], color[2], color[3]);
				
				cultureTextStart = System.currentTimeMillis();
				cultureTextDone = false;
				//System.out.println(cultureText.text);
			}
		}
		else if(cultureTextDone == false)
		{
			long currTime = System.currentTimeMillis();
			cultureTextRef =  (int) (Config.SCREEN_WIDTH -(cultureText.font.getWidth(cultureText.text) + Config.SCREEN_WIDTH) * (currTime - cultureTextStart)/1000.0f / Config.CULTURE_HEADLINE_TIME);
			
			if(cultureTextRef <= (int) (-cultureText.font.getWidth(cultureText.text)) )
			{
				cultureTextDone = true;
				cultureText.visible = false;
			}
			
			cultureText.setPos(cultureTextRef, 0);
			cultureText.visible = true;
		}
	}
	
	public boolean testInput(InputEvent e)
	{
		int newSpeed = speed;
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
		else if(!e.pressed)
		{
			switch(e.key)
			{
				case Keyboard.KEY_APOSTROPHE:
				case Keyboard.KEY_GRAVE:
				case Keyboard.KEY_0:
					newSpeed = 0;
					break;
				case Keyboard.KEY_1:
					newSpeed = 1;
					break;
				case Keyboard.KEY_2:
					newSpeed = 2;
					break;
				case Keyboard.KEY_3:
					newSpeed = 3;
					break;
				case Keyboard.KEY_4:
					newSpeed = 4;
					break;
				case Keyboard.KEY_5:
					newSpeed = 5;
					break;
				case Keyboard.KEY_6:
					newSpeed = 6;
					break;
				case Keyboard.KEY_7:
					newSpeed = 7;
					break;
				case Keyboard.KEY_8:
					newSpeed = 8;
					break;
				case Keyboard.KEY_9:
					newSpeed = 9;
					break;
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
					if(!insertedInteractionScreen)
						insertInteractionScreen = true;
					return true;
				default:
					break;
			}
		}
		
		if(newSpeed != speed && previousSpeed == 0)
		{
			speed = newSpeed;
			return true;
		}
		return false;
	}
	
	public void update()
	{
		if(lastUpdate == 0)
			lastUpdate = System.currentTimeMillis();
		
		if(lastCultureIteration != iteration)
		{
			countCultures();
		}
		
		long currTime = System.currentTimeMillis();
		if(speed > 0 && !(culturesRanking.size() == 1 && lastCultureIteration > 0))
		{
			double updates = (currTime - lastUpdate)*(float)Config.INTERACTIONS_SEC[speed]/1000f + accumulated_update;
			
			accumulated_update = updates-Math.floor(updates);
			
			for(int i = 0; i < Math.floor(updates); i++)
				runIteration();
		}
		lastUpdate = currTime;
		
		if(lastUp == 0)
			lastUp = currTime;
		else if(keyUp)
		{
			float movementsDone = (currTime-lastUp)/1000.0f*Config.MOVEMENTS_PER_SEC + accUp;
			int movementsPossible = (int) ((currTime-lastUp)/1000.0f*Config.MOVEMENTS_PER_SEC + accUp); 
			accUp = movementsDone - movementsPossible;
			playerY -= movementsPossible;
			
			if(playerY < 0)
				playerY = 0;
			lastUp = currTime;
		}
			
		if(lastDown == 0)
			lastDown = currTime;
		else if(keyDown)
		{
			float movementsDone = (currTime-lastDown)/1000.0f*Config.MOVEMENTS_PER_SEC + accDown;
			int movementsPossible = (int) ((currTime-lastDown)/1000.0f*Config.MOVEMENTS_PER_SEC + accDown); 
			accDown = movementsDone - movementsPossible;
			playerY += movementsPossible;
			
			if(playerY > sizeY-1)
				playerY = sizeY-1;
			lastDown = currTime;
		}
		
		if(lastLeft == 0)
			lastLeft = currTime;
		else if(keyLeft)
		{
			float movementsDone = (currTime-lastLeft)/1000.0f*Config.MOVEMENTS_PER_SEC + accLeft;
			int movementsPossible = (int) ((currTime-lastLeft)/1000.0f*Config.MOVEMENTS_PER_SEC + accLeft); 
			accLeft = movementsDone - movementsPossible;
			playerX -= movementsPossible;
			
			if(playerX < 0)
				playerX = 0;
			lastLeft = currTime;
		}
		
		if(lastRight == 0)
			lastRight = currTime;
		else if(keyRight)
		{
			float movementsDone = (currTime-lastRight)/1000.0f*Config.MOVEMENTS_PER_SEC + accRight;
			int movementsPossible = (int) ((currTime-lastRight)/1000.0f*Config.MOVEMENTS_PER_SEC + accRight); 
			accRight = movementsDone - movementsPossible;
			playerX += movementsPossible;
			
			if(playerX > sizeX-1)
				playerX = sizeX-1;
			lastRight = currTime;
		}
		
		if(insertInteractionScreen == true)
		{
			float[] color = grid[playerY][playerX].getColor();
			
			background = true;
			new InteractionScreen(Resources.getFont(24f), new Color(color[0], color[1], color[2], color[3]), this, playerX*Config.SIZE_ELEM + refX, playerY*Config.SIZE_ELEM + refY, (playerX+1)*Config.SIZE_ELEM + refX, (playerY+1)*Config.SIZE_ELEM + refY);
			
			keyUp = false;
			accUp = 0;
			keyDown = false;
			accDown = 0;
			keyLeft = false;
			accLeft = 0;
			keyRight = false;
			accRight = 0;
			
			previousSpeed = speed;
			speed = 0;
			
			insertInteractionScreen = false;
			insertedInteractionScreen = true;
		}
	}
	
	/**
	 * Receives interaction from Ritual class when the sequence is over.
	 * @param type is the message
	 * @param i is the amount of rightly pressed keys
	 * @param j is the total amount of keys
	 */
	public void interact(INTERACTION_TYPE type, int i, int j)
	{
		background = false;
		if(type != null)
		{
			feedback = true;
			feedbackString = "Result of action is " + type + " with " + i + "/" + j + " right.";
		}
		insertedInteractionScreen = false;
		speed = previousSpeed;
		previousSpeed = 0;
		
		//System.out.println(type + " " + i + " " + j);
		if(type != null)
			switch(type)
			{
				case FAIL:
					for(int y = 0; y < sizeY; y++)
					{
						for(int x = 0; x < sizeX; x++)
						{
							grid[y][x].flash(3);
						}
					}
				break;
				case FLASH:
					grid[playerY][playerX].flash(i);
					break;
				case SPELL_CHANGETRAIT:
					int[] traits = new int[Config.AMOUNT_TRAITS];
					for(int k = 0; k < Config.AMOUNT_TRAITS; k++)
					{
						traits[k] = grid[playerY][playerX].getTraits()[k];
					}
					for(int y = 0; y < sizeY; y++)
					{
						for(int x = 0; x < sizeX; x++)
						{
							boolean ok = true; 
							int[] otherTraits = grid[y][x].getTraits(); 
							for(int k = 0; k < Config.AMOUNT_TRAITS; k++)
							{
								if(traits[k] != otherTraits[k])
								{
									ok = false;
									break;
								}
							}
							if(ok == true)
								grid[y][x].setProperty(i, j);
						}
					}
					break;
				case SPELL_CULTURALISM:
					culturalism += i*1000;
					for(int k = 0; k < Config.AMOUNT_TRAITS; k++)
					{
						culturalismTraits[k] = grid[playerY][playerX].getTraits()[k];
					}
					break;
				case SPELL_RANDOMIZER:
					int[] thisTraits = new int[Config.AMOUNT_TRAITS];
					int[] newTraits = new int[Config.AMOUNT_TRAITS];
					for(int k = 0; k < Config.AMOUNT_TRAITS; k++)
					{
						thisTraits[k] = grid[playerY][playerX].getTraits()[k];
						newTraits[k] = Resources.getRandom().nextInt(Config.MAX_TRAIT[k]);
					}
					
					for(int y = 0; y < sizeY; y++)
					{
						for(int x = 0; x < sizeX; x++)
						{
							boolean ok = true; 
							int[] otherTraits = grid[y][x].getTraits(); 
							for(int k = 0; k < Config.AMOUNT_TRAITS; k++)
							{
								if(thisTraits[k] != otherTraits[k])
								{
									ok = false;
									break;
								}
							}
							if(ok == true)
							{
								for(int k = 0; k < Config.AMOUNT_TRAITS; k++)
									grid[y][x].setProperty(k, newTraits[k]);
							}
						}
					}
					break;
				case SPELL_MRSOCIAL:
					mrsocial += i*10;
					mrX = playerX;
					mrY = playerY;
					break;
				case JUST_TYPE:
					break;
				default:
					break;
			}
	}
	
	/**
	 * Count the amount of individuals for each culture on the grid
	 */
	public void countCultures()
	{
		culturesRanking.clear();
		
		for(int i = 0; i < sizeY; i++)
		{
			for(int j = 0; j < sizeX; j++)
			{
				String key = new String("");
				for(int k = 0; k < Config.AMOUNT_TRAITS; k++)
				{
					key += grid[i][j].getTraits()[k];
				}
				if(culturesRanking.containsKey(key))
					culturesRanking.put(key, culturesRanking.get(key)+1);
				else
					culturesRanking.put(key, 1);
			}
		}
		
		lastCultureIteration = iteration;
		//System.out.println("Amount cultures = " + culturesRanking.size());
	}
	
	/**
	 * Run one iteration from Axelrod's model
	 */
	public void runIteration()
	{
		int e_x = Resources.getRandom().nextInt(sizeX);
		int e_y = Resources.getRandom().nextInt(sizeY);
		
		int n_x = -1, n_y = -1;
		
		boolean found = false;
		while(!found)
		{
			int direction = Resources.getRandom().nextInt(4);
			switch(direction)
			{
				case 0:
					n_x = e_x-1;
					n_y = e_y;
					break;
				case 1:
					n_x = e_x+1;
					n_y = e_y;
					break;
				case 2:
					n_x = e_x;
					n_y = e_y-1;
					break;
				case 3:
					n_x = e_x;
					n_y = e_y+1;
					break;
				default:
					break;
			}
			if(n_x >= 0 && n_x < sizeX && n_y >= 0 && n_y < sizeY)
				found = true;
		}
		
		if(mrsocial > 0)
		{
			n_x = mrX;
			n_y = mrY;
			mrsocial--;
			if(mrsocial == 0)
			{
				feedback = true;
				feedbackString = "Action SPELL_MRSOCIAL is over.";
			}
		}
		
		if(culturalism < 0)
			culturalism = 0;
		
		if(culturalism == 0)
		{
			grid[e_y][e_x].interact(grid[n_y][n_x]);
		}
		else if(culturalism > 0)
		{
			//System.out.println(culturalism);
			if(!grid[e_y][e_x].isEqual(culturalismTraits))
				grid[e_y][e_x].interact(grid[n_y][n_x]);
			culturalism--;
			
			if(culturalism == 0)
			{
				feedback = true;
				feedbackString = "Action SPELL_CULTURALISM is over.";
			}
		}
			
		
		iteration++;
	}
}
