package unsw.gloriaromanus;
import java.util.ArrayList;
import org.json.JSONObject;

public class Province {
    private String name;
    private Player currentOwner;
    private ArrayList<Unit> units;
    private Wealth wealth;
    private int trainingSlots;
    private ArrayList<Unit> troopInTraining;
    private ArrayList<Unit> enemyUnits;
    private ArrayList<Unit> routedUnits;
    private ArrayList<Province> adjacentProvinces;
    private int buildingLevel;
    private int marketLevel;
    private double reduce = 0.0;
    private int mineLevel = 1;
    private double sreduce = 0.0;
    private int upgradeBuildingCap = 1;
    private int upgradeBuildingNum = 1;

    private String content = "{\r\n    \"Cavalry\": {\r\n        \"attackType\" : \"melee\",\r\n        \"morale\" : 40,\r\n        \"damage\" : 38,\r\n        \"charge\": 28,\r\n        \"movementPoints\" : 15,\r\n        \"recruitCost\" : 490,\r\n        \"trainCost\" : 100,\r\n        \"speed\" : 10,\r\n        \"armour\" : 15,\r\n        \"defenseSkill\" : 50,\r\n        \"shieldDefense\" : 7\r\n\r\n    },\r\n\r\n    \"Infantry\": {\r\n        \"attackType\" : \"melee\",\r\n        \"morale\" : 25,\r\n        \"damage\" : 26,\r\n        \"charge\": 10,\r\n        \"movementPoints\" : 10,\r\n        \"recruitCost\" : 260,\r\n        \"trainCost\" : 60,\r\n        \"speed\" : 7,\r\n        \"armour\" : 45,\r\n        \"defenseSkill\" : 36,\r\n        \"shieldDefense\" : 15\r\n    },\r\n\r\n    \"Artillery\": {\r\n        \"attackType\" : \"missile\",\r\n        \"morale\" : 40,\r\n        \"damage\" : 24,\r\n        \"charge\": 6,\r\n        \"movementPoints\" : 4,\r\n        \"recruitCost\" : 560,\r\n        \"trainCost\" : 110,\r\n        \"speed\" : 3,\r\n        \"armour\" : 40,\r\n        \"defenseSkill\" : 14,\r\n        \"shieldDefense\" : 15\r\n    },\r\n\r\n    \"Heavy infantry\": {\r\n        \"attackType\" : \"melee\",\r\n        \"morale\" : 30,\r\n        \"damage\" : 25,\r\n        \"charge\": 11,\r\n        \"movementPoints\" : 10,\r\n        \"recruitCost\" : 190,\r\n        \"trainCost\" : 60,\r\n        \"speed\" : 4,\r\n        \"armour\" : 45,\r\n        \"defenseSkill\" : 34,\r\n        \"shieldDefense\" : 20\r\n    },\r\n\r\n    \"Spearmen\": {\r\n        \"attackType\" : \"melee\",\r\n        \"morale\" : 25,\r\n        \"damage\" : 25,\r\n        \"charge\": 11,\r\n        \"movementPoints\" : 0,\r\n        \"recruitCost\" : 230,\r\n        \"trainCost\" : 50,\r\n        \"speed\" : 15,\r\n        \"armour\" : 45,\r\n        \"defenseSkill\" : 34,\r\n        \"shieldDefense\" : 10\r\n    },\r\n\r\n    \"Missile infantry\": {\r\n        \"attackType\" : \"missile\",\r\n        \"morale\" : 25,\r\n        \"damage\" : 24,\r\n        \"charge\": 3,\r\n        \"movementPoints\" : 10,\r\n        \"recruitCost\" : 290,\r\n        \"trainCost\" : 70,\r\n        \"speed\" : 10,\r\n        \"armour\" : 10,\r\n        \"defenseSkill\" : 12,\r\n        \"shieldDefense\" : 5\r\n    },\r\n\r\n    \"Melee cavalry\": {\r\n        \"attackType\" : \"melee\",\r\n        \"morale\" : 40,\r\n        \"damage\" : 38,\r\n        \"charge\": 28,\r\n        \"movementPoints\" : 15,\r\n        \"recruitCost\" : 490,\r\n        \"trainCost\" : 100,\r\n        \"speed\" : 10,\r\n        \"armour\" : 15,\r\n        \"defenseSkill\" : 50,\r\n        \"shieldDefense\" : 15\r\n    },\r\n\r\n    \"Horse archers\": {\r\n        \"attackType\" : \"missile\",\r\n        \"morale\" : 35,\r\n        \"damage\" : 24,\r\n        \"charge\": 8,\r\n        \"movementPoints\" : 20,\r\n        \"recruitCost\" : 450,\r\n        \"trainCost\" : 90,\r\n        \"speed\" : 20,\r\n        \"armour\" : 10,\r\n        \"defenseSkill\" : 15,\r\n        \"shieldDefense\" : 6\r\n    },\r\n\r\n    \"Elephants\": {\r\n        \"attackType\" : \"melee\",\r\n        \"morale\" : 55,\r\n        \"damage\" : 60,\r\n        \"charge\": 36,\r\n        \"movementPoints\" : 10,\r\n        \"recruitCost\" : 800,\r\n        \"trainCost\" : 180,\r\n        \"speed\" : 17,\r\n        \"armour\" : 30,\r\n        \"defenseSkill\" : 21,\r\n        \"shieldDefense\" : 5\r\n    },\r\n\r\n    \"Chariots\": {\r\n        \"attackType\" : \"melee\",\r\n        \"morale\" : 55,\r\n        \"damage\" : 35,\r\n        \"charge\": 20,\r\n        \"movementPoints\" : 10,\r\n        \"recruitCost\" : 800,\r\n        \"trainCost\" : 90,\r\n        \"speed\" : 15,\r\n        \"armour\" : 70,\r\n        \"defenseSkill\" : 61,\r\n        \"shieldDefense\" : 20\r\n    }    \r\n}";


    /**
     * initialise a province
     * @param nm name of the province
     * @throws Exception
     */
    public Province(String nm) throws Exception{
        this.name = nm;
        this.marketLevel=0;
        this.currentOwner = null;
        this.units = new ArrayList<Unit>();
        this.buildingLevel=0;
        this.wealth = new Wealth(this);
        this.trainingSlots = 2;
        this.troopInTraining = new ArrayList<Unit>();
        this.enemyUnits = new ArrayList<Unit>();
        this.routedUnits = new ArrayList<Unit>();
        this.adjacentProvinces = new ArrayList<Province>();
        
    }

    public String getName(){
        return name;
    }
    public int getMarketLevel(){
        return marketLevel;
    }
    public Boolean upgradeMarketLevel(){
        if(wealth.getTeasury()<10)  return false;
        marketLevel++;
        wealth.setTreasury(wealth.getTeasury()-10);
        reduce = (double) marketLevel*15/100;
        return true;
    }
    public int getBuildingLevel(){
        return buildingLevel;
    }
    public int getMineLevel(){
        return mineLevel;
    }
    public Boolean upgradeMine(){
        if(wealth.getTeasury()<40)  return false;
        if(mineLevel>2)    return false;
        
        mineLevel++;
        if(mineLevel==3)    upgradeBuildingCap++;
        sreduce +=0.15;
        int oldt = wealth.getTeasury();
        int cost = (int)(1-reduce)*40;
        int ne = (int) oldt- cost;
        wealth.setTreasury(ne);
        return true;
    }
    public void upgradeBuilding(){
        if(upgradeBuildingNum==0)   return;
        buildingLevel++;
        int oldt = wealth.getTeasury();
        int cost = (int)(1-reduce)*30;
        int ne = (int) oldt- cost;
        wealth.setTreasury(ne);
        upgradeBuildingNum--;
    }
    public Player getOwner(){
        return currentOwner;
    }
    public ArrayList<Unit> getUnits(){
        return units;
    }
    public void resetUpgradeBuildingNum(){
        upgradeBuildingNum = upgradeBuildingCap;
    }
    public Wealth getWealth(){
        return wealth;
    }
    public int getTrainingSlot(){
        return trainingSlots;
    }

    public void addAdjacentProvince(Province p){
        adjacentProvinces.add(p);
    }
    public void addUnit(Unit a){
        units.add(a);
    }
    public ArrayList<Unit> getRoutedUnits(){
        return routedUnits;
    }
    public void addRoutedUnit(Unit a){
        routedUnits.add(a);
    }
    public void clearRoutedUnit(){
        routedUnits.clear();
    }
   
    public void setOwner(Player p){
        this.currentOwner = p;
    }
    
    public void setTrainingSlot(int ns){
        this.trainingSlots = ns;
    }
    public void addMorale(int a){
        for(Unit u: units){
            u.setMorale(u.getMorale()+a);
        }
    }
    /**
     * recruit troop of type
     * @param type the type of unit to recruit
     * @return
     * @throws Exception
     */
    public Unit recruitTroop(String type) throws Exception {
        
        JSONObject unitInfo = new JSONObject(content);
        int recruitPrice = unitInfo.getJSONObject(type).getInt("recruitCost");

        if(wealth.getTeasury() >= recruitPrice){
            Unit n = new Unit(type, this);
            int cost = (int) (1-sreduce)*recruitPrice;
            wealth.setTreasury(wealth.getTeasury() - cost);
            units.add(n);
            n.setLocation(this);
            return n;
        }
        return null;
    }

    public void addTroopInTraining(Unit u){
        troopInTraining.add(u);
    }

    public ArrayList<Unit> getTroopInTraining(){
        return troopInTraining;
    }
    public void removeTroopInTraining(Unit u){
        troopInTraining.remove(u);
    }

    public void removeUnit(Unit a){
        units.remove(a);
    }
    public void clearTroopInTraining(){
        troopInTraining.clear();
    }
    public void clearUnits(){
        units.clear();
    }

    public ArrayList<Unit> getEnemyUnits(){
        return enemyUnits;
    }
    public void removeEnemyUnit(Unit a){
        enemyUnits.remove(a);
    }
    /**
     * return a list of available unit of this province
     * not including those broken
     * those routed
     * and those not trained
     * @return
     */
    public ArrayList<Unit> availUnits(){
        ArrayList<Unit> au = new ArrayList<Unit>();
        for(Unit u: units){
            if(u.getState().equals("trained") && !routedUnits.contains(u))  au.add(u);
        }
        return au;
    }
    public void clearEnemyUnit(){
        enemyUnits.clear();
    }

    public ArrayList<Province> getSuperAdjacentProvinces(){
        return adjacentProvinces;
    }    

    public void addEnemyUnit(Unit a){
        enemyUnits.add(a);
    }

    public ArrayList<Unit> getUntrainedUnits(){
        ArrayList<Unit> res = new ArrayList<Unit>();
        for(Unit u: units){
            if(u.getState().equals("untrained")) res.add(u);
        }
        return res;
    }
}
