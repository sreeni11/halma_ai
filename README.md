# halma_ai
AI program to play Helma
Uses AI concept of looking ahead at future game states using BFS and calculating value of best state.
This is programmed in Java using 4 major classes. 
1) The most basic class is a coordinate class that is used to store information about a given (x,y) coordinate on the board. It contains basic setters, getters and overridden equals and toString methods.
2) The Board class that contains all the information about the current board state and is used in generating the various branches for future board states. It conatins setters, getters and various methods to manipulate the board state. The methods are:
  - copyBoard: copies the current board state
  - getInCamp: which returns a list of pieces that are still in their start state of a particular colour
  - getEmpty: which returns a list of the coordinates that are empty in the start state for a particular colour
  - getJumpSpots: returns a list of valid move spots for a given piece
  - getNumWin: returns the number of pieces in the win position for a particular colour
  - getNumOut: returns the number of pieces out of the start state but not in a win state for a particular colour
  - getNumSpots: returns the number of win state spots available for a particular colour
  - getNumCamp: returns the number of pieces in start state for a particular colour
  - calcDist: returns the Euclidian distance between 2 coordinates on the board
  - getDist: returns the distance between 2 coordinates on the board
  - move: attemps to make a move on the board 
  - display: displays the current board state
3) The Node class is used to represent a node on the tree of various game states it contains the board state, parent, cost of the current game state, children and list of valid jumps from the current game state. It contains setters, getters and constructors.
4) The Game class contains the logic for the AI and functions for various operations in the running of the game. It contains setters, getters and various methods. The methods are:
  - initialise: initialises the application and reads the information from the input file
  - display: displays various information about the current game
  - calcTime: calculates the current time
  - run: runs the game and decides how much calculation is done based on the time remaining
  - maxVal: returns the game state in the tree with the best value to select the best move
  - expand: expands the game state tree with future moves
  - expandJumps: expands the game state tree with future moves that involve jumping of pieces
  - inCamp: to check if the move being performed is on a piece in the start state
  - isWin: checks if the board is in a win state
  - evalState: evaluates the value of the state in the game state tree
  - getMove: returns the best move for a given game state
