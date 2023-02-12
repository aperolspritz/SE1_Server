package server.rules;

import java.util.Set;

import messagesBase.UniqueGameIdentifier;
import messagesBase.UniquePlayerIdentifier;
import messagesBase.messagesFromClient.PlayerHalfMap;
import messagesBase.messagesFromClient.PlayerRegistration;
import server.exceptions.HalfMapException;
import server.gameData.Player;
import server.gameData.SingleGame;
import server.interfaces.IRule;
import server.validation.HalfMapValidator;

public class PlayerHalfMapFieldRule implements IRule {

	public UniqueGameIdentifier handleNewGame() {
		return null;
		// new UnsupportedOperationException("handleNewGame is not supportet in this
		// class");
	}

	public UniquePlayerIdentifier handleRegistration(PlayerRegistration reg, UniqueGameIdentifier gameID) {
		return null;
		// new UnsupportedOperationException("handleRegistration is not supportet in
		// this class");
	}

	public void handleRegistration(Set<Player> game, String PlayerID) {
		// TODO Auto-generated method stub
		// throw new UnsupportedOperationException("handleRegistration is not supportet
		// in this class");
	}

	@Override
	public void handleHalfMap(UniqueGameIdentifier gameID, PlayerHalfMap halfMap, SingleGame game) {

		HalfMapValidator halfMapValidator = new HalfMapValidator();

		if (!halfMapValidator.checkFieldAmount(halfMap)) {
			game.setWinner(halfMap.getUniquePlayerID());
			throw new HalfMapException("Name: HalfMapException", "Message: Incorrect Amount of Nodes in HalfMap");
		}

		if (halfMapValidator.checkGrasFieldAmount(halfMap) < 24) {
			game.setWinner(halfMap.getUniquePlayerID());
			throw new HalfMapException("Name: HalfMapException", "Message: Incorrect Amount of  Gras Fields in HalfMap",
					halfMapValidator.checkGrasFieldAmount(halfMap));

		}
		if (halfMapValidator.checkWaterFieldAmount(halfMap) < 7) {
			game.setWinner(halfMap.getUniquePlayerID());
			throw new HalfMapException("Name: HalfMapException", "Message: Incorrect Amount of Water Fields in HalfMap",
					halfMapValidator.checkWaterFieldAmount(halfMap));
		}
		if (halfMapValidator.checkMountainFieldAmount(halfMap) < 5) {
			game.setWinner(halfMap.getUniquePlayerID());
			throw new HalfMapException("Name: HalfMapException",
					"Message: Incorrect Amount of Mountain Fields in HalfMap",
					halfMapValidator.checkMountainFieldAmount(halfMap));
		}

		if (!halfMapValidator.checkIfCoordinatesAreCoorect(halfMap)) {
			game.setWinner(halfMap.getUniquePlayerID());
			throw new HalfMapException("Name: HalfMapException", "Message: Incorrect Coordinates");
		}

		// TODO Auto-generated method stub

	}

	public void handleGameState(SingleGame gameID, String playerID) {

		// throw new UnsupportedOperationException("handleGameState is not supportet in
		// this class");
	}
}
