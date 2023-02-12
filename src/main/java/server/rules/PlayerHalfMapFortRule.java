package server.rules;

import java.util.Set;

import messagesBase.UniqueGameIdentifier;
import messagesBase.UniquePlayerIdentifier;
import messagesBase.messagesFromClient.ETerrain;
import messagesBase.messagesFromClient.PlayerHalfMap;
import messagesBase.messagesFromClient.PlayerRegistration;
import server.exceptions.HalfMapException;
import server.gameData.Player;
import server.gameData.SingleGame;
import server.interfaces.IRule;
import server.validation.HalfMapValidator;

public class PlayerHalfMapFortRule implements IRule {

	public UniqueGameIdentifier handleNewGame() {
		return null;
		// throw new UnsupportedOperationException("handleNewGame is not supportet in
		// this class");
	}

	public UniquePlayerIdentifier handleRegistration(PlayerRegistration reg, UniqueGameIdentifier gameID) {
		return null;
		// throw new UnsupportedOperationException("handleRegistration is not supportet
		// in this class");
	}

	public void handleRegistration(Set<Player> game, String PlayerID) {
		// TODO Auto-generated method stub
		// throw new UnsupportedOperationException("handleRegistration is not supportet
		// in this class");
	}

	@Override
	public void handleHalfMap(UniqueGameIdentifier gameID, PlayerHalfMap halfMap, SingleGame game) {
		HalfMapValidator halfMapValidator = new HalfMapValidator();

		if (halfMapValidator.checkCorrectFortAmount(halfMap) != 1) {
			game.setWinner(halfMap.getUniquePlayerID());
			throw new HalfMapException("Name: HalfMapException", "Message: Incorrect Fort Amount",
					halfMapValidator.checkCorrectFortAmount(halfMap));
		}

		if (!halfMapValidator.fortOnGras(halfMap).getTerrain().equals(ETerrain.Grass)) {
			game.setWinner(halfMap.getUniquePlayerID());
			throw new HalfMapException("Name: HalfMapException", "Message: Incorrect Fort Placement",
					halfMapValidator.fortOnGras(halfMap).getTerrain().toString());
		}

	}

	public void handleGameState(SingleGame gameID, String playerID) {

		// throw new UnsupportedOperationException("handleGameState is not supportet in
		// this class");
	}

}
