// package test;
// import unsw.gloriaromanus.*;
// import java.util.ArrayList;
// import org.junit.jupiter.api.Test;


// public class GameTest {

    
//     @Test
//     public void gameTest1() throws Exception {
//         ArrayList<String> goal1 = new ArrayList<String>();
//         ArrayList<String> goal2 = new ArrayList<String>();
//         ArrayList<Player> players = new ArrayList<Player>();

//         goal1.add("conquest");
//         goal2.add("wealth");

//         Game g = new Game();

//         Player p1 = new Player(goal1, g);
//         Player p2 = new Player(goal2, g);

//         players.add(p1);
//         players.add(p2);

//         g.addPlayer(p1);
//         g.addPlayer(p2);
//         g.assignFactions();
    
        
//         BattleResolver.newTurn(p1, g.getProvinces());
        
//         assert(p1.equals(g.hasWinner())== false);
//         assert(p1.getFaction().getName().equals("Rome")||p1.getFaction().getName().equals("Gaul"));
//         assert(p1.getTurn());
//         assert(!p2.getTurn());

//         Province province1 = new Province("Britannia");
//         Province province2 = new Province("Lugdunensis");
//         assert(!g.isEnemy("Britannia", "Lugdunensis"));
//         p1.addProvince(province1);
//         province1.setOwner(p1);
//         p2.addProvince(province2);
//         province2.setOwner(p2);
        
//         Unit unit1 = province1.recruitTroop("Infantry");
//         unit1.setLocation(province1);

//         Unit unit2 = province2.recruitTroop("Infantry");
//         unit2.setLocation(province2);

//         Unit unit3 = province1.recruitTroop("Infantry");
//         unit3.setLocation(province2);

//         province2.addEnemyUnit(unit1);
//         province2.addUnit(unit1);
//         province2.addUnit(unit3);
//         assert(BattleResolver.attack(province2));
//         p1.recruit("Infantry", province1);
//         assert(BattleResolver.attack(province2));
//         p1.endTurn();
//         p2.endTurn();

//         Unit u2 = p2.recruit("Infantry", province2);
//         ArrayList<Unit> ul = new ArrayList<Unit>();

//         ul.add(u2);
//         p2.attack(ul, province1);
//         p2.endTurn();
        
//     }


// //     @Test 
// //     public void startGameTest(){

// //     }

//     @Test 
//     public void saveGameTest() throws Exception{
//         ArrayList<String> goal1 = new ArrayList<String>();
//         ArrayList<String> goal2 = new ArrayList<String>();
//         ArrayList<Player> players = new ArrayList<Player>();

//         goal1.add("conquest");
//         goal2.add("wealth");

//         Game g = new Game();

//         Faction f1 = new Faction("Rome");
//         Faction f2 = new Faction("Gaul");
//         g.addFaction(f1);
//         g.addFaction(f2);
//         Player p1 = new Player(goal1, g);
//         Player p2 = new Player(goal2, g);

//         players.add(p1);
//         players.add(p2);

//         g.addPlayer(p1);
//         g.addPlayer(p2);
        
//         p1.endTurn();
//         p1.save();
//         g.loadGame(p1);
//         assert(!p1.getTurn());
//     }

//     // @Test 
//     // public void loadGameTest(){
        
//     // }
// }
