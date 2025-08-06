# Red-Blue Nim Game

This project implements a text-based version of the Red-Blue Nim game in Java. The game is played between a human player and an AI, with the objective of strategically removing marbles from two piles to win the game.

## Programming Language and Dependencies

This program is written in Java. The code is compatible with Java SE 17 and later versions. It utilizes standard Java libraries and does not require any external dependencies.

## Compiling and Running the Program

### Compiling

To compile and run this program, you will need a Java Development Kit (JDK) version 17 or newer installed on your system.

1. Navigate to the directory where `red_blue_nim.java` is located.
2. To compile, use: `javac red_blue_nim.java`

This will create a `.class` file in the same directory.

### Running the Program

After compiling, you can run the program using the `java` command. 

#### Basic Usage

`java red_blue_nim <num-red> <num-blue> [options]`

##### Arguments

`<num-red>`: (Required) A positive integer representing the starting number of red marbles.

`<num-blue>`: (Required) A positive integer representing the starting number of blue marbles.

##### Options

`-m`: (Optional) Play the "misère" version of the game. In this mode, the player who takes the last marble loses. By default, the game is played in standard mode, where taking the last marble makes you win.

`-h`: (Optional) The human player goes first. By default, the computer player starts.

## How the Code is Structured

The program is organized into several classes, each with a specific responsibility, to create a well-defined and modular structure.

`red_blue_nim.java`: This is the main class that contains the main method, which serves as the entry point for the program. It is responsible for:

1. Parsing command-line arguments to configure the game (e.g., number of red and blue marbles, game mode).
2. Initializing the game state and the players (human and AI).
3. Managing the main game loop, alternating turns between the human and the computer.
4. Determining and announcing the winner at the end of the game.

`Color` (enum): A simple enumeration to represent the two colors of marbles: RED and BLUE.

`Move` (record): A data-centric class that represents a single move in the game. It stores the color of the pile and the count of marbles to be removed.

`GameState`: This class encapsulates the state of the game at any given point. It keeps track of the number of red and blue marbles. It also provides methods to apply a move, check if a move is valid, and determine if the game is over.

`HumanPlayer`: This class handles the logic for a human player's turn. It prompts the user for input, parses the input to create a Move object, and validates the move against the current game state.

`AIPlayer`: This class implements the logic for the computer opponent. It uses the minimax algorithm with alpha-beta pruning to determine the optimal move. The AI's strategy can be adjusted for either the standard or the misère version of the game.