# Assignment 4. Patient Zero

Student ID: 251071
Name: Shynarbek Nuraziz
Group: Se2536

## Program output

The 9 lines your program prints on your data (`java -cp out Main`):

    Mutations: 102
    Shared core: 959
    Marker hits: 8
    First hit: 2698
    Border length: 3
    Shared words: 263
    Tests needed: 28
    Cheapest testing: 120
    Longest chain: 23

My outbreak report (a screenshot of `output/outbreak.html`):

![Screenshot from 2026-11-08 19-32-17.png](output/Screenshot%20from%202026-11-08%2019-32-17.png)

## Questions

Answer in your own words, two or three sentences each.

**Mission 1. What does the cell d[i][j] of your table mean, and how is it computed from other cells?**
The cell `d[i][j]` represents the minimum number of edits required to transform the first `i` characters of strain A into the first `j` characters of strain B. It is computed by taking the minimum of three previous states: `d[i-1][j] + 1` (deletion), `d[i][j-1] + 1` (insertion), and `d[i-1][j-1] + cost` (substitution or match, where cost is 0 if characters match and 1 otherwise).

**Mission 2. How is the longest common subsequence different from the longest common substring? My Shared core is bigger than the number of letters that are the same at the same place: why?**
A common substring requires characters to be strictly consecutive, while a common subsequence allows skipping characters as long as their relative order is maintained. The "Shared core" is larger because it effectively ignores mutations (insertions and deletions) that shift the sequence, finding matching letters even if they are located at different indices in the two strains.

**Mission 3. What does the prefix function tell you, and why does the pointer in the text never go back in KMP? What is the Border length of my marker and what does it mean?**
The prefix function tells us the length of the longest proper prefix of a pattern that is also a suffix, which allows us to skip redundant comparisons. The text pointer never goes back in KMP because upon a mismatch, the prefix table indicates exactly how much of the current match can be safely reused. The "Border length" is the value of the prefix function for the entire marker, representing the longest prefix of the marker that matches its own suffix.

**Mission 4. Why must you compare the letters after the hashes are equal? What happens to the cost of Rabin-Karp if many windows have the same hash?**
We must compare the actual letters because different words can produce the exact same hash value (a hash collision). If many windows have the same hash (e.g., due to a small modulo), the algorithm frequently falls back to character-by-character comparison, degrading its time complexity from $O(1)$ per step to $O(k)$, making it significantly slower.

**Mission 5. What are the two states of a person in your DP, and how do you get the answer for a person from its children? Why does a greedy "test the person with most links first" not always give the cheapest answer?**
The two states are `take` (the person is tested) and `skip` (the person is not tested). If a person is tested, we add the minimum of `take` and `skip` costs from each child; if skipped, we must cover the links by adding the `take` cost of all children. A greedy approach fails because a person with many links might have an extremely high testing cost, making it much cheaper to test all their individual neighbors instead.

## AI use


I used Google Gemini to understand the underlying concepts of dynamic programming (Edit Distance, LCS, DP on a tree) and string algorithms (KMP, Rabin-Karp). I also used it to review my Java implementation, fix logical bugs, and help translate my understanding of the algorithms into clear English answers for this report.

## Timing (Mission 7)

Milliseconds, average of 3 runs, from `java -cp out Bench`.

| test | size | time |
|---|---|---|
| naive search | 20 000 / 40 000 / 80 000 |49.2 / 116.6 / 232.5 |
| KMP | 20 000 / 40 000 / 80 000 |  5.8 / 2.2 / 0.5 |
| edit distance | 500 / 1000 / 2000 / 4000 | 10.2 / 10.5 / 24.3 / 94.6 |

**What do the numbers say about how the time grows? When you double the size, by how much does each time grow? Two or three sentences.**
When the text size doubles, the naive search time and edit distance time grow by roughly 4 times, which perfectly demonstrates their $O(n^2)$ time complexity. On the other hand, the KMP execution time grows by roughly 2 times when the size doubles, proving its linear $O(n)$ time complexity.

## Complexity

n is the length of the text (or of a strain), m the length of the pattern, k the length of the window, v the number of people. Fill in every algorithm you wrote.

| algorithm | time | space | assumptions |
|---|---|---|---|
| Edit distance | $O(n \cdot m)$ | $O(n \cdot m)$ | Assuming lengths of strain A and B are n and m respectively. |
| LCS | $O(n \cdot m)$ | $O(n \cdot m)$ | Assuming lengths of strain A and B are n and m respectively. |
| Prefix function and KMP | $O(n + m)$ | $O(m)$ | $O(m)$ time to build prefix array, $O(n)$ time to search text. |
| Rabin-Karp (shared words) | $O(n + m)$ | $O(n)$ | Average time assuming few collisions. Space $O(n)$ is used for storing B's hashes. |
| Tree DP | $O(v)$ | $O(v)$ | Time to traverse all vertices and edges (which is v-1 in a tree). Space is for call stack and DP arrays. |

