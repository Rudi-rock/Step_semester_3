package constructs.class_problems;

class CineScreen {
    private int seatsTotal;
    private int seatsAvailable;

    public CineScreen(int seatsTotal) {
        if (seatsTotal <= 0) {
            System.out.println("Rejected: seatsTotal must be positive.");
            return;
        }
        this.seatsTotal = seatsTotal;
        this.seatsAvailable = seatsTotal;
    }

    public void bookSeat() {
        if (seatsAvailable > 0) seatsAvailable--;
    }

    public void cancelBooking() {
        if (seatsAvailable < seatsTotal) seatsAvailable++;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }
}

public class SeatBookingEncapsulationGuard {
    public static void main(String[] args) {
        CineScreen screen = new CineScreen(3);
        screen.bookSeat();
        screen.bookSeat();
        screen.bookSeat();
        screen.bookSeat();
        System.out.println(screen.getSeatsAvailable());
        
        screen.cancelBooking();
        screen.cancelBooking();
        screen.cancelBooking();
        screen.cancelBooking();
        System.out.println(screen.getSeatsAvailable());
        
        new CineScreen(0);
    }
}
