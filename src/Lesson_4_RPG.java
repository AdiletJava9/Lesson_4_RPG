
import java.util.Objects;
import java.util.Random;

public class Lesson_4_RPG{

    public static int bossHealth = 1800;
    public static int bossDamage = 100;
    public static String bossDefence;
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Medic", "Golem", "Lucky", "Witcher", "Thor"}; // Добавлен Thor
    public static int[] heroesHealth = {200, 200, 200, 500, 500, 200, 200, 200};
    public static int[] heroesDamage = {20, 20, 20, 0, 1, 20, 0, 50};
    public static int roundNumber;
    public static boolean bossStunned = false;

    public static void main(String[] args) {
        printStatistics();

        while (!isGameOver()) {
            playRound();
        }
    }

    public static boolean isGameOver() {
        if (bossHealth <= 0) {
            System.out.println("~~~~~~~~~~~~~~");
            System.out.println("Heroes won!!!");
            System.out.println("~~~~~~~~~~~~~~~");
            return true;
        }
        boolean allHeroesDead = true;
        for (int currentHP : heroesHealth) {
            if (currentHP > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("~~~~~~~~~~~");
            System.out.println("Boss won!!!");
            System.out.println("~~~~~~~~~~~~");
            return true;
        }
        return false;
    }

    public static void playRound() {
        roundNumber++;
        chooseBossDefence();
        bossAttack();
        heroesAttack();
        printStatistics();
    }

    public static void chooseBossDefence() {
        Random random = new Random();
        int randomIndex = random.nextInt(heroesAttackType.length); // 0,1,2
        bossDefence = heroesAttackType[randomIndex];
    }

    public static void heroesAttack() {
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0 && !bossStunned) {
                int damage = heroesDamage[i];
                if (Objects.equals(heroesAttackType[i], bossDefence)) {
                    Random random = new Random();
                    int coeff = random.nextInt(2, 10); // 2,3,4,5,6,7,8,9
                    damage = heroesDamage[i] * coeff;
                    System.out.println("CRITICAL DAMAGE x: " + damage + " (" + coeff + ")");
                    System.out.println("--------------------------------------------------");
                }
                if (bossHealth - damage < 0) {
                    bossHealth = 0;
                } else {
                    bossHealth = bossHealth - damage;
                }


                if (heroesAttackType[i].equals("Thor")) {
                    Random random = new Random();
                    if (random.nextInt(2) == 0) {
                        bossStunned = true;
                        System.out.println("---------------------------------------");
                        System.out.println("THOR HAAS STUNNED THE BOSS FOR 1 ROUND!");
                        System.out.println("----------------------------------------");
                    }
                }
            }
        }
    }

    public static void bossAttack() {
        if (bossStunned) {
            System.out.println("-------------------------------------------");
            System.out.println("THE BOSS IS STUNNED AND MISSES THIS ROUND!");
            System.out.println("------------------------------------------");
            bossStunned = false;
            return;
        }
        int damageSharedByGolem = bossDamage / 5;
        int percent = 25;
        int luckyIndex = 5;
        int witcherIndex = 6;
        if (heroesHealth[witcherIndex] > 0) {
            for (int i = 0; i < heroesHealth.length; i++) {
                if (heroesHealth[i] == 0) {
                    Random random = new Random();
                    if (random.nextBoolean()) {
                        heroesHealth[i] = heroesHealth[witcherIndex];
                        heroesHealth[witcherIndex] = 0;
                        System.out.println("-----------------------------------------------------------------------------------------");
                        System.out.println("WITCHER HAS RESURRECTED " + heroesAttackType[i] + " WITH " + heroesHealth[i] + " HEALTH");
                        System.out.println("-----------------------------------------------------------------------------------------");
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                int actualDamageBoss = bossDamage;

                if (i == luckyIndex) {
                    actualDamageBoss = (bossDamage * (100 - percent)) / 100;
                }
                if (i != 4 && heroesHealth[4] > 0) {
                    heroesHealth[4] -= damageSharedByGolem;
                    actualDamageBoss -= damageSharedByGolem;
                }
                heroesHealth[i] -= actualDamageBoss;
                if (heroesHealth[i] < 0) {
                    heroesHealth[i] = 0;
                }
            }
        }

        if (heroesHealth[4] > 0) {
            heroesHealth[4] -= damageSharedByGolem * (heroesHealth.length - 1);
            if (heroesHealth[4] < 0) {
                heroesHealth[4] = 0;
            }
        }

        boolean hasHealed = false;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (i != 3 && heroesHealth[i] > 0 && heroesHealth[i] < 100 && heroesHealth[3] > 0 && !hasHealed) {
                heroesHealth[i] += 100;
                System.out.println("--------------------------------------------------------------");
                System.out.println("THE MEDIC HEALED " + heroesAttackType[i] + " FOR +100 HEALTH");
                System.out.println("--------------------------------------------------------------");
                hasHealed = true;
            }
        }
        if (heroesHealth[witcherIndex] > 0) {
            heroesHealth[witcherIndex] -= bossDamage;
            if (heroesHealth[witcherIndex] < 0) {
                heroesHealth[witcherIndex] = 0;
            }
        }
    }

    public static void printStatistics() {
        System.out.println(":================================ROUND:" + roundNumber);
        System.out.println("Boss health: " + bossHealth + " damage: " + bossDamage +
                " defence: " + (bossDefence == null ? "No defence" : bossDefence));
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i] +
                    " damage: " + heroesDamage[i]);
        }
    }
}