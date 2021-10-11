package unsw.gloriaromanus;

import java.util.ArrayList;

public class Goal {

    private ArrayList<String> goals;
    private Player player;

    /**
     * implementation not finished yet
     * @param g the list of goals
     * @param p the player
     */
    public Goal(ArrayList<String> g, Player p){
        this.goals=g;
        this.player=p;
    }

    // public Boolean allmet(){
    //     for(String s: goals){
    //         if(!goalmet(player, s))  return false;
    //     }
    //     return false;
    // }
    // public static Boolean goalmet(Player p, String goal){
    //     if(goal.equals("conquest")) return conquerGoal(p);
    //     if(goal.equals("treasury")) return treasuryGoal(p);
    //     if(goal.equals("wealth")) return wealthGoal(p);
    //     return false;
    // }
    
    //BUG ALERT
    //HARD CODE THE NUMBER OF PROVINCES IN THE GAME
    // public static Boolean conquerGoal(Player p){
    //     return p.getProvinces().size()==50;
    // }

    // //100,000 gold (TREASURY goal)
    // public static Boolean treasuryGoal(Player p){
    //     return p.CalculateTotalTreasurey()>=100000;
    // }

    // //400,000 gold (WTF goal)
    // public static Boolean wealthGoal(Player p){
    //     return p.CalculateTotalWealth()>=400000;
    // }

}
