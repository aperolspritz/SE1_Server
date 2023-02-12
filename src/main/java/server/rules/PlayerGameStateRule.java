package server.rules;

import java.util.Set;

import messagesBase.UniqueGameIdentifier;
import messagesBase.UniquePlayerIdentifier;
import messagesBase.messagesFromClient.PlayerHalfMap;
import messagesBase.messagesFromClient.PlayerRegistration;
import messagesBase.messagesFromServer.EPlayerGameState;
import server.exceptions.GameException;
import server.gameData.Player;
import server.gameData.SingleGame;
import server.interfaces.IRule;

public class PlayerGameStateRule implements IRule {

	@Override
	public UniqueGameIdentifier handleNewGame() {
		return null;
		// throw new UnsupportedOperationException("UniqueGameIdentifier is not
		// supportet in this class");
	}

	@Override
	public UniquePlayerIdentifier handleRegistration(PlayerRegistration reg, UniqueGameIdentifier gameID) {
		return null;
		// throw new UnsupportedOperationException("handleRegistration is not supportet
		// in this class");

	}

	@Override
	public void handleRegistration(Set<Player> game, String PlayerID) {
		// throw new UnsupportedOperationException("handleRegistration is not supportet
		// in this class");

	}

	@Override
	public void handleHalfMap(UniqueGameIdentifier gameID, PlayerHalfMap halfMap, SingleGame game) {
		// throw new UnsupportedOperationException("handleHalfMap is not supportet in
		// this class");

	}

	@Override
	public void handleGameState(SingleGame game, String playerID) {

		if (game.getPlayer(playerID).getPlayerState().equals(EPlayerGameState.MustWait)) {
			game.setWinner(playerID);
			throw new GameException("Name: Game Exception.",
					"Message: Player sent Action while was on MustWait, which leads to losing the Game", playerID);
		}
		if (game.getPlayer(playerID).getPlayerActionCount().equals(320)) {
			game.setWinner(playerID);
			throw new GameException("Name: Game Exception.", "Message: Player sent too many Actions. Limit 320",
					playerID);

		}

	}

}
