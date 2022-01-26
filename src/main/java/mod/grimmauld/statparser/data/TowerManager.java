package mod.grimmauld.statparser.data;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TowerManager {
	private static final Gson gson = new Gson();
	public final Set<Upgrade> upgrades;
	public final Set<Tower> towers;


	public TowerManager() {
		upgrades = FileManager.getResourceListing("data/Upgrades")
			.map(FileManager::getFileFromResourceAsStream)
			.map(InputStreamReader::new)
			.map(JsonParser::parseReader)
			.map(je -> gson.fromJson(je, Upgrade.class))
			.filter(Objects::nonNull)
			.collect(Collectors.toSet());
		towers = FileManager.getResourceListing("data/Towers")
			.map(Tower::new)
			.collect(Collectors.toSet());
	}
}
