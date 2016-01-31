package ggj2016.axelrodsritual;

import ggj2016.axelrodsritual.util.Resources;

/**
 * Grid element for the grid
 * @author labrax
 *
 */
public class GridElement {
	private int[] traits;
	private boolean unique_color = false;
	private boolean modified = true;
	private long modifiedTime = 0;
	private float[] color;
	
	private int flash = 0;
	/**
	 * Create a grid element with random traits
	 */
	public GridElement()
	{
		traits = new int[Config.AMOUNT_TRAITS];
		for(int i = 0; i < Config.AMOUNT_TRAITS; i++)
		{
			traits[i] = Resources.getRandom().nextInt(Config.MAX_TRAIT[i]);
		}
	}
	
	/**
	 * Create a grid element with given traits
	 * @param straits are the traits
	 */
	public GridElement(String straits)
	{
		traits = new int[Config.AMOUNT_TRAITS];
		for(int i = 0; i < Config.AMOUNT_TRAITS; i++)
		{
			traits[i] = Integer.parseInt("" + straits.charAt(i));
		}
	}
	
	/**
	 * Change an element property
	 * @param index is the property index
	 * @param value is the new value
	 */
	public void setProperty(int index, int value)
	{
		if(index >= Config.AMOUNT_TRAITS)
			System.err.println("index >= AMOUNT_TRAITS");
		if(value >= Config.MAX_TRAIT[index])
			System.err.println("value >= MAX_TRAIT[index]");
		
		modified = true;
		modifiedTime = System.currentTimeMillis();
		traits[index] = new Integer(value);
	}
	
	/**
	 * Return the calculated element color
	 * @return
	 */
	public float[] getColor()
	{
		if(unique_color || !modified)
			return color;
		float[][] vects = { 
				{1.0f, 0.0f, 0.0f},
				{0.0f, 1.0f, 0.0f},
				{0.0f, 0.0f, 1.0f},
				
				{0.2f, 0.63f, 0.1f},
				{0.05f, 0.3f, 0.9f},
				{0.6f, 0.01f, 0.2f},
				{0.3f, 0.4f, 0.3f},
				{0.5f, 0.2f, 0.4f} };
		
		float[] retColor = {0.0f, 0.0f, 0.0f, 1.0f};
		for(int i = 0; i < Config.AMOUNT_TRAITS; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				retColor[j] += vects[i][j] * ((traits[i]+1)/(float) Config.MAX_TRAIT[i]);
			}
		}
		color = retColor;
		return retColor;
	}
	
	/*private void debugPrint(float[] retColor)
	{
		for(int i = 0; i < Config.AMOUNT_TRAITS; i++)
		{
			System.out.print(traits[i] + " ");
		}
		System.out.println();
		for(int i = 0; i < 3; i++)
		{
			System.out.print(retColor[i] + " ");
		}
		System.out.println();
	}*/
	
	/**
	 * 
	 * @return the element traits
	 */
	public int[] getTraits()
	{
		return traits;
	}
	
	/**
	 * Check culture passage from other element
	 * @param other is the other element
	 */
	public void interact(GridElement other)
	{
		int amount_equal = 0;
		int[] other_traits = other.getTraits();
		for(int i = 0; i < Config.AMOUNT_TRAITS; i++)
		{
			if(other_traits[i] == traits[i])
				amount_equal++;
		}
		
		//nothing to be done
		if(amount_equal == Config.AMOUNT_TRAITS)
			return;
		
		if(Resources.getRandom().nextInt(100) < amount_equal*100/(Config.AMOUNT_TRAITS-1))
		{
			while(true)
			{
				int t = Resources.getRandom().nextInt(Config.AMOUNT_TRAITS);
				if(traits[t] != other_traits[t])
				{
					if(other_traits[t] > Config.MAX_TRAIT[t])
						System.err.println("other_traits[t] > Config.MAX_TRAIT[t]");
					traits[t] = other_traits[t];
					break;
				}
			}
			modified = true;
			modifiedTime = System.currentTimeMillis();
		}
	}
	
	/**
	 * Get the time the the element was modified
	 * @return the modified time
	 */
	public long getModifiedTime()
	{
		return modifiedTime;
	}
	
	/**
	 * Updates the flashing count for an element
	 */
	public void updateFlash()
	{
		long currTime = System.currentTimeMillis();
		if(flash > 0 && (currTime-modifiedTime) > Config.FLASH_TIME)
		{
			modified = true;
			modifiedTime = currTime;
			flash--;
		}
	}
	
	/**
	 * Insert flashing values
	 * @param val is the amount of times to flash
	 */
	public void flash(int val)
	{
		flash += val;
		modified = true;
		modifiedTime = System.currentTimeMillis();
	}
	
	/**
	 * Check if an element has traits equal to other
	 * @param otherTraits are the other traits
	 * @return true if equal, false otherwise
	 */
	public boolean isEqual(int[] otherTraits)
	{
		boolean equal = true;
		for(int i = 0; i < Config.AMOUNT_TRAITS; i++)
		{
			if(traits[i] != otherTraits[i])
			{
				equal = false;
				break;
			}
		}
		return equal;
	}
}
