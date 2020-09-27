package Main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//This class is responsible for creating the entire UI
//Additionally, because this class maintains an instance
//of the gameCore class ('core') we can bind all of the
//UI components to actually interact with the game.
//The general model is that the gameCore will maintain
//the game state and perform all of the actual mechanics
//while the UI can feed the gameCore inputs and change
//depending on what the gameCore says the current state is
public class UI
{
    //These member variables are here so all of the
    //methods can access them without having a copy
    //passed in as an argument. These are basically
    //like global variables but only available inside
    //this class.

    //When we start the UI we will give it a new gameCore
    //instance and store it in this variable
    private gameCore core;

    //This is the UI component that will store the game board
    //I explain what each of the components are further below
    private GridPane gameBoard;

    //Because we update the label at different points in the
    //game it is really useful to have a reference to it here.
    //Since we are not being given this label we have to create
    //the label using the 'new Label()' constructor call.
    private Label playerLabel = new Label();


    //Here we are creating the image objects that our imageView
    //will be able to display on the board. The static keyword
    //means that for every instance of the UI class we create
    //there will only ever be 1 variable that every instance
    //shares. this might be a little redundant since we only
    //ever create 1 instance of UI (in main) and there is
    //actually a design pattern for classes you know you will
    //only ever need 1 instance of (Singleton classes), however,
    //I just want to show what static means in Java because it is
    //a core part of the language. If we removed the static keyword
    //from each of these variables, nothing we would actually change
    //except that if we created another instance of the UI class
    //it would get its own 'player1Image', 'player2Image', and
    //'emptyImage' which would just be wasteful. Why would we need
    //2 different copies of the same thing when we could just make
    //both instances share!
    static Image player1Image = new Image("Images/X.png");
    static Image player2Image = new Image("Images/O.png");
    static Image emptyImage = new Image("Images/empty.png");


    //**************************************************************
    //If you are reading these comments from top to bottom you might
    //want to scroll down to the 'createUI()' method and start there
    //and then move your way up to here.
    //**************************************************************


    //This method will just create and add the two labels we want
    //into a container and return the container.
    private VBox getTopContainer()
    {
        //Since we want the two labels on top of each other
        //we choose the VBox (vertical box) container
        VBox mainContainer = new VBox(26);
        mainContainer.setAlignment(Pos.CENTER);

        Label header = new Label("Tic Tac Toe");
        header.setStyle("-fx-font-size: 38");

        //Because the playerLabel is a member variable we do
        //not have to create that label in this method and we
        //just have to add it to the container.
        //we are calling setPlayerLabel() to just add a style
        //so we are not just writing redundant code in here
        setPlayerLabel();
        mainContainer.getChildren().addAll(header,playerLabel);

        return mainContainer;
    }

    //These method will get all of the Tiles from the game board
    //and set all of the Tile's 'disabled' field to whatever boolean
    //is passed in to the method
    private void setFreezeBoard(boolean freeze)
    {
        for(int i=0; i<gameBoard.getChildren().size(); i++)
        {
            Tile tile = (Tile) gameBoard.getChildren().get(i);
            tile.disabled = freeze;
        }
    }

    //This newGame method is the 'Master New Game' method
    //A new game means we have to reset all the Tiles,
    //unfreeze the board if we were in an end game state
    //then finally, set the player label to player 1.
    //the gameCore has its own newGame method that handles
    //resetting itself.
    private void newGame()
    {
        //reset gameCore
        core.newGame();

        //Get all of the tiles in the game board and call each
        //Tile's 'clearTile()' method
        for(int i=0; i<gameBoard.getChildren().size(); i++)
        {
            Tile tile = (Tile) gameBoard.getChildren().get(i);
            tile.clearTile();
        }

        //Set freeze board to false
        setFreezeBoard(false);

        //After we called newGame() in the core we know
        //that gameCores current player is player 1 so we
        //can call setPlayerLabel()
        setPlayerLabel();

    }

    //This method can be called to update the current player label
    //every time we make a move we call this/
    private void setPlayerLabel()
    {
        //We first check to see who is the current player in the gameCore
        //then we can update the label to represent that
        if(core.getCurrentPlayer() == 1)
        {
            playerLabel.setText("Player 1's Turn");
            playerLabel.setStyle("-fx-font-size: 28; -fx-text-fill: BLUE");
        }
        else
        {
            playerLabel.setText("Player 2's Turn");
            playerLabel.setStyle("-fx-font-size: 28; -fx-text-fill: GREY");
        }
    }

    //Here we instantiate the GridPane class and
    //then populate it with new Tile objects and
    //their respective coordinates.
    private GridPane getCenterContainer()
    {
        GridPane mainLayout = new GridPane();
        mainLayout.setGridLinesVisible(false);
        mainLayout.setAlignment(Pos.CENTER);

        for(int i=0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                mainLayout.add(new Tile(i,j),i,j);
            }
        }

        return mainLayout;
    }


    //Here we create the container that will contain
    //all of the buttons. We also create the action
    //listeners for each of the buttons.
    private HBox getBottomContainer(Stage window)
    {
        //Since we have 2 buttons and we want them next to each other
        //HBox is an obvious choice (Horizontal Box).
        HBox mainContainer = new HBox(20);
        mainContainer.setAlignment(Pos.CENTER);

        Button closeButton = new Button("Close");
        Button clearBoard = new Button("New Game");

        closeButton.setOnAction(e -> window.close());
        clearBoard.setOnAction(e -> newGame());

        mainContainer.getChildren().addAll(clearBoard,closeButton);

        return mainContainer;
    }


    //The purpose of this method is to update the UIs game state
    //after every move. First, we get the current state by asking
    //the gameCore instance using the 'getGameState()' method. this
    //will give us the state code (details are described in the
    //'gameCore' class). There are 4 separate states we need to handle:
    //
    //State 0 - Game is not over:
    //Action: No action required, just return
    //
    //State 1 - Player 1 Win
    //State 2 - Player 2 Win
    //State -1 - Tie Game
    //Action: Update the player label at the top to display the winner or Tie game
    //Action: change the color of the player label
    //Action: Freeze the board until a new game is started
    private void checkGame()
    {

        int state = core.getGameState();

        //Game is not over
        if(state == 0)
        {
            return;
        }

        //You will notice the 'setStyle' calls in each of the if cases. This is JavaFX's built in CSS support.
        //Almost all of the JavaFX components can be completely styled using cascading style sheets. These can
        //be applied as a separate file to skin an entire program. If you have heard of CSS before you know how
        //powerful this is, if not, this allows you to make programs look however you want them to look by creating
        //style sheets that can be dynamically swapped to create 'Themes'. You can create style classes to be applied
        //to all components of a certain type (ie. every button should look like this) and as long as the main container
        //has the stylesheet attached, all the internal components will be skinned. The calls below are an example of
        //inline CSS which is great for simple one off things like changing the font size or color of a specific
        //component. Here we are changing the font size to 28 and we are setting the text color to a linear-gradient
        //which is one of the many awesome effects you can do using CSS.

        //Player 1 Wins
        if(state == 1)
        {
            playerLabel.setText("Player 1 Wins! :D");
            playerLabel.setStyle("-fx-font-size: 28; -fx-text-fill: linear-gradient(to bottom right, #2242FF, #00B600)");
        }
        //Player 2 Wins
        else if(state == 2)
        {
            playerLabel.setText("Player 2 Wins! :D");
            playerLabel.setStyle("-fx-font-size: 28; -fx-text-fill: linear-gradient(to bottom right, #00B600, #4E4E58)");
        }
        //If the game is over and player 1 nor player 2 have won, it must be a tie game
        else
        {
            playerLabel.setText("Tie Game! :O");
            playerLabel.setStyle("-fx-font-size: 28; -fx-text-fill: linear-gradient(to bottom right, #FF5CEF, #FF9805)");
        }

        setFreezeBoard(true);

    }


    //This is the only public method is the UI class so this is
    //going to be the method that calls everything else, similar
    //to main. That means this is responsible for initializing
    //and setting up the entire user interface. Since this class
    //will have no knowledge that there is a game core we will
    //need to give it one which we do when we first instantiate
    //this class in the main class. Additionally, this method
    //does not technically create the full UI, it only populates
    //it. creation comes from the main class where we create the
    //the Stage instance ('window') which we just pass to this
    //method to be populated
    public void createUI(Stage window, gameCore core)
    {
        //The first thing we do is set the gameCore variable 'core'
        //in this class and make it equal to the instance passed in
        //as a parameter to this method. This gives us a reference
        //to the game core for the rest of the class to use
        this.core = core;

        //Set the window size and title
        window.setTitle("TicTacToe");
        window.setHeight(900);
        window.setWidth(1000);

        //Here we are creating the main container object which I would like to be
        //a border pane (for stylistic reasons)
        BorderPane mainLayout = new BorderPane();

        //Since we dont want everything in the UI right up against the sides we
        //can set insets for the entire window in the form of padding. The four
        //parameters in this call are a number of pixels for the Top, Right,
        //Bottom, Left sides respectively.
        mainLayout.setPadding(new Insets(40,10,40,10));

        //Border panes are useful because they allow us to separate a UI into a
        //Top, Left, Right, Center, and Bottom sections which we can set individually
        //using the setTop, setLeft etc. methods. Because each section can be pretty
        //complex it can be cleaner to simply create a separate method to create, populate
        //and then return a container component that we can just put right in the
        //main container. This is what we are doing here when we setTop, center,
        //and bottom.
        mainLayout.setTop(getTopContainer());

        //Because a lot of methods reference the gameBoard (GridPane) it is
        //easier to make the center container (GridPane) a member variable of
        //the class and then just initialize it with another method. Here,
        //getCenterContainer() creates a gridPane object, populates it with
        //all of the Tiles and then returns it. We can assign that return
        //value strait to the gameBoard member variable.
        gameBoard = getCenterContainer();

        //Now that the gameBoard variable has been initialized by the
        //'getCenterContainer()' method we can place it in the border pane
        //object's center section.
        mainLayout.setCenter(gameBoard);

        //Here 'getBottomContainer()' will create the container
        //that contains all the buttons. Since one of the buttons
        //is the close button we have to give the method a reference
        //to the window so it can actually close it when we click that
        //button
        mainLayout.setBottom(getBottomContainer(window));


        //Finally every stage needs a Scene to put the main layout container
        //Here we create a new Scene object called 'scene' and give it the
        //BorderPane object 'mainLayout' as the main container.
        Scene scene = new Scene(mainLayout);

        //Now that the scene is created, we give the scene to the window
        window.setScene(scene);

        //By default one of the buttons will have focus on the UI. Focus is
        //that blue box around components on user interfaces that makes it looks
        //like they are selected. This is great because 'focused objects' can
        //be useful for giving context for what the user is looking at (such as
        //which window is being looked at in a multi window program). In our case
        //there is no reason the buttons should be focused to start off with so we
        //switch the focus to the borderPane which simply accomplishes the blue box
        //disappearing. This is purely an aesthetic choice and it just bugs me to
        //see the blue box at start. Comment this line out and look at the 'New Game'
        //button to see what im talking about
        mainLayout.requestFocus();

        //Lastly, tell the window that you would actually like it to be displayed
        //to the user
        window.show();
    }



    //This class will encapsulate all the functionality of a tile (game board Button).
    //Because we want to be able to display pieces as images it was natural to start
    //with an ImageView object which is why we extend that class in the declaration.
    //the 'extends' keyword means we inherit everything an ImageView can already do
    //as well as add our own functionality. This is super powerful because it means
    //we don't need to reinvent the wheel when it comes to making a button. ImageView
    //already has methods for setting images and creating action listeners when we
    //click on them. The purpose of this Tile class is to perform all of the things
    //a button will need to do in Tic Tac Toe. Namely, having an (x,y) position on
    //on the board, having a 'type' (X, O, or a empty piece), and being able to be
    //disabled when someone wins (So you cant keep clicking a button).
    class Tile extends ImageView
    {


        //We defined an enumeration in the gameCore class for each of the
        //three possible pieces so here we give every button its own type
        //This variable will store the type of the Tile
        gameCore.piece currentPiece;

        //When we create a tile we give it an x and y coordinate so we
        //know where in the board it is.
        int xCoord, yCoord;

        //If this is true then the Tile should be disabled so nothing happens
        //when you click it. That way when we detect a win or tie state we stop
        //the button from doing something.
        boolean disabled;

        //This constructor takes two integers (x and y) that we can use to
        //set the coordinates right when we instantiate a new Tile object/
        public Tile(int x, int y)
        {
            //Here we are initializing the new Tile object with the coordinates,
            //setting the current piece to empty and setting disabled to false.
            //Here the 'this' keyword refers to the specific object that calls it.
            //since a class is a blueprint you can imagine that at some point there
            //will be more than one tile (9 to be exact) and so the 'this' keyword is
            //how we specify that the attributes we want to set should be set for the
            //specific object that is getting constructed.
            this.xCoord = x;
            this.yCoord = y;
            this.currentPiece = gameCore.piece.EMPTY;
            this.disabled = false;

            //setImage() is a method that is implemented in the
            //imageView class that Tile extends from.
            //it takes an 'Image' object and handles
            //actually changing the image displayed. You will notice
            //that we give a relative path to the 'empty.png' image.
            //this means that it knows where that picture is
            //relative to where the current file is. In this case
            //we want to set all new Tiles to empty so we set the empty.png
            setImage(emptyImage);

            //setOnMouseClicked() is another method that comes from the imageView class
            //most components will have this method built in so you can trigger an event
            //whenever that component is clicked. This method actually takes as a parameter
            //an 'EventHandler' object but here I use 'e -> makeMove()' which is a short hand
            //way of saying "When we click the imageView, call the method 'makeMove()'" This
            //short hand is called a lambda and lots of languages other than Java support them.
            //In this case we want to call the makeMove() method every time we click a Tile.
            setOnMouseClicked(e -> makeMove());
        }

        //Because this method is located inside the Tile class we have
        //the benefit of knowing exactly which Tile is trying to make
        //a move which is great because that means we will have all the
        //information already/
        private void makeMove()
        {
            //If the current piece of the Tile that is clicked is something other than an empty
            //piece someone has moved there and we should'nt do anything. Additionally, if the Tile
            //is disabled we should'nt do anything either so in this if statement we are saying:
            //If the current piece IS NOT empty OR disabled, do nothing and return immediately
            if(currentPiece != gameCore.piece.EMPTY || disabled)
            {
                return;
            }

            //If we get here we know the Tile that was clicked is empty and is
            //not disabled so now we just need to actually make a move. To do this
            //we have 2 different cases, if the Tile is Player 1, set the image to
            //the X otherwise we know the Tile is Player 2 and we set the image to O.
            //We know which player is currently moving because we have a reference to
            //the gameCore instance 'core' which maintains who is the current player.
            //Once we set the image to the right player we also have to update the Tiles
            //'currentPiece' variable to that new piece.
            if(core.getCurrentPlayer() == 1)
            {
                setImage(player1Image);
                this.currentPiece = gameCore.piece.PLAYER1;
            }
            else
            {
                setImage(player2Image);
                this.currentPiece = gameCore.piece.PLAYER2;
            }

            //Now that the image and piece has been set, we can tell the gameCore
            //to update it's state. since the makeMove() method inside the gameCore
            //needs x and y coordinates we can pass it the coordinates of the Tile
            //object that was clicked.
            core.makeMove(this.xCoord,this.yCoord);

            //This call will update label at the top of the screen indicating
            //who's turn it is.
            setPlayerLabel();

            //Finally, since we just moved we need to update the game state
            //for the UI we do this by calling checkGame(). This method
            //checks the state in the gameCore and then will set the label
            //to say Tie, P1 win, P2 Win or nothing if the game is not over.
            //Additionally, this method can disable all of the Tiles if the
            //game is over.
            checkGame();
        }


        //The last method in the Tile class is simply a method
        //to reset the piece and image to empty in the case of
        //a new game.
        void clearTile()
        {
            this.currentPiece = gameCore.piece.EMPTY;
            setImage(emptyImage);
        }


    }
}


