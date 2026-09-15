/**
 * LeetCode 1768. Merge Strings Alternately (Easy)
 * Topic: strings / two pointers
 *
 * Approach: `zip` already walks two sequences in lockstep and stops at the shorter one, so the
 * interleaved prefix is just a zip that glues each pair of characters together. Whatever is left
 * over belongs to exactly one of the two words, and `drop` returns an empty string when the count
 * exceeds the length, so concatenating both tails appends the surplus without a single branch.
 */
fun mergeAlternately(word1: String, word2: String): String =
    word1.zip(word2) { a, b -> "$a$b" }.joinToString(separator = "") +
        word1.drop(word2.length) +
        word2.drop(word1.length)
