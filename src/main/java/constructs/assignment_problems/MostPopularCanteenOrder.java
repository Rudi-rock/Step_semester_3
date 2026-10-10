package constructs.assignment_problems;

import java.util.HashMap;

public class MostPopularCanteenOrder {

    public static String[] findMostPopular(String[] orders) {

        HashMap<String, Integer> freqMap = new HashMap<>();
        for (String order : orders) {
            freqMap.put(order, freqMap.getOrDefault(order, 0) + 1);
        }

        String bestItem = orders[0];
        int bestFreq = freqMap.get(orders[0]);

        for (String order : orders) {
            int freq = freqMap.get(order);
            if (freq > bestFreq) {
                bestFreq = freq;
                bestItem = order;
            }
        }

        return new String[]{bestItem, String.valueOf(bestFreq)};
    }

    public static void main(String[] args) {

        String[] orders1 = {
            "dosa", "idli", "vada", "dosa",
            "idli", "dosa", "tea"
        };
        String[] result1 = findMostPopular(orders1);
        System.out.println("Most popular: (\"" + result1[0] + "\", " + result1[1] + ")");

        String[] orders2 = {"tea", "coffee", "coffee", "tea"};
        String[] result2 = findMostPopular(orders2);
        System.out.println("Most popular: (\"" + result2[0] + "\", " + result2[1] + ")");

    }
}
