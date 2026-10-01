package constructs.assignment_problems;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CampusPremiereTicketCounter {

    // ==========================================
    // Customer Entity
    // ==========================================

    public static class Customer {
        private final String customerId;
        private final String name;
        private final String phone;

        public Customer(String customerId, String name, String phone) {
            if (customerId == null || customerId.trim().isEmpty()) {
                throw new IllegalArgumentException("Customer ID cannot be empty.");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Customer name cannot be empty.");
            }
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Phone cannot be empty.");
            }
            this.customerId = customerId;
            this.name = name;
            this.phone = phone;
        }

        public String getCustomerId() {
            return customerId;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        @Override
        public String toString() {
            return name + " (" + customerId + ", Phone: " + phone + ")";
        }
    }

    // ==========================================
    // Seat Hierarchy (Polymorphic Pricing)
    // ==========================================

    public static abstract class Seat {
        private final String seatNumber;
        private boolean isBooked;

        public Seat(String seatNumber) {
            if (seatNumber == null || seatNumber.trim().isEmpty()) {
                throw new IllegalArgumentException("Seat number cannot be empty.");
            }
            this.seatNumber = seatNumber;
            this.isBooked = false;
        }

        public String getSeatNumber() {
            return seatNumber;
        }

        public boolean isBooked() {
            return isBooked;
        }

        public void setBooked(boolean booked) {
            isBooked = booked;
        }

        public abstract String getCategory();

        // Polymorphic pricing abstraction
        public abstract double getPrice();

        @Override
        public String toString() {
            return "[" + getCategory() + "] Seat " + seatNumber + " (INR " + getPrice() + ", Booked: " + isBooked + ")";
        }
    }

    public static class RegularSeat extends Seat {
        public RegularSeat(String seatNumber) {
            super(seatNumber);
        }

        @Override
        public String getCategory() {
            return "Regular";
        }

        @Override
        public double getPrice() {
            return 150.0;
        }
    }

    public static class PremiumSeat extends Seat {
        public PremiumSeat(String seatNumber) {
            super(seatNumber);
        }

        @Override
        public String getCategory() {
            return "Premium";
        }

        @Override
        public double getPrice() {
            return 250.0;
        }
    }

    public static class ReclinerSeat extends Seat {
        public ReclinerSeat(String seatNumber) {
            super(seatNumber);
        }

        @Override
        public String getCategory() {
            return "Recliner";
        }

        @Override
        public double getPrice() {
            return 400.0;
        }
    }

    // ==========================================
    // Show & Booking Entities
    // ==========================================

    public static class Show {
        private final String showId;
        private final String movieTitle;
        private final LocalDateTime showTime;
        private final Map<String, Seat> seats = new HashMap<>();

        public Show(String showId, String movieTitle, LocalDateTime showTime) {
            if (showId == null || showId.trim().isEmpty()) {
                throw new IllegalArgumentException("Show ID cannot be empty.");
            }
            if (movieTitle == null || movieTitle.trim().isEmpty()) {
                throw new IllegalArgumentException("Movie title cannot be empty.");
            }
            if (showTime == null) {
                throw new IllegalArgumentException("Show time cannot be null.");
            }
            this.showId = showId;
            this.movieTitle = movieTitle;
            this.showTime = showTime;
        }

        public String getShowId() {
            return showId;
        }

        public String getMovieTitle() {
            return movieTitle;
        }

        public LocalDateTime getShowTime() {
            return showTime;
        }

        public void addSeat(Seat seat) {
            seats.put(seat.getSeatNumber(), seat);
        }

        public Seat getSeat(String seatNumber) {
            return seats.get(seatNumber);
        }

        public Map<String, Seat> getSeats() {
            return Collections.unmodifiableMap(seats);
        }

        @Override
        public String toString() {
            return movieTitle + " (Show ID: " + showId + ", Time: " + showTime + ")";
        }
    }

    public enum BookingStatus {
        CONFIRMED,
        CANCELLED
    }

    public static class Booking {
        private final String bookingId;
        private final Customer customer;
        private final Show show;
        private final List<Seat> bookedSeats;
        private final double totalAmount;
        private BookingStatus status;

        public Booking(String bookingId, Customer customer, Show show, List<Seat> bookedSeats) {
            this.bookingId = bookingId;
            this.customer = customer;
            this.show = show;
            this.bookedSeats = new ArrayList<>(bookedSeats);

            double sum = 0.0;
            for (Seat s : bookedSeats) {
                sum += s.getPrice();
            }
            this.totalAmount = sum;
            this.status = BookingStatus.CONFIRMED;
        }

        public String getBookingId() {
            return bookingId;
        }

        public Customer getCustomer() {
            return customer;
        }

        public Show getShow() {
            return show;
        }

        public List<Seat> getBookedSeats() {
            return Collections.unmodifiableList(bookedSeats);
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public BookingStatus getStatus() {
            return status;
        }

        public boolean cancel(LocalDateTime cancelTime) {
            if (status == BookingStatus.CANCELLED) {
                System.out.println("[WARNING] Booking " + bookingId + " is already cancelled.");
                return false;
            }

            if (!cancelTime.isBefore(show.getShowTime())) {
                System.out.println("[REJECTED] Cancellation denied for booking " + bookingId +
                        ". Show started at " + show.getShowTime() + ", cancellation requested at " + cancelTime);
                return false;
            }

            for (Seat seat : bookedSeats) {
                seat.setBooked(false);
            }
            this.status = BookingStatus.CANCELLED;
            System.out.println("[CANCELLED] Booking " + bookingId + " cancelled. All " +
                    bookedSeats.size() + " seats have been freed.");
            return true;
        }

        @Override
        public String toString() {
            return "Booking #" + bookingId + " | " + customer.getName() + " | Movie: " + show.getMovieTitle() +
                    " | Seats: " + bookedSeats.size() + " | Total: INR " + totalAmount + " | Status: " + status;
        }
    }

    // ==========================================
    // System State & Operations
    // ==========================================

    private static final int MAX_SEATS_PER_BOOKING = 6;
    private final Map<String, Booking> bookings = new HashMap<>();

    public Booking bookTickets(String bookingId, Customer customer, Show show, List<String> seatNumbers) {
        if (seatNumbers == null || seatNumbers.isEmpty()) {
            System.out.println("[ERROR] No seats selected for booking.");
            return null;
        }

        if (seatNumbers.size() > MAX_SEATS_PER_BOOKING) {
            System.out.println("[LIMIT EXCEEDED] Cannot book " + seatNumbers.size() +
                    " seats. Maximum allowed seats per booking is " + MAX_SEATS_PER_BOOKING + ".");
            return null;
        }

        List<Seat> selectedSeats = new ArrayList<>();
        for (String seatNum : seatNumbers) {
            Seat seat = show.getSeat(seatNum);
            if (seat == null) {
                System.out.println("[ERROR] Seat " + seatNum + " does not exist in show " + show.getMovieTitle());
                return null;
            }
            if (seat.isBooked()) {
                System.out.println("[DOUBLE-BOOKING PREVENTED] Seat " + seatNum +
                        " is already booked! Entire booking request rejected.");
                return null;
            }
            selectedSeats.add(seat);
        }

        for (Seat seat : selectedSeats) {
            seat.setBooked(true);
        }

        Booking booking = new Booking(bookingId, customer, show, selectedSeats);
        bookings.put(bookingId, booking);
        System.out.println("[BOOKED] Successfully booked " + selectedSeats.size() +
                " seats for " + customer.getName() + ". Total Amount: INR " + booking.getTotalAmount());
        return booking;
    }

    public boolean cancelBooking(String bookingId, LocalDateTime cancelTime) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            System.out.println("[ERROR] Booking ID " + bookingId + " not found.");
            return false;
        }
        return booking.cancel(cancelTime);
    }

    public static void main(String[] args) {
        System.out.println("=== Campus Premiere Ticket Counter Demonstration ===");

        CampusPremiereTicketCounter counter = new CampusPremiereTicketCounter();

        // 1. Setup Show with Seats
        LocalDateTime showTime = LocalDateTime.of(2026, 10, 10, 18, 30);
        Show movieShow = new Show("SHW-01", "Interstellar Re-Release", showTime);

        movieShow.addSeat(new RegularSeat("A1"));
        movieShow.addSeat(new RegularSeat("A2"));
        movieShow.addSeat(new PremiumSeat("B1"));
        movieShow.addSeat(new PremiumSeat("B2"));
        movieShow.addSeat(new ReclinerSeat("C1"));
        movieShow.addSeat(new ReclinerSeat("C2"));

        Customer c1 = new Customer("C-1", "Varun Dhawan", "9988776655");
        Customer c2 = new Customer("C-2", "Alia Bhatt", "9876501234");

        // 2. Successful Booking: 1 Regular (150) + 1 Premium (250) + 1 Recliner (400) = 800
        System.out.println("\n--- Step 1: Customer 1 books 3 seats (A1, B1, C1) ---");
        Booking b1 = counter.bookTickets("BK-101", c1, movieShow, List.of("A1", "B1", "C1"));
        System.out.println(b1);

        // 3. Attempt to book > 6 seats
        System.out.println("\n--- Step 2: Attempting to book 7 seats (Exceeds Max Limit of 6) ---");
        counter.bookTickets("BK-102", c2, movieShow, List.of("A1", "A2", "B1", "B2", "C1", "C2", "D1"));

        // 4. Double-booking prevention
        System.out.println("\n--- Step 3: Customer 2 attempts to double-book Seat B1 ---");
        counter.bookTickets("BK-103", c2, movieShow, List.of("A2", "B1"));

        // 5. Successful cancellation before show starts
        System.out.println("\n--- Step 4: Customer 1 cancels booking before show starts (Oct 10 at 14:00) ---");
        counter.cancelBooking("BK-101", LocalDateTime.of(2026, 10, 10, 14, 0));

        // 6. Now Customer 2 can book the released seats
        System.out.println("\n--- Step 5: Customer 2 books previously freed seats (A1, B1, C1) ---");
        Booking b2 = counter.bookTickets("BK-104", c2, movieShow, List.of("A1", "B1", "C1"));
        System.out.println(b2);

        // 7. Attempting cancellation after show has started
        System.out.println("\n--- Step 6: Customer 2 attempts cancellation after show start time ---");
        counter.cancelBooking("BK-104", LocalDateTime.of(2026, 10, 10, 19, 0));
    }
}
