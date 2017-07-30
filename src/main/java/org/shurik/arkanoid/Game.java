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
    private final Timer mainTimer;
    private final Image img;
    private final int width;
    private final int height;
    private final int x0;
    private final int y0;
    private Ball ball;
    private BlockList blocks;
    private Player player;
    private List<Moveable> moveableElements = new ArrayList<>();
    private List<Paintable> paintableElements = new ArrayList<>();

    public Game() {
        this.img = new ImageIcon(getClass().getResource("/game.png")).getImage();
        this.width = img.getWidth (null);
        this.height = img.getHeight (null);
        this.x0 = 0;
        this.y0 = 0;
        this.mainTimer = new Timer (20, this);

        this.blocks = new BlockList (27);

        this.ball = new Ball (0, 0, "/ball.png");
        this.ball.setX0 (width / 2 - ball.getDiameter () / 2);
        this.ball.setY0 (height / 2);

        this.player = new Player (0, 0, "/player.png");
        player.setX0 (width / 2 - player.getWidth () / 2);
        player.setY0 (height - player.getHeight ());

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
        g.drawImage(img, 0, 0, null);
        for(Paintable p : paintableElements) {
            p.paint(g);
        }
    }

    /**
     * method is responsible for the movement
     */
    public void move() {
        for(Moveable m : moveableElements) {
            m.move();
        }
        if (ball.getY1() >= getHeight()) {
            JOptionPane.showMessageDialog(null, "You lose");
            System.exit(0);
        }

        if (ball.getX1() >= getWidth()) {
            ball.setDx(-ball.getV());
        } else if (ball.getX0() <= getX0()) {
            ball.setDx(ball.getV());
        } else if (ball.getY0() <= getY0()) {
            ball.setDy(ball.getV());
        }

        if (ball.getY0() >= player.getY0()) {
            ball.setDy(ball.getV());
        }

        if (player.getX1() >= getX1()) {
            player.setX1(getX1());
        }

        if (player.getX0() <= getX0()) {
            player.setX0(getX0());
        }
    }

    /**
     * method for determining the committed action
     * @param e event handler
     */
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        testCollisionWithPlayer();
        testCollisionWithBlock();
        if (blocks.getAllBlocks().size() == 0) {
            JOptionPane.showMessageDialog(null, "You win !!!");
            System.exit(0);
        }
    }

    /**
     * calculations in the collision of the ball with the block
     */
    private void testCollisionWithBlock() {
        List<Block> blocksToBeRemoved = new ArrayList<>();
        for (Block block : blocks.getAllBlocks()) { // go through all the blocks

            if (ball.ellipse().getBounds2D().intersects(block.rightRect().getBounds2D()) && ball.ellipse().getBounds2D().intersects(block.rightRect().getBounds2D())) {
                ball.setDy(ball.getV());
            } else if ((ball.getDy() != ball.getV()) && ball.ellipse().getBounds2D().intersects(block.rightRect().getBounds2D())) {
                ball.setDy(ball.getV());
            } else if ((ball.getDy() == ball.getV()) && ball.ellipse().getBounds2D().intersects(block.rightRect().getBounds2D())) {
                ball.setDy(-ball.getV());
            }

            if (ball.ellipse().getBounds2D().intersects(block.upRect().getBounds2D())) {
                ball.setDy(-ball.getV());
            } else if (ball.ellipse().getBounds2D().intersects(block.bottomRect().getBounds2D())) {
                ball.setDy(ball.getV());
            }

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
        blocks.getAllBlocks().removeAll(blocksToBeRemoved);
    }

    /**
     * calculations in the collision of the ball with the player
     */
    private void testCollisionWithPlayer() {
        if (player.rect().getBounds2D().intersects(ball.ellipse().getBounds2D())) {
            ball.setDy(-ball.getV());
        }

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
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_RIGHT) {
                player.setV(player.getVStart());
            }
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
            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_LEFT) {
                player.setV(player.getVStop());
            }
        }
    }
}

