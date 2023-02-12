package server.rules;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import messagesBase.UniqueGameIdentifier;
import messagesBase.UniquePlayerIdentifier;
import messagesBase.messagesFromClient.PlayerHalfMap;
import messagesBase.messagesFromClient.PlayerRegistration;
import server.exceptions.GameException;
import server.gameData.Player;
import server.gameData.SingleGame;
import server.interfaces.IRule;

public class PlayerActionConsiderTimeRule implements IRule {
	// TAKEN FROM <3>
	// https://docs.oracle.com/javase/8/docs/api/java/time/Instant.html

	@Override
	public void handleGameState(SingleGame game, String playerID) {

		// TAKEN FROM BEGIN <3>
		if (game.getActionTimer().until(Instant.now(), ChronoUnit.SECONDS) > 0.5) {
			// TAKEN FROM END <3>
			game.setWinner(playerID);
			throw new GameException("Name: Game Exception.", "Message: Player took too long to send next Action",
					playerID);
		}

	}

	public UniqueGameIdentifier handleNewGame() {
		// TODO Auto-generated method stub
		return null;
	}

	public UniquePlayerIdentifier handleRegistration(PlayerRegistration reg, UniqueGameIdentifier gameID) {
		// TODO Auto-generated method stub
		return null;
	}

	public void handleRegistration(Set<Player> game, String PlayerID) {
		// TODO Auto-generated method stub

	}

	public void handleHalfMap(UniqueGameIdentifier gameID, PlayerHalfMap halfMap, SingleGame game) {
		// TODO Auto-generated method stub

	}

}
