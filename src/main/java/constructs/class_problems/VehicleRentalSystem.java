package constructs.class_problems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleRentalSystem {

    // ==========================================
    // Entities & Abstractions
    // ==========================================

    public static class Customer {
        private final String customerId;
        private final String name;
        private final String licenseNumber;

        public Customer(String customerId, String name, String licenseNumber) {
            if (customerId == null || customerId.trim().isEmpty()) {
                throw new IllegalArgumentException("Customer ID cannot be empty.");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Customer name cannot be empty.");
            }
            if (licenseNumber == null || licenseNumber.trim().isEmpty()) {
                throw new IllegalArgumentException("License number cannot be empty.");
            }
            this.customerId = customerId;
            this.name = name;
            this.licenseNumber = licenseNumber;
        }

        public String getCustomerId() {
            return customerId;
        }

        public String getName() {
            return name;
        }

        public String getLicenseNumber() {
            return licenseNumber;
        }

        @Override
        public String toString() {
            return name + " (ID: " + customerId + ", License: " + licenseNumber + ")";
        }
    }

    public static abstract class Vehicle {
        private final String vehicleId;
        private final String model;
        private final double baseDailyRate;
        private boolean isRented;

        public Vehicle(String vehicleId, String model, double baseDailyRate) {
            if (vehicleId == null || vehicleId.trim().isEmpty()) {
                throw new IllegalArgumentException("Vehicle ID cannot be empty.");
            }
            if (model == null || model.trim().isEmpty()) {
                throw new IllegalArgumentException("Model cannot be empty.");
            }
            if (baseDailyRate <= 0) {
                throw new IllegalArgumentException("Base daily rate must be positive.");
            }
            this.vehicleId = vehicleId;
            this.model = model;
            this.baseDailyRate = baseDailyRate;
            this.isRented = false;
        }

        public String getVehicleId() {
            return vehicleId;
        }

        public String getModel() {
            return model;
        }

        public double getBaseDailyRate() {
            return baseDailyRate;
        }

        public boolean isRented() {
            return isRented;
        }

        public void setRented(boolean rented) {
            this.isRented = rented;
        }

        public abstract String getCategory();

        // Polymorphic pricing calculation
        public abstract double calculateRentalCost(int days);

        @Override
        public String toString() {
            return "[" + getCategory() + "] " + model + " (ID: " + vehicleId + ", Base Rate: INR " + baseDailyRate + "/day)";
        }
    }

    public static class Sedan extends Vehicle {
        private static final double LUXURY_TAX_RATE = 0.05; // 5% luxury tax

        public Sedan(String vehicleId, String model, double baseDailyRate) {
            super(vehicleId, model, baseDailyRate);
        }

        @Override
        public String getCategory() {
            return "Sedan";
        }

        @Override
        public double calculateRentalCost(int days) {
            if (days <= 0) {
                throw new IllegalArgumentException("Rental duration must be at least 1 day.");
            }
            double rawCost = getBaseDailyRate() * days;
            return rawCost + (rawCost * LUXURY_TAX_RATE);
        }
    }

    public static class SUV extends Vehicle {
        private static final double ALL_TERRAIN_DAILY_SURCHARGE = 200.0;

        public SUV(String vehicleId, String model, double baseDailyRate) {
            super(vehicleId, model, baseDailyRate);
        }

        @Override
        public String getCategory() {
            return "SUV";
        }

        @Override
        public double calculateRentalCost(int days) {
            if (days <= 0) {
                throw new IllegalArgumentException("Rental duration must be at least 1 day.");
            }
            return (getBaseDailyRate() + ALL_TERRAIN_DAILY_SURCHARGE) * days;
        }
    }

    public static class Truck extends Vehicle {
        private static final double FLAT_HAULING_FEE = 1500.0;

        public Truck(String vehicleId, String model, double baseDailyRate) {
            super(vehicleId, model, baseDailyRate);
        }

        @Override
        public String getCategory() {
            return "Truck";
        }

        @Override
        public double calculateRentalCost(int days) {
            if (days <= 0) {
                throw new IllegalArgumentException("Rental duration must be at least 1 day.");
            }
            return (getBaseDailyRate() * days) + FLAT_HAULING_FEE;
        }
    }

    public static class Rental {
        private final String rentalId;
        private final Customer customer;
        private final Vehicle vehicle;
        private final int days;
        private final double totalCost;
        private boolean isActive;

        public Rental(String rentalId, Customer customer, Vehicle vehicle, int days) {
            if (rentalId == null || rentalId.trim().isEmpty()) {
                throw new IllegalArgumentException("Rental ID cannot be empty.");
            }
            if (customer == null) {
                throw new IllegalArgumentException("Customer cannot be null.");
            }
            if (vehicle == null) {
                throw new IllegalArgumentException("Vehicle cannot be null.");
            }
            if (days <= 0) {
                throw new IllegalArgumentException("Rental days must be greater than zero.");
            }
            this.rentalId = rentalId;
            this.customer = customer;
            this.vehicle = vehicle;
            this.days = days;
            this.totalCost = vehicle.calculateRentalCost(days);
            this.isActive = true;
            this.vehicle.setRented(true);
        }

        public String getRentalId() {
            return rentalId;
        }

        public Customer getCustomer() {
            return customer;
        }

        public Vehicle getVehicle() {
            return vehicle;
        }

        public int getDays() {
            return days;
        }

        public double getTotalCost() {
            return totalCost;
        }

        public boolean isActive() {
            return isActive;
        }

        public void completeReturn() {
            if (!isActive) {
                throw new IllegalStateException("Rental is already completed/returned.");
            }
            this.isActive = false;
            this.vehicle.setRented(false);
        }

        @Override
        public String toString() {
            return "Rental #" + rentalId + " | " + customer.getName() + " -> " + vehicle.getModel() +
                    " (" + days + " days, Total: INR " + totalCost + ", Active: " + isActive + ")";
        }
    }

    // ==========================================
    // System State & Operations
    // ==========================================

    private final Map<String, Vehicle> inventory = new HashMap<>();
    private final Map<String, Rental> activeRentals = new HashMap<>();

    public void addVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Cannot add null vehicle.");
        }
        inventory.put(vehicle.getVehicleId(), vehicle);
    }

    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> available = new ArrayList<>();
        for (Vehicle v : inventory.values()) {
            if (!v.isRented()) {
                available.add(v);
            }
        }
        return available;
    }

    public Rental rentVehicle(String rentalId, Customer customer, String vehicleId, int days) {
        Vehicle vehicle = inventory.get(vehicleId);
        if (vehicle == null) {
            System.out.println("[ERROR] Vehicle with ID " + vehicleId + " does not exist in inventory.");
            return null;
        }

        if (vehicle.isRented()) {
            System.out.println("[REJECTED] Vehicle " + vehicle.getModel() + " (ID: " + vehicleId + ") is currently rented out.");
            return null;
        }

        Rental rental = new Rental(rentalId, customer, vehicle, days);
        activeRentals.put(rentalId, rental);
        System.out.println("[SUCCESS] Rented " + vehicle.getModel() + " to " + customer.getName() +
                " for " + days + " days. Total Charge: INR " + rental.getTotalCost());
        return rental;
    }

    public boolean returnVehicle(String rentalId) {
        Rental rental = activeRentals.get(rentalId);
        if (rental == null) {
            System.out.println("[ERROR] Rental ID " + rentalId + " not found.");
            return false;
        }

        if (!rental.isActive()) {
            System.out.println("[WARNING] Rental " + rentalId + " has already been returned.");
            return false;
        }

        rental.completeReturn();
        activeRentals.remove(rentalId);
        System.out.println("[RETURNED] Vehicle " + rental.getVehicle().getModel() +
                " returned by " + rental.getCustomer().getName() + ". Now available for rent.");
        return true;
    }

    public static void main(String[] args) {
        System.out.println("=== Vehicle Rental System Demonstration ===");

        VehicleRentalSystem system = new VehicleRentalSystem();

        // 1. Add vehicles of different categories
        Vehicle sedan = new Sedan("V-101", "Honda City", 2000.0);
        Vehicle suv = new SUV("V-201", "Toyota Fortuner", 3500.0);
        Vehicle truck = new Truck("V-301", "Tata Prima 4028", 5000.0);

        system.addVehicle(sedan);
        system.addVehicle(suv);
        system.addVehicle(truck);

        System.out.println("\nInitial Available Vehicles:");
        for (Vehicle v : system.getAvailableVehicles()) {
            System.out.println(" - " + v);
        }

        // 2. Create Customers
        Customer c1 = new Customer("C-1", "Aarav Sharma", "DL-04-202100123");
        Customer c2 = new Customer("C-2", "Priya Sen", "MH-12-201900456");

        // 3. Rent a Sedan for 3 days
        System.out.println("\n--- Renting Sedan ---");
        Rental r1 = system.rentVehicle("RNT-001", c1, "V-101", 3);

        // 4. Attempt to double-rent the same Sedan
        System.out.println("\n--- Attempting Double-Rental of Sedan ---");
        Rental r2 = system.rentVehicle("RNT-002", c2, "V-101", 2);

        // 5. Rent an SUV for 4 days
        System.out.println("\n--- Renting SUV ---");
        Rental r3 = system.rentVehicle("RNT-003", c2, "V-201", 4);

        // 6. Rent a Truck for 2 days
        System.out.println("\n--- Renting Truck ---");
        Customer c3 = new Customer("C-3", "Vikram Logistics", "KA-01-201800987");
        Rental r4 = system.rentVehicle("RNT-004", c3, "V-301", 2);

        System.out.println("\nAvailable Vehicles After Rentals:");
        for (Vehicle v : system.getAvailableVehicles()) {
            System.out.println(" - " + v);
        }

        // 7. Return the Sedan and verify availability
        System.out.println("\n--- Returning Sedan ---");
        system.returnVehicle("RNT-001");

        System.out.println("\nAvailable Vehicles After Returning Sedan:");
        for (Vehicle v : system.getAvailableVehicles()) {
            System.out.println(" - " + v);
        }

        // 8. Now Customer 2 can rent the returned Sedan
        System.out.println("\n--- Renting previously returned Sedan ---");
        system.rentVehicle("RNT-005", c2, "V-101", 2);
    }
}
