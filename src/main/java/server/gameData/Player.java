package server.gameData;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import messagesBase.UniquePlayerIdentifier;
import messagesBase.messagesFromClient.PlayerRegistration;
import messagesBase.messagesFromServer.EPlayerGameState;
import server.exceptions.GameException;

public class Player {
	private final static Logger logger = LoggerFactory.getLogger(Player.class);
	private EPlayerGameState playerState = EPlayerGameState.MustWait;
	private UniquePlayerIdentifier playerID = new UniquePlayerIdentifier();
	private PlayerRegistration playerData = new PlayerRegistration();
	private boolean sentHalfMap = false;
	private boolean sentTwoMaps = false;
	private Integer playerActionCount = 0;
	private Instant playerConsiderTime = Instant.now();

	public void setPlayerActionCountPlus1() {
		this.playerActionCount++;
	}

	public void setPlayerConsiderTime() {
		this.playerConsiderTime = Instant.now();
	}

	public Instant getPlayerConsiderTime() {
		return this.playerConsiderTime;
	}

	public Integer getPlayerActionCount() {
		return this.playerActionCount;
	}

	public void setSentTwoMaps() {
		sentTwoMaps = true;
	}

	public boolean getSentTwoMaps() {

		return this.sentHalfMap;
	}

	public Player() {
	}

	public Player(PlayerRegistration player) {

		if (player == null) {
			logger.info("parsed player in Player Constructor was null");
			throw new GameException("Game Exception:", "Message: parsed player in Player Constructor was null ");
		}

		this.playerData = player;
		playerID = new UniquePlayerIdentifier(UUID.randomUUID().toString());
		logger.info(playerData.getStudentUAccount() + " has GameID: " + playerID.getUniquePlayerID());
	}

	public boolean getSentHalfMap() {
		return this.sentHalfMap;
	}

	public void setSentHalfMapTrue() {
		this.sentHalfMap = true;
	}

	public PlayerRegistration getPlayerRegistration() {
		return this.playerData;
	}

	public UniquePlayerIdentifier getPlayerID() {
		return this.playerID;
	}

	public EPlayerGameState getPlayerState() {
		return playerState;
	}

	public void setPlayerState(EPlayerGameState state) {
		this.playerState = state;
	}

}
