package ooga.view;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class CardDisplay {

    private static final ResourceBundle imageResources = ResourceBundle.getBundle(
            "ooga.view.ImageFiles");

    private static Map<String, Image> IMAGES;

    private static final Map<String, Color> COLORS = Map.of(
            "blue", Color.BLUE,
            "red", Color.RED,
            "green", Color.GREEN,
            "yellow", Color.YELLOW
    );

    private static Color DEFAULT_COLOR = Color.BLACK;
    private static double CARD_WIDTH = 60;
    private static double CARD_HEIGHT = 100;
    private static double CARD_OFFSET = 10;

    private StackPane cardDisplay;

    public CardDisplay(String number, String type, String color) {
        cardDisplay = new StackPane();
        Rectangle base = new Rectangle(CARD_WIDTH+CARD_OFFSET, CARD_HEIGHT+CARD_OFFSET);
        base.setFill(DEFAULT_COLOR);
        Rectangle card = new Rectangle(CARD_WIDTH, CARD_HEIGHT);
        card.setFill(COLORS.get(color));

        String loadFileString;
        if (type.equals("Number")) {
            loadFileString = String.valueOf(number);
        }
        else {
            loadFileString = type;
        }

        Image image = IMAGES.get(loadFileString);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(CARD_WIDTH);
        imageView.setFitWidth(CARD_HEIGHT/2);

        cardDisplay.getChildren().addAll(base, card, imageView);
    }

    public Node getCard() {
        return cardDisplay;
    }

    public static void initializeCards() {
        try {
            IMAGES = new HashMap<>();
            for (String key : imageResources.keySet()) {
                IMAGES.put(key, new Image(new FileInputStream(imageResources.getString(key))));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
