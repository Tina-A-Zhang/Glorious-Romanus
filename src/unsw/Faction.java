package unsw.gloriaromanus;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class Faction {
    private String name;
    private ArrayList<Province> provinces;
    String content = "{\r\n  \"Rome\": [\r\n    \"Lugdunensis\",\r\n    \"Lusitania\",\r\n    \"Lycia et Pamphylia\",\r\n    \"Macedonia\",\r\n    \"Mauretania Caesariensis\",\r\n    \"Mauretania Tingitana\",\r\n    \"Moesia Inferior\",\r\n    \"Moesia Superior\",\r\n    \"Narbonensis\",\r\n    \"Noricum\",\r\n    \"Numidia\",\r\n    \"Pannonia Inferior\",\r\n    \"Pannonia Superior\",\r\n    \"Raetia\",\r\n    \"Sardinia et Corsica\",\r\n    \"Sicilia\",\r\n    \"Syria\",\r\n    \"Tarraconensis\",\r\n    \"Thracia\",\r\n    \"V\",\r\n    \"VI\",\r\n    \"VII\",\r\n    \"VIII\",\r\n    \"X\",\r\n    \"XI\"\r\n  ],\r\n  \"Gaul\": [\r\n    \"Achaia\",\r\n    \"Aegyptus\",\r\n    \"Africa Proconsularis\",\r\n    \"Alpes Cottiae\",\r\n    \"Alpes Graiae et Poeninae\",\r\n    \"Alpes Maritimae\",\r\n    \"Aquitania\",\r\n    \"Arabia\",\r\n    \"Armenia Mesopotamia\",\r\n    \"Asia\",\r\n    \"Baetica\",\r\n    \"Belgica\",\r\n    \"Bithynia et Pontus\",\r\n    \"Britannia\",\r\n    \"Cilicia\",\r\n    \"Creta et Cyrene\",\r\n    \"Cyprus\",\r\n    \"Dacia\",\r\n    \"Dalmatia\",\r\n    \"Galatia et Cappadocia\",\r\n    \"Germania Inferior\",\r\n    \"Germania Superior\",\r\n    \"I\",\r\n    \"II\",\r\n    \"III\",\r\n    \"IV\",\r\n    \"IX\",\r\n    \"Iudaea\"\r\n  ]\r\n}";
    JSONObject unitInfo = new JSONObject(content);

    public Faction(String nm) throws Exception {
        this.name=nm;
        setFactions(nm);
    }

    /**
     * create a faction of name faction
     * @param faction the name
     * @throws Exception
     */
    public void setFactions(String faction) throws Exception{
        JSONArray rps = unitInfo.getJSONArray(faction);
        ArrayList<Province> res = new ArrayList<Province>();
        for(Object j: rps){
            Province n = new Province(j.toString());
            res.add(n);
        }
        provinces = res;
    }
    public void setOwner(Player p){
        for(Province pv: provinces){
            pv.setOwner(p);
        }
    }
    public String getName(){
        return name;
    }
    public ArrayList<Province> getProvinces(){
        return provinces;
    }

    
}
