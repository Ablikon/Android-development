/**
 * LeetCode 1207. Unique Number of Occurrences (Easy)
 * Topic: hash maps / frequency counting
 *
 * Approach: `groupingBy { it }.eachCount()` builds the value-to-frequency map in one pass without
 * materialising the intermediate groups, which is what makes it cheaper than `groupBy`. The
 * question then reduces to asking whether those frequencies are pairwise distinct, and a set
 * collapses duplicates, so the counts are unique precisely when converting them to a set loses
 * nothing.
 */
fun uniqueOccurrences(arr: IntArray): Boolean =
    arr.toList()
        .groupingBy { it }
        .eachCount()
        .values
        .let { counts -> counts.size == counts.toSet().size }
