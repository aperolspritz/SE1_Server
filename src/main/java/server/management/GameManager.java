package server.management;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import messagesBase.UniqueGameIdentifier;
import messagesBase.UniquePlayerIdentifier;
import messagesBase.messagesFromClient.PlayerHalfMap;
import messagesBase.messagesFromClient.PlayerRegistration;
import messagesBase.messagesFromServer.GameState;
import server.exceptions.GameException;
import server.gameData.Player;
import server.gameData.SingleGame;
import server.gameID.GameIDGenerator;
import server.validation.GameStateValidator;

public class GameManager {

	protected final static Logger logger = LoggerFactory.getLogger(GameManager.class);
	public List<SingleGame> games = new ArrayList<SingleGame>();
	private Converter converter = new Converter();
	private IdManager idManager = new IdManager();

	public List<SingleGame> getGames() {
		return this.games;
	}

	public UniqueGameIdentifier handleNewGame() {

		UniqueGameIdentifier gameID = GameIDGenerator.generateGameID();
		SingleGame game = new SingleGame(gameID);
		GameStateValidator gameStateValidator = new GameStateValidator();

		if (games.size() == 999) {
			logger.info("Game with GameID: " + games.get(0).getGameID().toString() + "was removed from the Game");
			games.remove(0);
		}

		games.add(game);
		logger.info("New Game was added to List. Running Games: " + games.size());
		gameStateValidator.checkGameIsTenMinutesOld(games);
		return gameID;
	}

	public UniquePlayerIdentifier handleRegistration(PlayerRegistration reg, UniqueGameIdentifier gameID) {

		if (reg == null || gameID == null) {
			throw new GameException("Name: GameException",
					"Message: parsed Paramater in Registration Endpoint was null");
		}

		logger.info("handleRegistration Data processing");
		idManager.gameIdValidation(gameID, games);
		SingleGame game = idManager.getGame(gameID, games);
		Player player = game.registerPlayerToGame(reg, game);

		return player.getPlayerID();
	}

	public boolean handleHalfMap(UniqueGameIdentifier gameID, PlayerHalfMap halfMap) {
		if (gameID == null || halfMap == null) {
			throw new GameException("Name: GameException", "Message: parsed Paramater in HalfMap Endpoint was null");
		}
		logger.info("handleHalfMap Data processing");
		boolean ret = true;
		GameStateValidator gameStateValidator = new GameStateValidator();

		idManager.gameIdValidation(gameID, games);
		String playerID = halfMap.getUniquePlayerID();

		SingleGame game = idManager.getGame(gameID, games);
		idManager.verifyPlayerId(converter.convertStringToUniquePlayerIdentifier(playerID), game);

		game.checkIfPlayerAlreadySentMap(halfMap.getUniquePlayerID());
		gameStateValidator.checkEPlayerStates(game, playerID);
		game.addHalfMap(halfMap, game);
		game.mergeHalfMaps();

		return ret;
	}

	public GameState handleGameState(UniqueGameIdentifier gameID, UniquePlayerIdentifier playerID) {
		if (gameID == null || playerID == null) {
			throw new GameException("Name: GameException", "Message: parsed Paramater in GameState Endpoint was null");
		}

		logger.info("handleGameState Data processing");

		logger.info("gameIdValidation Data processing");
		idManager.gameIdValidation(gameID, games);
		logger.info("idManager.getGame Data processing");
		SingleGame game = idManager.getGame(gameID, games);
		logger.info("idManager.verifyPlayerId Data processing");
		idManager.verifyPlayerId(playerID, game);
		logger.info("chooseRandomPlayerToSendNextAction Data processing");
		game.chooseRandomPlayerToSendNextAction();
		logger.info(" gameState = converter Data processing");
		GameState gameState = converter.convertToGameState(game, playerID.getUniquePlayerID());
		return gameState;
	}

}
