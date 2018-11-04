package opdracht_optimalisatietechnieken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static final String FILENAME = "tvh_problem_3.txt";

    public static void main(String[] args) {

        Problem p = readInput();
        p.solve();
    }

    private static Problem readInput() {
        try {
            System.out.println("Reading input...");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(FILENAME));
            Scanner sc;

            // skip first line
            bufferedReader.readLine();

            // truck cap, truck working time
            sc = new Scanner(bufferedReader.readLine());
            sc.next();
            int TRUCK_CAPACITY = sc.nextInt();
            sc.close();
            sc = new Scanner(bufferedReader.readLine());
            sc.next();
            int TRUCK_WORKING_TIME = sc.nextInt();
            sc.close();

            // skip empty line
            bufferedReader.readLine();

            // LOCATIONS
            // adding location-objects to locationList
            List<Location> locationList = new ArrayList<Location>();
            sc = new Scanner(bufferedReader.readLine());
            sc.next();
            int numberOfLocations = sc.nextInt();
            sc.close();
            for (int i = 0; i < numberOfLocations; i++) {
                String[] array = bufferedReader.readLine().trim().split("\\s+");
                int id = Integer.parseInt(array[0]);
                double latitude = Double.parseDouble(array[1]);
                double longitude = Double.parseDouble(array[2]);
                String name = array[3];
                locationList.add(new Location(id, latitude, longitude, name));
            }

            // skip empty line
            bufferedReader.readLine();

            // DEPOTS
            // adding location-objects to locationList
            List<Depot> depotList = new ArrayList<Depot>();
            sc = new Scanner(bufferedReader.readLine());
            sc.next();
            int numberOfDepots = sc.nextInt();
            sc.close();
            for (int i = 0; i < numberOfDepots; i++) {
                String[] array = bufferedReader.readLine().trim().split("\\s+");
                int id = Integer.parseInt(array[0]);
                Location location = locationList.get(Integer.parseInt(array[1]));
                depotList.add(new Depot(id, location));
            }

            // skip empty line
            bufferedReader.readLine();

            // TRUCKS
            // adding truck-objects to truckList
            List<Truck> truckList = new ArrayList<Truck>();
            sc = new Scanner(bufferedReader.readLine());
            sc.next();
            int numberOfTrucks = sc.nextInt();
            sc.close();
            for (int i = 0; i < numberOfTrucks; i++) {
                String[] array = bufferedReader.readLine().trim().split("\\s+");
                int id = Integer.parseInt(array[0]);
                Location startLocation = locationList.get(Integer.parseInt(array[1]));
                Location endLocation = locationList.get(Integer.parseInt(array[2]));
                truckList.add(new Truck(id, startLocation, endLocation));
            }
            // skip empty line
            bufferedReader.readLine();

            // MACHINE TYPES
            // adding type-objects to machineTypeList
            List<MachineType> machineTypeList = new ArrayList<MachineType>();
            sc = new Scanner(bufferedReader.readLine());
            sc.next();
            int numberOfMachineTypes = sc.nextInt();
            sc.close();
            for (int i = 0; i < numberOfMachineTypes; i++) {
                String[] array = bufferedReader.readLine().trim().split("\\s+");
                int id = Integer.parseInt(array[0]);
                int volume = Integer.parseInt(array[1]);
                int serviceTime = Integer.parseInt(array[2]);
                String typeName = array[3];
                machineTypeList.add(new MachineType(id, volume, serviceTime, typeName));
            }

            // skip empty line
            bufferedReader.readLine();

            // MACHINE TYPES
            // adding type-objects to machineTypeList
            List<Machine> machineList = new ArrayList<Machine>();
            sc = new Scanner(bufferedReader.readLine());
            sc.next();
            int numberOfMachines = sc.nextInt();
            sc.close();
            for (int i = 0; i < numberOfMachines; i++) {
                String[] array = bufferedReader.readLine().trim().split("\\s+");
                int id = Integer.parseInt(array[0]);
                MachineType machineType = machineTypeList.get(Integer.parseInt(array[1]));
                Location location = locationList.get(Integer.parseInt(array[2]));
                machineList.add(new Machine(id, machineType, location));
            }

            // skip empty line
            bufferedReader.readLine();

            // DROPS
            // adding drop-objects to dropList
            List<Drop> dropList = new ArrayList<Drop>();
            sc = new Scanner(bufferedReader.readLine());
            sc.next();
            int numberOfDrops = sc.nextInt();
            sc.close();
            for (int i = 0; i < numberOfDrops; i++) {
                String[] array = bufferedReader.readLine().trim().split("\\s+");
                int id = Integer.parseInt(array[0]);
                MachineType machineType = machineTypeList.get(Integer.parseInt(array[1]));
                Location location = locationList.get(Integer.parseInt(array[2]));
                dropList.add(new Drop(id, machineType, location));
            }

            // skip empty line
            bufferedReader.readLine();

            // COLLECTS
            // adding collect-objects to dropList
            List<Collect> collectList = new ArrayList<Collect>();
            sc = new Scanner(bufferedReader.readLine());
            sc.next();
            int numberOfCollects = sc.nextInt();
            sc.close();
            for (int i = 0; i < numberOfCollects; i++) {
                String[] array = bufferedReader.readLine().trim().split("\\s+");
                int id = Integer.parseInt(array[0]);
                Machine machine = machineList.get(Integer.parseInt(array[1]));
                collectList.add(new Collect(id, machine));
            }

            // skip empty line
            bufferedReader.readLine();
            // skip unnecessary line
            bufferedReader.readLine();

            int[][] timeMatrix = new int[numberOfLocations][numberOfLocations];
            for (int i = 0; i < timeMatrix.length; i++) {
                String[] array = bufferedReader.readLine().trim().split("\\s+");
                for (int j = 0; j < array.length; j++) {
                    timeMatrix[i][j] = Integer.parseInt(array[j]);
                }
            }

            // skip empty line
            bufferedReader.readLine();
            // skip unnecessary line
            bufferedReader.readLine();

            int[][] distanceMatrix = new int[numberOfLocations][numberOfLocations];
            for (int i = 0; i < timeMatrix.length; i++) {
                String[] array = bufferedReader.readLine().trim().split("\\s+");
                for (int j = 0; j < array.length; j++) {
                    distanceMatrix[i][j] = Integer.parseInt(array[j]);
                }
            }
            bufferedReader.close();

            Problem p = new Problem(TRUCK_CAPACITY, TRUCK_WORKING_TIME, locationList, depotList, truckList,
                    machineTypeList, machineList, dropList, collectList, timeMatrix, distanceMatrix);
            System.out.println(p.getDepotList().get(0).toString());
            System.out.println("Input OK");
            return p;


        } catch (FileNotFoundException e) {
            System.out.println("Error reading file '" + FILENAME + "'");
            e.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error reading file '" + FILENAME + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
        return null;
    }

}
