package constructs.class_problems;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HotelBookingSystem {

    // ==========================================
    // Customer Entity
    // ==========================================

    public static class Customer {
        private final String customerId;
        private final String name;
        private final String phoneNumber;

        public Customer(String customerId, String name, String phoneNumber) {
            if (customerId == null || customerId.trim().isEmpty()) {
                throw new IllegalArgumentException("Customer ID cannot be empty.");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Customer name cannot be empty.");
            }
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                throw new IllegalArgumentException("Phone number cannot be empty.");
            }
            this.customerId = customerId;
            this.name = name;
            this.phoneNumber = phoneNumber;
        }

        public String getCustomerId() {
            return customerId;
        }

        public String getName() {
            return name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        @Override
        public String toString() {
            return name + " (ID: " + customerId + ", Phone: " + phoneNumber + ")";
        }
    }

    // ==========================================
    // Room Hierarchy (Polymorphic Pricing)
    // ==========================================

    public static abstract class Room {
        private final String roomNumber;
        private final double baseRatePerNight;

        public Room(String roomNumber, double baseRatePerNight) {
            if (roomNumber == null || roomNumber.trim().isEmpty()) {
                throw new IllegalArgumentException("Room number cannot be empty.");
            }
            if (baseRatePerNight <= 0) {
                throw new IllegalArgumentException("Base rate per night must be positive.");
            }
            this.roomNumber = roomNumber;
            this.baseRatePerNight = baseRatePerNight;
        }

        public String getRoomNumber() {
            return roomNumber;
        }

        public double getBaseRatePerNight() {
            return baseRatePerNight;
        }

        public abstract String getCategory();

        // Polymorphic dynamic price calculation based on category & duration
        public abstract double calculatePrice(int nights);

        @Override
        public String toString() {
            return "[" + getCategory() + "] Room #" + roomNumber + " (INR " + baseRatePerNight + "/night)";
        }
    }

    public static class StandardRoom extends Room {
        public StandardRoom(String roomNumber, double baseRatePerNight) {
            super(roomNumber, baseRatePerNight);
        }

        @Override
        public String getCategory() {
            return "Standard";
        }

        @Override
        public double calculatePrice(int nights) {
            if (nights <= 0) {
                throw new IllegalArgumentException("Duration must be at least 1 night.");
            }
            return getBaseRatePerNight() * nights;
        }
    }

    public static class DeluxeRoom extends Room {
        private static final double AMENITY_SURCHARGE_RATE = 0.15; // 15% luxury amenities surcharge

        public DeluxeRoom(String roomNumber, double baseRatePerNight) {
            super(roomNumber, baseRatePerNight);
        }

        @Override
        public String getCategory() {
            return "Deluxe";
        }

        @Override
        public double calculatePrice(int nights) {
            if (nights <= 0) {
                throw new IllegalArgumentException("Duration must be at least 1 night.");
            }
            double base = getBaseRatePerNight() * nights;
            double total = base + (base * AMENITY_SURCHARGE_RATE);
            if (nights >= 4) {
                total *= 0.95; // 5% extended stay discount
            }
            return total;
        }
    }

    public static class Suite extends Room {
        private static final double BUTLER_SERVICE_FEE = 1000.0;

        public Suite(String roomNumber, double baseRatePerNight) {
            super(roomNumber, baseRatePerNight);
        }

        @Override
        public String getCategory() {
            return "Suite";
        }

        @Override
        public double calculatePrice(int nights) {
            if (nights <= 0) {
                throw new IllegalArgumentException("Duration must be at least 1 night.");
            }
            double base = getBaseRatePerNight() * nights * 1.25; // 25% premium suite service
            double total = base + BUTLER_SERVICE_FEE;
            if (nights >= 3) {
                total *= 0.90; // 10% extended stay discount
            }
            return total;
        }
    }

    // ==========================================
    // Reservation & Booking Management
    // ==========================================

    public enum ReservationStatus {
        CONFIRMED,
        CANCELLED
    }

    public static class Reservation {
        private final String reservationId;
        private final Customer customer;
        private final Room room;
        private final LocalDate checkInDate;
        private final LocalDate checkOutDate;
        private final double totalPrice;
        private ReservationStatus status;

        public Reservation(String reservationId, Customer customer, Room room, LocalDate checkInDate, LocalDate checkOutDate) {
            if (reservationId == null || reservationId.trim().isEmpty()) {
                throw new IllegalArgumentException("Reservation ID cannot be empty.");
            }
            if (customer == null) {
                throw new IllegalArgumentException("Customer cannot be null.");
            }
            if (room == null) {
                throw new IllegalArgumentException("Room cannot be null.");
            }
            if (checkInDate == null || checkOutDate == null) {
                throw new IllegalArgumentException("Check-in and Check-out dates cannot be null.");
            }
            if (!checkOutDate.isAfter(checkInDate)) {
                throw new IllegalArgumentException("Check-out date must be strictly after Check-in date.");
            }

            this.reservationId = reservationId;
            this.customer = customer;
            this.room = room;
            this.checkInDate = checkInDate;
            this.checkOutDate = checkOutDate;

            int nights = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            this.totalPrice = room.calculatePrice(nights);
            this.status = ReservationStatus.CONFIRMED;
        }

        public String getReservationId() {
            return reservationId;
        }

        public Customer getCustomer() {
            return customer;
        }

        public Room getRoom() {
            return room;
        }

        public LocalDate getCheckInDate() {
            return checkInDate;
        }

        public LocalDate getCheckOutDate() {
            return checkOutDate;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public ReservationStatus getStatus() {
            return status;
        }

        public boolean overlaps(LocalDate candidateCheckIn, LocalDate candidateCheckOut) {
            if (this.status != ReservationStatus.CONFIRMED) {
                return false;
            }
            return candidateCheckIn.isBefore(this.checkOutDate) && candidateCheckOut.isAfter(this.checkInDate);
        }

        public boolean cancel(LocalDate currentDate) {
            if (status == ReservationStatus.CANCELLED) {
                System.out.println("[WARNING] Reservation " + reservationId + " is already cancelled.");
                return false;
            }

            LocalDate deadline = checkInDate.minusDays(2);
            if (currentDate.isAfter(deadline)) {
                System.out.println("[REJECTED] Cancellation denied for " + reservationId +
                        ". Deadline was " + deadline + ", current date is " + currentDate);
                return false;
            }

            this.status = ReservationStatus.CANCELLED;
            System.out.println("[CANCELLED] Reservation " + reservationId + " successfully cancelled.");
            return true;
        }

        @Override
        public String toString() {
            return "Reservation #" + reservationId + " | " + customer.getName() + " | Room: " + room.getRoomNumber() +
                    " (" + room.getCategory() + ") | " + checkInDate + " to " + checkOutDate +
                    " | Total: INR " + totalPrice + " | Status: " + status;
        }
    }

    // ==========================================
    // System State & Operations
    // ==========================================

    private final Map<String, Room> rooms = new HashMap<>();
    private final List<Reservation> reservations = new ArrayList<>();

    public void addRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Cannot add null room.");
        }
        rooms.put(room.getRoomNumber(), room);
    }

    public Reservation bookRoom(String reservationId, Customer customer, String roomNumber, LocalDate checkIn, LocalDate checkOut) {
        Room room = rooms.get(roomNumber);
        if (room == null) {
            System.out.println("[ERROR] Room #" + roomNumber + " does not exist.");
            return null;
        }

        if (!checkOut.isAfter(checkIn)) {
            System.out.println("[ERROR] Check-out date (" + checkOut + ") must be after check-in date (" + checkIn + ").");
            return null;
        }

        for (Reservation res : reservations) {
            if (res.getRoom().getRoomNumber().equals(roomNumber) && res.overlaps(checkIn, checkOut)) {
                System.out.println("[BOOKING CONFLICT] Room #" + roomNumber + " is already reserved from " +
                        res.getCheckInDate() + " to " + res.getCheckOutDate() +
                        " (Conflicts with requested: " + checkIn + " to " + checkOut + ")");
                return null;
            }
        }

        Reservation newReservation = new Reservation(reservationId, customer, room, checkIn, checkOut);
        reservations.add(newReservation);
        System.out.println("[BOOKING CONFIRMED] " + newReservation);
        return newReservation;
    }

    public boolean cancelReservation(String reservationId, LocalDate cancellationDate) {
        for (Reservation res : reservations) {
            if (res.getReservationId().equals(reservationId)) {
                return res.cancel(cancellationDate);
            }
        }
        System.out.println("[ERROR] Reservation ID " + reservationId + " not found.");
        return false;
    }

    public List<Reservation> getReservations() {
        return Collections.unmodifiableList(reservations);
    }

    public static void main(String[] args) {
        System.out.println("=== Hotel Booking System Demonstration ===");

        HotelBookingSystem hotel = new HotelBookingSystem();

        // 1. Add rooms of different categories
        Room std = new StandardRoom("101", 1500.0);
        Room del = new DeluxeRoom("201", 3000.0);
        Room ste = new Suite("301", 6000.0);

        hotel.addRoom(std);
        hotel.addRoom(del);
        hotel.addRoom(ste);

        System.out.println("\nRegistered Hotel Rooms:");
        System.out.println(" - " + std);
        System.out.println(" - " + del);
        System.out.println(" - " + ste);

        // 2. Customers
        Customer c1 = new Customer("CUST-1", "Karan Malhotra", "+91-9876543210");
        Customer c2 = new Customer("CUST-2", "Meera Iyer", "+91-9123456780");

        // 3. Book Standard Room: Oct 10 to Oct 15 (5 nights)
        System.out.println("\n--- Step 1: Booking Standard Room 101 (Oct 10 - Oct 15) ---");
        LocalDate d10 = LocalDate.of(2026, 10, 10);
        LocalDate d15 = LocalDate.of(2026, 10, 15);
        hotel.bookRoom("RES-001", c1, "101", d10, d15);

        // 4. Attempt overlapping booking for Room 101: Oct 12 to Oct 17 (Should be blocked)
        System.out.println("\n--- Step 2: Attempting overlapping booking for Room 101 (Oct 12 - Oct 17) ---");
        LocalDate d12 = LocalDate.of(2026, 10, 12);
        LocalDate d17 = LocalDate.of(2026, 10, 17);
        hotel.bookRoom("RES-002", c2, "101", d12, d17);

        // 5. Non-overlapping booking for Room 101: Oct 15 to Oct 18 (Allowed)
        System.out.println("\n--- Step 3: Non-overlapping booking for Room 101 (Oct 15 - Oct 18) ---");
        LocalDate d18 = LocalDate.of(2026, 10, 18);
        hotel.bookRoom("RES-003", c2, "101", d15, d18);

        // 6. Book Deluxe Room with extended stay discount
        System.out.println("\n--- Step 4: Booking Deluxe Room 201 (Oct 10 - Oct 15, 5 nights) ---");
        hotel.bookRoom("RES-004", c1, "201", d10, d15);

        // 7. Book Suite with luxury package
        System.out.println("\n--- Step 5: Booking Suite 301 (Oct 10 - Oct 14, 4 nights) ---");
        LocalDate d14 = LocalDate.of(2026, 10, 14);
        hotel.bookRoom("RES-005", c2, "301", d10, d14);

        // 8. Cancellation test: Cancel RES-001 well before deadline (Oct 5 -> checkIn is Oct 10)
        System.out.println("\n--- Step 6: Valid Cancellation of RES-001 on Oct 5 ---");
        hotel.cancelReservation("RES-001", LocalDate.of(2026, 10, 5));

        // 9. Re-booking the freed dates after cancellation
        System.out.println("\n--- Step 7: Booking newly freed Room 101 after cancellation (Oct 10 - Oct 15) ---");
        hotel.bookRoom("RES-006", c2, "101", d10, d15);

        // 10. Late cancellation attempt: Trying to cancel RES-006 on Oct 9 (CheckIn is Oct 10, deadline passed)
        System.out.println("\n--- Step 8: Late Cancellation attempt for RES-006 on Oct 9 (Deadline passed) ---");
        hotel.cancelReservation("RES-006", LocalDate.of(2026, 10, 9));
    }
}
