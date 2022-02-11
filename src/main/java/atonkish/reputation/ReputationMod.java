package atonkish.reputation;

import java.util.HashMap;
import java.util.Map;

import com.google.common.cache.Cache;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import atonkish.reputation.util.CachedData;

public class ReputationMod {
	public static final String MOD_ID = "reputation";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final int MAXIMUM_CACHE_SIZE = 128;
	public static final Map<PlayerEntity, Cache<VillagerEntity, CachedData>> PLAYER_REPUTATION_CACHE_MAP = new HashMap<>();
	public static final String REPUTATION_DATA_KEY = "reputation";
}
