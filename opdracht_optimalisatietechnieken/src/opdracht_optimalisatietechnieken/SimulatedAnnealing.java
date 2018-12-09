package opdracht_optimalisatietechnieken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SimulatedAnnealing {
	
	Solution initialSolution;

	public SimulatedAnnealing(Solution solution) {
		this.initialSolution=solution;
	}
	
	  public static double acceptanceProbability(int energy, int newEnergy, double temperature) {
	        // als de nieuwe solution beter is --> accepteren
	        if (newEnergy < energy) {
	            return 1.0;
	        }
	        // als de nieuwe solution slechter is --> accepteren met een probabiliteit
	        return Math.exp((energy - newEnergy) / temperature);
	  }
	  
	  
	  public void startSimulatedAnnealing() {
	  // initiele temp
      double temp = 10000;

      double coolingRate = 0.003;

      Solution currentSolution = new Solution(initialSolution);
      
      System.out.println("Initial solution distance: " + currentSolution.getTotalDistance());

      Solution bestSolution=new Solution(currentSolution);
      
      while (temp > 1) {
          Solution newSolution = new Solution(currentSolution);

          // swappen
          Random random=new Random();
          int neighbourmethode = random.nextInt(2);
          switch(1){
              case 0: //CD verplaatsen
                  newSolution = new Solution (moveDropCollect(newSolution));
                  break;
                  
              case 1: //C1D1 en C2D2 wisselen
                  newSolution = new Solution (swapDropCollect(newSolution));
                  break;
                  
                  
              default: System.out.println("Geen neighbourmethode gevonden"); break;
          }
      
          newSolution.updateTrucksDistancesAndTimes();
          
          if(newSolution.isFeasible()){
              // bereken enrgie van de solutions
              int currentEnergy = currentSolution.getTotalDistance();
              int neighbourEnergy = newSolution.getTotalDistance();

              // beslissen of we niewe oplossing gaan accepteren
              if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) > Math.random()) {
                  currentSolution = new Solution(newSolution);
              }

              // beste solution bijhouden
              if (currentSolution.getTotalDistance() < bestSolution.getTotalDistance()) {
            	  bestSolution = new Solution(currentSolution);
            	  System.out.println("best solution distance: " + bestSolution.getTotalDistance());
            	  try {
					bestSolution.writeOuput("best.txt");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
              }
              
              // Cool system
              temp =temp-1;
          }
          else{
        	  //System.out.println("niet feasible");
          }

      }

      System.out.println("Final solution distance: " + bestSolution.getTotalDistance());
  }
	  
	  
	  
	  
	  
	  
	  
	  // ------------------- permutatie methodes -------------------
	  
	  private Solution swapDropCollect(Solution sol) {
		  Random random=new Random();
	    	//System.out.println("start neighbour searching");
	        	Solution solution=null;
	        	solution=new Solution(sol);
	        	Action collect1 = null;
	        	Action drop1=null;
	        	Action collect2 = null;
	        	Action drop2=null;
	        	
	        	int collect1Index = 0;
	        	int drop1Index=0;
	        	int collect2Index=0;
	        	int drop2Index=0;
	        	
	        	List<Truck> trucks = new ArrayList(solution.getRoutes().keySet());
	        	int aantalTrucks=trucks.size();
	        	
	        	//2 verschillende randomtrucks nemen
	        	Truck randomTruck1=trucks.get(random.nextInt(aantalTrucks));
	        	while(solution.getRoutes().get(randomTruck1).size()<=2){
	        		randomTruck1=trucks.get(random.nextInt(aantalTrucks));
	        	}
	        	Truck randomTruck2=trucks.get(random.nextInt(aantalTrucks));
	        	while(randomTruck1.getId()==randomTruck2.getId()||solution.getRoutes().get(randomTruck2).size()<=2){
	        		randomTruck2=trucks.get(random.nextInt(aantalTrucks));
	        	}
	        	
	        	List<Action>lijst1=solution.getRoutes().get(randomTruck1);
	        	List<Action>lijst2=solution.getRoutes().get(randomTruck2);

	        	
	        	//zoek  drop in randomtruck1
	            while(true){
	            	drop1Index = random.nextInt(lijst1.size());
	                Action action=lijst1.get(drop1Index);
	                if(action.getType()==false ){
	                	drop1=action;
	                	break;
	                }
	                	
	            }

	        	//zoek bijhorende collection in randomtruck1
	            while(true){
	                collect1Index = random.nextInt(lijst1.size());
	                Action action=lijst1.get(collect1Index);
	                if(action.getType()==true && action.getMachine().getId()==drop1.getMachine().getId()){
	                	collect1=action;
	                	break;
	                }
	                	
	            }
	        	//zoek drop in randomtruck2
	            while(true){
	            	drop2Index = random.nextInt(lijst2.size());
	                Action action=lijst2.get(drop2Index);
	                if(action.getType()==false){
	                	drop2=action;
	                	break;
	                }
	                	
	            }

	            
	        	//zoek bijhorende collection in randomTruck2
	            while(true){
	            	collect2Index = random.nextInt(lijst2.size());
	                Action action=	lijst2.get(collect2Index);
	                if(action.getType()==true && action.getMachine().getId()==drop2.getMachine().getId()){
	                	collect2=action;
	                	break;
	                }
	                	
	            }
	            
	            solution.getRoutes().get(randomTruck1).remove(collect1);
	            solution.getRoutes().get(randomTruck1).remove(drop1);
	            solution.getRoutes().get(randomTruck2).remove(collect2);
	            solution.getRoutes().get(randomTruck2).remove(drop2);

	            int newDrop1Index=1+random.nextInt(solution.getRoutes().get(randomTruck1).size());
	            int newCollect1Index=random.nextInt(newDrop1Index);
	            int newDrop2Index=1+random.nextInt(solution.getRoutes().get(randomTruck2).size());
	            int newCollect2Index=random.nextInt(newDrop2Index);
	            
	            solution.getRoutes().get(randomTruck1).add(newCollect1Index,collect2);
	            solution.getRoutes().get(randomTruck1).add(newDrop1Index,drop2);
	            solution.getRoutes().get(randomTruck2).add(newCollect2Index,collect1);
	            solution.getRoutes().get(randomTruck2).add(newDrop2Index,drop1);
	            
	            
	            return solution;
	            
	   
	        }
	        
	  
	  
	  
	    private Solution moveDropCollect(Solution sol) {
	    	Random random=new Random();
	        //temp solution is kopie waarin de verwerkingen gebeuren van de acties
	        Solution solution = new Solution(sol);
	        int aantalRoutes = solution.routes.size();
	        // from is de route waar de acties uit worden verwijderd
	        // to is de route waar de acties aan toegevoegd worden
	        List<Action> routesFrom = new ArrayList<>();
	        List<Action> routesTo = null;

	        Truck fromTruck = null;
	        Truck toTruck = null;

	        //starten van een "niet-lege" route
	        while(routesFrom.isEmpty()) {
	            List<Map.Entry<Truck,List<Action>>> tempset = new ArrayList<>(solution.routes.entrySet());
	            Collections.shuffle(tempset);

	            routesFrom = tempset.get(0).getValue();
	            routesTo = tempset.get(1).getValue();

	            fromTruck = tempset.get(0).getKey();
	            toTruck = tempset.get(1).getKey();
	        }


	        int index = random.nextInt(routesFrom.size());

	        Action collect = null, drop = null;
	        Action temp = routesFrom.remove(index);

	        if(temp.getType()) {
	            collect = temp;
	        }
	        else {
	            drop = temp;
	        }

	        // find associated collect or drop
	        for(int i = 0; i<routesFrom.size(); i++){
	            if(routesFrom.get(i).getMachine().equals(temp.getMachine())){
	                if(temp.getType())
	                    drop = routesFrom.remove(i);
	                else collect = routesFrom.remove(i);
	                break;
	            }
	        }


	        if(routesTo.size() !=0) {
	            routesTo.add(random.nextInt(routesTo.size()), collect);
	            int indexOfCollect = routesTo.indexOf(collect);
	            int indexOfDrop = -1;
	            while (indexOfDrop <= indexOfCollect) indexOfDrop = random.nextInt(routesTo.size());
	            routesTo.add(indexOfDrop, drop);
	        } else {
	            routesTo.add(collect);
	            routesTo.add(drop);
	        }


	        return solution;
	    }
	    

}
