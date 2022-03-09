import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StrangeGameTest {
  @Test
  void test_a() {
    Player player = new Player("notorious", -1);

    StrangeGame strangeGame = new StrangeGame();

    Prison prisonStub = spy(Prison.class);
    strangeGame.setPrison(prisonStub);

    strangeGame.hour = mock(Hour.class);
    when(strangeGame.hour.getHour()).thenReturn(0);

    try {
      assertEquals("The game is not yet open!", strangeGame.enterGame(player));
    } catch (Exception e) {
    }

    verify(prisonStub, never()).crime(player);
  }

  @Test
  void test_b() {
    Player player = new Player("notorious", -1);

    StrangeGame strangeGame = new StrangeGame();

    Prison prisonStub = spy(Prison.class);
    try {
      doNothing().when(prisonStub).imprisonment(any(Player.class));
    } catch (Exception e) {
    }
    strangeGame.setPrison(prisonStub);

    strangeGame.hour = mock(Hour.class);
    when(strangeGame.hour.getHour()).thenReturn(12);

    try {
      assertEquals(
          "After a long period of punishment, the player can leave! :)",
          strangeGame.enterGame(player));
    } catch (Exception e) {
    }

    verify(prisonStub, times(1)).crime(player);
  }

  @Test
  void test_c() {
    Player player1 = new Player("p1", -1);
    Player player2 = new Player("p2", -1);
    Player player3 = new Player("p3", -1);

    StrangeGame strangeGame = new StrangeGame();

    Prison prisonStub = spy(Prison.class);
    try {
      doNothing().when(prisonStub).imprisonment(any(Player.class));
    } catch (Exception e) {
    }
    strangeGame.setPrison(prisonStub);

    strangeGame.hour = mock(Hour.class);
    when(strangeGame.hour.getHour()).thenReturn(12);

    try {
      strangeGame.enterGame(player1);
      strangeGame.enterGame(player2);
      strangeGame.enterGame(player3);
    } catch (Exception e) {
    }

    assertEquals(Arrays.asList("p1", "p2", "p3"), prisonStub.getLog());
  }

  @Test
  void test_d() {
    GAMEDb dbStub = mock(GAMEDb.class);
    when(dbStub.getScore("0710734")).thenReturn(100);

    StrangeGame strangeGame = new StrangeGame();
    strangeGame.db = dbStub;

    assertEquals(100, strangeGame.getScore("0710734"));
  }

  @Test
  void test_e() {
    StrangeGame strangeGame = new StrangeGame();
    FakePayPalService fakePayPalService = new FakePayPalService();

    fakePayPalService.result = "Success";
    assertEquals("Thank you", strangeGame.donate(fakePayPalService));

    fakePayPalService.result = "Failure";
    assertEquals("Some errors occurred", strangeGame.donate(fakePayPalService));
  }
}

class FakePayPalService implements paypalService {
  String result;

  @Override
  public String doDonate() {
    return result;
  }
}
