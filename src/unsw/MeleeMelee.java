package unsw.gloriaromanus;

import java.lang.Math;

public class MeleeMelee implements BattleResolver {

    /**
     * inflict casualty of between two melee units
     * @param a unit a
     * @param d unit d
     */
    public static void inflictCasualty(Unit a, Unit d) {
        double n = BattleResolver.getPosGaussian();
        int dcasualty = (int) Math.round(0.1*d.getInitialSize()*a.getDamage()*(n+1)/(d.getArmour()+d.getShieldDefense()+d.getDefenseSkill()));
        int acasualty = (int) Math.round(0.1*a.getInitialSize()*d.getDamage()*(n+1)/(a.getArmour()+a.getShieldDefense()+a.getDefenseSkill()));
        if(a.getBroken())   dcasualty=0;
        if(d.getBroken())   acasualty=0;
        if(dcasualty>=d.getCurrentSize()){
            d.setCurrentSize(0);
        }else{
            d.setCurrentSize(d.getCurrentSize()-dcasualty);
        }
        if(acasualty>=a.getCurrentSize()){
            a.setCurrentSize(0);
        }else{
            a.setCurrentSize(a.getCurrentSize()-acasualty);
        }
        if(a!=null) a.setInitialSize(a.getCurrentSize());
        if(d!=null) d.setInitialSize(d.getCurrentSize());
        //HOW AND WHERE DO WE REMOVE THE DEAD UNITS FROM THE USER?????
    }


}
