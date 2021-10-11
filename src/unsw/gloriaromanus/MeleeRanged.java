package unsw.gloriaromanus;

public class MeleeRanged implements BattleResolver {


    /**
     * inflict casualty between one ranged, one melee unit
     * engagement type given
     * @param a unit a
     * @param d unit d
     * @param et engagement type
     */
    public static void inflictCasualty(Unit a, Unit d, String et) {
        int acasualty=0;
        int dcasualty=0;
        if (et.equals("ranged")) {
            if (a.getAttackType().equals("melee")) {
                acasualty = 0;
                double n = BattleResolver.getPosGaussian();
                dcasualty = (int) Math.round(0.1 * d.getInitialSize() * a.getDamage() * (n + 1)
                        / (d.getArmour() + d.getShieldDefense() + d.getDefenseSkill()));
            } else if (d.getAttackType().equals("melee")) {
                dcasualty = 0;
                double n = BattleResolver.getPosGaussian();
                acasualty = (int) Math.round(0.1 * a.getInitialSize() * d.getDamage() * (n + 1)
                        / (a.getArmour() + a.getShieldDefense() + a.getDefenseSkill()));
            } 
        }else {
            double n = BattleResolver.getPosGaussian();
            dcasualty = (int) Math.round(0.1 * d.getInitialSize() * a.getDamage() * (n + 1)
                    / (d.getArmour() + d.getShieldDefense() + d.getDefenseSkill()));
            n = BattleResolver.getPosGaussian();
            acasualty = (int) Math.round(0.1 * a.getInitialSize() * d.getDamage() * (n + 1)
                    / (a.getArmour() + a.getShieldDefense() + a.getDefenseSkill()));
        }
        if(a.getBroken())   dcasualty=0;
        if(d.getBroken())   acasualty=0;
        if (dcasualty >= d.getCurrentSize()) {
            d.setCurrentSize(0);
        } else {
            d.setCurrentSize(d.getCurrentSize() - dcasualty);
        }

        if (acasualty >= a.getCurrentSize()) {
            a.setCurrentSize(0);
        } else {
            a.setCurrentSize(a.getCurrentSize() - acasualty);
        }
        if (a != null)
            a.setInitialSize(a.getCurrentSize());
        if (d != null)
            d.setInitialSize(d.getCurrentSize());
    }
    
}
