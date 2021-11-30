package ooga.model.cards;

import ooga.model.gameState.GameStatePlayerInterface;
import ooga.model.player.PlayerInterface;

/**
 * Card to force next player to draw one card
 *
 * @author Paul Truitt
 */
public class DrawOneCard extends Card {

  private final int DRAW_AMOUNT = 1;

  public DrawOneCard(String color) {
    super(color, "DrawOne", 10);
  }

  @Override
  @Deprecated
  public void executeAction(GameStatePlayerInterface game) {
    game.addDraw(DRAW_AMOUNT);
  }

  @Override
  public void executeAction(PlayerInterface player) {
    player.enforceDraw(DRAW_AMOUNT);
  }

  @Override
  public void flip() {
    // Do nothing
  }
}
