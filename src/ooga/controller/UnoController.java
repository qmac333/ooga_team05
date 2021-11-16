package ooga.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import javafx.stage.Stage;
import ooga.model.gameState.GameStateViewInterface;
import ooga.view.GameScreen;
import ooga.view.SplashScreen;
import ooga.view.UnoDisplay;
import ooga.model.gameState.GameState;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.nio.file.Files;

public class UnoController implements SplashScreenController, UnoDisplayController {

  private Stage myStage;
  private SplashScreen mySplashScreen;
  private UnoDisplay myUnoDisplay;

  private String filepath;
  private Moshi moshi;
  private GameState myModel;

  /**
   * initializes data structures for the UnoController
   *
   * @param stage the initial window for the application
   */
  public UnoController(Stage stage) {
    myStage = stage;
    moshi = new Moshi.Builder().add(new GameStateJsonAdapter()).build();
  }

  /**
   * passes the view's consumer to the model so the model can call .accept() whenever it needs to
   * notify the view of a change in its state
   */
  public void setupConsumer(Consumer viewConsumer) {

  }

  /**
   * steps through one turn of the game by calling the corresponding model method MAYBE pause the
   * timeline if it is the user's turn? In this case you would unpause the timeline once the
   * playUserCard method got called...
   */
  public void step() {

  }

  /**
   * Shows the splash screen of the application.
   */
  public void start() {
    if (mySplashScreen == null) {
      mySplashScreen = new SplashScreen(this);
    }
    showScreen(mySplashScreen);
  }

  /**
   * Creates new Uno game display
   */
  // TODO: start only if file has been loaded, create GameState object and pass to the view
  @Override
  public void playButtonHandler() {
    // if(myUnoDisplay == null && myModel != null){
    if (myUnoDisplay == null) {
      myUnoDisplay = new UnoDisplay(this);
    }
    showScreen(myUnoDisplay);
  }

  @Override
  public void loadExistingHandler() {
    System.out.println("Loading a File");
  }

  /**
   * Retrieves model parameters from the specified JSON file using Moshi before initializing a new model (GameState) object
   * @param filepath of the chosen JSON
   */
  @Override
  public void loadNewHandler(String filepath) {
    System.out.println("Loading a File");
    try{
      String json = getFileContent(filepath);
      JsonAdapter<GameState> jsonAdapter = moshi.adapter(GameState.class);
      myModel = jsonAdapter.fromJson(json);
    }
    catch (IOException e) {
      //TODO: better error handling
      System.out.println(e.getMessage());
    }
  }

  /**
   * Retrieves the content in the JSON file specified by the input
   * @param filepath of the JSON file
   * @return content of the JSON file specified by the filepath
   * @throws IOException
   */
  // TODO: maybe have view directly pass in a Path or File object so this method isn't needed?
  private String getFileContent(String filepath) throws IOException{
    Path path = Paths.get(filepath);
    String jsonContent = Files.readString(path);
    return jsonContent;
  }

  /**
   * Saves the current simulation/configuration to a JSON file
   */
  @Override
  public void saveCurrentHandler() {

  }

  @Override
  public void languageHandler() {
      System.out.println("Choose a language");
  }


  @Override
  public void backButtonHandler() {
    start();
  }

  @Override
  public GameStateViewInterface getGameState() {
    return null;
  }

  private void showScreen(GameScreen screen) {
    myStage.setScene(screen.setScene());
    myStage.show();
  }
}
