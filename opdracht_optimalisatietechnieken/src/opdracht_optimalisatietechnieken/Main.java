package opdracht_optimalisatietechnieken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

	public static void main(String[] args) {
		String fileName = "tvh_problem_1.txt";
		String line = "";
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
			Scanner sc;
			
			// skip first line
			bufferedReader.readLine();
			
			// truck cap, truck working time, service time
			sc = new Scanner(bufferedReader.readLine());
			sc.next(); 
			int TRUCK_CAPACITY = sc.nextInt();
			sc = new Scanner(bufferedReader.readLine());
			sc.next(); 
			int TRUCK_WORKING_TIME = sc.nextInt();
			sc = new Scanner(bufferedReader.readLine());
			sc.next(); 
			int SERVICE_TIME = sc.nextInt();
			
			// skip empty line
			bufferedReader.readLine();
			
			//LOCATIONS
			List<Location> locationList = new ArrayList<Location>();
			sc = new Scanner(bufferedReader.readLine());
			sc.next(); 
			int numberOfLocations = sc.nextInt();
			for (int i = 0; i < numberOfLocations; i++) {
				sc = new Scanner(bufferedReader.readLine());
				int id = sc.nextInt();
				int latitude = sc.nextInt();
				int longitude = sc.nextInt();
				locationList.add(new Location(id, latitude, longitude));
			}
			
			
			
			
			System.out.println(TRUCK_CAPACITY+ " " + TRUCK_WORKING_TIME + " " + SERVICE_TIME + " " + numberOfLocations);
			
			
			
			
			sc.close();
			bufferedReader.close();         
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
	}

}
