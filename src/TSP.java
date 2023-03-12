import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/*
 * TSP.java - a class that processes TSP file and stores essential information
 * @author Selvan Kumar
 * @version 1.0
 * 
 * Class adapted from: Jade J. (2018), "AntColOpt", github, [online at https://github.com/jkbestami/AntColOpt/blob/master/WorldGraph.java, accessed 3 Feb 2020].
 */

public class TSP {
	
	int numCities;
	double[][] distance;

	String[] cityNames;
	double[] lats;
	double[] lons;
	
	/*
	 * Construct method that process file and updates distances  
	 * @param fileName A variable of type String
	 */
	public TSP(String fileName) {
		readFile(fileName);
		upDist();
	}
	
	/*
	 * Method to process file and store essential information.
	 * @param fileName A variable of typre String
	 */
	public void readFile(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;

			text = reader.readLine();
			numCities = Integer.parseInt(text);

			cityNames = new String[numCities];
			lats = new double[numCities];
			lons = new double[numCities];
			
			int i = 0;
			
			//store data from file in corresponding arrays
			while ((text = reader.readLine()) != null) {
				String[] parts = text.split(",");
				cityNames[i] = parts[0];
				lats[i] = Double.parseDouble(parts[1]);
				lons[i] = Double.parseDouble(parts[2]);
				i++;
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		} 
		
		finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Method to calculate euclidean distance from two cities given their lattitudes and longtitudes.
	 * @param lat1 A variable of type double 
	 * @param lon1 A variable of type double
	 * @param lat2 A variable of type double
	 * @param lon2 A variable of type double
	 * @return A double data type
	 */
	public double calDist(double lat1,double lon1,double lat2,double lon2){
		double p = 0.017453292519943295;   
		double a = 0.5 - Math.cos((lat2 - lat1) * p)/2 + Math.cos(lat1 * p) * Math.cos(lat2 * p) * (1 - Math.cos((lon2 - lon1) * p))/2;
		double result = 12742 * Math.asin(Math.sqrt(a));
		
		return result; 
	}

	/*
	 * Getter method to obtain distance from array.
	 * @param o A variable of type Integer
	 * @param d A variable of type Integer
	 * @return A double data type
	 */
	public double getDist(int o, int d){
		return distance[o][d];
	}

	/*
	 * Getter method to obtain total number of cities.
	 * @return A Integer data type
	 */
	public int getnumCities(){
		return numCities;
	}

	/*
	 * Method to update distances between cities in array.
	 */
	public void upDist(){
		distance = new double[numCities][numCities];
		
		for(int i = 0; i<numCities; i++){
			for(int j = 0; j<numCities; j++){
				if(i==j){
					distance[i][j] = 0.0;
				}else{
					distance[i][j] = calDist(lats[i], lons[i], lats[j], lons[j]);
				}
			}
		}
	}
}
