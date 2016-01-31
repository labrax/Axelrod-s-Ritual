package ggj2016.axelrodsritual;

/**
 * Class that passes the input
 * @author labrax
 *
 */
public class InputEvent {
	public enum TYPE { MOUSE, KEYBOARD };
	public TYPE type;
	public boolean pressed;
	public int key;
	public int x;
	public int y;
	
	/**
	 * Create an input event with settings
	 * @param type mouse or keyboard
	 * @param pressed true or false
	 * @param key is the number
	 * @param x is the mouse x location (if mouse)
	 * @param y is the mouse y location (if mouse)
	 */
	public InputEvent(TYPE type, boolean pressed, int key, int x, int y)
	{
		this.type = type;
		this.pressed = pressed;
		this.key = key;
		this.x = x;
		this.y = y;
	}
	
	public String toString()
	{
		return "" + type + " " + pressed + " " + key + " (" + x + "," + y + ")";
	}
}
