package gd.rf.kongzhongtitian.DuckTech.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class RubberDuckEntity extends Mob {

    public RubberDuckEntity(EntityType<? extends Mob> type, Level level) {
        super(type, level);
        // 关键：禁用AI和重力，使鸭子永远静止
        this.setNoAi(true);
        this.setNoGravity(true);
    }

    // 定义实体的属性（生命值、移动速度等）
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 4.0D) // 生命值
                .add(Attributes.MOVEMENT_SPEED, 0.0D); // 移动速度为0，保证静止
    }

    // 重写此方法，禁止鸭子被其他实体推动
    @Override
    public boolean isPushable() {
        return false;
    }

    // 重写此方法，禁止鸭子受到来自实体的伤害推动
    @Override
    public boolean isPickable() {
        return false; // 或者 return true; 如果你希望它能被玩家交互
    }
}