package FullMapTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import messagesBase.messagesFromClient.ETerrain;
import messagesBase.messagesFromClient.PlayerHalfMapNode;

public class CreateRandomHalfMapForTesting {

	protected final static Logger logger = LoggerFactory.getLogger(CreateRandomHalfMapForTesting.class);

	public static List<PlayerHalfMapNode> addWaterFields(List<PlayerHalfMapNode> map) {
		Random random = new Random();
		int numNodesToChange = random.nextInt(3) + 7; // randomly generate a number between 5 and 8
		int numChanged = 0;
		while (numChanged < numNodesToChange) {
			int randomIndex = random.nextInt(map.size());
			PlayerHalfMapNode node = map.get(randomIndex);
			if (node.getTerrain() != ETerrain.Mountain && !node.isFortPresent()) {
				PlayerHalfMapNode curr = new PlayerHalfMapNode(node.getX(), node.getY(), node.isFortPresent(),
						ETerrain.Water);
				map.set(randomIndex, curr);
				numChanged++;
			}
		}

		int x = 0;
		for (PlayerHalfMapNode node : map)
			if (node.getTerrain().equals(ETerrain.Water))
				x++;

		return map;
	}

	public static void addMountainFields(List<PlayerHalfMapNode> map) {

		Random random = new Random();
		int numNodesToChange = random.nextInt(4) + 5; // randomly generate a number between 5 and 8
		int numChanged = 0;

		while (numChanged < numNodesToChange) {
			int randomIndex = random.nextInt(map.size());
			PlayerHalfMapNode node = map.get(randomIndex);
			if (node.getTerrain() == ETerrain.Grass && !node.isFortPresent()) {
				PlayerHalfMapNode curr = new PlayerHalfMapNode(node.getX(), node.getY(), node.isFortPresent(),
						ETerrain.Mountain);
				map.set(randomIndex, curr);
				numChanged++;
			}
		}
	}

	public static List<PlayerHalfMapNode> createHalfMap() {

		int width = 10;
		int height = 5;
		List<PlayerHalfMapNode> map = new ArrayList<PlayerHalfMapNode>();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				if (y == 3 && x == 4) {
					PlayerHalfMapNode curr = new PlayerHalfMapNode(x, y, true, ETerrain.Grass);
					map.add(curr);

				} else {
					PlayerHalfMapNode node = new PlayerHalfMapNode(x, y, false, ETerrain.Grass);

					map.add(node);
				}
			}
		}

		addWaterFields(map);
		addMountainFields(map);

		return map;
	}

}
