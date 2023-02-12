package server.management;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import messagesBase.UniquePlayerIdentifier;
import messagesBase.messagesFromServer.GameState;
import messagesBase.messagesFromServer.PlayerState;
import server.exceptions.GameException;
import server.gameData.Player;
import server.gameData.SingleGame;

public class Converter {
	protected final static Logger logger = LoggerFactory.getLogger(Converter.class);

	public String convertUniquePlayerIdentifierToString(UniquePlayerIdentifier playerID) {

		logger.info("convertUniquePlayerIdentifierToString");
		if (playerID == null) {
			logger.info("Parsed paramater in convertUniquePlayerIdentifierToString was null");
			throw new GameException("Name: GameException",
					"Message: Parsed paramater in convertUniquePlayerIdentifierToString was null");
		}
		return playerID.getUniquePlayerID();

	}

	public UniquePlayerIdentifier convertStringToUniquePlayerIdentifier(String playerID) {
		UniquePlayerIdentifier uniquePlayerID = new UniquePlayerIdentifier(playerID);
		return uniquePlayerID;
	}

	public GameState convertToGameState(SingleGame game, String playerID) {
		logger.info("convertToGameState");
		if (game == null || playerID == null) {
			logger.info("Parsed paramater in convertToGameState was null");
			throw new GameException("Name: GameException", "Message: Parsed paramater in convertToGameState was null");
		}

		Set<PlayerState> playerStates = new HashSet<PlayerState>();

		for (Player curr : game.getPlayers()) {
			if (playerID.equals(curr.getPlayerID().getUniquePlayerID())) {
				PlayerState playerState = new PlayerState(curr.getPlayerRegistration().getStudentFirstName(),
						curr.getPlayerRegistration().getStudentLastName(),
						curr.getPlayerRegistration().getStudentUAccount(), curr.getPlayerState(), curr.getPlayerID(),
						false);
				playerStates.add(playerState);
			} else {
				UniquePlayerIdentifier fakeID = new UniquePlayerIdentifier("p sherman wallaby 42 sidney");
				PlayerState playerState = new PlayerState(curr.getPlayerRegistration().getStudentFirstName(),
						curr.getPlayerRegistration().getStudentLastName(),
						curr.getPlayerRegistration().getStudentUAccount(), curr.getPlayerState(), fakeID, false);
				playerStates.add(playerState);
			}
		}

		GameState gameState = new GameState(Optional.ofNullable(game.getFullMap().get(playerID)), playerStates,
				game.getGameStateID());
		return gameState;

	}

}
