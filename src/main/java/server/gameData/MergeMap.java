package server.gameData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import messagesBase.messagesFromClient.ETerrain;
import messagesBase.messagesFromClient.PlayerHalfMap;
import messagesBase.messagesFromClient.PlayerHalfMapNode;
import messagesBase.messagesFromServer.EFortState;
import messagesBase.messagesFromServer.EPlayerPositionState;
import messagesBase.messagesFromServer.ETreasureState;
import messagesBase.messagesFromServer.FullMap;
import messagesBase.messagesFromServer.FullMapNode;
import server.exceptions.GameException;

public class MergeMap {

	public FullMapNode hideTreasure(FullMap map) {

		List<FullMapNode> grassNodes = new ArrayList<FullMapNode>();

		for (FullMapNode node : map.getMapNodes()) {
			if (node.getTerrain().equals(ETerrain.Grass)) {
				grassNodes.add(node);
			}
		}

		Random rand = new Random();
		int randomIndex = rand.nextInt(grassNodes.size());

		FullMapNode treasurePosNode = grassNodes.get(randomIndex);

		logger.info("Treasure from Player 1 is on: " + treasurePosNode.getX() + "," + treasurePosNode.getY());

		return treasurePosNode;

	}

	private final static Logger logger = LoggerFactory.getLogger(MergeMap.class);
	// Hier die HalfMaps in FullMaps umwandeln (beide)
	// dann fusionieren

	private PlayerHalfMap findPlayerHalfMap(String playerID, List<PlayerHalfMap> halfMaps) {

		PlayerHalfMap ret = new PlayerHalfMap();
		for (PlayerHalfMap curr : halfMaps) {

			if (curr.getUniquePlayerID().equals(playerID)) {
				ret = curr;
				break;
			}
		}
		return ret;
	}

	public List<FullMapNode> transformPlayer1HalfMap(String playerID, List<PlayerHalfMap> halfMaps) {

		PlayerHalfMap halfMapPlayer1 = findPlayerHalfMap(playerID, halfMaps);
		List<FullMapNode> fullMapNode = new ArrayList<FullMapNode>();

		List<FullMapNode> grassNodes = new ArrayList<FullMapNode>();
		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 10; x++) {
				for (PlayerHalfMapNode node : halfMapPlayer1.getMapNodes()) {
					if (node.getX() == x && node.getY() == y) {

						if (node.isFortPresent()) {
							FullMapNode curr = new FullMapNode(node.getTerrain(), EPlayerPositionState.MyPlayerPosition,
									ETreasureState.NoOrUnknownTreasureState, EFortState.MyFortPresent, node.getX(),
									node.getY());
							fullMapNode.add(curr);
						} else {
							FullMapNode curr = new FullMapNode(node.getTerrain(), EPlayerPositionState.NoPlayerPresent,
									ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState,
									node.getX(), node.getY());
							fullMapNode.add(curr);
							if (curr.getTerrain().equals(ETerrain.Grass))
								grassNodes.add(curr);
						}

					}
				}
			}
		}

		/*
		 * int count = 0; for (FullMapNode node : fullMapNode) {
		 * 
		 * if (node.getX() == treasurePosNode.getX() && node.getY() ==
		 * treasurePosNode.getY()) { FullMapNode curr = new
		 * FullMapNode(node.getTerrain(), EPlayerPositionState.NoPlayerPresent,
		 * ETreasureState.MyTreasureIsPresent, EFortState.NoOrUnknownFortState,
		 * node.getX(), node.getY()); fullMapNode.set(count, curr); break; } ++count; }
		 */
		logger.info("HalfMap from Player 1 was Transformed " + fullMapNode.size());

		return fullMapNode;
	}

	public List<FullMapNode> transformPlayer2HalfMap(String playerID, List<PlayerHalfMap> halfMaps) {

		PlayerHalfMap halfMapPlayer1 = findPlayerHalfMap(playerID, halfMaps);
		List<FullMapNode> fullMapNode = new ArrayList<FullMapNode>();

		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 10; x++) {
				for (PlayerHalfMapNode node : halfMapPlayer1.getMapNodes()) {
					if (node.getX() == x && node.getY() == y) {

						if (node.isFortPresent()) {
							FullMapNode curr = new FullMapNode(node.getTerrain(),
									EPlayerPositionState.EnemyPlayerPosition, ETreasureState.NoOrUnknownTreasureState,
									EFortState.NoOrUnknownFortState, node.getX(), node.getY());
							fullMapNode.add(curr);
						} else {

							FullMapNode curr = new FullMapNode(node.getTerrain(), EPlayerPositionState.NoPlayerPresent,
									ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState,
									node.getX(), node.getY());

							fullMapNode.add(curr);
						}
					}
				}
			}
		}
		logger.info("HalfMap from Player 2 was Transformed " + fullMapNode.size());
		return fullMapNode;

	}

	public List<FullMapNode> merge10x10(FullMap map1, FullMap map2) {
		List<FullMapNode> map1CoordinatesTransformation = new ArrayList<FullMapNode>();

		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 10; x++) {
				for (FullMapNode node : map1.getMapNodes()) {
					if (node.getX() == x && node.getY() == y) {
						FullMapNode curr = new FullMapNode(node.getTerrain(), node.getPlayerPositionState(),
								node.getTreasureState(), node.getFortState(), node.getX(), node.getY());
						map1CoordinatesTransformation.add(curr);
					}
				}
			}
		}

		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 10; x++) {
				for (FullMapNode node : map2.getMapNodes()) {
					if (node.getX() == x && node.getY() == y) {
						FullMapNode curr = new FullMapNode(node.getTerrain(), node.getPlayerPositionState(),
								node.getTreasureState(), node.getFortState(), node.getX(), node.getY() + 5);
						map1CoordinatesTransformation.add(curr);
					}
				}
			}
		}
		logger.info("Half Maps were merged into 10x10");
		return map1CoordinatesTransformation;
	}

	public List<FullMapNode> merge20x5(FullMap map1, FullMap map2) {
		List<FullMapNode> map1CoordinatesTransformation = new ArrayList<FullMapNode>();

		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 10; x++) {
				for (FullMapNode node : map1.getMapNodes()) {
					if (node.getX() == x && node.getY() == y) {
						FullMapNode curr = new FullMapNode(node.getTerrain(), node.getPlayerPositionState(),
								node.getTreasureState(), node.getFortState(), node.getX(), node.getY());
						map1CoordinatesTransformation.add(curr);
					}
				}
			}
		}

		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 10; x++) {
				for (FullMapNode node : map2.getMapNodes()) {
					if (node.getX() == x && node.getY() == y) {
						FullMapNode curr = new FullMapNode(node.getTerrain(), node.getPlayerPositionState(),
								node.getTreasureState(), node.getFortState(), node.getX() + 10, node.getY());
						map1CoordinatesTransformation.add(curr);
					}
				}
			}
		}
		logger.info("Half Maps were merged into 20x5");

		return map1CoordinatesTransformation;

	}

	public List<FullMapNode> mergeBothHalfFullMapNodes(FullMap map1, FullMap map2) {

		Random rand = new Random();
		int randomNumber = rand.nextInt(2) + 1;
		List<FullMapNode> map1CoordinatesTransformation = (randomNumber == 1) ? merge10x10(map1, map2)
				: merge20x5(map1, map2);

		return map1CoordinatesTransformation;
	}

	public Map<String, FullMap> mergeHalfMaps(Map<String, PlayerHalfMap> map) {

		if (map == null) {
			logger.info("parsed HalfMaps in mergeHalfMaps was null Game");
			throw new GameException("Name: GameException", "Message: Parsed Map in mergeHalfMaps was null");
		}

		String firstKey = map.entrySet().stream().findFirst().get().getKey();
		String secondKey = map.entrySet().stream().skip(1).findFirst().get().getKey();

		List<PlayerHalfMap> halfMaps = new ArrayList<PlayerHalfMap>(map.values());

		Collections.shuffle(halfMaps);

		Map<String, FullMap> ret = new HashMap<String, FullMap>();

		FullMap map1 = new FullMap(transformPlayer1HalfMap(firstKey, halfMaps));
		FullMap map2 = new FullMap(transformPlayer2HalfMap(secondKey, halfMaps));

		FullMap map3 = new FullMap(transformPlayer1HalfMap(secondKey, halfMaps));
		FullMap map4 = new FullMap(transformPlayer2HalfMap(firstKey, halfMaps));

		ret.put(firstKey, new FullMap(mergeBothHalfFullMapNodes(map1, map2)));
		ret.put(secondKey, new FullMap(mergeBothHalfFullMapNodes(map3, map4)));

		return ret;
	}

}
