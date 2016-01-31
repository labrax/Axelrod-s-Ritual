package ggj2016.axelrodsritual.util;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

/**
 * Class for drawing primitives
 * @author labrax
 *
 */
public class Draw
{
	/**
	 * Draw a rectangle
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param color is the color (RED, GREEN, BLUE, ALPHA)
	 */
	public static void drawRectangle(int x1, int y1, int x2, int y2, float[] color)
	{
		GL11.glColor4f(color[0], color[1], color[2], color[3]);
		GL11.glBegin(GL11.GL_QUADS);
	    GL11.glVertex2f(x1, y1);
	    GL11.glVertex2f(x2, y1);
	    GL11.glVertex2f(x2, y2);
	    GL11.glVertex2f(x1, y2);
	    GL11.glEnd();
	}
	
	/**
	 * Draw a rectangle and print to syserr the coordinates
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param color is the color (RED, GREEN, BLUE, ALPHA)
	 */
	public static void debugDrawRectangle(int x1, int y1, int x2, int y2, float[] color)
	{
		System.err.println("(" + x1 + ", " + y1 + ")->(" + x2 + ", " + y2 + ")");
		GL11.glColor4f(color[0], color[1], color[2], color[3]);
		GL11.glBegin(GL11.GL_QUADS);
	    GL11.glVertex2f(x1, y1);
	    GL11.glVertex2f(x2, y1);
	    GL11.glVertex2f(x2, y2);
	    GL11.glVertex2f(x1, y2);
	    GL11.glEnd();
	}
	
	/**
	 * Draw a text
	 * @param font is the font
	 * @param posX is the top right x position
	 * @param posY is the top right y position
	 * @param text is the text
	 * @param color is the color
	 */
	public static void drawText(TrueTypeFont font, int posX, int posY, String text, Color color)
	{
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		font.drawString(posX, posY, text, color);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

}
