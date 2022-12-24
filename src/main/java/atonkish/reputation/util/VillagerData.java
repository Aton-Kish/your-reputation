package atonkish.reputation.util;

import org.jetbrains.annotations.Nullable;

public class VillagerData {
    @Nullable
    private Integer reputation;
    private boolean isSnitch;

    public VillagerData(@Nullable Integer reputation, boolean isSnitch) {
        this.reputation = reputation;
        this.isSnitch = isSnitch;
    }

    public VillagerData() {
        this(null, false);
    }

    @Nullable
    public Integer getReputation() {
        return this.reputation;
    }

    public void setReputation(@Nullable Integer reputation) {
        this.reputation = reputation;
    }

    public boolean isSnitch() {
        return this.isSnitch;
    }

    public void setSnitch() {
        this.isSnitch = true;
    }

    public void resetSnitch() {
        this.isSnitch = false;
    }
}
