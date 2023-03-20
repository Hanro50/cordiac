package za.net.hanro50.cordiac;

import org.bukkit.Achievement;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import java.awt.Color;
import za.net.hanro50.cordiac.api.messages.Achievement.Struct;
@SuppressWarnings("deprecation")
public class ReMappings {

    public static String DMTranslate(DamageCause DM) {
        switch (DM) {
            case BLOCK_EXPLOSION: // BLOCK_EXPLOSION("death.attack.explosion"),
                return "death.attack.explosion";
            case CONTACT: // CONTACT("death.attack.generic.player"),
                return "death.attack.cactus";
            case CRAMMING: // CRAMMING("death.attack.cramming"),
                return "death.attack.cramming";
            default:
            case CUSTOM: // CUSTOM("%1$s died"),
                return "death.attack.generic";
            case DRAGON_BREATH: // DRAGON_BREATH("death.attack.dragonBreath"),
                return "death.attack.dragonBreath";
            case DROWNING: // DROWNING("death.attack.drown"),
                return "death.attack.drown";
            case DRYOUT:
                return "death.attack.drown";
            case ENTITY_ATTACK: // ENTITY_ATTACK("death.attack.mob"),
                return "death.attack.mob";
            case ENTITY_EXPLOSION: // ENTITY_EXPLOSION("death.attack.explosion.player"),
                return "death.attack.explosion";
            case ENTITY_SWEEP_ATTACK:
                return "death.attack.player";
            case FALL: // FALL("death.attack.fall"),
                return "death.attack.fall";
            case FALLING_BLOCK: // FALLING_BLOCK("death.attack.fallingBlock"),
                return "death.attack.fallingBlock"; // "death.attack.anvil"
            case FIRE: // FIRE("death.attack.inFire"),
                return "death.attack.inFire";
            case FIRE_TICK: // FIRE_TICK("death.attack.onFire"),
                return "death.attack.onFire";
            case FLY_INTO_WALL: // FLY_INTO_WALL("death.attack.flyIntoWall"),
                return "death.attack.flyIntoWall";
            case HOT_FLOOR: // HOT_FLOOR("death.attack.hotFloor"),
                return "death.attack.hotFloor";
            case LAVA: // LAVA("death.attack.lava"),
                return "death.attack.lava";
            case LIGHTNING: // LIGHTNING("death.attack.lightningBolt"),
                return "death.attack.lightningBolt";
            case MAGIC: // MAGIC("death.attack.magic"),
                return "death.attack.magic";
            case MELTING: // MELTING("death.attack.generic"),
                return "death.attack.generic";
            case POISON: // POISON("death.attack.magic"),
                return "death.attack.magic";
            case PROJECTILE: // PROJECTILE("death.attack.arrow"),
                return "death.attack.arrow";
            case STARVATION: // STARVATION("death.attack.starve"),
                return "death.attack.starve";
            case SUFFOCATION: // SUFFOCATION("death.attack.inWall"),
                return "death.attack.inWall";
            case SUICIDE: // SUICIDE("death.attack.even_more_magic"),
                return "death.attack.even_more_magic";
            case THORNS: // THORNS("death.attack.thorns"),
                return "death.attack.thorns";
            case VOID: // VOID("death.attack.outOfWorld"),
                return "death.attack.outOfWorld";
            case WITHER: // WITHER("death.attack.wither"),
                return "death.attack.wither";

        }
    }



    public static Struct AchievementTranslate(Achievement Ach) {
        switch (Ach) {
            case ACQUIRE_IRON:
                return new Struct("achievement.acquireIron", new Color(254, 59, 0));
            case BAKE_CAKE:
                return new Struct("achievement.bakeCake", new Color(254, 189, 0));
            case BOOKCASE:
                return new Struct("achievement.bookcase", new Color(0, 94, 156));
            case BREED_COW:
                return new Struct("achievement.breedCow", new Color(254, 189, 0));
            case BREW_POTION:
                return new Struct("achievement.potion", new Color(144, 0, 117));
            case BUILD_BETTER_PICKAXE:
                return new Struct("achievement.buildBetterPickaxe", new Color(254, 59, 0));
            case BUILD_FURNACE:
                return new Struct("achievement.buildFurnace", new Color(254, 59, 0));
            case BUILD_HOE:
                return new Struct("achievement.buildHoe", new Color(254, 189, 0));
            case BUILD_PICKAXE:
                return new Struct("achievement.buildPickaxe", new Color(254, 59, 0));
            case BUILD_SWORD:
                return new Struct("achievement.buildSword", new Color(254, 59, 0));
            case BUILD_WORKBENCH:
                return new Struct("achievement.buildWorkBench", new Color(0, 94, 156));
            case COOK_FISH:
                return new Struct("achievement.cookFish", new Color(254, 189, 0));
            case DIAMONDS_TO_YOU:
                return new Struct("achievement.diamondsToYou", new Color(0, 94, 156));
            case ENCHANTMENTS:
                return new Struct("achievement.enchantments", new Color(144, 0, 117));
            case END_PORTAL:
                return new Struct("achievement.theEnd", new Color(144, 0, 117));
            case EXPLORE_ALL_BIOMES:
                return new Struct("achievement.exploreAllBiomes", Color.pink);
            case FLY_PIG:
                return new Struct("achievement.flyPig", Color.pink);
            case FULL_BEACON:
                return new Struct("achievement.fullBeacon", new Color(144, 0, 117));
            case GET_BLAZE_ROD:
                return new Struct("achievement.blazeRod", new Color(254, 59, 0));
            case GET_DIAMONDS:
                return new Struct("achievement.diamonds", new Color(0, 94, 156));
            case GHAST_RETURN:
                return new Struct("achievement.ghast", Color.white);
            case KILL_COW:
                return new Struct("achievement.killCow", new Color(254, 59, 0));
            case KILL_ENEMY:
                return new Struct("achievement.killEnemy", new Color(254, 59, 0));
            case KILL_WITHER:
                return new Struct("achievement.killWither", new Color(144, 0, 117));
            case MAKE_BREAD:
                return new Struct("achievement.makeBread", new Color(254, 189, 0));
            case MINE_WOOD:
                return new Struct("achievement.mineWood", new Color(254, 189, 0));
            case NETHER_PORTAL:
                return new Struct("achievement.portal", new Color(144, 0, 117));
            case ON_A_RAIL:
                return new Struct("achievement.onARail", new Color(254, 59, 0));
            case OPEN_INVENTORY:
                return new Struct("achievement.openInventory", new Color(103, 254, 16));
            case OVERKILL:
                return new Struct("achievement.overkill", new Color(254, 59, 0));
            case OVERPOWERED:
                return new Struct("achievement.acquireIron", new Color(254, 59, 0));
            case SNIPE_SKELETON:
                return new Struct("achievement.snipeSkeleton", new Color(254, 59, 0));
            case SPAWN_WITHER:
                return new Struct("achievement.spawnWither", new Color(144, 0, 117));
            case THE_END:
                return new Struct("achievement.theEnd2", new Color(144, 0, 117));
        }
        return new Struct("achievement.unknown", new Color(144, 0, 117));
    }
}
