// package test;

// import static org.junit.jupiter.api.Assertions.assertEquals;

// import org.junit.jupiter.api.Test;
// import java.util.ArrayList;
// import unsw.gloriaromanus.*;

// public class BattleResolverTest{
//     @Test
//     public void inflictCasualtyTest() throws Exception{

//         Province p = new Province("Britannia");
//         assert(p.getWealth().getTeasury() == 1000);
//         Unit m1 = p.recruitTroop("Infantry");
//         Unit r1=p.recruitTroop("Artillery");

//         Province p2 = new Province("Arabia");
//         assert(p2.getWealth().getTeasury()== 1000);
//         Unit m2 = p2.recruitTroop("Infantry");
//         Unit r2=p2.recruitTroop("Artillery");

//         BattleResolver.inflictCasualty(m1, m2, "melee");
//         BattleResolver.inflictCasualty(r1, r2, "ranged");
//         String et = BattleResolver.getEngagementType(m1, r2);
//         BattleResolver.inflictCasualty(m1,r2,et);
//     }
    

//     //MOVE THIS TO PLAYER
//     @Test
//     public void attackTest() throws Exception{
//         Province p = new Province("Britannia");
//         assert(p.getWealth().getTeasury() == 1000);
//         Unit m1 = p.recruitTroop("Infantry");
//         Unit r1=p.recruitTroop("Horse archers");

//         Province p2 = new Province("Arabia");
//         assert(p2.getWealth().getTeasury() == 1000);
//         Unit m2 = p2.recruitTroop("Infantry");
//         Unit r2 = p2.recruitTroop("Missile infantry");

//     }

//     @Test
//     public void resetProvinceTest() throws Exception{
//         ArrayList<String> goal1 = new ArrayList<String>();
//         ArrayList<String> goal2 = new ArrayList<String>();
//         goal1.add("conquest");
//         goal2.add("wealth");
//         Game g = new Game();  
//         Player p1 = new Player(goal1, g);
//         Player p2 = new Player(goal2, g);
//         g.addPlayer(p1);
//         g.addPlayer(p2);
//         Province pv1 = new Province("Britannia");
//         pv1.setOwner(p1);

//         Unit routedUnit = new Unit("Infantry",pv1);
//         Unit enemyUnit = new Unit("Infantry",pv1);
//         Unit trainingUnit = new Unit("Infantry",pv1);
//         Unit unit = new Unit("Infantry",pv1);

//         BattleResolver.handleBreak(enemyUnit, unit, "melee");
//         enemyUnit.setBroken(true);
//         BattleResolver.handleBreak(enemyUnit, unit, "melee");

//         pv1.addRoutedUnit(routedUnit);
//         assert(pv1.getRoutedUnits().contains(routedUnit));
//         pv1.addTroopInTraining(trainingUnit);
//         assert(pv1.getTroopInTraining().contains(trainingUnit));
//         pv1.addUnit(unit);
//         assert(pv1.getUnits().contains(unit));
//         pv1.addEnemyUnit(enemyUnit);
//         assert(pv1.getEnemyUnits().contains(enemyUnit));

//         BattleResolver.resetProvince(p2, pv1);
//         assert(pv1.getRoutedUnits().isEmpty());
//         assert(pv1.getEnemyUnits().isEmpty());
//         assert(pv1.getUnits().contains(enemyUnit));
//         assert(pv1.getTroopInTraining().isEmpty());
//         assert(pv1.getOwner().equals(p2));
//     }

//     @Test
//     public void getEngagementTypeTest() throws Exception{
//         Province p = new Province("Britannia");
//         assert(p.getWealth().getTeasury()== 1000);
//         Unit m1 = p.recruitTroop("Infantry");
//         Unit r1=p.recruitTroop("Artillery");

//         Province p2 = new Province("Arabia");
//         //assert(p2.getWealth().getTeasury()== 1000);
//         Unit m2 = p2.recruitTroop("Infantry");
//         Unit r2=p2.recruitTroop("Horse archers");

//         assert(BattleResolver.getEngagementType(m1, m2).equals("melee"));
//         assert(BattleResolver.getEngagementType(r1, r2).equals("missile"));
//     }

//     @Test
//     public void finishTrainingTest(){
        
//     }

//     @Test
//     public void newTurnTest(){
//         assertEquals("a", "a");
//     }
// }

