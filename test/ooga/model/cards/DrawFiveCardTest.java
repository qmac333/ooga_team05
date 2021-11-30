package ooga.model.cards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import ooga.model.gameState.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class DrawFiveCardTest {

  GameState realGame;
  DrawFiveCard dfc;

  @Mock
  GameState gameMocked;

  @BeforeEach
  void start() {
    gameMocked = mock(GameState.class);
    realGame = new GameState();
    dfc = new DrawFiveCard("red", null);
  }

  @Test
  void callsTheAddDrawWithCorrectArgument() {
    dfc.executeAction(gameMocked);
    verify(gameMocked, times(1)).addDraw(5);
  }

  @Test
  void defaultsProperlySet() {
    assertEquals("DrawFive", dfc.getType());
    assertEquals(20, dfc.getNum());
  }

  @Test
  void colorSets() {
    assertEquals("red", dfc.getMyColor());
  }

  @Test
  void successfullyDiscards() {
    dfc.executeAction(realGame);
    assertEquals("DrawFive", realGame.getLastCardThrownType());
  }
}
