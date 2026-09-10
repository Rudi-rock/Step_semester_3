package constructs.assigment_problems;

public class LibraryMemberJavaBean {
    
    static class LibraryMember {
        private String membershipId;
        private String name;
        private boolean premiumMember;
        private String securityAnswer;

        public LibraryMember() {
        }

        public String getMembershipId() {
            return this.membershipId;
        }

        public void setMembershipId(String membershipId) {
            if (this.membershipId == null) {
                this.membershipId = membershipId;
            }
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isPremiumMember() {
            return this.premiumMember;
        }

        public void setPremiumMember(boolean premiumMember) {
            this.premiumMember = premiumMember;
        }

        public void setSecurityAnswer(String securityAnswer) {
            if (securityAnswer != null) {
                this.securityAnswer = Integer.toString(securityAnswer.hashCode());
            }
        }
    }

    public static void main(String[] args) {
        LibraryMember m = new LibraryMember();
        m.setMembershipId("LIB-8841");
        m.setName("Priya Nair");
        m.setPremiumMember(true);

        System.out.println(m.getMembershipId());
        m.setMembershipId("FAKE-0000");
        System.out.println(m.getMembershipId());
        System.out.println(m.isPremiumMember());
        
        m.setSecurityAnswer("BlueMountain");
    }
}
