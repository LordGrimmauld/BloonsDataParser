package mod.grimmauld.statparser;

import mod.grimmauld.statparser.data.TowerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
	public static final Logger LOGGER = LoggerFactory.getLogger(BuildConfig.APPID);
	public static final TowerManager TOWER_MANAGER = new TowerManager();

	public static void main(String[] args) {
		LOGGER.info("loaded tower json");
	}
}
