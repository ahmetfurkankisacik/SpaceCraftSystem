import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java Main <input_file> <spacecraft_name> <total_energy>");
            return;
        }

        String inputFile = args[0];
        String spacecraftName = args[1];
        double totalEnergy = Double.parseDouble(args[2]);

        SpacecraftSystem spacecraft = new SpacecraftSystem(spacecraftName, totalEnergy);

        // Read from the input file and set up the spacecraft
        readTxt(inputFile, spacecraft);

        // Launch the spacecraft
        spacecraft.launch();
    }

    private static void readTxt(String input, SpacecraftSystem spacecraft) {
        try (BufferedReader br = new BufferedReader(new FileReader(input))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                switch (parts[0]) {
                    case "propulSys":
                        double energyConsumption = Double.parseDouble(parts[1]);
                        double fuelLevel = Double.parseDouble(parts[2]);
                        double fuelConsumptionPerKm = Double.parseDouble(parts[3]);
                        spacecraft.setSystems(new PropulsionSystem(energyConsumption, fuelLevel, fuelConsumptionPerKm),
                                spacecraft.getLifeSupportSystem(),
                                spacecraft.getNavigationSystem());
                        break;
                    case "supportSys":
                        double lsEnergyConsumption = Double.parseDouble(parts[1]);
                        double cabinTemperature = Double.parseDouble(parts[2]);
                        double oxygenLevel = Double.parseDouble(parts[3]);
                        double oxygenCoefficient = Double.parseDouble(parts[4]);
                        spacecraft.setSystems(spacecraft.getPropulsionSystem(),
                                new LifeSupportSystem(lsEnergyConsumption, cabinTemperature, oxygenLevel, oxygenCoefficient),
                                spacecraft.getNavigationSystem());
                        break;
                    case "navSys":
                        double navEnergyConsumption = Double.parseDouble(parts[1]);
                        double currentLatitude = Double.parseDouble(parts[2]);
                        double currentLongitude = Double.parseDouble(parts[3]);
                        spacecraft.setSystems(spacecraft.getPropulsionSystem(),
                                spacecraft.getLifeSupportSystem(),
                                new NavigationSystem(navEnergyConsumption, currentLatitude, currentLongitude));
                        break;
                    case "exploreMission":
                        double targetLatitude = Double.parseDouble(parts[1]);
                        double targetLongitude = Double.parseDouble(parts[2]);
                        double duration = Double.parseDouble(parts[3]);
                        double targetTemperature = Double.parseDouble(parts[4]);
                        spacecraft.setMission(new ExploreMission(targetLatitude, targetLongitude, duration, targetTemperature));
                        break;
                    case "satalliteMission":
                        double targetLatitude1 = Double.parseDouble(parts[1]);
                        double targetLongitude1 = Double.parseDouble(parts[2]);
                        double duration1 = Double.parseDouble(parts[3]);
                        double targetTemperature1 = Double.parseDouble(parts[4]);
                        spacecraft.setMission(new SatelliteMission(targetLatitude1, targetLongitude1, duration1, targetTemperature1));
                        break;
                    case "supplyMission":
                        double targetLatitude2 = Double.parseDouble(parts[1]);
                        double targetLongitude2 = Double.parseDouble(parts[2]);
                        double duration2 = Double.parseDouble(parts[3]);
                        double targetTemperature2 = Double.parseDouble(parts[4]);
                        spacecraft.setMission(new SupplyMission(targetLatitude2, targetLongitude2, duration2, targetTemperature2));
                        break;
                    default:
                        System.out.println("Unknown system or mission type in input.");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}