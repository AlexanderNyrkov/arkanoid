package org.shurik.arkanoid;

import javafx.geometry.Bounds;
import javafx.scene.shape.Ellipse;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * class describing a ball
 * interface implements org.shurik.arkanoid.Moveable
 * interface implements org.shurik.arkanoid.Paintable
 */
public class Ball implements Moveable, Paintable {
    private final Image img; // ball image
    private final int diameter; // ball diameter
    private int v; // ball speed
    private int dx; // the direction of movement of the ball along the X axis
    private int dy; // movement direction of the ball on the Y axis
    private int x0; // variable declaration, are the coordinates of the left side of the ball on the X axis
    private int y0; // variable declaration, are the coordinates of the top of the ball on the Y axis
    /**
     * @param imgPath path to the image
     * @param x0 left point of the ball on the X axis
     * @param y0 highest point of the ball on the Y axis
     */
    public Ball(int x0, int y0, String imgPath) {
        this.img = new ImageIcon (getClass().getResource(imgPath)).getImage(); // create the image, pointing his way
        this.x0 = x0; // left ball point on the X axis
        this.y0 = y0; // upper ball point on the Y axis
        this.diameter = img.getWidth(null); // diameter equal to the width of the image
        this.v = 9; // set the speed of the ball
        this.dy = v; // ball on Y axis direction, a positive value when the ball is flying down at a negative - up
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
        return getX0() + diameter;
    }

    public int getY1() {
        return getY0() + diameter;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getDiameter() {
        return diameter;
    }

    public int getV() {
        return v;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    /**
     * method implements the interface main.java.org.shurik.arkanoid.Paintable
     * @param g class object that allows you to work on graphics
     */
    public void paint(Graphics g) {
        g.drawImage(img, x0, y0, null);
    }

    /**
     * ball flight direction along the X axis in the collision with the left-most part of the object
     */
    public void ultraLeftDx() {
        this.dx = -v;
    }

    /**
     * ball flight direction along the X axis in the collision with the left side of the object
     */
    public void leftDx() {
        this.dx = -v / 2;
    }

    /**
     * ball flight direction along the X axis in the collision with the center
     */
    public void centerDx() {
        this.dx = 0;
    }

    /**
     * ball flight direction along the X axis in the collision with the right-most side of the object
     */
    public void ultraRightDx() {
        this.dx = v;
    }

    /**
     * ball flight direction along the X axis in the collision with the right side of the object
     */
    public void rightDx() {
        this.dx = v / 2;
    }

    /**
     * @return ball ellipse
     */
    public Ellipse2D ellipse() {
        return new Ellipse2D.Double(x0, y0, diameter, diameter);
    }

    /**
     * method implements the interface main.java.org.shurik.arkanoid.Moveable and is responsible for the behavior of the ball
     */
    public void move() {
        if (dy == v) {
            x0 += dx;
            y0 += dy;
        } else {
            x0 += dx;
            y0 += dy;
        }
    }
}
