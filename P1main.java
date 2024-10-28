import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/********************Starter Code
 * 
 * This class contains some examples of required inputs and outputs
 * 
 * @author alice toniolo
 *
 * we assume parameters are valid, no need to check
 * 
 */

 public class P1main {

	public static final int NOT_IMPLEMENTED=-200;
	public static final int NOT_FOUND=-1;
	public static final int NOT_TERMINATED=-100;

	public static void main(String[] args) {

		if(args.length<4) {
			System.out.println("usage: java P1main <AStar|BestF|Alt|AStarOpt|BestFOpt> <D> <r,c> <coverage> [<verbose>]");
			System.exit(1);
		}

		//process input examples 
		boolean verbose=false;
		if(args[args.length-1].equals("verbose")) {
			verbose=true;
		}
		String algo=args[0];
		int d=Integer.parseInt(args[1]);
		
		String start=args[2];
		int perc = Integer.parseInt(args[3]);
		int coverage=Math.round(d*d*perc/100);
	
		int time_limit=30; //run at most for 30s
		//run your search algorithm p
		int path_cost=runSearch(algo, d, start, coverage, verbose, time_limit);


		/*
		 * The system must print the following information from your search methods
		 * All code below is for demonstration purposes only
		 */

		//Example: java P1main BestF 4 0,0 35




		System.out.println(path_cost);

	}
	private static int calculateHeuristic(int d, int coverage, int visitedCells) {
		return 3 * (coverage - visitedCells);
	}
	public static int calculateHeuristic2(int d, int coverage, int visitedCells) {
        int remainingCoverage = coverage - visitedCells;
        if (remainingCoverage <= 0) {
            return 0; 
        }
        

        int estimatedMovesToCoverage = estimateMovesToCoverage(remainingCoverage);
        return remainingCoverage + estimatedMovesToCoverage;
    }
    

    private static int estimateMovesToCoverage(int remainingCoverage) {

        
        int horizontalMoves = remainingCoverage / 3; 
        int diagonalMoves = (remainingCoverage % 3) > 0 ? 1 : 0; 
        

        return horizontalMoves * 3 + diagonalMoves * 4;
    }
	private static List<State> getValidMoves(State current, int d, int coverageRequired, int cellsVisited) {
		List<State> moves = new ArrayList<>();
		
		// Possible moves: horizontal/vertical (3 squares), diagonal (2 squares)
		int[][] possibleMoves = {
			{3, 0},  
			{-3, 0},
			{0, 3},  
			{0, -3}, 
			{2, 2},  
			{2, -2},
			{-2, 2}, 
			{-2, -2}, 
			
		};
	
		// Current position of the pawn
		int currentRow = current.getRow();
		int currentCol = current.getCol();
	
		// Loop through each possible move
		for (int[] move : possibleMoves) {
			int newRow = currentRow + move[0];
			int newCol = currentCol + move[1];
			
			// Check if the new position is within bounds and add the cost associated.
			if (newRow >= 0 && newRow < d && newCol >= 0 && newCol < d) {
			    if(move[0] != 0 && move[1] != 0){
					int additionalCost = 4;
					int newPathCost = additionalCost;
					State newState = new State(newRow, newCol, newPathCost, calculateHeuristic(d, coverageRequired, cellsVisited+1), calculateHeuristic2(d, coverageRequired, cellsVisited+1));
				    moves.add(newState);
				}
				else {
					int additionalCost = 3;
					int newPathCost = additionalCost;
					State newState = new State(newRow, newCol, newPathCost, calculateHeuristic(d, coverageRequired, cellsVisited+1), calculateHeuristic2(d, coverageRequired, cellsVisited+1));
				    moves.add(newState);
				}
				
			}
		}
		
		return moves;
	}
	private static int bestFirst(int d, String start, int coverageRequired, boolean verbose) {
        // Parse the starting position
		long startTime = System.currentTimeMillis();
        String[] startCoords = start.split(",");
        int startRow = Integer.parseInt(startCoords[0]);
        int startCol = Integer.parseInt(startCoords[1]);

        // Initial state with path cost 0 and initial heuristic
        int initialHeuristic = calculateHeuristic(d, coverageRequired, 1);
        State initialState = new State(startRow, startCol, 0, initialHeuristic, calculateHeuristic(d, coverageRequired, 1));
        
        // Priority queue for Best-First Search (priority by heuristic)
        PriorityQueue<State> frontier = new PriorityQueue<>(Comparator.comparingInt(State::getHeuristic));
		Queue<String> out = new LinkedList<>();
        Set<String> explored = new HashSet<>();

		

        // Add the initial state to the frontier
        frontier.add(initialState);

        int nodesExplored = 0;
		int total = 0;
		State previousState = null;

        while (!frontier.isEmpty()) {
			long elapsedTime = System.currentTimeMillis() - startTime;
			if (elapsedTime > 30 * 1000) {  // 30 seconds
				
				return NOT_TERMINATED;
			}
			
			if(verbose){
				List<State> sortedFrontier = new ArrayList<>(frontier);
                sortedFrontier.sort(Comparator.comparingInt(State::getHeuristic));
				System.out.print("[");
				for (int i = 0; i < sortedFrontier.size(); i++) {
					State stte = sortedFrontier.get(i);
					System.out.print(stte.prints());
					if (i < sortedFrontier.size() - 1) {
						System.out.print(", ");
					}
				}
				System.out.println("]");
		}
            
			
            State current = frontier.poll();
			while (explored.contains("(" + current.getRow() + "," + current.getCol() + ")") && !frontier.isEmpty()) {
				
				continue;
			}
			if (previousState != null && !isValidMove(previousState, current)) {
			    
			        continue; // If not a valid move, skip processing this state
			}
			nodesExplored++;
			total = total +  current.getPathCost();

			String currKey = "("+current.getRow() + "," + current.getCol()+")";


            // Check if coverage requirement is met
            if (nodesExplored >= coverageRequired) {
				if(verbose){
				System.out.println(nodesExplored);
				out.add(currKey);
				Iterator<String> it = out.iterator();
				
				while(it.hasNext()){
					System.out.print(it.next());
				}
				System.out.println();
	
                System.out.println(nodesExplored);
            
			    } 
                return total;
            }
		

            // Generate valid moves from the current state
            List<State> moves = getValidMoves(current, d, coverageRequired, nodesExplored);

            for (State move : moves) {
                String moveKey = "("+move.getRow() + "," + move.getCol()+")";
				//System.out.print("---moves---");
			   // System.out.println(moveKey+":"+move.getHeuristic());
				
				

                // Only add the move if it hasn't been explored
                if (!explored.contains(moveKey)) {
					//System.out.print("---moves---");
					//System.out.println(moveKey+":"+move.getHeuristic());
				//	storedStates.add(moveKey+":"+move.getHeuristic());
                    frontier.add(move);
                    explored.add(currKey);
                }

            }
			out.add(currKey);
			previousState = current;
			
	
        }

        // If the coverage requirement is not met and the frontier is empty, return NOT_FOUND
        return NOT_FOUND;
    }
	private static int bestFirstopt(int d, String start, int coverageRequired, boolean verbose) {
        // Parse the starting position
		long startTime = System.currentTimeMillis();
        String[] startCoords = start.split(",");
        int startRow = Integer.parseInt(startCoords[0]);
        int startCol = Integer.parseInt(startCoords[1]);

        // Initial state with path cost 0 and initial heuristic
        int initialHeuristic = calculateHeuristic(d, coverageRequired, 1);
        State initialState = new State(startRow, startCol, 0, initialHeuristic, calculateHeuristic(d, coverageRequired, 1));
        
        // Priority queue for Best-First Search (priority by heuristic2)
        PriorityQueue<State> frontier = new PriorityQueue<>(Comparator.comparingInt(State::getHeuristic2));
		Queue<String> out = new LinkedList<>();
        Set<String> explored = new HashSet<>();

        frontier.add(initialState);

        int nodesExplored = 0;
		int total = 0;
		State previousState = null;

        while (!frontier.isEmpty()) {
			long elapsedTime = System.currentTimeMillis() - startTime;
			if (elapsedTime > 30 * 1000) {  // 30 seconds
				
				return NOT_TERMINATED;
			}
			
			if(verbose){
				List<State> sortedFrontier = new ArrayList<>(frontier);
                sortedFrontier.sort(Comparator.comparingInt(State::getHeuristic2));
				System.out.print("[");
				for (int i = 0; i < sortedFrontier.size(); i++) {
					State stte = sortedFrontier.get(i);
					System.out.print(stte.prints2());
					if (i < sortedFrontier.size() - 1) {
						System.out.print(", ");
					}
				}
				System.out.println("]");
		}

			
            State current = frontier.poll();
		
			while (explored.contains("(" + current.getRow() + "," + current.getCol() + ")") && !frontier.isEmpty()) {
			
				continue;
			}
			if (previousState != null && !isValidMove(previousState, current)) { 
			
				    continue; // If not a valid move, skip processing this state
			}
			nodesExplored++;
			total = total +  current.getPathCost();

			String currKey = "("+current.getRow() + "," + current.getCol()+")";



            // Check if coverage requirement is met
            if (nodesExplored >= coverageRequired) {
				if(verbose){
				System.out.println(nodesExplored);
				out.add(currKey);
				Iterator<String> it = out.iterator();
				
				while(it.hasNext()){
					System.out.print(it.next());
				}
				System.out.println();
	
                System.out.println(nodesExplored);
            
			    } 
                return total;
            }
		

            // Generate valid moves from the current state
            List<State> moves = getValidMoves(current, d, coverageRequired, nodesExplored);

            for (State move : moves) {
                String moveKey = "("+move.getRow() + "," + move.getCol()+")";
				//System.out.print("---moves---");
			   // System.out.println(moveKey+":"+move.getHeuristic());
			  
				

                // Only add the move if it hasn't been explored
                if (!explored.contains(moveKey)) {
					//System.out.print("---moves---");
					//System.out.println(moveKey+":"+move.getHeuristic());
				//	storedStates.add(moveKey+":"+move.getHeuristic());
                    frontier.add(move);
                    explored.add(currKey);
                }

            }
			out.add(currKey);
			previousState = current;
			
	
        }

        // If the coverage requirement is not met and the frontier is empty, return NOT_FOUND
        return NOT_FOUND;
    }
	private static int uniSearch(int d, String start, int coverageRequired, boolean verbose) {
        // Parse the starting position
		long startTime = System.currentTimeMillis();
        String[] startCoords = start.split(",");
        int startRow = Integer.parseInt(startCoords[0]);
        int startCol = Integer.parseInt(startCoords[1]);

        // Initial state with path cost 0 and initial heuristic
        int initialHeuristic = calculateHeuristic(d, coverageRequired, 1);
        State initialState = new State(startRow, startCol, 0, initialHeuristic, calculateHeuristic2(d, coverageRequired, 1));
        
        // Priority queue for Best-First Search (priority by PathCost}
        PriorityQueue<State> frontier = new PriorityQueue<>(Comparator.comparingInt(State::getPathCost));
		Queue<String> out = new LinkedList<>();
        Set<String> explored = new HashSet<>();

        frontier.add(initialState);

        int nodesExplored = 0;
		int total = 0;
		State previousState = null;

        while (!frontier.isEmpty()) {
			long elapsedTime = System.currentTimeMillis() - startTime;
			if (elapsedTime > 30 * 1000) {  // 30 seconds 
				
				return NOT_TERMINATED;
			}
			
			if(verbose){
				List<State> sortedFrontier = new ArrayList<>(frontier);
                sortedFrontier.sort(Comparator.comparingInt(State::getPathCost));
				System.out.print("[");
				for (int i = 0; i < sortedFrontier.size(); i++) {
					State stte = sortedFrontier.get(i);
					System.out.print(stte.ppprints());
					if (i < sortedFrontier.size() - 1) {
						System.out.print(", ");
					}
				}
				System.out.println("]");
		}
	
            State current = frontier.poll();
			while (explored.contains("(" + current.getRow() + "," + current.getCol() + ")") && !frontier.isEmpty()) {
				    continue;
			}
			if (previousState != null && !isValidMove(previousState, current)) {
			        continue; // If not a valid move, skip processing this state
			}
			nodesExplored++;
	
			
			total = total +  current.getPathCost();

			String currKey = "("+current.getRow() + "," + current.getCol()+")";

           

            // Check if coverage requirement is met
            if (nodesExplored >= coverageRequired) {
				if(verbose){
				System.out.println(nodesExplored);
				out.add(currKey);
				Iterator<String> it = out.iterator();
				
				while(it.hasNext()){
					System.out.print(it.next());
				}
				System.out.println();
	
                System.out.println(nodesExplored);
            
			    } 
                return total;
            }
		

            // Generate valid moves from the current state
            List<State> moves = getValidMoves(current, d, coverageRequired, nodesExplored);

            for (State move : moves) {
                String moveKey = "("+move.getRow() + "," + move.getCol()+")";
				//System.out.print("---moves---");
			   // System.out.println(moveKey+":"+move.getHeuristic());
				
				

                // Only add the move if it hasn't been explored
                if (!explored.contains(moveKey)) {
					//System.out.print("---moves---");
					//System.out.println(moveKey+":"+move.getHeuristic());
				//	storedStates.add(moveKey+":"+move.getHeuristic());
                    frontier.add(move);
                    explored.add(currKey);
                }

            }
			previousState = current;
			out.add(currKey);
			
	
        }

        // If the coverage requirement is not met and the frontier is empty, return NOT_FOUND
        return NOT_FOUND;
    }
	private static int aStar(int d, String start, int coverageRequired, boolean verbose) {
        // Parse the starting position
		long startTime = System.currentTimeMillis();
        String[] startCoords = start.split(",");
        int startRow = Integer.parseInt(startCoords[0]);
        int startCol = Integer.parseInt(startCoords[1]);

        // Initial state with path cost 0 and initial heuristic
        int initialHeuristic = calculateHeuristic(d, coverageRequired, 1);
        State initialState = new State(startRow, startCol, 0, initialHeuristic, calculateHeuristic2(d, coverageRequired, 1));
        
        // Priority queue for Best-First Search (priority by heuristic+pathcost)
        PriorityQueue<State> frontier = new PriorityQueue<>(Comparator.comparingInt(state -> state.getPathCost() + state.getHeuristic()));
		Queue<String> out = new LinkedList<>();
        Set<String> explored = new HashSet<>();


        // Add the initial state to the frontier
        frontier.add(initialState);

        int nodesExplored = 0;
		int total = 0;
		State previousState = null;

        while (!frontier.isEmpty()) {
			long elapsedTime = System.currentTimeMillis() - startTime;
			if (elapsedTime > 30 * 1000) {  // 30 seconds 
				
				return NOT_TERMINATED;
			}
			
			
			if(verbose){
				List<State> sortedFrontier = new ArrayList<>(frontier);
                sortedFrontier.sort(Comparator.comparingInt(state -> state.getPathCost() + state.getHeuristic()));
				System.out.print("[");
				for (int i = 0; i < sortedFrontier.size(); i++) {
					State stte = sortedFrontier.get(i);
					System.out.print(stte.pprints());
					if (i < sortedFrontier.size() - 1) {
						System.out.print(", ");
					}
				}
				System.out.println("]");
		}

			
            State current = frontier.poll();
			
			while (explored.contains("(" + current.getRow() + "," + current.getCol() + ")") && !frontier.isEmpty()) {
			
				continue;
			}
			if (previousState != null && !isValidMove(previousState, current)) {
			      
			        continue; // If not a valid move, skip processing this state
			}
			nodesExplored++;
			total = total +  current.getPathCost();

			String currKey = "("+current.getRow() + "," + current.getCol()+")";

			

    
            // Check if coverage requirement is met
            if (nodesExplored >= coverageRequired) {
				if(verbose){
				System.out.println(nodesExplored);
				out.add(currKey);
				Iterator<String> it = out.iterator();
				
				while(it.hasNext()){
					System.out.print(it.next());
				}
				System.out.println();
	
                System.out.println(nodesExplored);
            
			    } 
                return total;
            }
		
			

            // Generate valid moves from the current state
            List<State> moves = getValidMoves(current, d, coverageRequired, nodesExplored);

            for (State move : moves) {
                String moveKey = "("+move.getRow() + "," + move.getCol()+")";
				//System.out.print("---moves---");
			   // System.out.println(moveKey+":"+move.getHeuristic());
				
				

                // Only add the move if it hasn't been explored
                if (!explored.contains(moveKey)) {
					//System.out.print("---moves---");
					//System.out.println(moveKey+":"+move.getHeuristic());
					//storedStates.add(moveKey+":"+move.getHeuristic());
                    frontier.add(move);
                    explored.add(currKey);
					
                }

            }
			
			previousState = current;
			out.add(currKey);
			
			
	
        }

        // If the coverage requirement is not met and the frontier is empty, return NOT_FOUND
        return NOT_FOUND;
    }
	private static int aStarOpt(int d, String start, int coverageRequired, boolean verbose) {
        // Parse the starting position
		long startTime = System.currentTimeMillis();
        String[] startCoords = start.split(",");
        int startRow = Integer.parseInt(startCoords[0]);
        int startCol = Integer.parseInt(startCoords[1]);

        // Initial state with path cost 0 and initial heuristic
        int initialHeuristic = calculateHeuristic(d, coverageRequired, 1);
        State initialState = new State(startRow, startCol, 0, initialHeuristic, calculateHeuristic2(d, coverageRequired, 1));
        
        // Priority queue for Best-First Search (priority by heuristic2+pathcost)
        PriorityQueue<State> frontier = new PriorityQueue<>(Comparator.comparingInt(state -> state.getPathCost() + state.getHeuristic2()));
		Queue<String> out = new LinkedList<>();
        Set<String> explored = new HashSet<>();


        // Add the initial state to the frontier
        frontier.add(initialState);

        int nodesExplored = 0;
		int total = 0;
		State previousState = null;

        while (!frontier.isEmpty()) {
			long elapsedTime = System.currentTimeMillis() - startTime;
			if (elapsedTime > 30 * 1000) {  // 30 seconds 
				return NOT_TERMINATED;
			}
			
			
			if(verbose){
				List<State> sortedFrontier = new ArrayList<>(frontier);
                sortedFrontier.sort(Comparator.comparingInt((state -> state.getPathCost() + state.getHeuristic2())));
				System.out.print("[");
				for (int i = 0; i < sortedFrontier.size(); i++) {
					State stte = sortedFrontier.get(i);
					System.out.print(stte.pprints2());
					if (i < sortedFrontier.size() - 1) {
						System.out.print(", ");
					}
				}
				System.out.println("]");
		}

		
            State current = frontier.poll();
			while (explored.contains("(" + current.getRow() + "," + current.getCol() + ")") && !frontier.isEmpty()) {
	
				continue;
			}
			if (previousState != null && !isValidMove(previousState, current)) {
			 
			        continue; // If not a valid move, skip processing this state
			}
			nodesExplored++;
			total = total +  current.getPathCost();
	
			String currKey = "("+current.getRow() + "," + current.getCol()+")";

			
            // Check if coverage requirement is met
            if (nodesExplored >= coverageRequired) {
				if(verbose){
				System.out.println(nodesExplored);
				out.add(currKey);
				Iterator<String> it = out.iterator();
				
				while(it.hasNext()){
					System.out.print(it.next());
				}
				System.out.println();
	
                System.out.println(nodesExplored);
            
			    } 
                return total;
            }
		
			

            // Generate valid moves from the current state
            List<State> moves = getValidMoves(current, d, coverageRequired, nodesExplored);

            for (State move : moves) {
                String moveKey = "("+move.getRow() + "," + move.getCol()+")";
				//System.out.print("---moves---");
			   // System.out.println(moveKey+":"+move.getHeuristic());
				
				

                // Only add the move if it hasn't been explored
                if (!explored.contains(moveKey)) {
					//System.out.print("---moves---");
					//System.out.println(moveKey+":"+move.getHeuristic());
					//storedStates.add(moveKey+":"+move.getHeuristic());
                    frontier.add(move);
                    explored.add(currKey);
					
                }

            }
			out.add(currKey);
			previousState = current;
			
			
	
        }

        // If the coverage requirement is not met and the frontier is empty, return NOT_FOUND
        return NOT_FOUND;
    }
    
	private static boolean isValidMove(State previous, State current) {
		int rowDiff = Math.abs(current.getRow() - previous.getRow());
		int colDiff = Math.abs(current.getCol() - previous.getCol());
	
		// Check if the move is valid based on possible moves
		if ((rowDiff == 3 && colDiff == 0) ||  
			(rowDiff == 0 && colDiff == 3) ||  
			(rowDiff == 2 && colDiff == 2)) { 
			return true;
		}
		return false;
	}


	private static int runSearch(String algo, int d, String start, int coverage, boolean verbose, int time_limit) {

		switch(algo) {

		case "BestF": //run BestF
			return bestFirst(d, start, coverage, verbose);
		case "AStar": //run AStar
			return aStar(d, start, coverage, verbose);
		case "BestFOpt": //run BestF with additional heuristic
			return bestFirstopt(d, start, coverage, verbose);
		case "AStarOpt": //run AStar with additional heuristic
			return aStarOpt(d, start, coverage, verbose);
		case "Alt":
		    return uniSearch(d, start, coverage, verbose);

		}
		return NOT_IMPLEMENTED;

	}




}