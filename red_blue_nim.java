import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;

public class red_blue_nim {    
    public static void main(String[] args) {                
        // Deal with Arguments
        int numRed = -1;
        int numBlue = -1;
        boolean isMisere = false;
        boolean isHumanFirst = false;
        boolean isDepthLimited = false; // Using depth limited search algorithm

        if (args.length < 2) {
            System.err.println("Error: Missing required arguments for the number of red and blue marbles.");
            System.err.println("Usage: java RedBlueNim <num-red> <num-blue> ..."); //fix
            System.exit(1);
        }

        try {
            numRed = Integer.parseInt(args[0]);
            numBlue = Integer.parseInt(args[1]);
            if (numRed < 0 || numBlue < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: <num-red> and <num-blue> must be positive integers.");
            System.exit(1)
;        }

        for (int i = 2; i < args.length; i++) {
            switch (args[i]) {
                case "-m" -> isMisere = true;
                case "-h" -> isHumanFirst = true;
                case "-d" -> isDepthLimited = true;
                default -> System.err.println("Warning: Ignoring '" + args[i] + "' option.");
            }
        }

        // Game Setup
        Scanner scanner = new Scanner(System.in);
        GameState gameState = new GameState(numRed, numBlue);
        HumanPlayer human = new HumanPlayer();
        AIPlayer computer = new AIPlayer(isMisere);

        boolean isHumanTurn = isHumanFirst;

        System.out.println("Welcome to Red-Blue Nim!");
        System.out.println("Version: " + (isMisere ? "Misere" : "Standard"));
        System.out.println("First Player: " + (isHumanFirst ? "Human" : "Computer"));
        System.out.println("Search Method: " + (isDepthLimited ? "Depth Limited Search" : "Standard"));
        System.out.println("Initial State: " + gameState + "\n");
        
        // Game Loop
        while (!gameState.isGameOver()) {
            System.out.println((isHumanTurn ? "Your turn!" : "Computer's turn!"));
            Move move = (isHumanTurn ? human.getMove(gameState, scanner) : computer.getMove(gameState));
            
            System.out.println((isHumanTurn ? "You" : "Computer") + " chose to " + move + ".");
            gameState.applyMove(move);
            System.out.println("\nCurrent State: " + gameState + "\n");

            // Switch turns
            isHumanTurn = !isHumanTurn;
        }

        // Game Over
        System.out.println("Game Over!");
        int score = 2 * gameState.getNumRed() + 3 * gameState.getNumBlue();
        System.out.println("Final State: " + gameState + "\n");

        // Determine winner based on game rules
        // If isMisere is true, the player with an empty stack on their turn wins
        // If isMisere is false, the player with an empty stack on their turn loses
        if (isHumanTurn) {
            System.out.println("A stack is empty on your turn!");
            if (isMisere) {
                System.out.println("You win with a score of " + score + "!");
            } else {
                System.out.println("Computer wins with a score of " + score + "!");
            }
        } else {
            System.out.println("A stack is empty on the computer's turn!");
            if (isMisere) {
                System.out.println("Computer wins with a score of " + score + "!");
            } else {
                System.out.println("You win with a score of " + score + "!");
            }
        }

        scanner.close();
    }
}

enum Color {
    RED, BLUE
}

// Moves can only take 1 or 2 pieces from either the red or blue pile
record Move(Color color, int count) {
    public Move {
        Objects.requireNonNull(color, "Color cannot be null");
        if (count < 1) throw new IllegalArgumentException("Count must be at least 1");
        if (count > 2) throw new IllegalArgumentException("Count must be at most 2");
    }

    @Override
    public final String toString() {
        return "take " + count + " from the " + color.name().toLowerCase() + " pile";
    }
}

class GameState {
    private int numRed, numBlue;

    public GameState(int numRed, int numBlue) {
        if (numRed < 0 || numBlue < 0) {
            throw new IllegalArgumentException("Number of pieces cannot be negative");
        }

        this.numRed = numRed;
        this.numBlue = numBlue;
    }
    
    public void applyMove(Move move) {
        if (move.color() == Color.RED) {
            numRed = numRed - move.count();
        } else {
            numBlue = numBlue - move.count();
        }
    }

    public boolean isValidMove(Move move) {
        if (move.color() == Color.RED) {
            return move.count() <= numRed;
        } else {
            return move.count() <= numBlue;
        }
    }

    public boolean isGameOver() {
        return numRed == 0 || numBlue == 0;
    }

    public int getNumRed() {
        return numRed;
    }

    public int getNumBlue() {
        return numBlue;
    }

    @Override
    public String toString() {
        return "Red marbles: " + numRed + ", Blue marbles: " + numBlue;
    }   
}

class HumanPlayer {
    public Move getMove(GameState state, Scanner scanner) {
        while(true) {
            System.out.println("Enter your move (e.g., \"red 1\" or \"blue 2\"): ");
            String input = scanner.nextLine().trim().toUpperCase();
            String[] tokens = input.split(" ");

            if (tokens.length != 2) {
                System.out.println("Invalid input: Please enter a color and a count.");
                continue;
            }
            
            Color color = null;
            try {
                color = Color.valueOf(tokens[0]);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: Color must be RED or BLUE, and count must be 1 or 2.");
                continue;
            }

            int count;
            try {
                count = Integer.parseInt(tokens[1]);
                if (count < 1 || count > 2) {
                    System.out.println("Invalid move: Cannot take that amount from the " + color.name().toLowerCase() + " pile.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: Count must be 1 or 2.");
                continue;
            }

            Move move = new Move(color, count);
            if (state.isValidMove(move)) {
                return move;
            } else {
                System.out.println("Invalid move: Cannot take that amount from the " + color.name().toLowerCase() + " pile.");
            }
        }
    }
}

class AIPlayer {
    private boolean isMisere = false; // Default to standard game mode

    public AIPlayer(boolean isMisere) {
        this.isMisere = isMisere;
    }

    // AI Player using MinMax algorithm with alpha-beta pruning for optimal move selection
    public Move getMove(GameState state) {
        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;
        
        // Iterate through all valid moves for computer
        for (Move move : getValidMoves(state)) {
            GameState nextState = generateNextState(state, move);
            // Use minValue to find the best move for the AI
            // I used bestValue as alpha value since we only
            // care about moves that improve the value. Other moves
            // will be pruned by the minValue method.
            int moveValue = minValue(nextState, bestValue, Integer.MAX_VALUE); 
            
            if (moveValue > bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }
        }
        return bestMove;        
    }

    // This method calculates the maximum value the AI can achieve
    private int maxValue(GameState state, int alpha, int beta) {
        if (state.isGameOver()) {
            int score = 2 * state.getNumRed() + 3 * state.getNumBlue();
            if (isMisere) {
                // Returns positive value because this is a winning state for the computer
                return score;
            } else {
                // Returns negative value because this is a losing state for computer
                return -score;
            }

            // Returns negative value because this is a losing state for computer
            //return -(2 * state.getNumRed() + 3 * state.getNumBlue()); 
        }

        int value = Integer.MIN_VALUE;

        for (Move move : getValidMoves(state)) {
            GameState nextState = generateNextState(state, move);
            value = Math.max(value, minValue(nextState, alpha, beta));
            if (value >= beta) {
                return value; // Beta pruning
            }
            alpha = Math.max(alpha, value);
        }

        return value;
    }

    // This method calculates the best move that the human can make 
    // to minimize the AI's score / maximize the human's score
    private int minValue(GameState state, int alpha, int beta) {
        if (state.isGameOver()) {
            int score = 2 * state.getNumRed() + 3 * state.getNumBlue();
            if (isMisere) {
                // Returns negative value because this is a losing state for the computer
                return -score;
            } else {
                // Returns positive value because this is a winning state for the computer
                return score;
            }
            // Returns positive value becuase this is a winning state for the computer
            //return 2 * state.getNumRed() + 3 * state.getNumBlue(); 
        }

        int value = Integer.MAX_VALUE;

        for (Move move : getValidMoves(state)) {
            GameState nextState = generateNextState(state, move);
            value = Math.min(value, maxValue(nextState, alpha, beta));
            if (value <= alpha) {
                return value; // Alpha pruning
            }
            beta = Math.min(beta, value);
        }

        return value;
    }

    private ArrayList<Move> getValidMoves(GameState state) {
        ArrayList<Move> validMoves = new ArrayList<>();

        ArrayList<Move> potentialMoves = new ArrayList<>();
        potentialMoves.add(new Move(Color.RED, 2));
        potentialMoves.add(new Move(Color.BLUE, 2));
        potentialMoves.add(new Move(Color.RED, 1));
        potentialMoves.add(new Move(Color.BLUE, 1));

        for (Move move : potentialMoves) {
            if (state.isValidMove(move)) {
                validMoves.add(move);
            }
        }

        if (isMisere) {
            Collections.reverse(validMoves);
        }

        return validMoves;
    }

    private GameState generateNextState(GameState state, Move move) {
        GameState newState = new GameState(state.getNumRed(), state.getNumBlue());
        newState.applyMove(move);
        return newState;
    }
}