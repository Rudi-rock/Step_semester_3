package constructs.class_problems;

final class BookingReceipt {
    private final String bookingId;
    private final String[] seatNumbers;

    public BookingReceipt(String bookingId, String[] seatNumbers) {
        this.bookingId = bookingId;
        this.seatNumbers = new String[seatNumbers.length];
        System.arraycopy(seatNumbers, 0, this.seatNumbers, 0, seatNumbers.length);
    }

    public String[] getSeatNumbers() {
        String[] copy = new String[this.seatNumbers.length];
        System.arraycopy(this.seatNumbers, 0, copy, 0, this.seatNumbers.length);
        return copy;
    }

    public BookingReceipt withUpdatedSeat(int index, String newSeat) {
        String[] currentSeats = getSeatNumbers();
        if (index >= 0 && index < currentSeats.length) {
            currentSeats[index] = newSeat;
        }
        return new BookingReceipt(this.bookingId, currentSeats);
    }
}

class GroupBookingReceipt {
    private String bookingId;
    private String[] seatNumbers;
    
    public GroupBookingReceipt(String bookingId, String[] seatNumbers) {
        this.bookingId = bookingId;
        this.seatNumbers = new String[seatNumbers.length];
        System.arraycopy(seatNumbers, 0, this.seatNumbers, 0, seatNumbers.length);
    }
}

public class ImmutableBookingReceiptNightlySettlement {
    static String processNightlySettlement(Object[] receipts) {
        int processed = 0;
        int nullSkipped = 0;
        int group = 0;
        int individual = 0;

        for (Object receipt : receipts) {
            if (receipt == null) {
                nullSkipped++;
            } else if (receipt instanceof GroupBookingReceipt) {
                group++;
                processed++;
            } else if (receipt instanceof BookingReceipt) {
                individual++;
                processed++;
            }
        }

        return processed + " processed | " + nullSkipped + " null skipped | " + group + " group | " + individual + " individual";
    }

    public static void main(String[] args) {
        Object[] receipts = {
            new GroupBookingReceipt("B-001", new String[]{"A1", "A2"}),
            null,
            new BookingReceipt("B-002", new String[]{"B1"})
        };
        System.out.println(processNightlySettlement(receipts));

        BookingReceipt r = new BookingReceipt("B-003", new String[]{"C1", "C2"});
        r.getSeatNumbers()[0] = "HACKED";
        System.out.println(r.getSeatNumbers()[0]);
        
        BookingReceipt updated = r.withUpdatedSeat(1, "C3");
        System.out.println(r.getSeatNumbers()[1]);
        System.out.println(updated.getSeatNumbers()[1]);
    }
}
