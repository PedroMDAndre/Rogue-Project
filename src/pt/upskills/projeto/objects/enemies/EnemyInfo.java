package pt.upskills.projeto.objects.enemies;

public enum EnemyInfo {

    BADGUY(3, 10, 200),
    BAT(1, 4,20),
    SKELETON(1, 10,20),
    FINALBOSS(5, 50, 400),
    THIEF(2, 4,30);

    // Attributes
    private int attackDamage;
    private int enemyLife;
    private int killScore;      // Score from killing an Enemy

    // Constructor
    private EnemyInfo(int attackDamage, int enemyLife, int killScore) {
        this.attackDamage = attackDamage;
        this.enemyLife = enemyLife;
        this.killScore = killScore;
    }

    // Methods

    public int attackDamage() {
        return attackDamage;
    }

    public int enemyLife() {
        return enemyLife;
    }

    public int killScore() {
        return killScore;
    }
}
