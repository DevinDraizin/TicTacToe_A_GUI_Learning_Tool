package Main;

import javafx.application.Application;
import javafx.stage.Stage;

//The main class is responsible for launching
//the application. At a large scale we create
//a gameCore instance and a UI instance and start
//the game. Because this game was made to be state
//based we dont need a main loop.
public class main extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage window)
    {
        
        UI gui = new UI();
        gameCore core = new gameCore();

        //We start the application by calling the
        //gui and giving it the gameCore and stage
        //instance. After that, the gui handles
        //everything
        gui.createUI(window,core);


    }
}
