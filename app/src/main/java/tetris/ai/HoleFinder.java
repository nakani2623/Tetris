package tetris.ai;

import java.util.ArrayList;
import java.util.List;

import tetris.engine.GameEngine;
import tetris.engine.Mino;
import tetris.engine.Tetromino;

/**
 * A Hole is defined as an empty space that is completely surrounded by filled spaces. 
 * an empty space can include more than 1 empty block
 * Currently an empty board is considered to have 1 hole.
 * This class finds the number of holes in a given board state.
 * 
 * HoleFinder
 */
public class HoleFinder {
    private List<Node> nodes; 

    public HoleFinder(GameEngine engine) {
        this.nodes = new ArrayList<>();

        // Initialize the grid array
        Node[][] grid = new Node[(int)engine.getWidth()][(int)engine.getHeight()];
        
        // Initialize each Node in the grid
        for (int x = 0; x < engine.getWidth(); x++) {
            for (int y = 0; y < engine.getHeight(); y++) {
                Node node = new Node();
                grid[x][y] = node;
                this.nodes.add(node);
            }
        }

        // Connect neighbors for each Node
        for (int x = 0; x < engine.getWidth(); x++) {
            for (int y = 0; y < engine.getHeight(); y++) {
                if (x > 0) 
                    grid[x][y].neighbors.add(grid[x - 1][y]); // left
                if (x < engine.getWidth() - 1) 
                    grid[x][y].neighbors.add(grid[x + 1][y]); // right  
                if (y > 0) 
                    grid[x][y].neighbors.add(grid[x][y - 1]); // up
                if (y < engine.getHeight() - 1) 
                    grid[x][y].neighbors.add(grid[x][y + 1]); // down
            }
        }

        for (Tetromino tetromino : engine.getAllTetrominos()) {
            if (tetromino == engine.getCurrentTetromino())
                continue;
            for (Mino m : tetromino.children) {
                int x = (int)m.getPosition().getX();
                int y = (int)m.getPosition().getY();
                grid[x][y].mino = m;
            }
        }
        
    }

    public int run() {
        int holes = 0;
        for (Node root : nodes) {
            if (root.isVisited) {
                continue;
            }

            if (root.mino != null) {
                continue; // Skip filled nodes
            }

            // Found a hole, perform BFS to mark all connected empty nodes
            holes++;
            List<Node> queue = new ArrayList<>();
            queue.add(root);
            while (!queue.isEmpty()) {
                Node current = queue.remove(0);
                if (current.isVisited) {
                    continue;
                }
                current.isVisited = true;
                for (Node neighbor : current.neighbors) {
                    if (!neighbor.isVisited && neighbor.mino == null) {
                        queue.add(neighbor);
                    }
                }
            }     
        }

        return holes;
    }
}

class Node {
    boolean isVisited;
    Mino mino;
    List<Node> neighbors;

    public Node() {
        this.isVisited = false;
        this.mino = null;
        this.neighbors = new ArrayList<>();
    }
}
