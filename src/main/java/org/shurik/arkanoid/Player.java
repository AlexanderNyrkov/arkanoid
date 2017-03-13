package org.shurik.arkanoid;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * class describing Player
 * interface implements org.shurik.arkanoid.Moveable
 * interface implements org.shurik.arkanoid.Paintable
 */
public class Player implements Moveable, Paintable{
    private final Image img; // player image
    private final int vStop; // speed, when a player does not move
    private final int vStart; // player's speed in motion
    private final int width; // the width of the player
    private final int height; // height of the player
    private final int centerWidth; // the width of the center
    private final int ultraLeftPartStart; // origin of the left-most part of the player
    private final int partWidth; // the width of the player
    private final int leftPartStart; // origin of the left side of the player
    private final int centerPartStart; // origin central player
    private final int rightPartStart; // origin of the right side of the player
    private final int ultraRightPartStart; // the origin of the rightmost part of the player
    private int v; // player's speed
    private int x0; // left point player X axis
    private int y0; // upper point player Y-axis

    /**
     * @param x0 coordinate of the left point player
     * @param y0 coordinate of the top of the player
     * @param imgPath path to image
     */
    public Player(int x0, int y0, String imgPath) {
        this.x0 = x0; // Left point player X axis
        this.y0 = y0; // upper point player Y axis
        this.img = new ImageIcon (getClass().getResource(imgPath)).getImage(); // the image that you want to specify the path to the player's image
        this.width = img.getWidth (null); // the width of the player is equal to the width of the image
        this.height = img.getHeight (null); // height of the player is equal to the height of the image
        this.vStart = 12; // set the speed of the player in motion
        this.vStop = 0; // set speed when the player does not move
        this.centerWidth = 5; // the width of the center when hit by the ball that must change its direction
        this.partWidth = (width - centerWidth) / 4; // often takes the player on top and bottom to the central part, in contact with which the ball must change its direction
        this.ultraLeftPartStart = 0; // leftmost part begins with the leftmost point of a player when hit by that between the beginning and the end of the ball should fly off much left
        this.leftPartStart = ultraLeftPartStart + partWidth; // left part will start with the leftmost end, when hit by that between the beginning and the end of the ball should fly off to the left
        this.centerPartStart = leftPartStart + partWidth; // central part will start at the end of the left, which when hit between the beginning and the end of the ball should fly off the center
        this.rightPartStart = centerPartStart + centerWidth; // right-hand side will start from the end of the center, which is in contact in between the beginning and the end of the ball should fly off to the right
        this.ultraRightPartStart = rightPartStart + partWidth; // the rightmost part will begin from the end of the right, if it enters into that between the beginning and the end of the ball should fly off strong right
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

    public void setX1(int x1) {
        this.x0 = x1 - width;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setV(int v) {
        this.v = v;
    }

    public int getVStop() {
        return vStop;
    }

    public int getVStart() {
        return vStart;
    }

    /**
     * method implements the interface main.java.org.shurik.arkanoid.Paintable
     * @param g class object that allows you to work on graphics
     */
    public void paint(Graphics g) {
        g.drawImage(img, x0, y0, null);
    }

    /**
     * @return rectangle from the left point to the first quarter of the player in contact with which the object changes its direction
     */
    public Rectangle2D ultraLeftRect() {
        return new Rectangle2D.Double(x0 + ultraLeftPartStart, y0, partWidth, height);
    }

    /**
     * @return rectangle from the third quarter until the end of the right point of contact at which the object changes its direction
     */
    public Rectangle2D ultraRightRect() {
        return new Rectangle2D.Double(x0 + ultraRightPartStart, y0, partWidth, height);
    }

    /**
     * @return rectangle from the first quarter to the second quarter, in contact with which the object changes its direction
     */
    public Rectangle2D leftRect() {
        return new Rectangle2D.Double(x0 + leftPartStart, y0, partWidth, height);
    }

    /**
     * @return rectangle from the second quarter to the center of the length, in contact with which the object changes its direction
     */
    public Rectangle2D centerRect() {
        return new Rectangle2D.Double(x0 + centerPartStart, y0, centerWidth, height);
    }

    /**
     * @return rectangle from the end of the length of the center to the third quarter, when hit by that object changes its direction
     */
    public Rectangle2D rightRect() {
        return new Rectangle2D.Double(x0 + rightPartStart, y0, partWidth, height);
    }

    /**
     * @return rectangle from the beginning to the end, in contact with which the object changes its direction
     */
    public Rectangle2D rect() {
        return new Rectangle2D.Double(x0, y0, width, height);
    }

    /**
     * method implements the interface main.java.org.shurik.arkanoid.Moveable and is responsible for the behavior of the player
     */
    public void move() {
        x0 += v;
    }
}
