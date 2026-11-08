import java.util.*;

/**
 * Missions 3 and 4. Find a word in a long text.
 */
public class Marker {

    /**
     * Mission 3a. The prefix function of p.
     */
    public static int[] prefixFunction(String p) {
        int[] pi = new int[p.length()];
        for (int i = 1; i < p.length(); i++) {
            int j = pi[i - 1];
            // Ищем предыдущий подходящий префикс
            while (j > 0 && p.charAt(i) != p.charAt(j)) {
                j = pi[j - 1];
            }
            // Если символы совпали, увеличиваем длину префикса
            if (p.charAt(i) == p.charAt(j)) {
                j++;
            }
            pi[i] = j;
        }
        return pi;
    }

    /**
     * Mission 3b. KMP. All start positions (counting from 0, in increasing order) where p occurs in text.
     */
    public static int[] findAll(String text, String p) {
        if (p.length() == 0) return new int[0];

        int[] pi = prefixFunction(p);
        List<Integer> result = new ArrayList<>();
        int j = 0;

        for (int i = 0; i < text.length(); i++) {
            // Указатель текста i всегда идет только вперед
            while (j > 0 && text.charAt(i) != p.charAt(j)) {
                j = pi[j - 1];
            }
            if (text.charAt(i) == p.charAt(j)) {
                j++;
            }
            // Если нашли полное совпадение
            if (j == p.length()) {
                result.add(i - p.length() + 1);
                j = pi[j - 1]; // Сдвигаемся для поиска перекрывающихся вхождений
            }
        }

        // Преобразуем List в массив int[]
        int[] resArray = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            resArray[i] = result.get(i);
        }
        return resArray;
    }

    /**
     * Mission 4. Rabin-Karp.
     */
    public static int sharedWords(String a, String b, int k) {
        if (a.length() < k || b.length() < k) return 0;

        long BASE = 131;
        long MOD = 1_000_000_007;

        // Считаем множитель для самого старшего символа окна: (BASE ^ (k-1)) % MOD
        long top = 1;
        for (int i = 1; i < k; i++) {
            top = (top * BASE) % MOD;
        }

        // 1. Хэшируем все окна длины k из строки b и запоминаем их индексы
        Map<Long, List<Integer>> bHashes = new HashMap<>();
        long hB = 0;
        for (int i = 0; i < k; i++) {
            hB = (hB * BASE + b.charAt(i)) % MOD;
        }
        bHashes.computeIfAbsent(hB, x -> new ArrayList<>()).add(0);

        for (int i = 1; i <= b.length() - k; i++) {
            // Удаляем старый символ и добавляем новый (Rolling Hash за O(1))
            hB = (hB - (b.charAt(i - 1) * top) % MOD + MOD) % MOD;
            hB = (hB * BASE + b.charAt(i + k - 1)) % MOD;
            bHashes.computeIfAbsent(hB, x -> new ArrayList<>()).add(i);
        }

        // 2. Идем окном по строке a и проверяем совпадения
        int count = 0;
        long hA = 0;

        for (int i = 0; i < k; i++) {
            hA = (hA * BASE + a.charAt(i)) % MOD;
        }
        if (checkMatch(a, 0, b, bHashes.get(hA), k)) {
            count++;
        }

        for (int i = 1; i <= a.length() - k; i++) {
            hA = (hA - (a.charAt(i - 1) * top) % MOD + MOD) % MOD;
            hA = (hA * BASE + a.charAt(i + k - 1)) % MOD;

            // Проверяем честно буквы только если совпали хэши
            if (checkMatch(a, i, b, bHashes.get(hA), k)) {
                count++;
            }
        }

        return count;
    }

    // Вспомогательный метод: если хэши равны, обязательно проверяем символы вручную (чтобы избежать ложных совпадений)
    private static boolean checkMatch(String a, int i, String b, List<Integer> bIndices, int k) {
        if (bIndices == null) return false;
        for (int j : bIndices) {
            boolean match = true;
            for (int m = 0; m < k; m++) {
                if (a.charAt(i + m) != b.charAt(j + m)) {
                    match = false;
                    break;
                }
            }
            if (match) return true;
        }
        return false;
    }
}