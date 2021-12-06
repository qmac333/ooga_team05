package ooga.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import ooga.controller.SplashScreenController;
import ooga.util.Config;
import ooga.view.table.Table;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class SplashScreen implements GameScreen {

  private static final String CSS_STYLE = "/ooga/resources/%s.css";

  private static final String LIGHT_MODE_FNAME = "splashScreen";
  private static final String DARK_MODE_FNAME = "splashScreenDark";
  private static final String SPRING_FNAME = "splashScreenSpring";
  private static final String FALL_FNAME = "splashScreenFall";
  private static final String UNC_FNAME = "splashScreenUnc";
  private static final String DUKE_FNAME = "splashScreenDuke";
  private static final String MAIN_LIGHT_MODE = "mainDisplay";
  private static final String MAIN_DARK_MODE = "mainDisplayDark";
  private static final String MAIN_UNC = "mainDisplayUnc";
  private static final String MAIN_DUKE = "mainDisplayDuke";
  private static final String MAIN_SPRING = "mainDisplaySpring";
  private static final String MAIN_FALL = "mainDisplayFall";

  private static final String UNO_LOGO = "./data/images/logos/Basic.png";

  public static final String PLAY_CSS_ID = "PlayButton";
  public static final String LOAD_NEW_GAME_CSS = "LoadNewGame";

  private static double CELL_HEIGHT = 30;
  private static double CELL_WIDTH = 100;

  private ResourceBundle languageResources;

  private VBox tableDisplay;
  private Text readyIndicator;

  private TableView<Player> playerTable;

  private boolean stackable;
  private String gameType;
  private Map<String, String> playerMap;

  private Scene scene;
  private boolean dark;
  private boolean unc;
  private boolean springColors;

  SplashScreenController controller;

  public SplashScreen(SplashScreenController controller, String language) {
    this.controller = controller;
    languageResources = ResourceBundle.getBundle(String.format("ooga.resources.%s", language));
    initializeTable();
    playerMap = new HashMap<>();
  }

  public Scene setScene() {
    BorderPane borderPane = new BorderPane();
    borderPane.setTop(addTopNode());
    borderPane.setLeft(addLeftNode());
    borderPane.setBottom(addBottomNode());
    borderPane.setRight(createRightNode());
    try {
      Image image = new Image(new FileInputStream(UNO_LOGO));
      ImageView imageView = new ImageView(image);
      imageView.setFitHeight(300);
      imageView.setFitWidth(300);
      borderPane.setCenter(imageView);
    } catch(Exception e) {
      // TODO: Logging for errors when file for image is not found
      System.out.println("Uno Logo Image not found");
    }

    scene = new Scene(borderPane, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
    scene.getStylesheets()
        .add(SplashScreen.class.getResource(String.format(CSS_STYLE, LIGHT_MODE_FNAME)).toExternalForm());
    controller.setColorTheme(String.format(CSS_STYLE, MAIN_LIGHT_MODE));
    return scene;
  }

  private HBox addTopNode() {
    HBox hbox = new HBox();
    hbox.getStyleClass().add("hbox");

    Text checkForUpdates = new Text(languageResources.getString("Title"));
    checkForUpdates.getStyleClass().add("text-title");
    hbox.getChildren().add(checkForUpdates);

    return hbox;
  }

  private VBox addLeftNode() {
    VBox root = new VBox();
    root.getStyleClass().add("vbox");

    Button darkMode = new Button(languageResources.getString("DarkMode"));
    darkMode.setOnAction(e -> changeColorMode(darkMode));

    Button schoolColors = new Button(languageResources.getString("UNC"));
    schoolColors.setOnAction(e -> changeSchool(schoolColors));

    Button seasonColors = new Button(languageResources.getString("SpringColors"));
    seasonColors.setOnAction(e -> changeFontSize(seasonColors));

    Button loadNew = new Button(languageResources.getString("LoadNew"));
    loadNew.setId(LOAD_NEW_GAME_CSS);
    loadNew.setOnAction(e -> chooseFile());

    root.getChildren().addAll(darkMode, schoolColors, seasonColors, new Separator(), loadNew);

    return root;
  }

  private void changeFontSize(Button button) {
    springColors = !springColors;
    if (springColors) {
      scene.getStylesheets().clear();
      scene.getStylesheets()
              .add(SplashScreen.class.getResource(String.format(CSS_STYLE, SPRING_FNAME)).toExternalForm());
      button.setText(languageResources.getString("FallColors"));
      controller.setColorTheme(String.format(CSS_STYLE, MAIN_SPRING));
    } else {
      scene.getStylesheets().clear();
      scene.getStylesheets()
              .add(SplashScreen.class.getResource(String.format(CSS_STYLE, FALL_FNAME)).toExternalForm());
      button.setText(languageResources.getString("SpringColors"));
      controller.setColorTheme(String.format(CSS_STYLE, MAIN_FALL));
    }
  }

  private void changeSchool(Button button) {
    unc = !unc;
    if (unc) {
      scene.getStylesheets().clear();
      scene.getStylesheets()
              .add(SplashScreen.class.getResource(String.format(CSS_STYLE, UNC_FNAME)).toExternalForm());
      button.setText(languageResources.getString("Duke"));
      controller.setColorTheme(String.format(CSS_STYLE, MAIN_UNC));

    } else {
      scene.getStylesheets().clear();
      scene.getStylesheets()
              .add(SplashScreen.class.getResource(String.format(CSS_STYLE, DUKE_FNAME)).toExternalForm());
      button.setText(languageResources.getString("UNC"));
      controller.setColorTheme(String.format(CSS_STYLE, MAIN_DUKE));

    }
  }

  private void changeColorMode(Button button) {
    dark = !dark;
    if (dark) {
      scene.getStylesheets().clear();
      scene.getStylesheets()
              .add(SplashScreen.class.getResource(String.format(CSS_STYLE, DARK_MODE_FNAME)).toExternalForm());
      button.setText(languageResources.getString("LightMode"));
      controller.setColorTheme(String.format(CSS_STYLE, MAIN_DARK_MODE));
    } else {
      scene.getStylesheets().clear();
      scene.getStylesheets()
              .add(SplashScreen.class.getResource(String.format(CSS_STYLE, LIGHT_MODE_FNAME)).toExternalForm());
      button.setText(languageResources.getString("DarkMode"));
      controller.setColorTheme(String.format(CSS_STYLE, MAIN_LIGHT_MODE));
    }
  }

  private void setGameHandler(TextField points) {
    try {
      int pointsToWin = Integer.parseInt(points.getText());
      boolean successfulSetup = controller.setGameParameters(gameType, playerMap, pointsToWin,
          stackable);
      if (successfulSetup) {
        readyIndicator.setText(languageResources.getString("SetParametersManual"));
      } else {
        showError(languageResources.getString("ValidValues"));
      }
    } catch (NumberFormatException e) {
      showError(languageResources.getString("InvalidPoints"));
    }
  }

  private void stack(Button button) {
    stackable = !stackable;
    if (!stackable) {
      button.setText(languageResources.getString("NoStack"));
    } else {
      button.setText(languageResources.getString("YesStack"));
    }
  }

  private void chooseFile() {
    FileChooser fileChooser = new FileChooser();
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)",
        "*.json");
    fileChooser.getExtensionFilters().add(extFilter);
    File selectedFile = fileChooser.showOpenDialog(null);
    if (selectedFile != null) {
      boolean successfulLoad = controller.loadFile(selectedFile.getAbsolutePath());
      if (successfulLoad) {
        readyIndicator.setText(languageResources.getString("SetParametersFile"));
      } else {
        showError(languageResources.getString("BadFileFormat"));
      }
    }
  }

  private VBox addBottomNode() {
    VBox root = new VBox();
    root.getStyleClass().add("hbox");

    Button playButton = new Button(languageResources.getString("Play"));
    playButton.getStyleClass().add("play-button");
    playButton.setId(PLAY_CSS_ID);
    playButton.setOnAction(e -> playNewGame());

    readyIndicator = new Text("");
    readyIndicator.getStyleClass().add("ready-indicator");

    root.getChildren().addAll(readyIndicator, playButton);
    return root;
  }

  private void playNewGame() {
    // TODO: create Mod dropdown (with default value of "Traditional") and retrieve value from that dropdown right here before passing it controller.playNewGame()
    String mod = "Traditional";
    boolean successfulPlay = controller.playNewGame(mod);
    if (!successfulPlay) {
      showError(languageResources.getString("PlayButtonEarly"));
    }
  }

  private void addNewPlayer(TextField nameInput, ChoiceBox<String> playerTypeInput) {
    // TODO: Error checking, restrict name to be 15 characters long maximum
    String name = nameInput.getText();
    String playerType = playerTypeInput.getValue();
    if (playerType == "Human" || playerType == "CPU") {
      if (!playerMap.containsKey(name)) {
        if (playerMap.size() < 11) {
          nameInput.clear();
          playerTable.getItems().add(new Player(name, playerType));
          playerMap.put(name, playerType);
        } else {
          showError(languageResources.getString("MaxPlayers"));
        }
      } else {
        showError(languageResources.getString("DuplicateName"));
      }
    } else {
      showError(languageResources.getString("SelectType"));
    }
  }

  private VBox createRightNode() {
    VBox table = new VBox();
    table.getStyleClass().add("vbox");

    TextField points = new TextField();
    points.setPromptText(languageResources.getString("PointsInput"));

    ChoiceBox<String> game = new ChoiceBox<>();
    game.setValue(languageResources.getString("GameHeader"));
    game.getItems().add(languageResources.getString("Basic"));
    game.getItems().add(languageResources.getString("Flip"));
    game.getItems().add(languageResources.getString("Blast"));
    game.setOnAction(e -> gameType = translateToEnglish(game.getValue()));

    Button stackCards = new Button(languageResources.getString("NoStack"));
    stackCards.setOnAction(e -> stack(stackCards));

    Button setGame = new Button(languageResources.getString("GameParameters"));
    setGame.setOnAction(e -> setGameHandler(points));

    TextField nameInput = new TextField();
    nameInput.setPromptText(languageResources.getString("NameInput"));

    ChoiceBox<String> playerTypeInput = new ChoiceBox<>();
    playerTypeInput.setValue(languageResources.getString("TypeInput"));
    playerTypeInput.getItems().add("Human");
    playerTypeInput.getItems().add("CPU");

    Button addPlayer = new Button(languageResources.getString("AddPlayer"));
    addPlayer.setOnAction(e -> addNewPlayer(nameInput, playerTypeInput));

    table.getChildren().addAll(points, game, stackCards, new Separator(),
        nameInput, playerTypeInput, addPlayer, new Separator(), tableDisplay, setGame);

    return table;
  }

  private void initializeTable() {
    tableDisplay = new VBox();
    tableDisplay.getStyleClass().add("vbox");
    playerTable = new TableView<>();

    TableColumn<Player, String> playerNameCol = new TableColumn<>(
        languageResources.getString("TableHeaderLeft"));
    TableColumn<Player, String> playerTypeCol = new TableColumn<>(
        languageResources.getString("TableHeaderMiddle"));
    playerNameCol.setMinWidth(150);
    playerTypeCol.setMinWidth(150);
    playerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    playerTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
    playerTable.getColumns().addAll(playerNameCol, playerTypeCol);

    Button deleteButton = new Button(languageResources.getString("Delete"));
    deleteButton.getStyleClass().add("delete-button");
    deleteButton.setOnMousePressed(e -> {
      Player selectedPlayer = playerTable.getSelectionModel().getSelectedItem();
      if (selectedPlayer != null) {
        playerTable.getItems().remove(selectedPlayer);
        playerMap.remove(selectedPlayer.getName());
      }
    });

    tableDisplay.getChildren().addAll(playerTable, deleteButton);
  }

  private String translateToEnglish(String value) {
    for (String key : languageResources.keySet()) {
      if (Objects.equals(languageResources.getString(key), value)) {
        return key;
      }
    }
    return null;
  }

  // displays alert/error message to the user
  private void showError(String alertMessage) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setContentText(alertMessage);
    alert.show();
  }

  public class Player {

    private String playerType;
    private String name;

    public Player(String name, String type) {
      playerType = type;
      this.name = name;

    }

    public String getType() {
      return playerType;
    }

    public String getName() {
      return name;
    }


  }

}
