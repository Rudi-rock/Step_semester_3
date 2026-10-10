package constructs.class_problems;

public class LibraryCatalogLookup {

    public static String findBook(String[][] catalog, String targetIsbn) {
        int low = 0;
        int high = catalog.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int cmp = catalog[mid][0].compareTo(targetIsbn);

            if (cmp == 0) {
                return catalog[mid][1];
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return "Not Found";
    }

    public static void main(String[] args) {
        String[][] catalog = {
            {"0001112223", "Introduction to Algorithms"},
            {"0003334445", "Classic Mythology"},
            {"0005556667", "Data Structures Handbook"},
            {"0007778889", "Modern Operating Systems"},
            {"0012345678", "Design Patterns"}
        };

        String result1 = findBook(catalog, "0003334445");
        System.out.println("Lookup \"0003334445\" -> " + result1);

        String result2 = findBook(catalog, "0009998887");
        System.out.println("Lookup \"0009998887\" -> " + result2);

        String result3 = findBook(catalog, "0001112223");
        System.out.println("Lookup \"0001112223\" -> " + result3);

        String result4 = findBook(catalog, "0012345678");
        System.out.println("Lookup \"0012345678\" -> " + result4);

    }
}
