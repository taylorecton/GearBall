/**
 * Author           Taylor Ecton
 * File Name        GearBallAStar.java
 * Date Modified    2017-09-23
 * Purpose          A class that implements the A* algorithm for the gear ball.
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.Calendar;

public class GearBallAStar {
    // the root node of the search graph
    private GBNode root;
    // the current node in the search graph
    private GBNode currentNode;
    // priority queue ordered by f(node) = g(node) + h(node)
    private PriorityQueue<GBNode> frontier = new PriorityQueue<>();
    // tree map for easy access of states rather than implementing a new data structure for
    // queue and map functionality
    private TreeMap<String, GBNode> frontierMap = new TreeMap<>();
    // array list containing a string representation of explored states
    private ArrayList<String> explored = new ArrayList<>();

    /**
     * Constructor for GearBallAStar
     * @param rootGB The gear ball configuration that will serve as the root node
     */
    public GearBallAStar(GearBall rootGB) {
        // create a new node using the starting configuration
        this.root = new GBNode(rootGB);
        // set the current node to the root
        this.currentNode = root;
        // add the root to the frontier
        frontier.add(root);
        frontierMap.put(rootGB.toString(), root);
    }

    /**
     * Performs A* search algorithm on gear ball
     * @return True if a solution is found, false if no solution is found.
     */
    public boolean performSearch() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        System.out.println("Searching. This may take a while...\n");
        // Prints the time at start of search so you can know how long search has been running
        System.out.println("Search started at: " + simpleDateFormat.format(calendar.getTime()));

        // continue searching until a solved configuration is found
        while (!currentNode.getConfiguration().isSolved()) {
            // return false if there are no nodes left to explore
            if (frontier.size() == 0) return false;

            // get the next node off of the queue
            currentNode = frontier.poll();
            // remove that node from the map since map should match queue
            frontierMap.remove(currentNode.getConfiguration().toString());

            // return true if the configuration of the retrieved node is solved
            if (currentNode.getConfiguration().isSolved()) return true;

            /*
            * UNCOMMENT THIS BLOCK TO SEE PROGRESSION OF f(node) OVER TIME
            *
            System.out.println("Distance of current node from root is: " + currentNode.getGValue());
            System.out.println("Heuristic value of current node is: " + currentNode.getHValue());
            System.out.println("f(node) = g(node) + h(node) = " + currentNode.getFValue() + "\n");
            *
            */

            // add the current node to the explored nodes
            explored.add(currentNode.getConfiguration().toString());

            // for every type of move on the gear ball
            for (int moveNum = 0; moveNum < 12; moveNum++) {
                // get the configuration of gear ball after the move is made
                GearBall newConf = new GearBall(currentNode.getConfiguration());
                newConf.rotate(moveNum);
                // create a node with this new configuration
                GBNode child = new GBNode(newConf, currentNode);

                if (!(explored.contains(newConf.toString()) || frontierMap.containsKey(newConf.toString()))) {
                    // if the new configuration is not in explored or frontier, add it to the frontier
                    frontier.add(child);
                    frontierMap.put(newConf.toString(), child);
                } else if (frontierMap.containsKey(newConf.toString()) &&
                           (frontierMap.get(newConf.toString()).getFValue() > child.getFValue())) {
                    // if the new configuration is in the frontier at a higher cost than the current node,
                    // replace it with this lower cost version of this configuration
                    frontier.remove(frontierMap.get(newConf.toString()));
                    frontier.add(child);
                    frontierMap.replace(newConf.toString(), child);
                }
            }
        }
        return true;
    }

    /**
     * Gets the configuration of the current node.
     * @return A GearBall in the configuration of the current GBNode
     */
    public GearBall getCurrentNode() {
        return currentNode.getConfiguration();
    }

    public int getSolutionDepth() {
        return currentNode.getGValue();
    }

    public int getNodesExpanded() {
        return explored.size();
    }

    /**
     * Internal GBNode class for nodes on the search path.
     * Implements comparable for ordering in priority queue
     */
    private class GBNode implements Comparable<GBNode> {
        // the configuration of the gear ball at this node
        private GearBall configuration;
        // the path from the root to this node
        private ArrayList<GBNode> path;
        // f(node) = g(node) + h(node)
        private int f;

        /**
         * Initial constructor for the GBNode. Called only for root node.
         * @param gearBall The configuration of the gear ball at this node.
         */
        private GBNode(GearBall gearBall) {
            // set the configuration
            this.configuration = gearBall;
            // initialize path to be an empty array
            this.path = new ArrayList<>();
            // calculate the f value for this node
            this.f = getFValue();
        }

        private GBNode(GearBall gearBall, GBNode parent) {
            // set the configuration
            this.configuration = gearBall;
            // initialize an empty ArrayList for the path then add all nodes in the parent's path
            // plus the parent
            this.path = new ArrayList<>();
            this.path.addAll(parent.getPath());
            this.path.add(parent);
            // calculate the f value
            this.f = getFValue();
        }

        /**
         * Gets the path to this node from the root.
         * @return An ArrayList of nodes with the root as the first element.
         */
        private ArrayList<GBNode> getPath() {
            return path;
        }

        /**
         * Calculates h(node) for this node, where h is the heuristic value.
         * See documentation for more information on heuristic.
         * @return Integer value for h(node)
         */
        private int getHValue() {
            int value = 0;
            int h1;
            int h2;

            for (GearBallFace face : configuration.getFaces()) {
                value += face.getNumSquaresOutOfPlace();
            }

            h1 = (int) Math.ceil(value / 24.0);

            h2 = (int) Math.floor(configuration.getNumGearsNotInStateZero() / 4.0);

            return Math.max(h1, h2);
        }

        /**
         * Calculates g(node) where g is the path length to the node from the root.
         * @return The path cost, g(node)
         */
        private int getGValue() {
            return path.size();
        }

        /**
         * Calculates f(node) = g(node) + h(node)
         * @return Integer value of f(node)
         */
        private int getFValue() {
            return this.getHValue() + this.getGValue();
        }

        /**
         * Gets the configuration of the gear ball at this node.
         * @return The configuration at this node.
         */
        private GearBall getConfiguration() {
            return configuration;
        }

        /**
         * Compares this node to another node using the f(node) value. Implemented for
         * the comparable interface to work, which is used for the PriorityQueue.
         * @param that The other node being compared.
         * @return -1 if the other node has a larger f value, 1 if this node has a larger f value
         *         or 0 if the two nodes have the same f value.
         */
        public int compareTo(GBNode that) {
            int priority = this.f - that.f;
            if (priority < 0) return -1;
            if (priority > 0) return 1;
            return 0;
        }
    }
}
