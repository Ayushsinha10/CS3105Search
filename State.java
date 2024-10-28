import java.util.*;

public class State implements Comparable<State> {
    int row, col, pathCost, heuristic, heuristic2;
  

    public State(int row, int col, int pathCost, int heuristic, int heuristic2) {
        this.row = row;
        this.col = col;
        this.pathCost = pathCost;
        this.heuristic = heuristic;
        this.heuristic2 = heuristic2;
        
    }

    @Override
    public int compareTo(State other) {
        return Integer.compare(this.pathCost + this.heuristic, other.pathCost + other.heuristic);
    }

    public int getRow() {
        return this.row;
    }
    public int getCol(){
        return this.col;
    }
    public int getPathCost(){
        return this.pathCost;
    }
    public int getHeuristic(){
        return this.heuristic;
    }
    public int getHeuristic2(){
        return this.heuristic2;
    }
    public String prints(){
       return "("+this.getRow() + "," + this.getCol()+"):"+this.getHeuristic();
    }
    public String prints2(){
        return "("+this.getRow() + "," + this.getCol()+"):"+this.getHeuristic2();
     }
    public String pprints(){
        int fx = this.getPathCost() + this.getHeuristic();
        String ffx = Integer.toString(fx);
        return "("+this.getRow() + "," + this.getCol()+"):"+ffx;
     }
     public String pprints2(){
        int fx = this.getPathCost() + this.getHeuristic2();
        String ffx = Integer.toString(fx);
        return "("+this.getRow() + "," + this.getCol()+"):"+ffx;
     }
     public String ppprints(){
        
        String ffx = Integer.toString(this.getPathCost());
        return "("+this.getRow() + "," + this.getCol()+"):"+ffx;
     }
}

