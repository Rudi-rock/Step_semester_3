package constructs.assigment_problems;

public class ReferenceDeskSubclassReach {
    
    static String classifyAccess(String fieldModifier, String accessorContext) {
        switch (fieldModifier) {
            case "private":
                return "SAME_CLASS".equals(accessorContext) ? "ALLOWED" : "DENIED";
            case "default":
                return ("SAME_CLASS".equals(accessorContext) || "SAME_PACKAGE".equals(accessorContext)) ? "ALLOWED" : "DENIED";
            case "protected":
                if ("SAME_CLASS".equals(accessorContext) || "SAME_PACKAGE".equals(accessorContext) || "SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE".equals(accessorContext)) {
                    return "ALLOWED";
                }
                return "DENIED";
            case "public":
                return "ALLOWED";
            default:
                return "DENIED";
        }
    }

    static String firstDeniedAttempt(String[][] attempts) {
        for (int i = 0; i < attempts.length; i++) {
            String modifier = attempts[i][0];
            String context = attempts[i][1];
            if ("DENIED".equals(classifyAccess(modifier, context))) {
                return modifier + " via " + context + " (attempt #" + (i + 1) + ")";
            }
        }
        return "None Denied";
    }

    public static void main(String[] args) {
        String[][] attempts1 = {
            {"public", "DIFFERENT_PACKAGE"},
            {"protected", "SUBCLASS_DIFFERENT_PACKAGE_PARENT_TYPE"}
        };
        System.out.println(firstDeniedAttempt(attempts1));
    }
}
