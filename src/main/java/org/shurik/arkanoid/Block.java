package org.shurik.arkanoid;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * class describing the block
 */
public class Block  {
    private final Image img;
    private final int upHeight;
    private final int bottomHeight;
    private final int width;
    private final int height;
    private int spaceX;
    private int spaceY;
    private int x0;
    private int y0;

    /**
     * @param x0 left point on the X axis unit
    * @param y0 top point unit on the X axis
    * @param spaceX indent X axis
    * @param spaceY indent on Y axis
    * @param imgPath path to the image
     */
    public Block(int x0, int y0, int spaceX, int spaceY, String imgPath) {
        this.x0 = x0 + spaceX;
        this.y0 = y0 + spaceY;
        this.img = new ImageIcon(getClass().getResource(imgPath)).getImage();
        this.width = img.getWidth(null);
        this.height = img.getHeight(null);
        this.upHeight = height / 2;
        this.bottomHeight = height;
    }

    public int getX0() {
        return x0;
    }

    public void setX0(int x0) {
        this.x0 = x0;
    }

    public int getY0() {
        return y0;
    }

    public void setY0(int y0) {
        this.y0 = y0;
    }

    public int getX1() {
        return this.x0 + width;
    }

    public int getY1() {
        return this.y0 + height;
    }

    public int getSpaceX() {
        return spaceX;
    }

    public void setSpaceX(int spaceX) {
        this.spaceX = spaceX;
    }

    public int getSpaceY() {
        return spaceY;
    }

    public void setSpaceY(int spaceY) {
        this.spaceY = spaceY;
    }

    public Image getImg() {
        return img;
    }

    /**
     * @return block rectangle from half to the top in contact with which the ball must fly away up
     */
    public Rectangle2D upRect() {
        return new Rectangle2D.Double(x0, y0, width, upHeight);
    }

    /**
     * @return block rectangle from half to the top in contact with which the ball should fly off down
     */
    public Rectangle2D bottomRect() {
        return new Rectangle2D.Double(x0, y0, width, bottomHeight);
    }

    /**
     * @return rectangle is responsible for the left side of the block in contact with which the ball should fly off to the left
     */
    public Rectangle2D leftRect() {
        return new Rectangle2D.Double(x0, y0, width / 2 - 1, height);
    }

    /**
     * @return rectangle is responsible for the central portion of the block in contact with which the ball should fly off the center
     */
    public Rectangle2D centerRect() {
        return new Rectangle2D.Double(x0, y0, width / 2 + 1, height);
    }

    /**
     * @return rectangle in charge of the right side of the block in contact with which the ball should fly off to the right
     */
    public Rectangle2D rightRect() {
        return new Rectangle2D.Double(x0, y0, width, height);
    }
}
