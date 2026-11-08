import java.util.*;

/**
 * Mission 5. Dynamic programming on the tree of infections. Root the tree in person 0.
 */
public class Outbreak {

    /**
     * Choose people to test so that for every link at least one of the two is tested.
     */
    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        int[] take = new int[n];
        int[] skip = new int[n];
        boolean[] tested = new boolean[n];

        // Шаг 1: Считаем DP снизу вверх (от листьев к корню 0)
        dfsDP(0, -1, nb, cost, take, skip);

        // Шаг 2: Восстанавливаем ответ сверху вниз (кто именно будет тестироваться)
        dfsRebuild(0, -1, nb, take, skip, false, tested);

        return tested;
    }

    private static void dfsDP(int u, int p, int[][] nb, int[] cost, int[] take, int[] skip) {
        take[u] = cost[u];
        skip[u] = 0;

        for (int v : nb[u]) {
            if (v != p) {
                dfsDP(v, u, nb, cost, take, skip);
                // Если u тестируем, ребенка v можно как тестировать, так и пропустить (берем что дешевле)
                take[u] += Math.min(take[v], skip[v]);
                // Если u пропускаем, всех детей v ОБЯЗАТЕЛЬНО нужно тестировать, чтобы покрыть связь
                skip[u] += take[v];
            }
        }
    }

    private static void dfsRebuild(int u, int p, int[][] nb, int[] take, int[] skip, boolean parentSkipped, boolean[] tested) {
        if (parentSkipped) {
            // Если родитель не тестировался, связь надо покрыть, поэтому u тестируем 100%
            tested[u] = true;
        } else {
            // Иначе выбираем самый дешевый вариант
            tested[u] = take[u] <= skip[u];
        }

        for (int v : nb[u]) {
            if (v != p) {
                dfsRebuild(v, u, nb, take, skip, !tested[u], tested);
            }
        }
    }

    /**
     * The number of links on the longest chain of infections.
     */
    public static int longestChain(int n, int[][] nb) {
        int[] maxChain = new int[1]; // Используем массив из 1 элемента как ссылку на глобальный максимум
        dfsChain(0, -1, nb, maxChain);
        return maxChain[0];
    }

    private static int dfsChain(int u, int p, int[][] nb, int[] maxChain) {
        int max1 = 0; // Самая длинная ветвь вниз
        int max2 = 0; // Вторая по длине ветвь вниз

        for (int v : nb[u]) {
            if (v != p) {
                int h = dfsChain(v, u, nb, maxChain) + 1; // +1 за саму связь до ребенка
                if (h > max1) {
                    max2 = max1;
                    max1 = h;
                } else if (h > max2) {
                    max2 = h;
                }
            }
        }

        // Обновляем глобальный максимум, складывая две самые длинные ветви, проходящие через узел u
        maxChain[0] = Math.max(maxChain[0], max1 + max2);

        return max1;
    }
}