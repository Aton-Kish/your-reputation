package atonkish.reputation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReputationMod {
	public static final String MOD_ID = "reputation";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final String REPUTATION_CUSTOM_DATA_KEY = "ReputationModData";
	public static final String VILLAGER_REPUTATION_KEY = "Reputation";
	public static final String VILLAGER_IS_SNITCH_KEY = "IsSnitch";
	public static final String IRON_GOLEM_ANGRY_AT_DATA = "AngryAt";

	public static final int MAXIMUM_CACHE_SIZE = 128;
}
