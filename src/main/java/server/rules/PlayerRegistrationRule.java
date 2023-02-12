package server.rules;

import java.util.Set;

import messagesBase.UniqueGameIdentifier;
import messagesBase.UniquePlayerIdentifier;
import messagesBase.messagesFromClient.PlayerHalfMap;
import messagesBase.messagesFromClient.PlayerRegistration;
import server.exceptions.RegistrationException;
import server.gameData.Player;
import server.gameData.SingleGame;
import server.interfaces.IRule;

public class PlayerRegistrationRule implements IRule {

	@Override
	public void handleRegistration(Set<Player> players, String gameID) {

		if (players.size() == 2) {

			throw new RegistrationException("Name: RegistrationException", "Message: Already 2 Players registered");
		}

	}

	public UniqueGameIdentifier handleNewGame() {
		return null;
		// throw new UnsupportedOperationException("handleNewGame is not supportet in
		// this class");
	}

	public void handleGameState(SingleGame gameID, String playerID) {

		// throw new UnsupportedOperationException("handleGameState is not supportet in
		// this class");
	}

	public UniquePlayerIdentifier handleRegistration(PlayerRegistration reg, UniqueGameIdentifier gameID) {
		return null;
		// new UnsupportedOperationException("handleRegistration is not supportet in
		// this class");
	}

	public void handleHalfMap(UniqueGameIdentifier gameID, PlayerHalfMap halfMap, SingleGame game) {
		// TODO Auto-generated method stub
		// throw new UnsupportedOperationException("handleHalfMap is not supportet in
		// this class");
	}
}
