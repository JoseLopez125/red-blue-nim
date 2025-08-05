import java.util.Objects;
import java.util.Scanner;

public class red_blue_nim {    
    public static void main(String[] args) {
        System.out.println("Welcome to Red-Blue Nim!");
        
        Scanner scanner = new Scanner(System.in);
        


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

class AIPlayer {}