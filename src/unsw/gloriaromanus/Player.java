package unsw.gloriaromanus;

import java.util.ArrayList;


public class Player {
    private ArrayList<Province> provinces;
    private ArrayList<Unit> units;
    private String name;
    private Faction faction;
    private Boolean hasAttack;
    private Game game;
    private Goal goals;
    private Boolean hasUpgrade;

    /**
     * 
     * @param g goals of this player
     * @param game the game 
     */
    public Player (Game game) {
        this.provinces = new ArrayList<Province>();
        this.units = new ArrayList<Unit>();
        this.faction = null;
        this.hasAttack = false;
        this.hasUpgrade=false;
        this.game = game;
        // this.goals=new Goal(g,this);
    }

    public ArrayList<Unit> getUnit(){
        return units;
    }

    public ArrayList<Province> getProvinces() {
        return provinces;
    }

    public void setHasUpgrade(Boolean t){
        hasUpgrade =t;
    }


    public void removeUnit(Unit a) {
        units.remove(a);
    }
    public void removeProvince(Province p){
        if(p==null) return;
        provinces.remove(p);
    }
    public void addProvince(Province p){
        provinces.add(p);
    }

    public String getName() {
        return name;
    }
    public Faction getFaction() {
        return faction;
    }

    public void display() {
        System.out.printf("The faction %s has the battle", name);
    }
  
    public void setFaction(Faction f){
        this.faction = f;
        f.setOwner(this);
    }


    public Boolean getHA(){
        return hasAttack;
    }

/////////////   MAIN FUNCTIONALITY STARTS HERE      ////////

    /**
     * attack province p with units army
     * @param army attacking units
     * @param p attack dest
     */
    public void attack(ArrayList<Unit> army, Province p) {
        if(hasAttack)   return;
        hasAttack=true;
        BattleResolver.attack(p);
    }

    public void setAttack(Boolean ha){
        hasAttack = ha;
    }

    /**
     * move units to province p
     * assumption: units are in the  same province
     * @param units units to be moved
     * @param p destination province
     * @return whehter the move was successful
     */
    public Boolean moveUnits(ArrayList<Unit> units, Province p) {  

        if(!p.getOwner().equals(this))  return false; 
        Boolean res = moveUnitsTA(units, p);
        return res;
    }

    /**
     * move units to attack
     * difference with moveUnit is that its dest can be enemy prov
     * @param units units to be moved
     * @param p destinatino province
     * @return whether the move was sucessful
     */
    public Boolean moveUnitsTA(ArrayList<Unit> units, Province p) {
        Boolean isEnemy = !units.get(0).getLocation().getOwner().equals(p.getOwner());
       // if(!provinces.contains(p))  return false;
        String src = units.get(0).getLocation().getName();
        BFS bfs = new BFS(game);
        int np = bfs.bfs(src,p.getName());
        if(np==-1)   return false;
        int mpneeded = np*4;
        for(Unit u: units){
            if(u.getCurrentMovementPoints() < mpneeded)   return false;
        }
        for(Unit u: units){
            u.subtractMovementPoint(mpneeded);
            if(isEnemy){
                p.addEnemyUnit(u);
                
                
            }else{
                p.addUnit(u);
            }
            u.setPrevP(u.getLocation());
            u.setLocation(p);
            u.getPrevp().removeUnit(u);
        }
        return true;
    }

    /**
     * recruit a unit of type in province p
     * @param type unit type
     * @param p province where recruitment happen
     * @return  the unit recruited or null
     * @throws Exception 
     */
    public Unit recruit(String type, Province p) throws Exception {
        if(p.getOwner()!=this)  return null;
        Unit u = p.recruitTroop(type);
        if (u != null)
            units.add(u);
        return u;
    }

    /**
     * train a unit u in province p
     * @param u unit to be trained
     * @param p province where the unit belongs
     * @return  whether the training occur
     */
    public Boolean train(Unit u, Province p) {
        if(!provinces.contains(p))  return false;
        if (p.getTrainingSlot() < 1)
            return false;
        if (u.getState().equals("trained") || u.getState().equals("training"))
            return false;
        int treasury = p.getWealth().getTeasury();
        if (u.getTrainCost() > treasury)
            return false;
        p.addTroopInTraining(u);
        u.setState("training");
        p.getWealth().setTreasury(treasury - u.getTrainCost());
        p.setTrainingSlot(p.getTrainingSlot() - 1);
        return true;
    }

    public int CalculateTotalTreasurey() {
        int sum = 0;
        for (Province p : provinces)
            sum += p.getWealth().getTeasury();
        return sum;
    }

    /**
     * save game state using the  save class
     */
    public void save(){
        if(hasAttack)   return;
        game.save();
    }

    public Boolean upgradeBuilding(Province p){
        if(hasUpgrade)  return false;
        if(p.getBuildingLevel()>=3) return false;
        if(p.getWealth().getTeasury()<30)   return false;
        p.upgradeBuilding();
        return true;
    }
    public Boolean upgradeMarket(Province p){
        if(!p.getOwner().equals(this))  return false;
        if(p.upgradeMarketLevel())  return true;
        return false;
    }

    public Boolean upgradeMine(Province p){
        if(!p.getOwner().equals(this))  return false;
        return p.upgradeMine();
        
    }
    /**
     * load the game using loadgame function
     * 
     * @throws Exception
     */
    public void load() throws Exception {
        if(hasAttack) return;
        game.loadGame(this);
    }
    
    public int CalculateTotalWealth(){
        int sum = 0;
        for(Province p: provinces)  sum += p.getWealth().getWealthTown();
        return sum;
    }

    
    /**
     * set the  tax rate of a province p
     * @param p province
     * @param tr new taxrate of the province
     */
    public void setProvinceTR(Province p,int tr){
        p.getWealth().setTaxRate(tr);
    }

    
}
