package Main;

// This class is responsible for the actual game logic.
// It maintains its own game board, game state, and current
// player. Additionally, we can do all normal game functions
// using methods from this class.
public class gameCore
{
    //Because there are only 3 pieces in Tic Tac Toe
    //we can use an 'enum' which stands for enumeration
    //This can be thought of as '#define Player1' in C.
    //It just allows us to represent a finite amount of
    //things using their names instead of having to use
    //something like an int. For example, if we wanted
    //to represent 3 different pieces we could place on
    //the game board (empty, p1, p2) we could create an
    //integer variable and simply assign the numbers (0,1,2)
    //for each of the types of pieces. Then when we wanted
    //to create a p1 piece we could do that with something like
    //this 'int piece = 1'. This would work fine except now
    //for the entire program we have to remember that 0 means
    //empty, 1 means p1, and 2 means p2. Not only can this make
    //things confusing but what happens when we assign a different
    //number to our 'int piece'? it can hold any value so if it
    //accidentally gets assigned a -2 or 395322 the whole thing
    //breaks. With that said, enums are just a way to make a new
    //type of variable when you know exactly how many 'values'
    //(empty, p1, p2), that type can actually hold. what this
    //block is saying is "Create a new enum type called 'piece'
    //and there are 3 different pieces, 'PLAYER1', 'PLAYER2',
    //'EMPTY'" By convention enums are usually in all caps. One
    //special attribute of enums is that they are referenced
    //statically. There is a description of what the static
    //keyword means in the UI class when I create the images
    //for each of the pieces. However, in this context what
    //it means is that when we want to get a variable of
    //type 'piece' named for example 'a', we can assign it
    //a value by saying 'piece a = piece.EMPTY'.
    enum piece
    {
        PLAYER1, PLAYER2, EMPTY
    }

    //This line will create the game board by creating a 2D array that
    //holds any of the 3 enum types we defined above. There are a few ways
    //we can think of 2d arrays but I find it most helpful to think of it
    //as a 3x3 grid. What is really happening is that we are creating a list
    //of 3 and in each slot of the list we are making another list of 3 that
    //holds enums. The 'private' key word is an access modifier and it means
    //that only the methods we write in this class can access the game board
    //we do that so no other classes can just make their own instance of the
    //'gameCore' class and mess around with its internal game board.
    private piece[][] gameBoard = new piece[3][3];

    //****************************************************************************
    //The next two variables use an integer to represent different players and
    //game states. This works but its a little wonky because for 1, you have to
    //know all the time what each int represents, and 2, we are wasting every
    //other number the int can represent! What if somewhere one of these variables
    //gets set to a number that is not in our convention? A better solution would
    //be to create another enum type (one for player and one for game states).
    //As an exercise, change the code to include 2 more enum types ('players' and
    //'gameStates') Then update the code in the gameCore and UI classes to use these
    //new enums instead of the integers.
    //****************************************************************************
    //              THERE IS A HINT AT THE BOTTOM OF THIS FILE
    //****************************************************************************

    //These 2 member variables will hold the current state of the game.
    //specifically who the current player is and what state we are in.
    //these both are 'private' for the same reason as gameBoard
    private int currentPlayer = 1;
    //Available Players:
    //  Player 1        = 1
    //  Player 2        = 2

    private int gameState = 0;
    //Available game states
    //  Player 1 Win    = 1
    //  Player 2 Win    = 2
    //  Tie Game        = -1
    //  No Win          = 0



    //These two methods (getCurrentPlayer() and getGameState())
    //are called 'getters' and they simply get a value. currentPlayer
    //and gameState are both private which is great because no external
    //classes can change the value but it might be useful to be able to
    //see what the actual value of these variables are. These methods are
    //public which means every external class CAN call these methods so we
    //copy the value and then 'get' it to whoever called it. This way external
    //classes can see the value but they cant change it
    public int getCurrentPlayer()
    {
        return currentPlayer;
    }

    public int getGameState()
    {
        return gameState;
    }


    //This is a special method called a constructor
    //it is the method that is called when we create
    //a new instance of a class and usually initializes
    //things in the class. whenever you write something
    //like 'String a = new String()' you are calling
    //the constructor of the String class. In this
    //case, whenever we create an instance of 'gameCore'
    //we want to start with a new game.
    public gameCore()
    {
        newGame();
    }


    //This methods is pretty self explanatory
    //loop through the entire game board and
    //explicitly set all of the pieces to empty.
    private void clearBoard()
    {
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                gameBoard[i][j] = piece.EMPTY;
            }
        }
    }

    //This new game method will reset
    //the gameCore to a new game state.
    //This means a clear board, set the
    //current player to player 1 and
    //set the game state to 0 (no win)
    public void newGame()
    {
        clearBoard();
        currentPlayer = 1;
        gameState = 0;

    }

    //This is responsible for making a move
    //on the internal game board. So we just
    //check which player we are, set the
    //appropriate piece at the x,y coordinates
    //once we place a piece on the board we set
    //the current player to the opposite player
    //finally, we update the game state by
    //calling getGameState()
    public void makeMove(int x, int y)
    {
        if(currentPlayer == 1)
        {
            gameBoard[x][y] = piece.PLAYER1;
            currentPlayer = 2;
        }
        else
        {
            gameBoard[x][y] = piece.PLAYER2;
            currentPlayer = 1;
        }

        gameState = updateGameState();
    }

    //This method will check to see
    //what state the current board is in
    //and returning the following codes
    //Returns 1 for player 1 Win
    //Returns 2 for player 2 Win
    //Returns 0 for no Win
    //Returns -1 for Tie Game
    Integer updateGameState()
    {
        if(checkWin(piece.PLAYER1))
        {
            return 1;
        }
        else if(checkWin(piece.PLAYER2))
        {
            return 2;
        }
        else if(checkFullBoard())
        {
            return -1;
        }

        return 0;
    }

    //If there is an empty piece anywhere
    //on the board then we know the board
    //is not empty
    Boolean checkFullBoard()
    {
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                if(gameBoard[i][j] == piece.EMPTY)
                {
                    return false;
                }

            }
        }

        return true;
    }


    //Here we check for any of the 3 possible
    //wins (diagonal, horizontal, vertical)
    Boolean checkWin(piece player)
    {
        if(player == piece.EMPTY)
        {
            return false;
        }

        Boolean hasWon = false;
        int i;

        //Check for vertical win
        for(i=0; i<3; i++)
        {
            if(gameBoard[0][i] == player && gameBoard[1][i] == player && gameBoard[2][i] == player)
            {
                return true;
            }
        }


        //Check for horizontal win
        for(i=0; i<3; i++)
        {
            if(gameBoard[i][0] == player && gameBoard[i][1] == player && gameBoard[i][2] == player)
            {
                return true;
            }
        }

        //Check top left to bottom right diagonal
        if(gameBoard[0][0] == player && gameBoard[1][1] == player && gameBoard[2][2] == player)
        {
            return true;
        }
        //Check bottom right to top left diagonal
        if(gameBoard[2][0] == player && gameBoard[1][1] == player && gameBoard[0][2] == player)
        {
            return true;
        }

        return false;

    }


    //****************************************************************************
    //                              EXERCISE HINT
    //****************************************************************************
    //DECLARING NEW ENUMS:
    //You'll notice that there is already a 'PLAYER1' and 'PLAYER2' value declared
    //in the piece enum. However, we need enums for the current player and for the
    //current game state. the current player can't be empty so in this case it is
    //necessary to give it it's own enum even if its just going to be 'PLAYER1'
    //and 'PLAYER2'. For the game states enum simply take each of the states
    //described in the comment below the variable and replace the number with the
    //name of the state. For the 'player' enum you could actually just copy and
    //paste the 'piece' enum block, delete the 'EMPTY' value, and rename piece to
    //'player' that would be it!
    //
    //REPLACING THE OLD CODE:
    //One of the nice things about using an integer to represent different variable
    //states is that they are easily comparable. You'll notice that when we need to
    //check to see if a certain player is the current player we can simply use the
    //comparator operators (==, <=, >=, !=) directly on the integer. For states
    //like game state or current player, it doesnt make sense to say a state is
    //less than or greater than another state but it certainly makes sense to say
    //a state is not equal or equal to. The good news is that enums support these
    //operators so not much in the code has to change when converting it. We can
    //still use '==' or '!=' on the new enumerations as if they were integers.
    //Another observation is how we reference the enums in the UI class. It seems
    //pretty strait forward in the gameCore class, when we wanted to use a 'piece'
    //we just said make a new variable 'piece thisIsAPieceVariable = piece.EMPTY;'
    //While we are in UI we cant just say we want a new piece type because it isn't
    //declared in that class. Instead we use the getter methods I described above.
    //You can see in the UI class, when we need to know a game state or a current
    //player we can just call its getter methods like 'getCurrentPlayer()'. We
    //could do the exact same thing with enums only the return type of the getter
    //method would change.
    //
    //TO DO LIST:
    //1. Create 2 new enums (players and gameStates)
    //2. Change the data type of currentPlayer and gameState from int to 'player'
    //   and 'gameState' respectively (Or whatever you decide to name your enums)
    //3. Update the getter methods for currentPlayer and gameState to reflect the
    //   change in data type. (Change the return type from int to [name of enum])
    //4. Go through gameCore and UI and replace all of the references that are
    //   still using integers instead of the enum. Example: line 439 in the UI
    //   class is trying to compare the current player to the int 1 to check if
    //   it is player 1. If we had an enum called 'player' that had a 'PLAYER1'
    //   value inside it we could replace line 439 with:
    //   if(core.getCurrentPlayer() == gameCore.player.PLAYER1)
    //****************************************************************************



}
