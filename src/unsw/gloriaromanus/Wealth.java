package unsw.gloriaromanus;


public class Wealth {
    private int taxRate;
    private Province province;
    private int wealthTown;
    private int treasury;
    private int growth;

    /**
     * initialise thr wealth of a province p
     */
    public Wealth(Province p){
        this.taxRate = 10;
        this.province = p;
        this. wealthTown = 100000;
        this.treasury = 1000;
        this.growth = 10;

    }

    public int getWealthTown(){
        return wealthTown;
    }

    public int getTeasury(){
        return treasury;
    }


    /**
     * set the tax rate of this province and corresponding growth
     */
    public void setTaxRate(int tr){
        this.taxRate = tr;
        if(tr == 10){
            setGrowth(10);
        }else if(tr == 15){
            setGrowth(0);
        }else if(tr == 20){
            setGrowth(-10);
        }else{
            setGrowth(-30);
            province.addMorale(-1);
        }
    }

    public int getGrowth(){
        return growth;
    }
    public void setGrowth(int r){
        this.growth = r;
    }
  
    public void setTreasury(int t){
        this.treasury = t;
    }

    /**
     * collect tax and update treasury and town wealth
     * @return
     */
    public int tax(){
        if(wealthTown<=0)   return 0;
        int tax = Math.round(taxRate*wealthTown/100);
        treasury += tax;
        wealthTown -= tax;
        return tax;
    }

    public void addGrowth(){
        wealthTown += growth;
    }

    public int getTaxRate(){
        return taxRate;
    }
}
