package server.gameID;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import messagesBase.UniqueGameIdentifier;
import server.exceptions.GameException;

public class GameIDGenerator {

	// TAKENFROM <1> https://chat.openai.com/chat
	// The Idea with StringBuilder and CHAR_LIST was Suggested by The AI. I Took it
	// and adapted it.

	// TAKEN FROM START <1>
	private static final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

	private final static Logger logger = LoggerFactory.getLogger(GameIDGenerator.class);

	public static UniqueGameIdentifier generateGameID() {
		StringBuilder gameID = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < 5; i++) {
			int index = random.nextInt(CHAR_LIST.length());
			gameID.append(CHAR_LIST.charAt(index));
		}

		try {
			checkGameIDLength(gameID.toString());
		} catch (Exception e) {
			logger.info(e.getMessage());
		}

		logger.info("GameID: " + gameID.toString());
		UniqueGameIdentifier ret = new UniqueGameIdentifier(gameID.toString());
		return ret;
	}

	// TAKEN FROM END<1>

	private static void checkGameIDLength(String gameID) {
		if (gameID.length() < 5) {
			throw new GameException("Name: GameIDException", "Message: Created invalid Game ID");
		}
	}

}