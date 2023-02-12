package server.validation;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;

import server.gameData.SingleGame;
import server.rules.PlayerGameStateRule;

public class GameStateValidator {

	// TAKEN FROM<2>
	// https://docs.oracle.com/javase/8/docs/api/java/time/Instant.html

	public void checkEPlayerStates(SingleGame game, String playerID) {

		PlayerGameStateRule gameStateRule = new PlayerGameStateRule();
		gameStateRule.handleGameState(game, playerID);
	}

	public void checkGameIsTenMinutesOld(List<SingleGame> games) {

		Iterator<SingleGame> iterator = games.iterator();
		while (iterator.hasNext()) {
			SingleGame game = iterator.next();
			// TAKEN FROM START<2>
			long time = game.getInstantTime().until(Instant.now(), ChronoUnit.MINUTES);
			if (time >= 10)
				iterator.remove();
		}
		// TAKEN FROM END <2>

	}
}
