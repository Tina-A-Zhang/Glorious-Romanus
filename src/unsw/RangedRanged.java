package unsw.gloriaromanus;

public class RangedRanged implements BattleResolver {

    public static void inflictCasualty(Unit a, Unit d) {
        double n = BattleResolver.getPosGaussian();
        int dcasualty = (int) Math.round(0.1*d.getInitialSize()*a.getDamage()*(n+1)/(d.getArmour()+d.getShieldDefense()));
        int acasualty = (int) Math.round(0.1*a.getInitialSize()*d.getDamage()*(n+1)/(a.getArmour()+a.getShieldDefense()));
        if(a.getBroken())   dcasualty=0;
        if(d.getBroken())   acasualty=0;
        if(dcasualty>=d.getCurrentSize()){
            d.setCurrentSize(0);
        }else{
            d.setCurrentSize(d.getCurrentSize()-dcasualty);
        } 

        if(acasualty >=a.getCurrentSize()){
            a.setCurrentSize(0);
        }else{
            a.setCurrentSize(a.getCurrentSize()-acasualty);
        }
        if(a!=null) a.setInitialSize(a.getCurrentSize());
        if(d!=null) d.setInitialSize(d.getCurrentSize());
    }
}
