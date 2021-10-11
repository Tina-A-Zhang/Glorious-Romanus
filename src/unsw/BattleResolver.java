package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;
import java.util.concurrent.ThreadLocalRandom;


public interface BattleResolver {

    /**
     * two units inflict casualty on each other in et type battle
     * @param atk the attacking unit
     * @param dfn the defending unit
     * @param et the engagement type
     */
    public static void inflictCasualty(Unit atk, Unit dfn, String et){
        String atk_kind = atk.getKind();
        String dfn_kind = dfn.getKind();
        if(!atk_kind.equals(dfn_kind)){
            MeleeRanged.inflictCasualty(atk,dfn,et);
       }else if(atk_kind.equals("melee")){
           MeleeMelee.inflictCasualty(atk,dfn);
       }else{
           RangedRanged.inflictCasualty(atk,dfn);
       }
    }

    /**
     * attack a province
     * @param p the province to attack
     * @return whether the attack was successful
     */
    public static Boolean attack(Province p){
        Player attacker = p.getEnemyUnits().get(0).getPrevp().getOwner();
        for(int i = 0; i < 200; i++){
            
            if(p.availUnits().isEmpty() || p.getEnemyUnits().isEmpty()) break;
            Unit atk = chooseUnit(p.getEnemyUnits());
            Unit dfn = chooseUnit(p.availUnits());
            String et = getEngagementType(atk, dfn);
            inflictCasualty(atk, dfn, et);
            Unit route = handleBreak(atk, dfn,et);
            if(route!=null){
                p.addRoutedUnit(route);
            }
            //clear dead && update
            if(atk.getCurrentSize()==0){
                p.removeEnemyUnit(atk);
                atk.getPrevp().getOwner().removeUnit(atk);

                System.out.println(atk.getID()+" is dead");
            }else if(dfn.getCurrentSize()==0){
                p.removeUnit(dfn);
                dfn.getLocation().getOwner().removeUnit(dfn);
                System.out.println(dfn.getID()+" is dead");
            }else if(atk.equals(route)){
                p.removeEnemyUnit(atk);

                System.out.println(atk.getID()+" has routed");
            }else if(dfn.equals(route)){
                p.removeUnit(dfn);

                System.out.println(dfn.getID()+" has routed");
            }
        }
       
       if(p.availUnits().isEmpty()){
        //attack won
            resetProvince(attacker,p);
            return true;
        }else{
        //defense won
            for(Unit u: p.getRoutedUnits()){
                if(isPreviousOwner(u)){
                    u.getPrevp().addUnit(u);
                }else{
                    p.addUnit(u);
                }
            }
            p.clearRoutedUnit();
            return false;
        }
    }

    /**
     * reset the ownership of a province
     * happen when a province is conquered
     * @param newOwner the new owner of the province 
     * @param p the province to reset
     */
    public static void resetProvince(Player newOwner, Province p){
        Player previousOwner = p.getOwner();
        p.clearUnits();
        for(Unit u: p.getRoutedUnits()){ 
            if(isPreviousOwner(u)){
                previousOwner.removeUnit(u);
            }else{
                p.addUnit(u);
            }
        }
        p.clearRoutedUnit();
        //remove all units from previous owner's units
        for(Unit u: p.getEnemyUnits()){
               p.addUnit(u);
        }
        p.clearEnemyUnit();
        //delete the province saving only the wealth and treasury
        p.setTrainingSlot(2);
        p.clearTroopInTraining();
        previousOwner.removeProvince(p);
        newOwner.addProvince(p);
        p.setOwner(newOwner);

    }

    /**
     * whether a unit belongs to the previous owner of the province
     * @param a the unit 
     * @return true if yes
     */
    public static Boolean isPreviousOwner(Unit a){
        return a.getCurrentMovementPoints()==a.getMovementPoints();
    }

    /**
     * get the chance of breaking and routing 
     * perform related operations such as inflict casualty
     * @param a the attacking unit
     * @param d the defending unit
     * @param et the engagement type
     * @return
     */
    public static Unit handleBreak(Unit a, Unit d,String et){
        Boolean aBreak = getBreakA(a, d);
        Boolean dBreak = getBreakA(d, a);
        if(aBreak)  a.setBroken(true);
        if(dBreak)  d.setBroken(true);
        if(aBreak&& dBreak)    return null;
        Boolean aRoute= false;
        Boolean dRoute = false;
        if(aBreak){
            while(!aRoute && a.getCurrentSize()==0){
                inflictCasualty(a, d, et);
                aRoute=getRouteA(a, d);   
            } 
        }else{
            while(!dRoute && d.getCurrentSize()==0){
                inflictCasualty(a, d, et);
                dRoute=getRouteA(a, d);
            }
        }
        if(aRoute)  return a;
        return d;

    }
    


    /**
     * get the engagement type based on unit types and random chance
     * @param a one unit
     * @param b the other unit 
     * @return the engagement type
     */   
    public static String getEngagementType(Unit a, Unit b) {
        String type = "";
        if (a.getAttackType().equals(b.getAttackType())) {
            if (a.getAttackType().equals("melee")) {
                type = "melee";
            } else {
                type = "missile";
            }
        } else {
            if (a.getAttackType().equals("melee")) {
                int melee_prob = 50 + 10 * (a.getSpeed() - b.getSpeed());
                if (melee_prob > 95) {
                    melee_prob = 95;
                }
                if (melee_prob < 0) {
                    type = "missile";
                }
                int ran = BattleResolver.getRandomNumberUsingNextInt(0, 100);
                if (ran > melee_prob) {
                    type = "missile";
                } else {
                    type = "melee";
                }

            }
        }
        return type;
    }

    /**
     * finish the training on player pl's turn
     * @param pl it's this player's turn
     * @param provinces a list of all the provinces
     */
    public static ArrayList<Unit> finishTraining(Player pl, ArrayList<Province> provinces){
        ArrayList<Unit> res = new ArrayList<Unit>();
        for(Province p: provinces){
            for(Unit u: p.getUnits()){
                if(!u.getState().equals("training"))   continue;
                String kind = u.getKind();
                if( (kind.equals("Elephants")&&!u.getLocation().getOwner().equals(pl) ) || !kind.equals("Elephants") ){
                    u.setState("trained");
                    res.add(u);
                    int curts = u.getLocation().getTrainingSlot();
                    u.getLocation().setTrainingSlot(curts+1);
                    u.getLocation().removeTroopInTraining(u);
                }
            }
        }
        return res;
    }

    /**
     * allocate a new turn to a player
     * @param player the player of the new turn
     * @param p a list of all provinces
     */
    public static void newTurn(Player player, ArrayList<Province> p){ 
        resetMovementPoint(p);
        addAllGrowth(p);
        collectAllTax(p);
        finishTraining(player,p);
    }

    /**
     * reset the movement points of all the units in a map
     * preparing for a new turn
     * @param pv a list of all provinces
     */
    public static void resetMovementPoint(ArrayList<Province> pv){
        for(Province p: pv){
            for(Unit u: p.getUnits())    u.resetMP();
        }    
    }

    /**
     * automatically collect all taxs
     * used at the start of a turn
     * @param provinces a listof all provinces
     */
    public static void collectAllTax(ArrayList<Province> provinces){
        for(Province p: provinces)  p.getWealth().tax();
    }

    /**
     * add the growth of turn automatically to all provinces
     * @param provinces a list of all provinces
     */
    public static void addAllGrowth(ArrayList<Province> provinces){
        for(Province p: provinces)  p.getWealth().addGrowth();
    }

    /**
     * does the unit a break?
     * @param a
     * @param b
     * @return true if yes
     */
    public static Boolean getBreakA(Unit a, Unit b){
        if(a.getCurrentSize()==0)   return false;
        double base = 1- a.getMorale()*0.1;
        if(base ==0)    return false;
        double add = (a.getInitialSize()-a.getCurrentSize())*b.getInitialSize()*0.1/a.getInitialSize()*(b.getInitialSize()-b.getCurrentSize());
        double prob = base+add;
        if(prob<0.05)   prob = 0.05;
        if(prob>=1) return true;
        double ran = ThreadLocalRandom.current().nextDouble(0, 1);
        if(ran>prob)    return false;
        return true;
    }

    /**
     * whehter a routes successfully
     * @param a
     * @param b
     * @return if a routes successfully, then true
     */
    public static Boolean getRouteA(Unit a, Unit b){
        if(a.getCurrentSize()==0)   return false;
        double prob = 0.5 + 0.1*(a.getSpeed()-b.getSpeed());
        if(prob <0.1) prob = 0.1;
        if(prob>=1)  return true;
        double ran = ThreadLocalRandom.current().nextDouble(0, 1);
        if(ran>prob)    return false;
        return true;
    }


    /**
     * randomly choosing a unit from list ul
     * @param ul a list of units
     * @return
     */
    public static Unit chooseUnit(ArrayList<Unit> ul){
        int unit_index = getRandomNumberUsingNextInt(0, ul.size());
        return ul.get(unit_index);     
    }


    /**
     * generate a random integer betwen min and max
     * @param min   minimum
     * @param max maximum
     * @return the random number
     */
    public static int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    /**
     * generate a positive gaussian double
     * @return the random double
     */
    public static double getPosGaussian(){
        Random r  = new Random();
        return Math.abs(r.nextGaussian());
    }
  
}