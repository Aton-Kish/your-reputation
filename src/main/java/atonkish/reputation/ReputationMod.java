package atonkish.reputation;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReputationMod {
	public static final String MOD_ID = "reputation";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final int MAXIMUM_CACHE_SIZE = 128;
	public static final Cache<Integer, Integer> REPUTATION_CACHE = CacheBuilder.newBuilder()
			.maximumSize(MAXIMUM_CACHE_SIZE).build();
	public static final String REPUTATION_DATA_KEY = "reputation";
}
