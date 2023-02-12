package server.validation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import messagesBase.messagesFromClient.ETerrain;
import messagesBase.messagesFromClient.PlayerHalfMap;
import messagesBase.messagesFromClient.PlayerHalfMapNode;

public class HalfMapValidator {
	private final static Logger logger = LoggerFactory.getLogger(HalfMapValidator.class);

	private int iteratingOverHalfMapFunctionAndCountFieldTypes(PlayerHalfMap halfMap, ETerrain terrain) {
		int count = 0;

		for (PlayerHalfMapNode node : halfMap.getMapNodes()) {
			if (node.getTerrain().equals(terrain))
				count++;
		}

		return count;

	}

	public boolean checkFieldAmount(PlayerHalfMap halfMap) {

		return halfMap.getMapNodes().size() == 50 ? true : false;
	}

	public int checkGrasFieldAmount(PlayerHalfMap halfMap) {

		return iteratingOverHalfMapFunctionAndCountFieldTypes(halfMap, ETerrain.Grass);

	}

	public int checkWaterFieldAmount(PlayerHalfMap halfMap) {

		return iteratingOverHalfMapFunctionAndCountFieldTypes(halfMap, ETerrain.Water);

	}

	public int checkMountainFieldAmount(PlayerHalfMap halfMap) {

		return iteratingOverHalfMapFunctionAndCountFieldTypes(halfMap, ETerrain.Mountain);
	}

	public int checkCorrectFortAmount(PlayerHalfMap halfMap) {

		int castleCount = 0;

		for (PlayerHalfMapNode node : halfMap.getMapNodes())
			if (node.isFortPresent())
				castleCount++;
		logger.info("Fort on Map Count: " + castleCount);
		return castleCount;
	}

	public PlayerHalfMapNode fortOnGras(PlayerHalfMap halfMap) {

		PlayerHalfMapNode ret = new PlayerHalfMapNode();
		for (PlayerHalfMapNode node : halfMap.getMapNodes())
			if (node.isFortPresent())
				ret = node;
		logger.info("Fort is on " + ret.getTerrain().toString());
		return ret;

	}

	public boolean checkIfCoordinatesAreCoorect(PlayerHalfMap halfMapPlayer) {

		int count = 0;

		for (int y = 0; y < 5; ++y) {
			for (int x = 0; x < 10; ++x) {
				for (PlayerHalfMapNode node : halfMapPlayer.getMapNodes()) {
					if (node.getX() == x && node.getY() == y)
						count++;
				}
			}
		}
		logger.info("If count = 50 coordinates are correct. count = " + count);
		return count == 50 ? true : false;
	}

	public boolean checkMapBoardersForWaterFields(PlayerHalfMap halfMapPlayer) {
		int waterXBoundaries = 0;
		int waterYBoundaries = 0;
		boolean ret = true;

		for (PlayerHalfMapNode node : halfMapPlayer.getMapNodes()) {
			if (node.getX() == 0 || node.getX() == 9) {
				if (node.getTerrain().equals(ETerrain.Water))
					waterXBoundaries++;
			}
			if (node.getY() == 0 || node.getY() == 4) {
				if (node.getTerrain().equals(ETerrain.Water))
					waterYBoundaries++;
			}

		}
		logger.info("waterXBoundaries = " + waterXBoundaries + ", waterYBoundaries = " + waterYBoundaries);
		if (waterXBoundaries > 4)
			ret = false;
		if (waterYBoundaries > 2)
			ret = false;

		return ret;

	}

	public boolean checkIsland(PlayerHalfMap halfMapPlayer) {

		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 10; x++) {
				int xPlus1 = x + 1;
				int yPlus1 = y + 1;
				int xMinus1 = x - 1;
				int yMinus1 = y - 1;
				List<PlayerHalfMapNode> adjacents = new ArrayList<PlayerHalfMapNode>();

				for (PlayerHalfMapNode curr : halfMapPlayer.getMapNodes()) {
					if (curr.getX() == xPlus1 && curr.getY() == y) {
						if (curr.getTerrain().equals(ETerrain.Water))
							adjacents.add(curr);
					}
					if (x > 0) {

						if (curr.getX() == xMinus1 && curr.getY() == y) {
							if (curr.getTerrain().equals(ETerrain.Water))
								adjacents.add(curr);
							;
						}
					}
					if (curr.getX() == x && curr.getY() == yPlus1) {
						if (curr.getTerrain().equals(ETerrain.Water))
							adjacents.add(curr);
					}

					if (y > 0) {
						if (curr.getX() == x && curr.getY() == yMinus1) {
							if (curr.getTerrain().equals(ETerrain.Water))
								adjacents.add(curr);
						}
					}
				}
				if (adjacents.size() == 4) {
					logger.info("island detected. surrounded by 4 waterfields");
					return true;
				}

				if ((x == 0 || x == 9) && (y == 0 || y == 4)) {
					if (adjacents.size() == 2) {
						logger.info("island detected. surrounded by 2 waterfields");
						return true;
					}
				}
				if ((x != 0 && x != 9) && (y == 0 || y == 4)) {
					if (adjacents.size() == 3) {
						logger.info("island detected. surrounded by 3 waterfields");
						return true;
					}
				}

				if ((x == 0 || x == 9) && (y != 0 || y != 4)) {
					if (adjacents.size() == 3) {
						logger.info("island detected. surrounded by 3 waterfields");
						return true;
					}
				}

			}
		}

		return false;
	}

}
