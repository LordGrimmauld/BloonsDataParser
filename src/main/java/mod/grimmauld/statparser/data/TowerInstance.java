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
	public int[] tiers;
	public int tier;

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

	public boolean compatibleWith(TowerInstance other) {
		if (!this.baseId.equals(other.baseId))
			return true;
		if (this.tier != 5 || other.tier != 5)
			return true;
		if (this.tiers.length != 3 || other.tiers.length != 3)
			throw new AssertionError("Found tower with weird paths"); // this should never happen
		if (this.tiers[0] == 5 && other.tiers[0] == 5)
			return false;
		if (this.tiers[1] == 5 && other.tiers[1] == 5)
			return false;
		return this.tiers[2] != 5 || other.tiers[2] != 5;
	}
}
