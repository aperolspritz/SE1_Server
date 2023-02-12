package server.gameData;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import messagesBase.UniqueGameIdentifier;
import messagesBase.messagesFromClient.PlayerHalfMap;
import messagesBase.messagesFromClient.PlayerRegistration;
import messagesBase.messagesFromServer.EPlayerGameState;
import messagesBase.messagesFromServer.FullMap;
import messagesBase.messagesFromServer.FullMapNode;
import server.exceptions.GameException;
import server.rules.PlayerActionConsiderTimeRule;
import server.rules.PlayerHalfMapFieldRule;
import server.rules.PlayerHalfMapFortRule;
import server.rules.PlayerHalfMapWaterRules;
import server.rules.PlayerRegistrationRule;

public class SingleGame {

	private final static Logger logger = LoggerFactory.getLogger(SingleGame.class);
	private Set<Player> players = new HashSet<Player>();
	private UniqueGameIdentifier gameID = new UniqueGameIdentifier();
	private Map<String, PlayerHalfMap> halfMaps = new HashMap<String, PlayerHalfMap>();
	private Integer gameStateID = 0;
	private Map<String, FullMap> fullMaps = new HashMap<String, FullMap>();
	private boolean randomPlayerToSendActionWasChosen = false;
	private Map<String, FullMapNode> treasureNode = new HashMap<String, FullMapNode>();
	private Instant now;
	private Instant actionTimer = Instant.now();

	public Instant getActionTimer() {
		return actionTimer;
	}

	public Player getPlayer(String playerID) {

		Player ret = new Player();

		for (Player p : players) {
			if (p.getPlayerID().getUniquePlayerID().equals(playerID))
				ret = p;
		}
		return ret;
	}

	public Instant getInstantTime() {
		return this.now;
	}

	public void setWinner(String playerID) {
		for (Player p : players) {
			if (p.getPlayerID().getUniquePlayerID().equals(playerID))
				p.setPlayerState(EPlayerGameState.Lost);
			if (!p.getPlayerID().getUniquePlayerID().equals(playerID))
				p.setPlayerState(EPlayerGameState.Won);
		}
	}

	private void changePlayerState(String playerID) {
		for (Player p : players) {
			if (p.getPlayerID().getUniquePlayerID().equals(playerID))
				p.setPlayerState(EPlayerGameState.MustWait);
			if (!p.getPlayerID().getUniquePlayerID().equals(playerID))
				p.setPlayerState(EPlayerGameState.MustAct);
		}
	}

	public boolean checkIfPlayerAlreadySentMap(String playerID) {
		boolean ret = false;

		if (players.size() == 0)
			return ret;

		if (players != null) {
			for (Player p : players) {
				if (p.getPlayerID().getUniquePlayerID().equals(playerID)) {
					if (p.getSentTwoMaps() && halfMaps.size() == 2) {

						setWinner(playerID);
						ret = true;
						throw new GameException("Name: GameException", "Message: Player sent 2 Half Maps", playerID);
					}
				}
			}
		}
		return ret;
	}

	private void check(PlayerHalfMap halfMap, SingleGame game) {

		PlayerHalfMapFieldRule halfMapFieldRule = new PlayerHalfMapFieldRule();
		PlayerHalfMapFortRule halfMapFortRule = new PlayerHalfMapFortRule();
		PlayerHalfMapWaterRules halfMapWaterRule = new PlayerHalfMapWaterRules();
		PlayerActionConsiderTimeRule playerActionTime = new PlayerActionConsiderTimeRule();
		playerActionTime.handleGameState(game, halfMap.getUniquePlayerID());
		halfMapFieldRule.handleHalfMap(gameID, halfMap, game);
		halfMapFortRule.handleHalfMap(gameID, halfMap, game);
		halfMapWaterRule.handleHalfMap(gameID, halfMap, game);
	}

	public Map<String, FullMap> getFullMap() {
		return this.fullMaps;
	}

	public void mergeHalfMaps() {

		if (halfMaps.size() == 2) {

			MergeMap mergeMap = new MergeMap();

			this.fullMaps = mergeMap.mergeHalfMaps(halfMaps);
			logger.info("HalfMaps were merged to FullMap");
		}

	}

	public void addHalfMap(PlayerHalfMap halfMap, SingleGame game) {

		if (halfMap == null) {
			logger.info("parsed HalfMap in addHalfMap was null");
			throw new GameException("Name GameException", "Message: HalfMap was null");
		}

		Player player1 = getPlayer(halfMap.getUniquePlayerID());

		// considerTime: player 1 consider Time was set before choosing who send map
		// first
		// considerTime: player2 consider Time was set after player1 sent successful
		// action.

		if (!player1.getSentHalfMap()) {
			player1.setPlayerActionCountPlus1();
			check(halfMap, game);
			gameStateID++;
			logger.info("Player successfully sent Map");
			player1.setSentHalfMapTrue();
			// Reset time, start counting seconds until next Action comes in
			this.actionTimer = Instant.now();
			changePlayerState(halfMap.getUniquePlayerID());
			this.halfMaps.put(halfMap.getUniquePlayerID(), halfMap);
		} else {
			player1.setSentTwoMaps();
		}
	}

	public SingleGame() {
	}

	public SingleGame(UniqueGameIdentifier ID) {
		this.gameID = ID;
		this.now = Instant.now();
	}

	public Set<Player> getPlayers() {
		return this.players;
	}

	public String getGameStateID() {
		return Integer.toString(this.gameStateID);
	}

	public void increaseGameStateID() {
		this.gameStateID++;
	}

	public Player registerPlayerToGame(PlayerRegistration playerData, SingleGame game) {

		PlayerRegistrationRule regRule = new PlayerRegistrationRule();
		regRule.handleRegistration(playerData, gameID);
		Player player = new Player(playerData);
		players.add(player);
		gameStateID++;
		return player;
	}

	public UniqueGameIdentifier getGameID() {
		return this.gameID;
	}

	public void addPlayer(Player player) {
		players.add(player);
	}

	public void chooseRandomPlayerToSendNextAction() {

		if (players.size() == 2 && !randomPlayerToSendActionWasChosen) {
			randomPlayerToSendActionWasChosen = true;
			Random random = new Random();
			Player curr = new Player();

			int index = random.nextInt(players.size());
			int i = 0;
			for (Player player : players) {
				if (i == index) {
					curr = player;
				} else
					i++;
			}
			actionTimer = Instant.now();
			curr.setPlayerState(EPlayerGameState.MustAct);
		}
	}

}
