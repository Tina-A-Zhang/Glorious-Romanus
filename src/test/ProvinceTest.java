package test;

import org.junit.jupiter.api.Test;

import unsw.gloriaromanus.*;

public class ProvinceTest {
    
    @Test
    public void testRecruitTroops() throws Exception{
        Province p = new Province("Britannia");
        assert(p.getWealth().getTeasury() == 1000);
        Unit u = p.recruitTroop("Infantry");
        assert(p.getWealth().getTeasury() == 740);
        assert(p.getUnits().contains(u));

        Unit c = p.recruitTroop("Chariots");
        assert(c==null);
        assert(p.getWealth().getTeasury()== 740);
        assert(p.getUnits().size()==1);

        Unit d = p.recruitTroop("Spearmen");
        assert(p.getWealth().getTeasury() == 510);
        assert(d.getAttackType().equals("melee"));
        assert(p.getUnits().contains(d));
        assert(p.getUnits().size()==2);
        
    }

   @Test 
    public void testAddMorale() throws Exception{
        Province p = new Province("Britannia");
        assert(p.getWealth().getTeasury()==1000);

        Unit u = p.recruitTroop("Elephants");
        assert(p.getWealth().getTeasury() == 200);
        assert(p.getUnits().contains(u));
        assert(u.getMorale()==55);
        p.getWealth().setTaxRate(30);

        assert(u.getMorale()==54);
        assert(p.getWealth().getGrowth()==-30);

    }

    @Test
    public void testTaxRate() throws Exception{
        Province p = new Province("Britannia");
        assert(p.getWealth().getTeasury()==1000);
        p.getWealth().setTaxRate(10);
        assert(p.getWealth().getGrowth()==10);

        assert(p.getWealth().getWealthTown()==100000);
        int tax = p.getWealth().tax();
        assert(tax==10000);
       
    }

    @Test
    public void testGrowth() throws Exception{
        Province p = new Province("Britannia");
        p.getWealth().setTaxRate(20);
        assert(p.getWealth().getGrowth()==-10);
        assert(p.getWealth().getWealthTown()==100000);
        p.getWealth().addGrowth();
        assert(p.getWealth().getWealthTown()==99990);
    }

    @Test
    public void testTreasury() throws Exception{
        Province p = new Province("Britannia");
        assert(p.getWealth().getTeasury()==1000);
        p.getWealth().setTaxRate(10);

        assert(p.getWealth().getGrowth() == 10);
        assert(p.getWealth().getWealthTown()==100000);

        assert(p.getWealth().tax()==10000);
        assert(p.getWealth().getTeasury()==11000);
        assert(p.getWealth().getWealthTown()==90000);
    }

    
}
