// package test;



// import org.junit.jupiter.api.Test;
// import java.util.ArrayList;
// import unsw.gloriaromanus.*;

// public class PlayerTest {
    
//     @Test
//     public void moveTroopsTest() throws Exception{
//         ArrayList<String> goal1 = new ArrayList<String>();
//         ArrayList<String> goal2 = new ArrayList<String>();


//         goal1.add("conquest");
//         goal2.add("wealth");

//         Game g = new Game(); 

//         Player player1 = new Player(goal1, g);
//         Player player2 = new Player(goal2, g);
        
//         g.addPlayer(player1);
//         g.addPlayer(player2);
        
//         Province province1 = new Province("Britannia");
//         Province province2 = new Province("Lugdunensis");
//         Province province3 = new Province("Belgica");
//         Province province4 = new Province("Germania Superior");

//         player1.addProvince(province1);
//         player2.addProvince(province3);
//         player1.addProvince(province4);
//         player2.addProvince(province2);
//         province1.setOwner(player1);
//         province2.setOwner(player2);
//         province4.setOwner(player1);
//         province3.setOwner(player2);


//         ArrayList<Unit> unitList1 = new ArrayList<Unit>();
//         ArrayList<Unit> unitList2 = new ArrayList<Unit>();


//         Unit unit1 = province1.recruitTroop("Infantry");
//         unit1.setLocation(province1);

//         Unit unit2 = province3.recruitTroop("Infantry");
//         unit2.setLocation(province3);

//         Unit unit3 = province2.recruitTroop("Infantry");
//         unit3.setLocation(province2);

//         Unit unit4 = province2.recruitTroop("Spearmen");
//         unit4.setLocation(province1);

//         unitList1.add(unit1);
//         unitList2.add(unit2);
//         unitList2.add(unit3);
//         unitList1.add(unit4);

//         assert(unit1.getCurrentMovementPoints() == unit1.getMovementPoints());

//         //If units are not in same province, reject 
//         assert(!player1.moveUnitsTA(unitList1, province2));

//         //no path
//         BFS bfs1 = new BFS(g);
//         assert(bfs1.bfs("Britannia", "Germania Superior") == -1);

//         //Check one path, in which theres an enemy province, reject
//         player2.setTurn(true);
//         player1.setTurn(true);

//         assert(!player2.moveUnitsTA(unitList2, province4));
//         assert(player1.getProvinces().contains(province1));
//         assert(bfs1.bfs("Britannia","Lugdunensis") == -1);
//         assert(!player1.moveUnitsTA(unitList1, province2));

//         //One unit has less than enough mp
//         assert(!player1.moveUnitsTA(unitList1, province4));
//         assert(player2.getProvinces().contains(province3));
//         assert(bfs1.bfs("Belgica","Germania Superior") == -1);
//         assert(player2.moveUnitsTA(unitList2, province3));

//         //Check original province does not have the unit
//         assert(province1.getUnits().contains(unit1) != false);
//         assert(province2.getUnits().contains(unit2) == false);

//         assert(!player1.moveUnits(unitList1, province2));
//         assert(player2.moveUnitsTA(unitList2, province3));

//         player1.attack(unitList1, province2);
//         player1.attack(unitList1, province2);

//     }

    
//     @Test
//     public void recruitTest() throws Exception{
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
//         Province pv1 = new Province("Britannia");
//         p1.addProvince(pv1);
//         assert(p2.recruit("Infantry", pv1)==null);
//     }


//     @Test 
//     public void trainTest() throws Exception {
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
//         Province pv1 = new Province("Britannia");
//         Province pv2 = new Province("Belgica");
//         p1.addProvince(pv1);
//         p2.addProvince(pv2);
//         Unit u1 = new Unit("Infantry",pv1);
//         Unit u2 = new Unit("Chariots",pv1);
//         Unit u3 = new Unit("Infantry",pv1);
//         Unit u4 = new Unit("Infantry",pv1);
//         Unit u5 = new Unit("Elephants",pv2);
//         pv1.addUnit(u1);
//         pv1.addUnit(u2);
//         pv1.addUnit(u3);
//         pv1.addUnit(u4);
//         pv2.addUnit(u5);
//         assert(!p2.train(u1, pv1));
//         assert(!p1.train(u2, pv1));
//         assert(!p1.train(u1, pv1));
//         assert(!pv1.getTroopInTraining().contains(u1));
//         assert(pv1.getTrainingSlot()==2);
//         assert(!p1.train(u3, pv1));
//         assert(!pv1.getTroopInTraining().contains(u3));
//         assert(pv1.getTrainingSlot()==2);
//         assert(!p1.train(u4, pv1));
//         assert(!p2.train(u5, pv2));
//         assert(!pv2.getTroopInTraining().contains(u5));
//         assert(pv2.getTrainingSlot()==2);
//         ArrayList<Province> pl = new ArrayList<Province>();
//         pl.add(pv1);
//         pl.add(pv2);
//         BattleResolver.finishTraining(p2, pl);
//         assert(pv1.getTroopInTraining().isEmpty());
//         assert(pv1.getTrainingSlot()==2);
//         assert(pv1.getTroopInTraining().isEmpty());
//         assert(pv1.getTrainingSlot()==2);
//         assert(!u1.getState().equals("Trained"));
//         assert(!u3.getState().equals("Trained"));
//         assert(pv1.getUnits().contains(u4));
//         assert(!u5.getState().equals("Trained"));

//     }
   

    
//     // public static void main(String[] args) throws Exception {
//     //     moveTroopsTest();
//     // }

    
// }
