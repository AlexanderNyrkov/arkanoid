package org.shurik.arkanoid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * class describing the game
 * extends JPanel class
 * interface implements ActionListener
 * interface implements org.shurik.arkanoid.Paintable
 */
public class Game extends JPanel implements ActionListener, Paintable {
    private final Timer mainTimer; // actions to be performed with a certain interval
    private final Image img; // background image
    private final int width; // game width
    private final int height;  // game height
    private final int x0; // coordinate of the left side of the game on the X axis
    private final int y0; // coordinate of the top of the game on the Y axis
    private Ball ball; // ball
    private BlockList blocks; // blocks
    private Player player; // player
    private List<Moveable> moveableElements = new ArrayList<>(); // list of objects that will move
    private List<Paintable> paintableElements = new ArrayList<>(); // list of objects that will be painted

    public Game() {
        this.img = new ImageIcon(getClass().getResource("/game.png")).getImage(); // object with the image of the game
        this.width = img.getWidth (null); // width of the game becomes equal to the width of the image
        this.height = img.getHeight (null); // height of the game becomes equal to the height of the image
        this.x0 = 0; // left coordinate point of the X axis is the left side of the game - 0
        this.y0 = 0; // coordinate point on the upper Y axis is the top of the game - 0
        this.mainTimer = new Timer (20, this); // set the interval perform actions

        this.blocks = new BlockList (27); // object block list with the number of lines in the lines and blocks

        this.ball = new Ball (0, 0, "/ball.png"); // object ball with the image
        this.ball.setX0 (width / 2 - ball.getDiameter () / 2); // position of the ball on the x-axis becomes the center of the game
        this.ball.setY0 (height / 2); // position of the ball on the Y axis becomes the center of the game

        this.player = new Player (0, 0, "/player.png"); // player object image
        player.setX0 (width / 2 - player.getWidth () / 2); // position of the player on the X axis becomes the center of the game
        player.setY0 (height - player.getHeight ()); // player position on the axis Y becomes the bottom of the game

        moveableElements.add (player); // add a player in the list, then to set it in motion
        moveableElements.add (ball); // adds the ball on the list, then to set it in motion
        paintableElements.add (blocks); // add blocks to the list, then to draw them
        paintableElements.add (player); // add a player in the list, then to draw it
        paintableElements.add (ball); // adds the ball on the list, then to draw it
        mainTimer.start (); // starts the timer
        blocks.generateBlocks (width); // add blocks to the list, then to draw them
        addKeyListener (new MyKeyAdapter ()); // handle keyboard events
        setFocusable (true); // set the focused state of the component
    }

    public int getX0() {
        return x0;
    }

    public int getY0() {
        return y0;
    }

    public int getX1() {
        return x0 + width;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * method implements the interface main.java.org.shurik.arkanoid.Paintable
     * @param g class object that allows you to work on graphics
     */
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, null); // background
        for(Paintable p : paintableElements) {
            p.paint(g); // draws elements paintableElements list
        }
    }

    /**
     * method is responsible for the movement
     */
    public void move() {
        for(Moveable m : moveableElements) {
            m.move();
        }
        // if the ball touches the bottom of the point, the game is over
        if (ball.getY1() >= getHeight()) {
            JOptionPane.showMessageDialog(null, "You lose");
            System.exit(0);
        }

        /*
         if the ball touches the right border of the game, it starts to fall to the left
         else if the ball touches the left border of the game, it starts to fall to the right
         else if the ball touches the top of the game, it starts to fall down
         */
        if (ball.getX1() >= getWidth()) {
            ball.setDx(-ball.getV());
        } else if (ball.getX0() <= getX0()) {
            ball.setDx(ball.getV());
        } else if (ball.getY0() <= getY0()) {
            ball.setDy(ball.getV());
        }

        if (ball.getY0() >= player.getY0()) {
            ball.setDy(ball.getV()); // if the ball is below the player, it will be impossible to repulse
        }

        // if the player touches the border of the game right, then stops
        if (player.getX1() >= getX1()) {
            player.setX1(getX1());
        }
        // if the player touches the border of the game on the left, then stops
        if (player.getX0() <= getX0()) {
            player.setX0(getX0());
        }
    }

    /**
     * method for determining the committed action
     * @param e event handler
     */
    public void actionPerformed(ActionEvent e) {
        move(); // launches method move
        repaint(); // redraw
        testCollisionWithPlayer(); // check player and ball touch
        testCollisionWithBlock(); // check and block the ball touches
        if (blocks.getAllBlocks().size() == 0) {
            JOptionPane.showMessageDialog(null, "You win !!!");
            System.exit(0);
        }
    }

    /**
     * calculations in the collision of the ball with the block
     */
    private void testCollisionWithBlock() {
        List<Block> blocksToBeRemoved = new ArrayList<>(); // create a list of blocks, which touches the ball and it will be necessary to remove
        for (Block block : blocks.getAllBlocks()) { // go through all the blocks
            /*
            if the ball touched the two blocks at the same time, it starts to fall down
            else if the ball is faced with rising unit, it falls down
            else if the ball falling down faced with the unit, it will begin to rise
             */
            if (ball.ellipse().getBounds2D().intersects(block.rightRect().getBounds2D()) && ball.ellipse().getBounds2D().intersects(block.rightRect().getBounds2D())) {
                ball.setDy(ball.getV());
            } else if ((ball.getDy() != ball.getV()) && ball.ellipse().getBounds2D().intersects(block.rightRect().getBounds2D())) {
                ball.setDy(ball.getV());
            } else if ((ball.getDy() == ball.getV()) && ball.ellipse().getBounds2D().intersects(block.rightRect().getBounds2D())) {
                ball.setDy(-ball.getV());
            }

            /*
            if the ball will face the top of the unit, it will begin to rise,
            else if the ball collide with the bottom part, it will start to fall
             */
            if (ball.ellipse().getBounds2D().intersects(block.upRect().getBounds2D())) {
                ball.setDy(-ball.getV());
            } else if (ball.ellipse().getBounds2D().intersects(block.bottomRect().getBounds2D())) {
                ball.setDy(ball.getV());
            }

            /*
            if the ball will face the left side of the unit, then place the unit in blocksToBeRemoved list and the ball will fly to the left
            else if the ball collide with the central part of the block, then put the block in the list blocksToBeRemoved and the ball will fly in the middle
            else, if the ball will face the right side of the unit, then place the block in the list blocksToBeRemoved and the ball will fly to the right
             */
            if (ball.ellipse().getBounds2D().intersects(block.leftRect().getBounds2D())) {
                blocksToBeRemoved.add(block);
                ball.leftDx();
            } else if (ball.ellipse().getBounds2D().intersects(block.centerRect().getBounds2D())) {
                blocksToBeRemoved.add(block);
                ball.centerDx();
            } else if (ball.ellipse().getBounds2D().intersects(block.rightRect().getBounds2D())) {
                blocksToBeRemoved.add(block);
                ball.rightDx();
            }
        }
        blocks.getAllBlocks().removeAll(blocksToBeRemoved); // deletes all blocks, if they touched the ball
    }

    /**
     * calculations in the collision of the ball with the player
     */
    private void testCollisionWithPlayer() {
        // if a player touched the ball, the ball will fly up
        if (player.rect().getBounds2D().intersects(ball.ellipse().getBounds2D())) {
            ball.setDy(-ball.getV());
        }

        /*
        if the ball hit the left side of the player, the ball would fly much left
        else if the ball hit the left side of the player, then the ball will fly to the left
        else if the ball hit the central part of the player, then the ball will fly in the middle
        else if the ball hit the right side of the player, then the ball will fly to the right
        else if the ball hit the right side of the player, the ball would fly much right
         */
        if (player.ultraLeftRect().getBounds2D().intersects(ball.ellipse().getBounds2D())) {
            ball.ultraLeftDx();
        } else if (player.leftRect().getBounds2D().intersects(ball.ellipse().getBounds2D())) {
            ball.leftDx();
        } else if (player.centerRect().getBounds2D().intersects(ball.ellipse().getBounds2D())) {
            ball.centerDx();
        } else if (player.rightRect().intersects(ball.ellipse().getBounds2D())) {
            ball.rightDx();
        } else if (player.ultraRightRect().getBounds2D().intersects(ball.ellipse().getBounds2D())) {
            ball.ultraRightDx();
        }
    }

    /**
     * a class that extends from the abstract class adapter in order to get the keyboard events
     */
    private class MyKeyAdapter extends KeyAdapter {
        /**
         * method is responsible for the actions when keys are pressed
         * @param e contains all the necessary information about the occurred event
         */
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode(); // recognizes the key code that was pressed

            // if the user clicks on the "right" arrow, the player begins to move to the right
            if (key == KeyEvent.VK_RIGHT) {
                player.setV(player.getVStart());
            }
            // if the user clicks on the "left" arrow, the player begins to move to the left
            if (key == KeyEvent.VK_LEFT) {
                player.setV(-player.getVStart());
            }
        }

        /**
         * method is responsible for the action, until the button is released
         * @param e contains all the necessary information about the occurred event
         */
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            // if the key "right" and / or "left" is released, the player does not move
            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_LEFT) {
                player.setV(player.getVStop());
            }
        }
    }
}

