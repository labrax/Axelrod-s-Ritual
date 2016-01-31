package ggj2016.axelrodsritual;

/**
 * This class has the settings for the game
 * @author labrax
 *
 */
public class Config {
	/**
	 * The game title
	 */
	public static String TITLE = "Axelrod's Ritual";
	
	/**
	 * The font file for the game
	 */
	public static String FONT_FILE = "shablagoo.ttf";
	
	/**
	 * Size of an element
	 */
	public static int SIZE_ELEM = 20;
	
	/**
	 * Grid size 
	 */
	public static int GRID_X = 50;
	public static int GRID_Y = 30;
	
	/**
	 * Cultural traits information
	 */
	public static int AMOUNT_TRAITS = 3;
	public static int[] MAX_TRAIT = {2, 5, 4, 3, 4};
	public static String[] TRAITS_NAME =
		{
			"Dessert",
			//"Letter",
			"Mood",
			"Animal"//,
			//"Book Genre"
		};
	public static String[][] TRAITS_ITEMS =
		{
			{"Pie", "Tea"},
			//{"A", "B", "C", "D", "E"},
			{"Crazy Happy", "Happy", "Angry", "Very damn Angry"},
			{"Dog", "Cat", "Fish"}//,
			//{"Classics", "Sci-Fi", "Fantasy", "Self-help"}
		};
	
	/**
	 * Flash time for a modified element or failing spell
	 */
	public static float FLASH_TIME = 1;
	
	/**
	 * Movement speed for the player selection
	 */
	public static float MOVEMENTS_PER_SEC = 25;
	
	/**
	 * Time that a text scrolls above
	 */
	public static float CULTURE_HEADLINE_TIME = 10;
	
	/**
	 * Iteractions before the game
	 */
	public static int PRE_GAME_ITERATIONS = 500000;
	
	/**
	 * Screen information
	 */
	public static boolean FULLSCREEN = true;
	public static int SCREEN_WIDTH = 1280;
	public static int SCREEN_HEIGHT = 720;
	
	/**
	 * Iteractions per second per speed
	 */
	public static int[] INTERACTIONS_SEC = {0, 4, 10, 25, 50, 100, 1000, 5000, 10000, 100000};
	
	/**
	 * Sound setting
	 */
	public static boolean SOUND = false;
}
