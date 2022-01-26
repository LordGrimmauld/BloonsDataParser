package mod.grimmauld.statparser.data;

import java.util.Set;
import java.util.stream.Stream;

public class TowerInstance {
	public int cost;
	public boolean isSubTower;
	public Set<String> appliedUpgrades;
	public String name;
	public String baseId;
	public String towerSet;

	public Stream<Upgrade> getUpgrades(TowerManager manager) {
		return manager.upgrades.stream()
			.filter(upgrade -> appliedUpgrades.contains(upgrade.name));
	}

	public int getPrice(TowerManager manager) {
		return cost + getUpgrades(manager).map(Upgrade::getCost).mapToInt(Integer::intValue).sum();
	}

	public boolean isPurchasableTower() {
		return !isSubTower && cost > 0;
	}

	public int getBaseCost() {
		return cost;
	}

	@Override
	public String toString() {
		return name;
	}

	public boolean isChimpsViable() {
		if (baseId.equals("BananaFarm"))
			return false;
		return !towerSet.equals("Hero") || appliedUpgrades.size() >= 19;
	}
}
