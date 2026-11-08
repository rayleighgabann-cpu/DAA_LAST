/**
 * Missions 1 and 2. Compare two strains with dynamic programming.
 */
public class Strains {

    /**
     * Mission 1. The smallest number of single-letter changes that turn a into b.
     */
    public static int editDistance(String a, String b) {
        int n = a.length();
        int m = b.length();
        int[][] d = new int[n + 1][m + 1];

        // Базовые случаи: превращение пустой строки в строку длиной j (только вставки)
        // и строки длиной i в пустую (только удаления)
        for (int i = 0; i <= n; i++) {
            d[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            d[0][j] = j;
        }

        // Заполняем таблицу
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;

                // Выбираем минимум из трех операций: удаление, вставка, замена/совпадение
                d[i][j] = Math.min(
                        Math.min(d[i - 1][j] + 1,       // Удаление символа из a
                                d[i][j - 1] + 1),      // Вставка символа из b
                        d[i - 1][j - 1] + cost          // Замена (если символы разные) или ничего (если одинаковые)
                );
            }
        }

        return d[n][m];
    }

    /**
     * Mission 2. The length of the longest common subsequence.
     */
    public static int lcsLength(String a, String b) {
        int n = a.length();
        int m = b.length();
        int[][] l = new int[n + 1][m + 1];

        // В Java массивы int по умолчанию заполнены нулями,
        // поэтому первую строку и первый столбец явно нулями можно не заполнять.
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    // Символы совпали, добавляем +1 к результату для строк без этих символов
                    l[i][j] = l[i - 1][j - 1] + 1;
                } else {
                    // Символы разные, берем максимум из двух вариантов (пропускаем символ из a или из b)
                    l[i][j] = Math.max(l[i - 1][j], l[i][j - 1]);
                }
            }
        }

        return l[n][m];
    }
}