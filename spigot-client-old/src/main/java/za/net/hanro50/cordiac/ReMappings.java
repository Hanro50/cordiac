package za.net.hanro50.cordiac;

import org.bukkit.Achievement;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
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


    public static String AchievementTranslate(Achievement Ach) {
        switch (Ach) {
            case ACQUIRE_IRON:
                return "achievement.acquireIron";
            case BAKE_CAKE:
                return "achievement.bakeCake";
            case BOOKCASE:
                return "achievement.bookcase";
            case BREED_COW:
                return "achievement.breedCow";
            case BREW_POTION:
                return "achievement.acquireIron";
            case BUILD_BETTER_PICKAXE:
                return "achievement.buildBetterPickaxe";
            case BUILD_FURNACE:
                return "achievement.buildFurnace";
            case BUILD_HOE:
                return "achievement.buildHoe";
            case BUILD_PICKAXE:
                return "achievement.buildPickaxe";
            case BUILD_SWORD:
                return "achievement.buildSword";
            case BUILD_WORKBENCH:
                return "achievement.buildWorkBench";
            case COOK_FISH:
                return "achievement.cookFish";
            case DIAMONDS_TO_YOU:
                return "achievement.diamondsToYou";
            case ENCHANTMENTS:
                return "achievement.enchantments";
            case END_PORTAL:
                return "achievement.theEnd";
            case EXPLORE_ALL_BIOMES:
                return "achievement.exploreAllBiomes";
            case FLY_PIG:
                return "achievement.flyPig";
            case FULL_BEACON:
                return "achievement.fullBeacon";
            case GET_BLAZE_ROD:
                return "achievement.blazeRod";
            case GET_DIAMONDS:
                return "achievement.diamonds";
            case GHAST_RETURN:
                return "achievement.ghast";
            case KILL_COW:
                return "achievement.killCow";
            case KILL_ENEMY:
                return "achievement.killEnemy";
            case KILL_WITHER:
                return "achievement.killWither";
            case MAKE_BREAD:
                return "achievement.makeBread";
            case MINE_WOOD:
                return "achievement.mineWood";
            case NETHER_PORTAL:
                return "achievement.portal";
            case ON_A_RAIL:
                return "achievement.onARail";
            case OPEN_INVENTORY:
                return "achievement.openInventory";
            case OVERKILL:
                return "achievement.overkill";
            case OVERPOWERED:
                return "achievement.acquireIron";
            case SNIPE_SKELETON:
                return "achievement.snipeSkeleton";
            case SPAWN_WITHER:
                return "achievement.spawnWither";
            case THE_END:
                return "achievement.theEnd2";
        }
        return "achievement.unknown";
    }
}
