// import package
package unsw.gloriaromanus;

// import libraries
import java.io.*;
import org.json.JSONObject;

/**
 * Represents a basic unit of soldiers
 * 
 * incomplete - should have heavy infantry, skirmishers, spearmen, lancers, heavy cavalry, elephants, chariots, archers, slingers, horse-archers, onagers, ballista, etc...
 * higher classes include ranged infantry, cavalry, infantry, artillery
 * 
 * current version represents a heavy infantry unit (almost no range, decent armour and morale)
 */
public class Unit {

    private String id;
    private String attackType; // melee or missile
    private String kind; // name of the troop
    private int morale; // resistance to fleeing
    private int damage; // damage value
    private int charge; // charge value
    private Province location; // location the unit is currently in
    private String state; // trained or untrained
    private int movementPoints; // based on the spec
    private int currentMovementPoints; // how many points left
    private int initialSize; // the initial number of troops in this unit
    private int currentSize; // range of the unit
    private int recruitCost; // cost for recruit
    private int trainCost; // cost fot train
    private int speed;  // ability to disengage from disadvantageous battle
    private int armour;  // armour defense 
    private int defenseSkill;  // skill to defend in battle. Does not protect from arrows!
    private int shieldDefense; // a shield
    private Province prevP; //previous province, for attack
    private Boolean broken; // if unit is broken or not

    /**
     * constructor for unit
     * @param kind name of the troop
     * @param location the province the unit is currently in
     * @param initialSize initial size of the unit
     * @throws IOException when unit type is not defined
     */
    public Unit(String kind, Province location) throws IOException {

        // read the units from units json config file
        String content = "{\r\n    \"Cavalry\": {\r\n        \"attackType\" : \"melee\",\r\n        \"morale\" : 40,\r\n        \"damage\" : 38,\r\n        \"charge\": 28,\r\n        \"movementPoints\" : 15,\r\n        \"recruitCost\" : 490,\r\n        \"trainCost\" : 100,\r\n        \"speed\" : 10,\r\n        \"armour\" : 15,\r\n        \"defenseSkill\" : 50,\r\n        \"shieldDefense\" : 7\r\n\r\n    },\r\n\r\n    \"Infantry\": {\r\n        \"attackType\" : \"melee\",\r\n        \"morale\" : 25,\r\n        \"damage\" : 26,\r\n        \"charge\": 10,\r\n        \"movementPoints\" : 10,\r\n        \"recruitCost\" : 260,\r\n        \"trainCost\" : 60,\r\n        \"speed\" : 7,\r\n        \"armour\" : 45,\r\n        \"defenseSkill\" : 36,\r\n        \"shieldDefense\" : 15\r\n    },\r\n\r\n    \"Artillery\": {\r\n        \"attackType\" : \"missile\",\r\n        \"morale\" : 40,\r\n        \"damage\" : 24,\r\n        \"charge\": 6,\r\n        \"movementPoints\" : 4,\r\n        \"recruitCost\" : 560,\r\n        \"trainCost\" : 110,\r\n        \"speed\" : 3,\r\n        \"armour\" : 40,\r\n        \"defenseSkill\" : 14,\r\n        \"shieldDefense\" : 15\r\n    },\r\n\r\n    \"Heavy infantry\": {\r\n        \"attackType\" : \"melee\",\r\n        \"morale\" : 30,\r\n        \"damage\" : 25,\r\n        \"charge\": 11,\r\n        \"movementPoints\" : 10,\r\n        \"recruitCost\" : 190,\r\n        \"trainCost\" : 60,\r\n        \"speed\" : 4,\r\n        \"armour\" : 45,\r\n        \"defenseSkill\" : 34,\r\n        \"shieldDefense\" : 20\r\n    },\r\n\r\n    \"Spearmen\": {\r\n        \"attackType\" : \"melee\",\r\n        \"morale\" : 25,\r\n        \"damage\" : 25,\r\n        \"charge\": 11,\r\n        \"movementPoints\" : 15,\r\n        \"recruitCost\" : 230,\r\n        \"trainCost\" : 50,\r\n        \"speed\" : 15,\r\n        \"armour\" : 45,\r\n        \"defenseSkill\" : 34,\r\n        \"shieldDefense\" : 10\r\n    },\r\n\r\n    \"Missile infantry\": {\r\n        \"attackType\" : \"missile\",\r\n        \"morale\" : 25,\r\n        \"damage\" : 24,\r\n        \"charge\": 3,\r\n        \"movementPoints\" : 10,\r\n        \"recruitCost\" : 290,\r\n        \"trainCost\" : 70,\r\n        \"speed\" : 10,\r\n        \"armour\" : 10,\r\n        \"defenseSkill\" : 12,\r\n        \"shieldDefense\" : 5\r\n    },\r\n\r\n    \"Melee cavalry\": {\r\n        \"attackType\" : \"melee\",\r\n        \"morale\" : 40,\r\n        \"damage\" : 38,\r\n        \"charge\": 28,\r\n        \"movementPoints\" : 15,\r\n        \"recruitCost\" : 490,\r\n        \"trainCost\" : 100,\r\n        \"speed\" : 10,\r\n        \"armour\" : 15,\r\n        \"defenseSkill\" : 50,\r\n        \"shieldDefense\" : 15\r\n    },\r\n\r\n    \"Horse archers\": {\r\n        \"attackType\" : \"missile\",\r\n        \"morale\" : 35,\r\n        \"damage\" : 24,\r\n        \"charge\": 8,\r\n        \"movementPoints\" : 20,\r\n        \"recruitCost\" : 450,\r\n        \"trainCost\" : 90,\r\n        \"speed\" : 20,\r\n        \"armour\" : 10,\r\n        \"defenseSkill\" : 15,\r\n        \"shieldDefense\" : 6\r\n    },\r\n\r\n    \"Elephants\": {\r\n        \"attackType\" : \"melee\",\r\n        \"morale\" : 55,\r\n        \"damage\" : 60,\r\n        \"charge\": 36,\r\n        \"movementPoints\" : 10,\r\n        \"recruitCost\" : 800,\r\n        \"trainCost\" : 180,\r\n        \"speed\" : 17,\r\n        \"armour\" : 30,\r\n        \"defenseSkill\" : 21,\r\n        \"shieldDefense\" : 5\r\n    },\r\n\r\n    \"Chariots\": {\r\n        \"attackType\" : \"melee\",\r\n        \"morale\" : 55,\r\n        \"damage\" : 35,\r\n        \"charge\": 20,\r\n        \"movementPoints\" : 10,\r\n        \"recruitCost\" : 800,\r\n        \"trainCost\" : 90,\r\n        \"speed\" : 15,\r\n        \"armour\" : 70,\r\n        \"defenseSkill\" : 61,\r\n        \"shieldDefense\" : 20\r\n    }    \r\n}";
        JSONObject unitInfo = new JSONObject(content);

        // initialise the attributes
        this.kind = kind;
        this.id = null;
        this.location = location;
        this.state = "untrained";
        this.initialSize = 10;
        this.currentSize = initialSize;
        this.prevP = location;
        this.broken = false;
        this.attackType = unitInfo.getJSONObject(kind).getString("attackType");
        this.morale = unitInfo.getJSONObject(kind).getInt("morale");
        this.damage = unitInfo.getJSONObject(kind).getInt("damage");
        this.damage = unitInfo.getJSONObject(kind).getInt("charge");
        this.movementPoints= unitInfo.getJSONObject(kind).getInt("movementPoints");
        this.currentMovementPoints = movementPoints;
        this.recruitCost = unitInfo.getJSONObject(kind).getInt("recruitCost");
        this.trainCost = unitInfo.getJSONObject(kind).getInt("trainCost");
        this.speed = unitInfo.getJSONObject(kind).getInt("speed");
        this.armour = unitInfo.getJSONObject(kind).getInt("armour");
        this.defenseSkill = unitInfo.getJSONObject(kind).getInt("defenseSkill");
        this.shieldDefense = unitInfo.getJSONObject(kind).getInt("shieldDefense");

    }

    /**
     * 
     * @return
     */
    public Boolean getBroken(){
        return broken;
    }

    public void setID(String id){
        this.id = id;
    }

    public void setArmour(int a){
        armour=a;
    }

    public void setShield(int a){
        shieldDefense = a;
    }

    public void setBroken(Boolean b){
        broken = b;
    }

    public String getAttackType() {
        return attackType;
    }

    public String getKind() {
        return kind;
    }

    public int getMorale() {
        return morale;
    }

    public int getDamage() {
        return damage;
    }


    public Province getLocation() {
        return location;
    }

    
    public void setPrevP(Province p){
        this.prevP = p;
    }

    public Province getPrevp(){
        return prevP;
    }
    public String getState() {
        return state;
    }

    public int getMovementPoints() {
        return movementPoints;
    }

    public int getCurrentMovementPoints() {
        return currentMovementPoints;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public void setLocation(Province p){
        location = p;
    }
    public void subtractMovementPoint(int a){
        currentMovementPoints -= a;
    }
    public int getRecruitCost(){
        return recruitCost;
    }

    public int getTrainCost() {
        return trainCost;
    }

    public int getSpeed() {
        return speed;
    }

    public int getArmour() {
        return armour;
    }

    public int getDefenseSkill() {
        return defenseSkill;
    }

    public int getShieldDefense() {
        return shieldDefense;
    }

  
    public void setMorale(int newMorale) {
        this.morale = newMorale;
    }

    public void setInitialSize(int i){
        initialSize=i;
    }

    public void setState(String newState) {
        this.state = newState;
    }

    public void setCurrentSize(int newCurrentSize) {
        this.currentSize = newCurrentSize;
    }


    public void resetMP() {
        this.currentMovementPoints = movementPoints;
    }

    public String getID(){
        return id;
    }
}

