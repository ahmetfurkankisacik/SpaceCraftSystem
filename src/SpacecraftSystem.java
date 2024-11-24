public class SpacecraftSystem {
    private String name;
    private double totalEnergy;
    private PropulsionSystem propulsionSystem;
    private LifeSupportSystem lifeSupportSystem;
    private NavigationSystem navigationSystem;
    private Mission mission;

    public SpacecraftSystem(String name, double totalEnergy) {
        this.name = name;
        this.totalEnergy = totalEnergy;
    }

    public void setSystems(PropulsionSystem propulsion, LifeSupportSystem support, NavigationSystem navigation) {
        this.propulsionSystem = propulsion;
        this.lifeSupportSystem = support;
        this.navigationSystem = navigation;
    }

    public String getName() {
        return name;
    }

    public double getTotalEnergy() {
        return totalEnergy;
    }

    public PropulsionSystem getPropulsionSystem() {
        return propulsionSystem;
    }

    public LifeSupportSystem getLifeSupportSystem() {
        return lifeSupportSystem;
    }

    public NavigationSystem getNavigationSystem() {
        return navigationSystem;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public void launch() {
        if (mission == null || propulsionSystem == null || lifeSupportSystem == null || navigationSystem == null) {
            System.out.println("Cannot launch: Ensure all systems and mission are properly initialized.");
            return;
        }

        double distance = navigationSystem.calculateDistance(mission.targetLatitude, mission.targetLongitude);

        boolean propulsionReady = propulsionSystem.canActivate(distance);
        boolean lifeSupportReady = lifeSupportSystem.canActivate(mission.targetTemperature, mission.duration);

        propulsionSystem.activate();
        lifeSupportSystem.activate();
        navigationSystem.activate();
        System.out.println("All systems active");

        if (propulsionReady && lifeSupportReady) {

            // Update fuel and oxygen levels
            propulsionSystem.consumeFuel(distance);
            lifeSupportSystem.consumeOxygen(mission.targetTemperature, mission.duration);

            mission.startMission();

            System.out.printf("Spacecraft Status: %s\n", name.replaceAll("[^a-zA-Z]"," "));
            System.out.printf("Propulsion System Status: Operating at %.1f%% fuel\n", propulsionSystem.getFuelLevel());
            System.out.printf("Fuel Level: %.13f\n", propulsionSystem.getFuelLevel());
            System.out.printf("Life Support System Status: Oxygen Level: %.1f%%, Temperature: %.1f°C\n",
                    lifeSupportSystem.getOxygenLevelPercentage(), lifeSupportSystem.getCabinTemperature());
            System.out.println("Navigation System Status: Active");
            System.out.printf("Current Latitude: %.1f Current Longitude: %.1f\n",
                    navigationSystem.getCurrentLatitude(), navigationSystem.getCurrentLongitude());
            System.out.println("Explore rescue mission is ended");
        } else {
            if (!lifeSupportReady) {
                System.out.println("Cannot perform spaceflight, oxygen level is not enough!");
            }
            if (!propulsionReady) {
                System.out.println("Cannot perform spaceflight, fuel level is not enough!");
            }

            System.out.println("Propulsion System is now standby.");
            System.out.println("Life Support System is now standby.");
            System.out.println("Navigation System is now standby.");
            System.out.println("Explore mission start failed!");
        }
    }


}
 class NavigationSystem {
    private double energyConsumption;
    private double currentLatitude;
    private double currentLongitude;

    public NavigationSystem(double energyConsumption, double currentLatitude, double currentLongitude) {
        this.energyConsumption = energyConsumption;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
    }

    public double getEnergyConsumption() {
        return energyConsumption;
    }

    public void setEnergyConsumption(double energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public double calculateDistance(double targetLatitude, double targetLongitude) {
        return haversine(currentLatitude, currentLongitude, targetLatitude, targetLongitude);
    }

    double haversine(double currentLatitude, double
            currentLongitude, double targetLatitude, double
                             targetLongitude){
        final int R = 6371;
        double latDistance = Math.toRadians(targetLatitude -
                currentLatitude);
        double lonDistance = Math.toRadians(targetLongitude -
                currentLongitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance
                / 2) +
                Math.cos(Math.toRadians(currentLatitude)) * Math.
                        cos(Math.toRadians(targetLatitude)) *
                        Math.sin(lonDistance / 2) * Math.sin(
                        lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public void activate() {
        System.out.println("Navigation System is now active.");
        System.out.println("Navigation system activated and stabilizing environment.");
    }

}
class PropulsionSystem {
    private double energyConsumption;
    private double initialFuelLevel;
    private double fuelLevel;
    private double fuelConsumptionPerKm;

    public PropulsionSystem(double energyConsumption, double fuelLevel, double fuelConsumptionPerKm) {
        this.energyConsumption = energyConsumption;
        this.initialFuelLevel = fuelLevel; // Save the initial fuel level for percentage calculation
        this.fuelLevel = fuelLevel;
        this.fuelConsumptionPerKm = fuelConsumptionPerKm;
    }

    public boolean canActivate(double distance) {
        double requiredFuel = distance * fuelConsumptionPerKm;
        return requiredFuel <= fuelLevel;
    }

    public void activate() {
        System.out.println("Propulsion System is now active.");
        System.out.println("Propulsion system is ready for launch.");
    }


    public void consumeFuel(double distance) {
        double requiredFuel = distance * fuelConsumptionPerKm;
        fuelLevel -= requiredFuel; // Reduce the fuel based on the distance traveled
    }

    public double getFuelLevelPercentage() {
        // The percentage should reflect the remaining fuel level compared to the initial fuel level
        return (fuelLevel / initialFuelLevel) * 100.0;
    }

    public double getFuelLevel() {
        return fuelLevel;
    }
}
 class LifeSupportSystem {
    private double energyConsumption;
    private double cabinTemperature;
    private double oxygenLevel;
    private double oxygenCoefficient;

    public LifeSupportSystem(double energyConsumption, double cabinTemperature, double oxygenLevel, double oxygenCoefficient) {
        this.energyConsumption = energyConsumption;
        this.cabinTemperature = cabinTemperature;
        this.oxygenLevel = oxygenLevel;
        this.oxygenCoefficient = oxygenCoefficient;
    }

    public boolean canActivate(double targetTemperature, double missionDuration) {
        double oxygenRequired = Math.abs(targetTemperature - cabinTemperature) * oxygenCoefficient * missionDuration;
        return oxygenRequired <= oxygenLevel;
    }

    public void activate() {
        System.out.println("Life Support System is now active.");
        System.out.println("Life support system activated and stabilizing environment.");
    }

    public void consumeOxygen(double targetTemperature, double missionDuration) {
        double oxygenRequired = Math.abs(targetTemperature - cabinTemperature) * oxygenCoefficient * missionDuration;
        oxygenLevel -= oxygenRequired; // Deduct the required oxygen
        cabinTemperature = targetTemperature; // Stabilize the cabin temperature to the target
    }

    public double getOxygenLevelPercentage() {
        // Normalize the oxygen level as a percentage
        return (oxygenLevel / 100.0) * 100.0;
    }

    public double getCabinTemperature() {
        return cabinTemperature;
    }

    public double getOxygenLevel() {
        return oxygenLevel;
    }
}


