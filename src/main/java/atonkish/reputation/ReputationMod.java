package atonkish.reputation;

import java.util.HashMap;
import java.util.Map;

import com.google.common.cache.Cache;

import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import atonkish.reputation.util.IronGolemData;
import atonkish.reputation.util.VillagerData;

public class ReputationMod {
	public static final String MOD_ID = "reputation";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final String REPUTATION_CUSTOM_DATA_KEY = "ReputationMod";
	public static final String VILLAGER_REPUTATION_KEY = "Reputation";
	public static final String VILLAGER_IS_SNITCH_KEY = "IsSnitch";
	public static final String IRON_GOLEM_IS_ANGRY_DATA = "IsAngry";

	public static final int MAXIMUM_CACHE_SIZE = 128;
	public static final Map<PlayerEntity, Cache<VillagerEntity, VillagerData>> PLAYER_REPUTATION_CACHE_MAP = new HashMap<>();
	public static final Map<PlayerEntity, Cache<IronGolemEntity, IronGolemData>> IRON_GOLEM_ANGRY_CACHE_MAP = new HashMap<>();
}
