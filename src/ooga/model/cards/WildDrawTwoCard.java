package ooga.model.cards;

import java.util.function.Supplier;
import ooga.model.gameState.GameStatePlayerInterface;

/**
 * Card that allows user to choose color and makes the next player draw 2
 *
 * @author Paul Truitt
 */
public class WildDrawTwoCard extends Card {

  private final int DRAW_AMOUNT = 2;

  public WildDrawTwoCard(String color, Supplier<String> supplier) {
    super("Black", "WildDrawTwo", 50, supplier);
  }

  @Override
  public void executeAction(GameStatePlayerInterface game) {
    game.addDraw(DRAW_AMOUNT);
    super.setCardColor(super.getSupplier().get());
    game.discardCard(this);
  }

  @Override
  public void flip() {
    // Do nothing
  }
}
