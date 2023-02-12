package server.management;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import messagesBase.UniqueGameIdentifier;
import messagesBase.UniquePlayerIdentifier;
import server.exceptions.GameException;
import server.exceptions.RegistrationException;
import server.gameData.Player;
import server.gameData.SingleGame;

public class IdManager {
	protected final static Logger logger = LoggerFactory.getLogger(IdManager.class);

	public boolean gameIdValidation(UniqueGameIdentifier gameID, List<SingleGame> games) {

		if (games == null || gameID == null) {
			logger.info("Parsed paramater in getGame was null");
			throw new GameException("Name: GameException", "Message: Parsed paramater in getGame was null");
		}

		boolean gameIsValid = false;

		for (SingleGame curr : games) {
			if (curr.getGameID().equals(gameID)) {
				gameIsValid = true;
				break;
			}
		}
		if (!gameIsValid) {
			throw new GameException("Name: GameException", "Message: GameID does not exist");
		}

		return gameIsValid;
	}

	public SingleGame getGame(UniqueGameIdentifier gameID, List<SingleGame> games) {

		if (games == null || gameID == null) {
			logger.info("Parsed paramater in getGame was null");
			throw new GameException("Name: GameException", "Message: Parsed paramater in getGame was null");
		}

		SingleGame game = new SingleGame();

		for (SingleGame curr : games) {
			if (curr.getGameID().equals(gameID)) {
				game = curr;
				break;
			}
		}
		return game;
	}

	public boolean verifyPlayerId(UniquePlayerIdentifier playerID, SingleGame game) {

		if (playerID == null) {
			throw new GameException("Name: GameException", "Message: PlayerID must not be null");
		}

		if (game == null) {
			throw new GameException("Name: GameException", "Message: Game must not be null");
		}

		Set<Player> players = game.getPlayers();
		boolean isRegistered = false;
		if (players.size() == 0) {
			return false;
		}

		for (Player curr : players) {
			if (playerID.equals(curr.getPlayerID())) {
				isRegistered = true;
				break;
			}
		}

		if (!isRegistered)
			throw new RegistrationException("Name: RegistrationException", "Message: Player must be registered");
		return isRegistered;

	}

}
