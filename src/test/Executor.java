// package unsw.gloriaromanus;

// import static org.junit.Assert.assertEquals;

// public class Executor {

//     public static void main(String []args) throws Exception {
//         Game g = new Game();
//         Player p = new Player(g);
//         Player p1 = new Player(g);
//         g.addPlayer(p);
//         g.addPlayer(p1);
//         g.startGame();
//         ManageTurn et = new ManageTurn();
//         et.add_player(p);
//         et.add_player(p1);


//         // System.out.println(g.getPlayes());
//         Province prov = g.getPlayes().get(0).getProvinces().get(0);

//         assertEquals(prov.getUnits().size(), 0);
//         Unit u = p.recruit("Elephants", prov);
        
//         assertEquals(prov.getUnits().size(), 1);

//         et.EndTurn(g);

//     }
// }
