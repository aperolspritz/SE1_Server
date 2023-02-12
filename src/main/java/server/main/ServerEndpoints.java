package server.main;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import messagesBase.ResponseEnvelope;
import messagesBase.UniqueGameIdentifier;
import messagesBase.UniquePlayerIdentifier;
import messagesBase.messagesFromClient.ERequestState;
import messagesBase.messagesFromClient.PlayerHalfMap;
import messagesBase.messagesFromClient.PlayerRegistration;
import messagesBase.messagesFromServer.GameState;
import server.exceptions.GenericExampleException;
import server.management.GameManager;

@RestController
@RequestMapping(value = "/games")
public class ServerEndpoints {

	private GameManager gameManager = new GameManager();

	// Endpoint newGame
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody UniqueGameIdentifier newGame(
			@RequestParam(required = false, defaultValue = "false", value = "enableDebugMode") boolean enableDebugMode,
			@RequestParam(required = false, defaultValue = "false", value = "enableDummyCompetition") boolean enableDummyCompetition) {
		return gameManager.handleNewGame();
	}

//Endpoint registerPlayer
	@RequestMapping(value = "/{gameID}/players", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<UniquePlayerIdentifier> registerPlayer(
			@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @RequestBody PlayerRegistration playerRegistration) {

		UniquePlayerIdentifier newPlayerID = gameManager.handleRegistration(playerRegistration, gameID);
		ResponseEnvelope<UniquePlayerIdentifier> playerIDMessage = new ResponseEnvelope<>(newPlayerID);
		return playerIDMessage;
	}

	// Endpoint HalfMap
	@RequestMapping(value = "/{gameID}/halfmaps", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope validatePlayerHalfMap(@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @RequestBody PlayerHalfMap playerHalfMap) {

		gameManager.handleHalfMap(gameID, playerHalfMap);

		ResponseEnvelope<ERequestState> playerIDMessage = new ResponseEnvelope<>(ERequestState.Okay);
		return playerIDMessage;

	}

	@RequestMapping(value = "/{gameID}/states/{playerID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<GameState> gameState(@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @PathVariable UniquePlayerIdentifier playerID) {

		GameState gameState = new GameState();
		gameState = gameManager.handleGameState(gameID, playerID);

		ResponseEnvelope<GameState> playerIDMessage = new ResponseEnvelope<>(gameState);
		return playerIDMessage;

	}

	@ExceptionHandler({ GenericExampleException.class })
	public @ResponseBody ResponseEnvelope<?> handleException(GenericExampleException ex, HttpServletResponse response) {
		ResponseEnvelope<?> result = new ResponseEnvelope<>(ex.getErrorName(), ex.getMessage());

		// reply with 200 OK as defined in the network documentation
		// Side note: We only do this here for simplicity reasons. For future projects,
		// you should check out HTTP status codes and
		// what they can be used for. Note, the WebClient used during the Client
		// implementation can react
		// to them using the .onStatus(...) method.

		response.setStatus(HttpServletResponse.SC_OK);
		return result;
	}
}
