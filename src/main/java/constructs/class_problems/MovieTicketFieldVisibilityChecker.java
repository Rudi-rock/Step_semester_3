package constructs.class_problems;

class MovieTicket {
    private String screenId;
    int seatNumber; // default
    protected double ticketPrice;
    public String movieTitle;
}

public class MovieTicketFieldVisibilityChecker {
    static String classifyAccess(String fieldModifier, String accessorContext) {
        switch (fieldModifier) {
            case "private": return "SAME_CLASS".equals(accessorContext) ? "ALLOWED" : "DENIED";
            case "default": return ("SAME_CLASS".equals(accessorContext) || "SAME_PACKAGE".equals(accessorContext)) ? "ALLOWED" : "DENIED";
            case "protected": return ("SAME_CLASS".equals(accessorContext) || "SAME_PACKAGE".equals(accessorContext)) ? "ALLOWED" : "DENIED";
            case "public": return "ALLOWED";
            default: return "DENIED";
        }
    }
    
    static String summarizeBatch(String[][] attempts) {
        int[] allowed = new int[4];
        int[] denied = new int[4];
        String[] mods = {"private", "default", "protected", "public"};
        
        for (String[] attempt : attempts) {
            String mod = attempt[0];
            String ctx = attempt[1];
            String res = classifyAccess(mod, ctx);
            int idx = -1;
            for (int i=0; i<4; i++) if(mods[i].equals(mod)) idx = i;
            if (idx >= 0) {
                if ("ALLOWED".equals(res)) allowed[idx]++;
                else denied[idx]++;
            }
        }
        return String.format("private: %d allowed / %d denied | default: %d allowed / %d denied | protected: %d allowed / %d denied | public: %d allowed / %d denied",
            allowed[0], denied[0], allowed[1], denied[1], allowed[2], denied[2], allowed[3], denied[3]);
    }

    public static void main(String[] args) {
        String[][] attempts = {
            {"private","SAME_CLASS"}, {"private","SAME_PACKAGE"},
            {"default","SAME_PACKAGE"}, {"default","DIFFERENT_PACKAGE"},
            {"protected","SAME_PACKAGE"}, {"protected","SAME_CLASS"},
            {"public","DIFFERENT_PACKAGE"}
        };
        System.out.println(summarizeBatch(attempts));
    }
}
