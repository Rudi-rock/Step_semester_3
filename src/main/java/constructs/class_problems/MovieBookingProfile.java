package constructs.class_problems;

public class MovieBookingProfile {
    private String name;
    private boolean confirmed;
    private String otp;

    public MovieBookingProfile() {
    }

    public MovieBookingProfile(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public void setOtp(String otp) {
        if (otp != null && otp.length() >= 4 && otp.length() <= 6 && otp.matches("\\d+")) {
            this.otp = Integer.toString(otp.hashCode());
        }
    }

    public static void main(String[] args) {
        MovieBookingProfile p = new MovieBookingProfile("Test");
        p.setConfirmed(true);
        p.setOtp("12345");
        System.out.println(p.getName());
        System.out.println(p.isConfirmed());
    }
}
