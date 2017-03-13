package org.shurik.arkanoid;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * class describing the blocks
 * interface implements org.shurik.arkanoid.Paintable
 */
public class BlockList implements Paintable {
    private List<Block> allBlocks = new ArrayList<>(); // a list of all blocks
    private int blocksAmount; // the number of blocks

    public BlockList(int blocksAmount) {
        this.blocksAmount = blocksAmount;
    }

    public List<Block> getAllBlocks() {
        return allBlocks;
    }

    /**
     * a method that is responsible for the addition of units in allBlocks list
     */
    public void generateBlocks(int widthLimit) {
        for (int i = 0; i < this.blocksAmount; i++) {
            if (i == 0) {
                allBlocks.add(new Block(0,0,0,0, "/block.png"));
                continue; // if this is the first block, then add it to the array coordinates of the upper left corner and continue the cycle
            }
            // already have a previous block
            Block previousBlock = allBlocks.get(i - 1);
            Block newBlock = new Block(previousBlock.getX1(),previousBlock.getY0(),9,0, "/block.png");
            /*
             if the block calls in the edge of the screen, it is indented on the Y axis
              */
            if (newBlock.getX1() > widthLimit) {
                newBlock.setX0(0);
                newBlock.setY0(previousBlock.getY1());
                newBlock.setSpaceX(0);
                newBlock.setSpaceY(1);
            }
            allBlocks.add(new Block(newBlock.getX0(), newBlock.getY0(), newBlock.getSpaceX(), newBlock.getSpaceY(), "/block.png")); // add blocks to the list
        }
    }

    /**
     * method implements the interface main.java.org.shurik.arkanoid.Paintable
     * @param g class object that allows you to work on graphics
     */
    public void paint(Graphics g) {
        for (Block b : allBlocks) {
            g.drawImage(b.getImg(), b.getX0(), b.getY0(), null);
        }
    }
}
