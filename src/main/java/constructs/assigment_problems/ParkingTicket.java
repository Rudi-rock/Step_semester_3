package constructs.assigment_problems;

public class ParkingTicket {
    String ticketId;
    double fineRatePerMinute;

    public ParkingTicket(String ticketId, double fineRatePerMinute) {
        this.ticketId = ticketId;
        this.fineRatePerMinute = fineRatePerMinute;
    }

    public ParkingTicket(String ticketId) {
        this(ticketId, 2.0);
    }

    public final double calculateFine(int overstayMinutes) {
        return overstayMinutes * fineRatePerMinute;
    }

    public final void printReceipt(int overstayMinutes) {
        if (overstayMinutes <= 0) {
            System.out.println(ticketId + " - Within limit, no fine");
        } else {
            System.out.println(ticketId + " | Overstay: " + overstayMinutes + " mins | Fine: Rs " + calculateFine(overstayMinutes));
        }
    }

    public static void main(String[] args) {
        String[] ticketIds = {"PK-101", "PK-102", "PK-103", "PK-104"};
        int[] overstayMinutes = {30, 0, -10, 45};

        for (int i = 0; i < ticketIds.length; i++) {
            ParkingTicket ticket = new ParkingTicket(ticketIds[i]);
            ticket.printReceipt(overstayMinutes[i]);
        }
    }
}
