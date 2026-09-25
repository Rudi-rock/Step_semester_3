package constructs.assignment_problems;

abstract class ClassroomDevice {
    public abstract String operate();
}

interface Chargeable {
    String charge();

    String charge(int minutes);
}

class Tablet extends ClassroomDevice implements Chargeable {
    private String assetTag;

    public Tablet(String assetTag) {
        super();
        this.assetTag = assetTag;
    }

    @Override
    public String operate() {
        return "Tablet " + assetTag + " displaying lesson";
    }

    @Override
    public String charge() {
        return assetTag + " charging";
    }

    @Override
    public String charge(int minutes) {
        return assetTag + " charging for " + minutes + " minutes";
    }
}

public class DigitalClassroomSetup {
}
