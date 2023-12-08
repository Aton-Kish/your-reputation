package atonkish.reputation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;

import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.Identifiers;

import atonkish.reputation.provider.IronGolemProvider;
import atonkish.reputation.provider.VillagerReputationProvider;
import atonkish.reputation.provider.VillagerSnitchProvider;

public class ReputationPlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerEntityDataProvider(IronGolemProvider.INSTANCE, IronGolemEntity.class);
        registration.registerEntityDataProvider(VillagerReputationProvider.INSTANCE, VillagerEntity.class);
        registration.registerEntityDataProvider(VillagerSnitchProvider.INSTANCE, VillagerEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(IronGolemProvider.INSTANCE, IronGolemEntity.class);
        registration.registerEntityComponent(VillagerReputationProvider.INSTANCE, VillagerEntity.class);
        registration.registerEntityComponent(VillagerSnitchProvider.INSTANCE, VillagerEntity.class);

        registration.addTooltipCollectedCallback((rootElement, accessor) -> {
            if (accessor instanceof EntityAccessor entityAccessor) {
                Entity entity = entityAccessor.getEntity();
                if (entity instanceof VillagerEntity) {
                    rootElement.getTooltip().remove(Identifiers.CORE_OBJECT_NAME);
                }
            }
        });
    }
}
