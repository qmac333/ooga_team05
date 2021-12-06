package ooga.view;

import ooga.controller.interfaces.UnoDisplayController;
import ooga.model.gameState.GameStateViewInterface;

public class MockController implements UnoDisplayController {

  private MockGameViewInterface mock;

  public MockController() {
    mock = new MockGameViewInterface();

  }

  @Override
  public boolean saveCurrentFile(String filename) {
    return true;
  }

  @Override
  public void returnToSplashScreen() {

  }

  @Override
  public GameStateViewInterface getGameState() {
    return mock;
  }

  @Override
  public String getGameVersion() {
    return "";
  }

  public void addPlayer() {
    mock.addPlayer();
  }

}
