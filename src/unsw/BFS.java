package unsw.gloriaromanus;

import java.util.LinkedList;
import java.util.Queue;


public class BFS {
    private String[] provinces = { "Britannia", "Lugdunensis", "Belgica", "Germania Inferior", "Aquitania",
            "Germania Superior", "Alpes Graiae et Poeninae", "XI", "Alpes Cottiae", "Alpes Maritimae", "IX",
            "Narbonensis", "Tarraconensis", "Baetica", "Lusitania", "Raetia", "Noricum", "X", "VIII", "VII", "VI", "IV",
            "V", "I", "III", "Sicilia", "Pannonia Superior", "Pannonia Inferior", "Dalmatia", "II",
            "Sardinia et Corsica", "Moesia Superior", "Dacia", "Moesia Inferior", "Thracia", "Macedonia", "Achaia",
            "Bithynia et Pontus", "Cilicia", "Creta et Cyrene", "Cyprus", "Aegyptus", "Arabia", "Iudaea", "Syria",
            "Africa Proconsularis", "Numidia", "Mauretania Caesariensis", "Mauretania Tingitana",
            "Galatia et Cappadocia", "Lycia et Pamphylia", "Asia", "Armenia Mesopotamia" };

    private Game game;
    private int[][] adjMatrix;

    public BFS(Game g) {
        this.game = g;
        this.adjMatrix = generateMatrix();
    }

    /**
     * get the index of the province in the list
     * @param p the mame of the province
     * @return the index
     */
    public int ProvinceToInt(String p) {
        int i = -1;
        for (i = 0; i < 53; i++) {
            if (provinces[i].equals(p))
                return i;
        }
        return i;
    }

    public void printMatrix(){
        int[][] matrix = generateMatrix();
        for(int i = 0; i < 53; i++){
            for(int j = 0; j<53; j++){
                System.out.print(matrix[i][j]);
                if(j==52)   System.out.println();
            }
        }
    }
    /**
     * generate an adjacency matrix from the game
     * @return the adjacency matrix
     */
    public int[][] generateMatrix() {
        int[][] dis = new int[53][53];
        for (int i = 0; i < 53; i++) {
            for (int j = 0; j < 53; j++) {
                Province tis = game.getProvinceByName(provinces[i]);
                Province tat = game.getProvinceByName(provinces[j]);
                dis[i][j] = 0;
                if (tis.getSuperAdjacentProvinces().contains(tat))
                    dis[i][j] = 1;
            }
        }
        return dis;
    }

    /**
     * return -1 if not friendly path
     * return teh number of distances in between otherwise
     * @param src the name of source province
     * @param des the name of the destination province
     * @return see above
     */
    public int bfs(String src, String des){
        int source = ProvinceToInt(src);
        int dest = ProvinceToInt(des);
        int[] visited = new int[adjMatrix.length];
        for(int i =0;i<adjMatrix.length;i++)   visited[i]=-1;
        visited[source] = source;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        while(!queue.isEmpty()){
            int x = queue.poll();
            int i;
            for(i=0; i<adjMatrix.length;i++){
                if(adjMatrix[x][i] == 1 && visited[i] == -1 &&(!game.isEnemy(provinces[i],provinces[x])||i==dest)){
                    queue.add(i);
                    visited[i] = x;
                }
            }
        }
        int np =0;
        int index = dest;
        if(visited[dest]==-1)   return -1;
        while(visited[index]!=source){
            index = visited[index];
            np++;
        }
        return np+1;
    }

}
