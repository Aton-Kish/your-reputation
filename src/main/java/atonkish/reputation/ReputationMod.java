package atonkish.reputation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.common.cache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReputationMod {
	public static final String MOD_ID = "reputation";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final int MAXIMUM_CACHE_SIZE = 128;
	public static final Map<UUID, Cache<Integer, Integer>> PLAYER_REPUTATION_CACHE_MAP = new HashMap<>();
	public static final String REPUTATION_DATA_KEY = "reputation";
}
