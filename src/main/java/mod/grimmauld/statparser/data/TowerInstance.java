package mod.grimmauld.statparser.data;

import java.util.Set;
import java.util.stream.Stream;

public class TowerInstance {
	public int baseCost;
	public boolean isSubTower;
	public Set<String> appliedUpgrades;

	public Stream<Upgrade> getUpgrades(TowerManager manager) {
		return manager.upgrades.stream()
			.filter(upgrade -> appliedUpgrades.contains(upgrade.name));
	}

	public int getPrice(TowerManager manager) {
		return baseCost + getUpgrades(manager).map(Upgrade::getCost).mapToInt(Integer::intValue).sum();
	}
}
