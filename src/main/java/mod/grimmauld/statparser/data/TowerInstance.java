package mod.grimmauld.statparser.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class TowerInstance {
	private final Set<AttackData> attackVariants = new HashSet<>();
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

	public TowerInstance withWeapons(JsonElement je) {
		attackVariants.clear();
		/*
		if (!je.getAsJsonObject().has("behaviors"))
			return this;

		StreamSupport.stream(je.getAsJsonObject().getAsJsonArray("behaviors").spliterator(), false)
			.filter(JsonElement::isJsonObject)
			.map(JsonElement::getAsJsonObject)
			.filter(jo -> jo.has("weapons"))
			.flatMap(this::getBoxedWeapons)
			.flatMap(this::parseWeaponBehaviours)

		 */
		getBoxedWeapons(je)
			.flatMap(this::parseWeaponBehaviours)
			// .filter(jo -> jo.has("filters"))
			// .filter(jo -> !jo.get("filters").isJsonNull())
			.filter(jo -> jo.has("id"))
			.filter(jo -> !jo.get("id").getAsString().equals("Projectile"))
			// .peek(System.out::println)
			.map(AttackData::new)
			.forEach(attackVariants::add);

		return this;
	}

	private Stream<JsonObject> getBoxedWeapons(JsonElement jsonElement) {
		if (!(jsonElement instanceof JsonObject jsonObject))
			return Stream.empty();

		Stream<JsonObject> weaponSets = Stream.empty();
		if (jsonObject.has("weapons")) {
			weaponSets = Stream.concat(weaponSets, StreamSupport.stream(jsonObject.getAsJsonArray("weapons").spliterator(), false)
				.filter(JsonElement::isJsonObject)
				.map(JsonElement::getAsJsonObject));
		}
		if (jsonObject.has("behaviors")) {
			JsonElement behaviours = jsonObject.get("behaviors");
			if (behaviours instanceof JsonArray behavioursArray)
				weaponSets = Stream.concat(weaponSets,
					StreamSupport.stream(behavioursArray.spliterator(), false)
						.filter(JsonElement::isJsonObject)
						.map(JsonElement::getAsJsonObject)
						.flatMap(this::getBoxedWeapons)
				);
		}
		return weaponSets;
	}

	private Stream<JsonObject> parseWeaponBehaviours(JsonObject jsonElement) {
		Stream<JsonObject> projectiles = Stream.empty();
		if (jsonElement.has("projectile")) {
			projectiles = Stream.concat(projectiles, extractSubProjectiles(jsonElement.getAsJsonObject("projectile")));

		}
		if (jsonElement.has("behaviours")) {
			projectiles = Stream.concat(projectiles, extractProjectiles(jsonElement.getAsJsonArray("behaviours")));
		}
		return projectiles;
	}

	private Stream<JsonObject> extractProjectiles(JsonArray behaviours) {
		return StreamSupport.stream(behaviours.spliterator(), false)
			.filter(JsonElement::isJsonObject)
			.map(JsonElement::getAsJsonObject)
			.filter(jsonObject -> jsonObject.has("projectile"))
			.flatMap(this::extractSubProjectiles);
	}

	private Stream<JsonObject> extractSubProjectiles(JsonObject projectile) {
		return Stream.concat(Stream.of(projectile), projectile.has("behaviours") ?
			extractProjectiles(projectile.getAsJsonArray("behaviours")) : Stream.empty());
	}

	public boolean canAttack(int bloonType) {
		return attackVariants.stream().anyMatch(attackData -> attackData.canAttack(bloonType));
	}

	public boolean canHitCamo() {
		return attackVariants.stream().anyMatch(AttackData::canHitCamo);
	}

	public boolean ishero() {
		return tier > 6;
	}

	public String getPath() {
		if (ishero())
			return baseId;
		if (tier >= 4)
			return baseId + " " + (tiers[0] >= 4 ? "" + tiers[0] : "x") +  (tiers[1] >= 4 ? "" + tiers[1] : "x") +  (tiers[2] >= 4 ? "" + tiers[2] : "x");
		return baseId + " " + tiers[0] + "" + tiers[1] + "" + tiers[2];
	}
}
