import java.util.Objects;

public class red_blue_nim {    
    static int remainingRed, remainingBlue;
    
    public static void main(String[] args) {
        System.out.println("Welcome to Red-Blue Nim!");
    }
}

enum Color {
    RED, BLUE
}

// Moves can only take 1 or 2 pieces from either the red or blue pile
record Move(Color color, int count) {
    public Move {
        Objects.requireNonNull(color, "Color cannot be null");
        Objects.requireNonNull(count, "Count cannot be null");
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
    
    public GameState applyMove(Move move) {
        if (move.color() == Color.RED) {
            return new GameState(numRed - move.count(), numBlue);
        } else {
            return new GameState(numRed, numBlue - move.count());
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

