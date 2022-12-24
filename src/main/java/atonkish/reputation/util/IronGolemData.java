package atonkish.reputation.util;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;

public class IronGolemData {
    @Nullable
    private UUID angryAt;

    public IronGolemData(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

    public IronGolemData() {
        this(null);
    }

    @Nullable
    public UUID getAngryAt() {
        return this.angryAt;
    }

    public void setAngryAt(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }
}
