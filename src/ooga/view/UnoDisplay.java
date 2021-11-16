package ooga.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ooga.controller.UnoDisplayController;
import ooga.util.Config;

public class UnoDisplay implements GameScreen {

  private UnoDisplayController controller;
  private TurnInfoDisplay turnDisplay;
  private HandListDisplay handListDisplay;
  private Scene myScene;

  /**
   * initializes data structures and saves the given controller
   *
   * @param controller a variable that provides access to controller methods
   */
  public UnoDisplay(UnoDisplayController controller) {
    this.controller = controller;
    this.turnDisplay = new TurnInfoDisplay(controller);
    this.handListDisplay = new HandListDisplay(controller);

    createScene();
  }

  /**
   * creates the Scene for the display
   *
   * @return the current scene that we are displaying
   */
  public Scene setScene() {
    return myScene;
  }

  /**
   * provides a user-friendly way for errors to be displayed controller needs to call this method
   * when errors are encountered
   *
   * @param e the exception that was thrown from the error
   */
  public void showError(Exception e) {

  }

  private void createScene() {
    VBox root = new VBox();
    root.setAlignment(Pos.CENTER);
    Button button = new Button("Back");
    button.setOnAction(e -> controller.backButtonHandler());
    root.getChildren().addAll(button, turnDisplay.getDisplayableItem(), handListDisplay.getDisplayableItem());
    Scene scene = new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
    myScene = scene;
  }


}
