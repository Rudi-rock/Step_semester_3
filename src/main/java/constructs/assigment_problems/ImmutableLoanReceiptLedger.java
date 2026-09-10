package constructs.assigment_problems;

final class LoanReceipt {
    private final String memberId;
    private final String[] bookIds;

    public LoanReceipt(String memberId, String[] bookIds) {
        this.memberId = memberId;
        this.bookIds = new String[bookIds.length];
        System.arraycopy(bookIds, 0, this.bookIds, 0, bookIds.length);
    }

    public String[] getBookIds() {
        String[] copy = new String[this.bookIds.length];
        System.arraycopy(this.bookIds, 0, copy, 0, this.bookIds.length);
        return copy;
    }

    public LoanReceipt withCorrectedBookId(int index, String newId) {
        String[] currentIds = getBookIds();
        if (index >= 0 && index < currentIds.length) {
            currentIds[index] = newId;
        }
        return new LoanReceipt(this.memberId, currentIds);
    }
}

class ReferenceOnlyLoanReceipt {
    private String memberId;
    private String[] bookIds;
    private String roomNumber;

    public ReferenceOnlyLoanReceipt(String memberId, String[] bookIds, String roomNumber) {
        this.memberId = memberId;
        this.bookIds = new String[bookIds.length];
        System.arraycopy(bookIds, 0, this.bookIds, 0, bookIds.length);
        this.roomNumber = roomNumber;
    }
}

public class ImmutableLoanReceiptLedger {

    static String processNightlyCirculation(Object[] receipts) {
        int processed = 0;
        int nullSkipped = 0;
        int referenceOnly = 0;
        int regular = 0;

        for (Object receipt : receipts) {
            if (receipt == null) {
                nullSkipped++;
            } else if (receipt instanceof ReferenceOnlyLoanReceipt) {
                referenceOnly++;
                processed++;
            } else if (receipt instanceof LoanReceipt) {
                regular++;
                processed++;
            }
        }

        return processed + " processed | " + nullSkipped + " null skipped | " + referenceOnly + " reference-only | " + regular + " regular";
    }

    public static void main(String[] args) {
        Object[] receipts = {
            new ReferenceOnlyLoanReceipt("LIB-001", new String[]{"BK-200"}, "Reading Room 3"),
            null,
            new LoanReceipt("LIB-002", new String[]{"BK-201"})
        };
        System.out.println(processNightlyCirculation(receipts));

        LoanReceipt r = new LoanReceipt("LIB-8841", new String[]{"BK-100", "BK-101"});
        String[] ids = r.getBookIds();
        ids[0] = "HACKED";
        System.out.println(r.getBookIds()[0]);

        LoanReceipt corrected = r.withCorrectedBookId(1, "BK-102");
        System.out.println(r.getBookIds()[0]);
        System.out.println(r.getBookIds()[1]);
        System.out.println(corrected.getBookIds()[0]);
        System.out.println(corrected.getBookIds()[1]);
    }
}
