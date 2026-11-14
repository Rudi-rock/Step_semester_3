import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CampusVehiclePassSystem {
    private interface Chargeable {
        boolean supportsCharging();
    }

    private static class Vehicle {
        private final String type;
        private final String passNumber;
        private final String owner;

        Vehicle(String type, String passNumber, String owner) {
            this.type = type;
            this.passNumber = passNumber;
            this.owner = owner;
        }

        String getType() {
            return type;
        }

        String getPassNumber() {
            return passNumber;
        }

        String getOwner() {
            return owner;
        }

        int yearlyFee() {
            return type.equals("Bike") || type.equals("EBike") ? 300 : 1000;
        }
    }

    private static final class ElectricVehicle extends Vehicle implements Chargeable {
        ElectricVehicle(String type, String passNumber, String owner) {
            super(type, passNumber, owner);
        }

        @Override
        public boolean supportsCharging() {
            return true;
        }
    }

    public static void main(String[] args) throws IOException {
        Map<String, Vehicle> vehicles = new HashMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 0 || parts[0].isEmpty()) {
                continue;
            }
            if (parts[0].equals("PASS")) {
                String type = parts[1];
                Vehicle vehicle = type.equals("EBike") || type.equals("ECar")
                        ? new ElectricVehicle(type, parts[2], parts[3])
                        : new Vehicle(type, parts[2], parts[3]);
                vehicles.put(vehicle.getPassNumber(), vehicle);
                System.out.println(vehicle.getPassNumber() + " (" + vehicle.getType() + ") pass fee "
                        + vehicle.yearlyFee());
            } else if (parts[0].equals("CHARGE")) {
                Vehicle vehicle = vehicles.get(parts[1]);
                if (vehicle instanceof Chargeable chargeable && chargeable.supportsCharging()) {
                    System.out.println(vehicle.getPassNumber() + " charging bay allotted");
                } else {
                    System.out.println(parts[1] + " rejected: charging unsupported");
                }
            }
        }
    }
}
