package server.interfaces;

import java.util.Set;

import messagesBase.UniqueGameIdentifier;
import messagesBase.UniquePlayerIdentifier;
import messagesBase.messagesFromClient.PlayerHalfMap;
import messagesBase.messagesFromClient.PlayerRegistration;
import server.gameData.Player;
import server.gameData.SingleGame;

public interface IRule {

	public UniqueGameIdentifier handleNewGame();

	public UniquePlayerIdentifier handleRegistration(PlayerRegistration reg, UniqueGameIdentifier gameID);

	public void handleRegistration(Set<Player> game, String PlayerID);

	// public boolean handleHalfMap(UniqueGameIdentifier gameID, PlayerHalfMap
	// halfMap);

	public void handleHalfMap(UniqueGameIdentifier gameID, PlayerHalfMap halfMap, SingleGame game);

	public void handleGameState(SingleGame gameID, String playerID);

}
