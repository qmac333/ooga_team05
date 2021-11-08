package ooga.model;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Interface implemented by the GameState Class. Provides an API to
 * be used by the Controller for accessing data in the model.
 */
public interface GameStateInterface {

    /**
     * makes the currentPlayer member play its turn
     */
    public void playTurn();

    /**
     * returns the player index whose turn it is to play
     * @return index of the current player
     */
    public int getCurrentPlayer();


    /**
     * returns a map of card types to the different Colors and Types of cards
     * that exist in a players hand with that number
     * @return a Map of Integers to Lists of Strings
     */
    public Map<Integer, List<List<String>>> getCurrentPlayerCards();
}