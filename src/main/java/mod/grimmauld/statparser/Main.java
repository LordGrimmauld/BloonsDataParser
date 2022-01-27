package mod.grimmauld.statparser;

import mod.grimmauld.statparser.data.Immunities;
import mod.grimmauld.statparser.data.RoundManager;
import mod.grimmauld.statparser.data.TowerInstance;
import mod.grimmauld.statparser.data.TowerManager;
import mod.grimmauld.statparser.util.UnorderedPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;


public class Main {
	public static final Logger LOGGER = LoggerFactory.getLogger(BuildConfig.APPID);
	public static final TowerManager TOWER_MANAGER = new TowerManager();
	public static final RoundManager ROUND_MANAGER = new RoundManager();

	public static void main(String[] args) {
		LOGGER.info("loaded tower json");
		generatePossibleCombos()
			.map(UnorderedPair.applyOnBoth(TowerInstance::getPath))
			.distinct()
			.forEach(Main::printCombo);
	}

	public static void printCombo(UnorderedPair<?, ?> combo) {
		LOGGER.info("{} + {}", combo.getFirst(), combo.getSecond());
	}

	public static Stream<UnorderedPair<TowerInstance, TowerInstance>> generatePossibleCombos() {
		return TOWER_MANAGER.chimpsViable.stream()
			.filter(towerInstance -> towerInstance.cost < ROUND_MANAGER.startingCash)
			.flatMap(towerOne -> TOWER_MANAGER.chimpsViable.stream()
				.filter(towerOne::compatibleWith)
				.filter(towerTwo -> towerOne.getPrice(TOWER_MANAGER) + towerTwo.getPrice(TOWER_MANAGER) < ROUND_MANAGER.totalCash)
				.filter(towerTwo -> towerOne.getPrice(TOWER_MANAGER) + towerTwo.getPrice(TOWER_MANAGER) > 25000)
				.map(towerTwo -> UnorderedPair.of(towerOne, towerTwo)))
			.distinct()
			.filter(UnorderedPair.matchesBoth(towerInstance -> towerInstance.ishero() || towerInstance.getPrice(TOWER_MANAGER) > 2000))
			// .filter(UnorderedPair.matchesAtLeastOne(TowerInstance::canHitCamo))
			.filter(UnorderedPair.matchesAtLeastOne(towerInstance -> towerInstance.canAttack(Immunities.LEAD | Immunities.BLACK)))
			.filter(UnorderedPair.matchesAtLeastOne(towerInstance -> towerInstance.canAttack(Immunities.WHITE)))
			.filter(UnorderedPair.matchesAtLeastOne(towerInstance -> towerInstance.canAttack(Immunities.PURPLE)));
	}
}
