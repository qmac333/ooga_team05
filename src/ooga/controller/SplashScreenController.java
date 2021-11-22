package ooga.controller;

import java.util.Map;

/**
 * Interface used to serve as the controller for the splash screen view.
 */
public interface SplashScreenController {

  void playButtonHandler();

  void setGameParameters(String version, Map<String, String> playerMap, String pointsToWin, boolean stackable);

  void loadExistingHandler();

  void loadNewHandler(String filepath);

  void languageHandler();

}
