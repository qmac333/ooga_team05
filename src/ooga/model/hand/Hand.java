package ooga.model.hand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import ooga.model.cards.CardInterface;
import ooga.model.gameState.GameStatePlayerInterface;
import ooga.model.player.playerGroup.PlayerGroupPlayerInterface;
import ooga.model.player.player.PlayerCardInterface;
import org.jetbrains.annotations.NotNull;

public class Hand implements Iterable<CardInterface>, HandInterface {

  private static final String BUNDLE_PATH = "ooga.model.hand.resources.HandResources";
  private static final String TOO_LARGE = "InvalidInput";
  private static final String DEFAULT = "DefaultColor";
  private static final String IGNORE = "ColorToIgnore";

  private static final ResourceBundle handResources = ResourceBundle.getBundle(BUNDLE_PATH);

  private final List<CardInterface> myCards;

  public Hand() {
    myCards = new ArrayList<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void add(Collection<CardInterface> card) {
    myCards.addAll(card);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Deprecated
  public void play(int indexOfCard, GameStatePlayerInterface game)
      throws InvalidCardSelectionException {
    // Do Nothing
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void play(int indexOfCard, PlayerGroupPlayerInterface group, PlayerCardInterface player)
      throws InvalidCardSelectionException {
    if (indexOfCard >= myCards.size()) {
      throw new InvalidCardSelectionException(
          String.format(handResources.getString(TOO_LARGE), indexOfCard));
    }
    myCards.get(indexOfCard).executeAction(player);
    group.discardCard(myCards.get(indexOfCard));
    myCards.remove(indexOfCard);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void flip() {
    for (CardInterface card : myCards) {
      card.flip();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int size() {
    return myCards.size();
  }

  /**
   * {@inheritDoc}
   */
  @NotNull
  @Override
  public Iterator<CardInterface> iterator() {
    return new HandIterator();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<CardInterface> removeColor(String color){
    List<CardInterface> removed = new ArrayList<>();
    for (CardInterface card : myCards){
      if (card.getMyColor().equals(color)){
        removed.add(card);
      }
    }
    myCards.removeAll(removed);
    return removed;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMaxColor() {
    Map<String, Integer> map = new HashMap<>();
    for (CardInterface card : myCards){
      if (!card.getMyColor().equals(handResources.getString(IGNORE))) {
        map.putIfAbsent(card.getMyColor(), 0);
        map.put(card.getMyColor(), map.get(card.getMyColor()) + 1);
      }
    }
    int max = -1;
    String maxColor = handResources.getString(DEFAULT);
    for (String color : map.keySet()){
      if (map.get(color) > max){
        maxColor = color;
        max = map.get(color);
      }
    }
    return maxColor;
  }

  /**
   * Used to compare two Hands - TESTING PURPOSES ONLY
   * @return list of cards
   */
  public List<CardInterface> getMyCards() {
    return myCards;
  }

  // Class used to allow us to iterate through the hand
  private class HandIterator implements Iterator<CardInterface> {

    private int position = 0;

    @Override
    public boolean hasNext() {
      return position < myCards.size();
    }

    @Override
    public CardInterface next() {
      if (hasNext()) {
        return myCards.get(position++);
      }
      return null;
    }
  }
}
