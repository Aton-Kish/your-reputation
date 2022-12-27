package atonkish.reputation.util;

import org.jetbrains.annotations.Nullable;

import net.minecraft.util.Formatting;

import atonkish.reputation.ReputationMod;

public enum ReputationStatus {
    FRIENDLY("friendly", Formatting.DARK_GREEN),
    TRUSTWORTHY("trustworthy", Formatting.GREEN),
    NEUTRAL("neutral", Formatting.GRAY),
    SUSPICIOUS("suspicious", Formatting.RED),
    HOSTILE("hostile", Formatting.DARK_RED),
    UNKNOWN("unknown", Formatting.DARK_GRAY);

    private final String translateKey;
    private final Formatting formatting;

    private ReputationStatus(String status, Formatting formatting) {
        this.translateKey = String.format("entity.%s.villager.reputation.%s", ReputationMod.MOD_ID, status);
        this.formatting = formatting;
    }

    public static ReputationStatus getStatus(@Nullable Integer reputation) {
        if (reputation == null) {
            return UNKNOWN;
        } else if (reputation <= -100) {
            return HOSTILE;
        } else if (reputation < 0) {
            return SUSPICIOUS;
        } else if (reputation == 0) {
            return NEUTRAL;
        } else if (reputation < 100) {
            return TRUSTWORTHY;
        } else {
            return FRIENDLY;
        }
    }

    public String getTranslateKey() {
        return this.translateKey;
    }

    public Formatting getFormatting() {
        return this.formatting;
    }
}
