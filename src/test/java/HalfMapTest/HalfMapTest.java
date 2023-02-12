package HalfMapTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.Test;
import org.slf4j.LoggerFactory;

import FullMapTest.CreateRandomHalfMapForTesting;
import ch.qos.logback.classic.Logger;
import messagesBase.messagesFromClient.ETerrain;
import messagesBase.messagesFromClient.PlayerHalfMapNode;

public class HalfMapTest {

	private final static Logger logger = (Logger) LoggerFactory.getLogger(HalfMapTest.class);

	@Test
	public void GivenRandomHalfMap_IterateAndControllCoordinates_ExpectCorrectCoordinates() {

		List<PlayerHalfMapNode> map = CreateRandomHalfMapForTesting.createHalfMap();
		for (int i = 0; i < map.size(); i++) {
			int x = i % 10;
			int y = i / 10;
			PlayerHalfMapNode node = map.get(i);
			// logger.info("HalfMap:" + node.getX() + "," + node.getY() + "," +
			// node.getTerrain());
			assertEquals(x, node.getX());
			assertEquals(y, node.getY());

		}
	}

	@Test
	public void GivenRandomHalfMap_IterateOverNodesAndCheckETerrain_ExpectMinimum24GrasFields() {
		List<PlayerHalfMapNode> map = CreateRandomHalfMapForTesting.createHalfMap();

		int grasCount = 0;
		for (PlayerHalfMapNode node : map) {
			if (node.getTerrain() == ETerrain.Grass) {
				grasCount++;
			}
		}

		assertTrue(grasCount >= 24);
	}

	@Test
	public void GivenRandomHalfMap_IterateOverNodesAndCheckETerrain_ExpectMinimum7WaterFields() {
		List<PlayerHalfMapNode> map = CreateRandomHalfMapForTesting.createHalfMap();

		int waterCount = 0;
		for (PlayerHalfMapNode node : map) {
			if (node.getTerrain() == ETerrain.Water) {
				waterCount++;
			}
		}

		assertTrue(waterCount >= 7);
	}

	@Test
	public void GivenRandomHalfMap_IterateOverNodesAndCheckETerrain_ExpectMinimum5MountainFields() {
		List<PlayerHalfMapNode> map = CreateRandomHalfMapForTesting.createHalfMap();

		int mountainCount = 0;
		for (PlayerHalfMapNode node : map) {
			if (node.getTerrain() == ETerrain.Mountain) {
				mountainCount++;
			}
		}

		assertTrue(mountainCount >= 5);
	}

	@Test
	public void GivenRandomHalfMap_IterateAndCheckAdjacents_ExpectNoWaterIslands() {
		List<PlayerHalfMapNode> map = CreateRandomHalfMapForTesting.createHalfMap();

		boolean isIslandFound = false;
		for (int i = 0; i < map.size(); i++) {
			PlayerHalfMapNode node = map.get(i);
			if ((node.getTerrain() == ETerrain.Grass || node.getTerrain() == ETerrain.Mountain)) {
				int x = i % 10;
				int y = i / 10;

				boolean isIsland = true;

				// check north
				if (y > 0) {
					PlayerHalfMapNode northNode = map.get(i - 10);
					if (northNode.getTerrain() != ETerrain.Water) {
						isIsland = false;
					}
				}
				// check east
				if (x < 9) {
					PlayerHalfMapNode eastNode = map.get(i + 1);
					if (eastNode.getTerrain() != ETerrain.Water) {
						isIsland = false;
					}
				}
				// check south
				if (y < 4) {
					PlayerHalfMapNode southNode = map.get(i + 10);
					if (southNode.getTerrain() != ETerrain.Water) {
						isIsland = false;
					}
				}
				// check west
				if (x > 0) {
					PlayerHalfMapNode westNode = map.get(i - 1);
					if (westNode.getTerrain() != ETerrain.Water) {
						isIsland = false;
					}
				}

				if (isIsland) {
					isIslandFound = true;
					break;
				}
			}
		}
		assertFalse(isIslandFound);
	}

	@Test
	public void GivenRandomHalfMap_CheckXYCoordinates_ExpectCorrectCoordinates() {
		List<PlayerHalfMapNode> map = CreateRandomHalfMapForTesting.createHalfMap();
		int count = 0;

		for (int y = 0; y < 5; ++y) {
			for (int x = 0; x < 10; ++x) {
				for (PlayerHalfMapNode node : map) {
					if (node.getX() == x && node.getY() == y)
						count++;
				}
			}
		}
		assertEquals(count, 50);

	}

	@Test
	public void GivenRandomHalfMap_CheckIfCorrectFortProperties_ExpectOneFort() {

		List<PlayerHalfMapNode> map = CreateRandomHalfMapForTesting.createHalfMap();
		int count = 0;
		for (PlayerHalfMapNode node : map) {
			if (node.isFortPresent())
				count++;
		}

		assertTrue(count == 1);
	}

	@Test
	public void GivenRandomHalfMap_CheckIfCorrectFortProperties_ExpectFortOnGras() {

		List<PlayerHalfMapNode> map = CreateRandomHalfMapForTesting.createHalfMap();
		boolean onGras = false;
		for (PlayerHalfMapNode node : map) {
			if (node.isFortPresent() && node.getTerrain().equals(ETerrain.Grass))
				onGras = true;
		}

		assertTrue(onGras);
	}
}
