package wumpusworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import wumpusworld.Agente;
import wumpusworld.Ambiente;
import wumpusworld.Ambiente.Acao;
import wumpusworld.Jogador;
import wumpusworld.Jogador.Direcao;

/**
 * An Agent that implements a basic heuristic strategy. The heuristic actions are as following:
 * H1: Grab the gold if sees glitter;
 * H2: Shoots to every not visited tiles if feels a stench;
 * H3: Mark the adjacent, not visited tiles has dangerous if feels a breeze;
 * H4: If do not have gold, choose the branch the non visited branch with less turns to take;
 * H5: Choose the path that surely does not have a danger;
 * H5: If have found the gold get back by the visited path;
 */
public class HeuristicAgent implements Agente {
    private int w, h;

    private boolean debug = true;
    private double[][] dangers;
    private boolean[][] visited;
    private boolean[][] shoot;

    private LinkedList<Acao> nextActions = new LinkedList<Acao>();

    /**
     * The strategy constructor.
     * @param width The board width
     * @param height  The board height
     */
    public HeuristicAgent(int width, int height) {
        w = width;
        h = height;
        dangers = new double[w][h];
        visited = new boolean[w][h];
        shoot = new boolean[w][h];
    } 

    /**
     * Sets weather to show the debug messages or not.
     * @param value <tt>true</tt> to display messages
     */
    public void setDebug(boolean value) {
        debug = value;
    }

  

    /**
     * Prints the last action taken.
     * @param player The player instance
     */
    public void afterAction(Jogador player) {
        if (debug) {
            // Players Last action
            System.out.println(player.getUltimaAcao());
            // Show a very happy message
            if (player.estaMorto()) {
                System.out.println("GAME OVER!");
            }
            // Turn on step-by-step
        }
    }

    /**
     * Implements the player artificial intelligence strategy.
     * @param player The player instance
     * @return The next action
     */
    public Acao getAction(Jogador player) {
        // Mark this block has visited
        int x = player.getX();
        int y = player.getY();

        // Set this block as visited
        visited[x][y] = true;

        // Apply actions pools
        if (nextActions.size() > 0) {
            return nextActions.poll();
        }

        // Grab the gold if senses glitter
        if (player.temBrilho()) return Acao.PEGAR;

        // Calculate the neighbor branches
        int[][] branches = getNeighbors(x, y);

        // Shoot an arrow to every non visited tiles if senses a stench
        if (player.temFedor() && player.temFlechas()) {
            // Apply killer instinct
            for(int[] branch : branches) {
                if (!visited[branch[0]][branch[1]] && !shoot[branch[0]][branch[1]]) {
                    shoot[branch[0]][branch[1]] = true;

                    ArrayList<Acao> actions = getActionsToShoot(player, branch);
                    nextActions.addAll(actions);
                    return nextActions.poll();
                }
            }
        }

        // Mark non visited neighbors has dangerous
        if (player.temBrisa()) {
            boolean knowPitPosition = false;
            // Verify if a pit was already found
            for(int[] branch : branches) {
                if (dangers[branch[0]][branch[1]] == 1) {
                    knowPitPosition = true;
                    break;
                }
            }
            // Estimate the pit location
            if (!knowPitPosition) {
                // Increase by 50% the probability of having some danger
                for(int[] branch : branches) {
                    if (!visited[branch[0]][branch[1]]) {
                        if (dangers[branch[0]][branch[1]] < 1) {
                            dangers[branch[0]][branch[1]] += 0.5;
                        }
                        // Pit was found
                        if (dangers[branch[0]][branch[1]] == 1) {
                            knowPitPosition = true;
                        }
                    }
                }
                // If a pit was found clear the dangers from other tiles
                if (knowPitPosition) {
                    for (int[] branch : branches) {
                        if (dangers[branch[0]][branch[1]]  < 1) {
                            dangers[branch[0]][branch[1]] = 0.0;
                        }
                    }
                }
            }
        } else {
            // From this tile nothing has sensed so set the neighbors to dangers
            for (int[] branch : branches) {
                if (dangers[branch[0]][branch[1]] < 1) {
                    dangers[branch[0]][branch[1]] = 0.0;
                }
            }
        }

        // Evaluate the cost of neighbor branches
        int currentCost = 999;
        int[] next = {-1, -1};
        for (int[] branch : branches) {
            int cost = getCost(player, branch);
            if(cost < currentCost) {
                currentCost = cost;
                next = branch;
            }
        }
        // Print the chosen tile
        if (debug) {
            System.out.format("Go to (%d,%d)%n", next[0], next[1]);
        }

        // Execute the action to get to the branch with less cost
        ArrayList<Acao> actions = getActionsTo(player, next);
        nextActions.addAll(actions);

        // Auto execute the first action
        return nextActions.poll();
    }

    /**
     * Gets the adjacent tiles of the given coordinates.
     * @param x The tile X coordinate
     * @param y The tile Y coordinate
     * @return An array of 2D coordinates
     */
    private int[][] getNeighbors(int x, int y) {
        HashMap<Direcao, Integer> nodesMap = new HashMap<Direcao, Integer>();

        // Calculate the next block
        int north = y - 1;
        int south = y + 1;
        int east = x + 1;
        int west = x - 1;

        // Check if branch is into bounds
        if (north >= 0) nodesMap.put(Direcao.N, north);
        if (south < h)  nodesMap.put(Direcao.S, south);
        if (east < w)   nodesMap.put(Direcao.L, east);
        if (west >= 0)  nodesMap.put(Direcao.O, west);

        // Build the branches array
        int branch = 0;
        int[][] nodes = new int[nodesMap.size()][2];
        for (Direcao direction : nodesMap.keySet()) {
            switch (direction) {
                case N: nodes[branch] = new int[]{x, north}; break;
                case S: nodes[branch] = new int[]{x, south}; break;
                case L: nodes[branch] = new int[]{east, y}; break;
                case O: nodes[branch] = new int[]{west, y}; break;
            }
            branch++;
        }

        return nodes;
    }

    /**
     * Returns the amount of turns player need to take to get into given position.
     * @param player The player's instance
     * @param to The destination tile
     * @return The number of turns
     */
    private int getTurns(Jogador player, int[] to) {
        // The current vector
        int[] from = {1, 0};
        switch (player.direcao()) {
            case N:
                from[0] = 0;
                from[1] = 1;
                break;
            case S:
                from[0] = 0;
                from[1] = -1;
                break;
            case O:
                from[0] = -1;
                from[1] = 0;
                break;
        }
        // The destination vector
        int[] dest = {to[0] - player.getX(), player.getY() - to[1]};
        // The angle between the two vectors
        double dotProduct = from[0] * dest[0] + from[1] * dest[1];
        double lenProduct = Math.hypot(from[0], from[1]) * Math.hypot(dest[0], dest[1]);
        double theta = Math.acos(dotProduct / lenProduct);
        // Inverts when facing backwards
        if (    player.direcao() == Direcao.N && getDirection(dest) == Direcao.L ||
                player.direcao() == Direcao.L && getDirection(dest) == Direcao.S ||
                player.direcao() == Direcao.S && getDirection(dest) == Direcao.O ||
                player.direcao() == Direcao.O && getDirection(dest) == Direcao.N) {
            theta *= -1;
        }
        // Count how many turns
        return (int)(theta / (Math.PI / 2));
    }

    /**
     * Returns the cost for to reach the given branch.
     * @param player The player's instance
     * @param to The destination block coordinates
     * @return The cost estimation tho reach the tile
     */
    private int getCost(Jogador player, int[] to) {
        // Start with at least one forward
        int sum = 1;
        // If found gold choose the safest path otherwise costs more to return
        if (visited[to[0]][to[1]]) {
            if (player.temOuro()) sum -= 5;
            else sum += 5;
        } else {
            // If senses a breeze avoid unvisited path
            if (player.temBrisa()) {
                if (dangers[to[0]][to[1]] < 1) {
                    sum += 10;
                } else if (dangers[to[0]][to[1]] == 1) {
                    // Avoid tiles marked as 100% danger
                    sum += 100;
                }
            }
        }

        // The amount fo turns to take
        int turns = getTurns(player, to);
        sum += Math.abs(turns);

        return sum;
    }

    /**
     * Returns the actions that player must take to reach the given destination.
     * @param player The player's instance
     * @param to The destination tile coordinates
     * @return An array of actions
     */
    private ArrayList<Acao> getActionsTo(Jogador player, int[] to) {
        ArrayList<Acao> actions = new ArrayList<Acao>();
        int turns = getTurns(player, to);
        for (int i = 0; i < Math.abs(turns); i++) {
            if (turns < 0) actions.add(Acao.DIREITA);
            if (turns > 0) actions.add(Acao.ESQUERDA);

        }
        // Go to the block
        actions.add(Acao.FRENTE);

        return actions;
    }
    
    /**
     * Returns the actions that player must take to reach the given destination.
     * @param player The player's instance
     * @param to The destination tile coordinates
     * @return An array of actions
     */
    private ArrayList<Acao> getActionsToShoot(Jogador player, int[] to) {
        ArrayList<Acao> actions = new ArrayList<Acao>();
        int turns = getTurns(player, to);
        for (int i = 0; i < Math.abs(turns); i++) {
            if (turns < 0) actions.add(Acao.DIREITA);
            if (turns > 0) actions.add(Acao.ESQUERDA);

        }
        // Go to the block
        actions.add(Acao.ATIRAR_FLECHA);

        return actions;
    }

    /**
     * Returns the direction based on the vector coordinates
     * @param coords The 2D coordinates
     * @return The direction
     */
    private Direcao getDirection(int[] coords) {
        if (coords[0] == +0 && coords[1] == +1) return Direcao.N;
        if (coords[0] == +1 && coords[1] == +0) return Direcao.L;
        if (coords[0] == +0 && coords[1] == -1) return Direcao.S;
        if (coords[0] == -1 && coords[1] == +0) return Direcao.O;
        return Direcao.L;
    }

    @Override
    public Acao getAcao(Jogador jogador) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void depoisAcao(Jogador jogador) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void antesAcao(Jogador jogador) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}