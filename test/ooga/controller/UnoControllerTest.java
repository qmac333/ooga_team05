package ooga.controller;

import static org.junit.jupiter.api.Assertions.*;

import javafx.stage.Stage;
import ooga.view.subdisplays.CardDisplay;
import ooga.model.gameState.GameState;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class UnoControllerTest extends DukeApplicationTest {
    private UnoController controller;
    private static final String VALID_NEW_FILE_1_PATH = Paths.get(".", "\\data\\configuration_files\\Test Files\\validNewFile1.json").toAbsolutePath().normalize().toString();
    private static final String VALID_NEW_FILE_2_PATH = Paths.get(".", "\\data\\configuration_files\\Test Files\\validNewFile2.json").toAbsolutePath().normalize().toString();
    private static final String INVALID_NEW_FILE_1_PATH = Paths.get(".", "\\data\\configuration_files\\Test Files\\invalidNewFile1.json").toAbsolutePath().normalize().toString();
    private static final String INVALID_GAME_IN_PROGRESS_FILE_1_PATH = Paths.get(".", "\\data\\configuration_files\\Test Files\\invalidGameInProgressFile1.json").toAbsolutePath().normalize().toString();
    private static final String BASIC_4CPUs_PATH = Paths.get(".", "\\data\\configuration_files\\Example Files\\Basic_4CPUs.json").toAbsolutePath().normalize().toString();
    private static final String BLAST_4CPUs_PATH = Paths.get(".", "\\data\\configuration_files\\Example Files\\Blast_4CPUs.json").toAbsolutePath().normalize().toString();
    private static final String FLIP_4CPUs_PATH = Paths.get(".", "\\data\\configuration_files\\Example Files\\Flip_4CPUs.json").toAbsolutePath().normalize().toString();

    private static final String SAVE_FILENAME = "jUnitTest_SaveFile";
    private static final String INVALID_SAVE_FILENAME = "/this.won't/work/";
    private static final String SAVE_FILENAME_PATH = Paths.get(".", "\\data\\configuration_files\\Save Files\\" + SAVE_FILENAME + ".json").toAbsolutePath().normalize().toString();
    private static final String DEFAULT_COLOR_THEME_PATH = "/ooga/resources/mainDisplay.css";

    private static final String DEFAULT_LANGUAGE = "English";


    @Override
    public void start(Stage stage){
        controller = new UnoController(stage);
        CardDisplay.initializeCards();
    }

    @Test
    void creatingInitialMenuScreenAndChangingLanguage(){
        runAsJFXAction(() -> controller.start());
        assertNotNull(controller.getLanguageScreen());
        controller.setLanguage(DEFAULT_LANGUAGE);
    }

    @Test
    void creatingSplashScreenAndChangingColorTheme(){
        runAsJFXAction(() -> controller.createSplashScreen(DEFAULT_LANGUAGE));
        assertNotNull(controller.getSplashScreen());
        controller.setColorThemeFilepath(DEFAULT_COLOR_THEME_PATH);
    }

    private boolean playNewGame(){
        runAsJFXAction(() -> controller.playNewGame("Traditional"));
        return controller.getUnoDisplay() != null;
    }

    @Test
    void loadingNewFile(){
        String version = "Basic";
        Map<String, String> playerMap = new HashMap<>();
        playerMap.put("Andrew", "Human");
        playerMap.put("Drew", "CPU");
        playerMap.put("Quentin", "Human");
        int pointsToWin = 500;
        boolean stackable = true;
        GameState expected = new GameState(version, playerMap, pointsToWin, stackable);

        assertTrue(controller.loadFile(VALID_NEW_FILE_1_PATH));
        assertTrue(controller.getGameVersion().equals(version));
        assertTrue(controller.getPlayerMap().equals(playerMap));
        assertTrue(controller.getPoints() == pointsToWin);
        assertTrue(controller.getStackable() == stackable);
        assertTrue(playNewGame());
        assertTrue(expected.compareInitialParameters(controller.getModel()));
    }

    @Test
    void loadingInvalidNewFile(){
        assertFalse(controller.loadFile(INVALID_NEW_FILE_1_PATH));
        assertFalse(playNewGame());
    }

    @Test
    void loadingTwoNewFilesInARow(){
        String version = "Basic";
        Map<String, String> playerMap = new HashMap<>();
        playerMap.put("Jackson", "Human");
        playerMap.put("Drew", "Human");
        playerMap.put("Ryan", "CPU");
        playerMap.put("Luke", "CPU");
        int pointsToWin = 70;
        boolean stackable = false;
        GameState expected = new GameState(version, playerMap, pointsToWin, stackable);

        assertTrue(controller.loadFile(VALID_NEW_FILE_1_PATH));
        assertTrue(controller.loadFile(VALID_NEW_FILE_2_PATH));
        assertTrue(playNewGame());
        assertTrue(expected.compareInitialParameters(controller.getModel()));
    }

    @Test
    void manuallySettingGameParameters(){
        String version = "Basic";
        Map<String, String> playerMap = new HashMap<>();
        playerMap.put("player1", "Human");
        playerMap.put("Player 2", "Human");
        playerMap.put("Player3", "CPU");
        int pointsToWin = 778;
        boolean stackable = true;

        assertTrue(controller.setGameParameters(version, playerMap, pointsToWin, stackable));
        assertTrue(controller.getGameVersion().equals(version));
        assertTrue(controller.getPlayerMap().equals(playerMap));
        assertTrue(controller.getPoints() == pointsToWin);
        assertTrue(controller.getStackable() == stackable);
        controller.setColorThemeFilepath(DEFAULT_COLOR_THEME_PATH);
        assertTrue(playNewGame());
        GameState expected = new GameState(version, playerMap, pointsToWin, stackable);
        assertTrue(expected.compareInitialParameters(controller.getModel()));
    }

    @Test
    void manuallySettingInvalidGameParameters(){
        String version1 = "Basic";
        Map<String, String> playerMap1 = new HashMap<>();
        int pointsToWin1 = 9;
        boolean stackable1 = false;
        assertFalse(controller.setGameParameters(version1, playerMap1, pointsToWin1, stackable1));
        assertFalse(playNewGame());

        String version2 = null;
        Map<String, String> playerMap2 = new HashMap<>();
        playerMap2.put("Adam", "CPU");
        playerMap2.put("Player 2", "Human");
        int pointsToWin2 = 99;
        boolean stackable2 = true;
        assertFalse(controller.setGameParameters(version2, playerMap2, pointsToWin2, stackable2));
        assertFalse(playNewGame());

        String version3 = "Basic";
        Map<String, String> playerMap3 = new HashMap<>();
        playerMap3.put("Drew", "Human");
        playerMap3.put("Quentin", "Human");
        playerMap3.put("Will Long", "CPU");
        int pointsToWin3 = -1;
        boolean stackable3 = false;
        assertFalse(controller.setGameParameters(version3, playerMap3, pointsToWin3, stackable3));
        assertFalse(playNewGame());
    }

    @Test
    void playingNewGameBeforeLoadingFileOrManuallySettingParameters(){
        assertFalse(controller.playNewGame("Traditional"));
    }

    @Test
    void returningToSplashScreenAfterPlayingNewGame(){
        loadingNewFile();
        runAsJFXAction(() -> controller.returnToSplashScreen());
        assertNull(controller.getModel());
        assertNull(controller.getUnoDisplay());
        assertNotNull(controller.getLanguageScreen());
    }

    @Test
    void savingAfterLoadingNewFile(){
        loadingNewFile();
        assertTrue(controller.saveCurrentFile(SAVE_FILENAME));
    }

    @Test
    void savingAfterManuallySettingParameters(){
        manuallySettingGameParameters();
        assertTrue(controller.saveCurrentFile(SAVE_FILENAME));
    }

    @Test
    void savingUsingInvalidFilename(){
        assertTrue(controller.loadFile(VALID_NEW_FILE_1_PATH));
        assertTrue(playNewGame());
        assertFalse(controller.saveCurrentFile(INVALID_SAVE_FILENAME));
    }

    @Test
    void reloadingSavedFile(){
        savingAfterLoadingNewFile();
        runAsJFXAction(() -> controller.returnToSplashScreen());
        assertTrue(controller.loadFile(SAVE_FILENAME_PATH));
    }

    @Test
    void loadingInvalidGameInProgressFile(){
        assertFalse(controller.loadFile(INVALID_GAME_IN_PROGRESS_FILE_1_PATH));
        assertFalse(playNewGame());
    }

    private void reloadingAndPlayingGameInProgress(String loadFilepath){
        assertTrue(controller.loadFile(loadFilepath));
        assertTrue(playNewGame());
        assertTrue(controller.saveCurrentFile(SAVE_FILENAME));
        simulateGame(10);
        GameState expected = controller.getModel();
        assertTrue(controller.saveCurrentFile(SAVE_FILENAME));
        runAsJFXAction(() -> controller.returnToSplashScreen());
        assertTrue(controller.loadFile(SAVE_FILENAME_PATH));
        assertTrue(controller.getModel().compareGameInProgressParameters(expected));
        simulateGame(10);
        assertFalse(controller.getModel().compareGameInProgressParameters(expected));
    }

    private void simulateGame(int numTurns){
        for(int i = 0; i < numTurns; i++){
            controller.getModel().playTurn();
        }
    }

    @Test
    void reloadingAndPlayingGameInProgress_Basic(){
        reloadingAndPlayingGameInProgress(BASIC_4CPUs_PATH);
    }

    @Test
    void reloadingAndPlayingGameInProgress_Blast(){
        reloadingAndPlayingGameInProgress(BLAST_4CPUs_PATH);
    }

    @Test
    void reloadingAndPlayingGameInProgress_Flip(){
        reloadingAndPlayingGameInProgress(FLIP_4CPUs_PATH);
    }
}