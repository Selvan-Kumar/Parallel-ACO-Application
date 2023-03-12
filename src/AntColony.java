import java.util.*;

/*
 * AntColony.java - a class that simulates Ant Colony Optimisation on TSP.
 * @author Selvan Kumar
 * @version 1.0
 * 
 * Class adapted from: Jade J. (2018), "AntColOpt", github, [online at https://github.com/jkbestami/AntColOpt/blob/master/Colony.java, accessed 3 Feb 2020].
 */

public class AntColony implements Runnable{
	
	Random random = new Random();

	int itr = 0;
	int numOfCities;
	int numberOfAnts;
	int iterations;
	
	double alpha; 
	double beta; 
	double evapRate; 
	double[][] pheromone;
	
	TSP tsp;
	Ant OverallOptimalAnt;
	
	List<List<Integer>> tours;
	List<Ant> Ants;
	
	/*
	 * Constructor Method
	 * @param alpha A variable of type double 
	 * @param beta A variable of type double 
	 * @param evapRate A variable of type double
	 * @param numOfAnts A variable of type Integer
	 * @param tsp A variable of TSP data type
	 * @param iterations A variable of Integer data type
	 */
	public AntColony(double alpha, double beta, double evapRate, int numberOfAnts,TSP tsp, int iterations){

		this.iterations = iterations;
		this.alpha = alpha;
		this.beta = beta;
		this.evapRate = evapRate;
		this.numberOfAnts = numberOfAnts;
		this.tsp = tsp;
		this.numOfCities = tsp.getnumCities();
		
		//update pheromone array
		pheromone = new double[numOfCities][numOfCities];
		
		for(int i = 0; i<numOfCities ; i++){
			for(int j = 0; j<numOfCities; j++){
				pheromone[i][j] =numberOfAnts/(300000.0);
			}
		}
		
		Ants = new ArrayList<Ant>();
	}

	 /*
	  * Method to simulate pheromone evaporation.
	  */
	public void evaporate(){
		//loop through pheromone array and multiply each value by 1-evaporation rate.
		for(int i = 0; i<numOfCities ; i++){
			for(int j = 0; j<numOfCities ; j++){
				pheromone[i][j] = pheromone[i][j]*(1.0-evapRate);
			}
		}
	}

	/*
	 * Method to obtain the best ant from Ants array.
	 */
	public Ant optimalAnt(){
		double min = Double.MAX_VALUE;
		int minIndex = -1;

		//order ants based on best tour length 
		for(int i = 0; i<Ants.size(); i++){
			if(Ants.get(i).tourLength < min){
				min = (int)Ants.get(i).tourLength;
				minIndex = i;
			}
		}
		
		//return ant with best tour length
		return Ants.get(minIndex);
    }

	/* 
	 * Method to simulate ants depositing pheromone on trail.
	 * @param ant A variable of Ant data type.
	 */
	public void deposit(Ant ant){
		//from equation
		double Ak = evapRate*(1/ant.tourLength); 
		
		//loop and update pheromone array.
		for(int i = 0;i<ant.tour.size() -1; i++){
			int r = ant.tour.get(i);
			int s = ant.tour.get(i+1);
			pheromone[r][s] = pheromone[r][s] + Ak;
			pheromone[s][r] = pheromone[s][r] + Ak;
		}
	}

	/*
	 * Method to start iterations.
	 */
	public void run() {
        for(int i = 0; i<iterations ; i++){
            iterate();
        }
    }
	
	/*
	 * Method that simulates 1 iteration, construct ants->launch sequential run->update pheromone. 
	 */
	public void iterate(){
		List<Thread> threads = new ArrayList<Thread>();
		Ants = new ArrayList<Ant>();

		//construct and run ants using threading
		for(int k = 0; k<numberOfAnts; k++){
			Ant anny = new Ant();
			Ants.add(anny);
			Thread t = new Thread(anny);
			threads.add(t);
			t.start();
		}

		try{

			for (Thread thread : threads) {
				thread.join();
			}
			
			//local best ant in colony
			Ant localOptimalAnt = optimalAnt();

			//tour completion
			if(itr>0){
				if(localOptimalAnt.tourLength < OverallOptimalAnt.tourLength){
					OverallOptimalAnt = localOptimalAnt;
				}
			}else{
				OverallOptimalAnt = localOptimalAnt;
			}

			//deposit pheromone on trail
			for(Ant ant: Ants){
				deposit(ant);
			}

			//simulate pheromone evaporation
			evaporate();
			
			//update iteration
			itr++ ;

		}catch(Exception e){
			System.err.println("thread problem");
		}
	}

	/*
	 * A class to simulate an Ant.
	 */
	public class Ant implements Runnable{

		int startCity;
		int currentCity;
		
		double tourLength;
		
		List<Integer> visitedCities;    
		List<Integer> tour;      
		List<Integer> neighborCities;
		
	/*
	 * Constructor method
	 */
    public Ant(){

		startCity = random.nextInt(numOfCities); 
		tourLength= 0;
		
		visitedCities = new ArrayList<Integer>();
		visitedCities.add(startCity);
		
		//add starting city to start of tour
		tour = new ArrayList<Integer>();
		tour.add(startCity);
		
		currentCity=startCity;
    }

    /*
     * Method to update current city where ant is at.
     */
    public void updateCurrCity(){
    	currentCity = tour.get(tour.size()-1);
    }

    /*
     * Method to update denominator(used in formula) which is sum of all the trailIntensity * visibility of adjacent cities.
     * @return A double data type
     */
    public double getDenominator(){
		double trailIntensity = 0.0;
		double visibility = 0.0;
		double result = 0.0;
		
		for(int i: neighborCities){
			//equation calculations
			trailIntensity = Math.pow(pheromone[currentCity][i],alpha);
			visibility = Math.pow(1/tsp.getDist(currentCity, i),beta);
			result = result + visibility * trailIntensity;
		}
		
		return result;
    }
    
    /*
     * Method to calculate probability of ant going to the city given as parameter.
     * @param neighbor A variable of Integer type
     * @return A double data type
     */
    public double calcP(int neighbor){
    	//equation probability = (trailIntensity * visibility of city)/denominator
    	double trailIntensity = Math.pow(pheromone[currentCity][neighbor],alpha);
		double visibility = Math.pow(1/tsp.getDist(currentCity,neighbor),beta);
		double denominator = getDenominator();
		
		return (trailIntensity*visibility)/denominator;
    }

    /*
     * Method to decide next move by checking available cities; if not backtrack. 
     * @return A Integer data type
     */
    public int move(){

		List<Double> possibleCityList = new ArrayList<Double>();

		//no neighboring cities
		if(neighborCities.isEmpty()){
			System.out.print("EMPTY NEIGHBORS");
			return -1;
		}
		
		for(int n : neighborCities){
			double possibleCity = calcP(n);
			possibleCityList.add(possibleCity);
		}

		double sum = 0;
		for(double pc : possibleCityList){
			sum = sum + pc;
		}

		//In case value gets too small
		if(sum==0.0){
			return random.nextInt(possibleCityList.size());
		}

		double  r = random.nextDouble();

		sum = 0;
		int index =  0;
		
		for(double p : possibleCityList){
			sum = sum + p;
			
			//cumulative probability
			if(r<=sum){    
				return index;
			}else{
				index++;
			}
		}
		
		return index;
    }

    /*
     * Method to update on unvisited neighboring cities.
     */
    public void unvisitedNeighbors(){

      List<Integer> resultCities = new ArrayList<Integer>();
      
      for(int i=0; i<numOfCities; i++){
			resultCities.add(i);
      }
      
      for(Integer visitedCity: visitedCities){
		resultCities.remove(visitedCity);
      }
      
      neighborCities = resultCities;
    }

    /*
     * Method to construct and run an ant tour.
     */
    public void run(){

    int next = 0;
    int i = 0;
        
    	do{
			unvisitedNeighbors();
			i = move();
			
			//No possible move
			if(i==-1){
				System.out.println("returned -1?");
			}

			//update next city
			next = neighborCities.get(i);

			//update tour
			tour.add(next);
			visitedCities.add(next);
			
			//move to current city
			updateCurrCity();
		}while(visitedCities.size()<numOfCities);
      
    	//update distance
    	updateTourLength();
    }
    
    /*
     * Method to obtain distance of tour and update tour length
     */
    public void updateTourLength(){
      double sum = 0.0;
      
      for(int i = 0; i<tour.size() -1 ; i++){
        sum = sum + tsp.getDist(tour.get(i), tour.get(i+1));
      }
      
      tourLength  = sum;
    }
  }
}
