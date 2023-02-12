package FullMapTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import messagesBase.messagesFromClient.PlayerHalfMap;
import messagesBase.messagesFromClient.PlayerHalfMapNode;
import messagesBase.messagesFromServer.FullMap;
import messagesBase.messagesFromServer.FullMapNode;
import server.gameData.MergeMap;

public class FullMapTest {

	@Test
	public void GivenTwoRandomHalfMaps_TransFormEachIntoFullMaps_ExpectCorrectCoordinatesTransformation() {

		List<PlayerHalfMapNode> halfMap1 = CreateRandomHalfMapForTesting.createHalfMap();
		PlayerHalfMap map1 = new PlayerHalfMap("player1", halfMap1);
		List<PlayerHalfMapNode> halfMap2 = CreateRandomHalfMapForTesting.createHalfMap();
		PlayerHalfMap map2 = new PlayerHalfMap("player2", halfMap2);

		List<PlayerHalfMap> maps = new ArrayList<PlayerHalfMap>();
		maps.add(map1);
		maps.add(map2);
		MergeMap mergeMap = new MergeMap();

		List<FullMapNode> fullMap1 = mergeMap.transformPlayer1HalfMap(map1.getUniquePlayerID(), maps);
		List<FullMapNode> fullMap2 = mergeMap.transformPlayer1HalfMap(map1.getUniquePlayerID(), maps);

		for (int i = 0; i < halfMap1.size(); i++) {
			assertEquals(halfMap1.get(i).getX(), fullMap1.get(i).getX());
			assertEquals(halfMap1.get(i).getY(), fullMap1.get(i).getY());
		}

		for (int i = 0; i < halfMap2.size(); i++) {
			assertEquals(halfMap2.get(i).getX(), fullMap2.get(i).getX());
			assertEquals(halfMap2.get(i).getY(), fullMap2.get(i).getY());
		}

	}

	@Test
	public void GivenTwoRandomHalfMaps_TransFormEachIntoFullMaps_Expect50FieldsEachFullMap() {
		String playerID1 = "player1";
		PlayerHalfMap map1 = new PlayerHalfMap(playerID1, CreateRandomHalfMapForTesting.createHalfMap());
		String playerID2 = "player2";
		PlayerHalfMap map2 = new PlayerHalfMap(playerID2, CreateRandomHalfMapForTesting.createHalfMap());
		List<PlayerHalfMap> maps = new ArrayList<PlayerHalfMap>();
		maps.add(map1);
		maps.add(map2);
		MergeMap mergeMap = new MergeMap();
		List<FullMapNode> fullMap1 = mergeMap.transformPlayer1HalfMap(playerID1, maps);
		List<FullMapNode> fullMap2 = mergeMap.transformPlayer2HalfMap(playerID2, maps);

		assertTrue(fullMap1.size() == 50);
		assertTrue(fullMap2.size() == 50);

	}

	@Test
	public void GivenFullMap_CheckMapSize_Expect100() {
		String playerID1 = "player1";
		PlayerHalfMap map1 = new PlayerHalfMap(playerID1, CreateRandomHalfMapForTesting.createHalfMap());
		String playerID2 = "player2";
		PlayerHalfMap map2 = new PlayerHalfMap(playerID2, CreateRandomHalfMapForTesting.createHalfMap());
		List<PlayerHalfMap> maps = new ArrayList<PlayerHalfMap>();
		maps.add(map1);
		maps.add(map2);
		MergeMap mergeMap = new MergeMap();
		List<FullMapNode> fullMap1 = mergeMap.transformPlayer1HalfMap(playerID1, maps);
		List<FullMapNode> fullMap2 = mergeMap.transformPlayer2HalfMap(playerID2, maps);
		FullMap fM1 = new FullMap(fullMap1);
		FullMap fM2 = new FullMap(fullMap2);
		List<FullMapNode> fullMap = mergeMap.mergeBothHalfFullMapNodes(fM1, fM2);
		assertEquals(fullMap.size(), 100);

	}

	@Test
	public void GivenFullMap_CheckCoordinates_ExpectCorrectXandYCoordinates() {
		String playerID1 = "player1";
		PlayerHalfMap map1 = new PlayerHalfMap(playerID1, CreateRandomHalfMapForTesting.createHalfMap());
		String playerID2 = "player2";
		PlayerHalfMap map2 = new PlayerHalfMap(playerID2, CreateRandomHalfMapForTesting.createHalfMap());
		List<PlayerHalfMap> maps = new ArrayList<PlayerHalfMap>();
		maps.add(map1);
		maps.add(map2);
		MergeMap mergeMap = new MergeMap();

		List<FullMapNode> fullMap1 = mergeMap.transformPlayer1HalfMap(playerID1, maps);
		List<FullMapNode> fullMap2 = mergeMap.transformPlayer2HalfMap(playerID2, maps);
		FullMap fM1 = new FullMap(fullMap1);
		FullMap fM2 = new FullMap(fullMap2);
		List<FullMapNode> fullMap = mergeMap.mergeBothHalfFullMapNodes(fM1, fM2);

		int CoordinatesCount = 0;
		for (int y = 0; y < 10; ++y) {
			for (int x = 0; x < 10; ++x) {
				for (FullMapNode node : fullMap) {
					if (node.getX() == x && node.getY() == y)
						CoordinatesCount++;

				}
			}
		}
		assertEquals(CoordinatesCount, 100);

	}

	@Test
	public void GivenMapOfHalfMapsAndStringAsPlayerID_MergeThem_ExpectsMapSize2AndEachFullMapWith100Nodes() {
		String playerID1 = "player1";
		PlayerHalfMap map1 = new PlayerHalfMap(playerID1, CreateRandomHalfMapForTesting.createHalfMap());
		String playerID2 = "player2";
		PlayerHalfMap map2 = new PlayerHalfMap(playerID2, CreateRandomHalfMapForTesting.createHalfMap());

		Map<String, PlayerHalfMap> testMap = new HashMap<String, PlayerHalfMap>();
		testMap.put(playerID1, map1);
		testMap.put(playerID2, map2);
		MergeMap mergeMap = new MergeMap();

		Map<String, FullMap> fullTestMap = mergeMap.mergeHalfMaps(testMap);
		FullMap firstValue = fullTestMap.entrySet().stream().findFirst().get().getValue();
		FullMap secondValue = fullTestMap.entrySet().stream().skip(1).findFirst().get().getValue();
		assertEquals(fullTestMap.size(), 2);
		assertEquals(firstValue.getMapNodes().size(), 100);
		assertEquals(secondValue.getMapNodes().size(), 100);

	}

	@Test
	public void GivenMapOfHalfMapsAndStringAsPlayerID_CheckIfKeysAreDifferent_ExpectUniqueKeys() {
		String playerID1 = "player1";
		PlayerHalfMap map1 = new PlayerHalfMap(playerID1, CreateRandomHalfMapForTesting.createHalfMap());
		String playerID2 = "player2";
		PlayerHalfMap map2 = new PlayerHalfMap(playerID2, CreateRandomHalfMapForTesting.createHalfMap());

		Map<String, PlayerHalfMap> testMap = new HashMap<String, PlayerHalfMap>();
		testMap.put(playerID1, map1);
		testMap.put(playerID2, map2);
		MergeMap mergeMap = new MergeMap();
		Map<String, FullMap> fullTestMap = mergeMap.mergeHalfMaps(testMap);

		String firstKey = fullTestMap.entrySet().stream().findFirst().get().getKey();
		String secondKey = fullTestMap.entrySet().stream().skip(1).findFirst().get().getKey();

		assertTrue(!firstKey.equals(secondKey));

	}

	public void GivenFullMap_CheckPlayerPositionState_ExpectPlayersOnFort() {

	}

}
