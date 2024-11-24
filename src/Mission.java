abstract class Mission {
    protected double targetLatitude;
    protected double targetLongitude;
    protected double duration;
    protected double targetTemperature;

    public Mission(double targetLatitude, double targetLongitude, double duration, double targetTemperature) {
        this.targetLatitude = targetLatitude;
        this.targetLongitude = targetLongitude;
        this.duration = duration;
        this.targetTemperature = targetTemperature;
    }

    public abstract void startMission();
}
class ExploreMission extends Mission {
    public ExploreMission(double targetLatitude, double targetLongitude, double duration, double targetTemperature) {
        super(targetLatitude, targetLongitude, duration, targetTemperature);
    }

    @Override
    public void startMission() {
        System.out.println("Explore mission started.");

    }
}
class SatelliteMission extends Mission {

    public SatelliteMission(double targetLatitude, double targetLongitude, double duration, double destinationTemperature) {
        super(targetLatitude, targetLongitude, duration, destinationTemperature);
    }



    @Override
    public void startMission() {
        System.out.println("Satellite mission started.");
    }
}
 class SupplyMission extends Mission {

    public SupplyMission(double targetLatitude, double targetLongitude, double duration, double destinationTemperature) {
        super(targetLatitude, targetLongitude, duration, destinationTemperature);
    }



     @Override
     public void startMission() {
         System.out.println("Supply mission started.");

     }
 }

