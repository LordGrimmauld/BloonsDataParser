package mod.grimmauld.statparser.data;

import java.util.Set;
import java.util.stream.Collectors;

public class TowerManager {
	public final Set<Tower> towers;

	public TowerManager() {
		towers = FileManager.getResourceListing("data/Towers")
			.map(Tower::new)
			.collect(Collectors.toSet());
	}
}
