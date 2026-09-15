// LeetCode 1768. Merge Strings Alternately (Easy) - strings

// zip stops at the shorter string, so it already does the interleaving for me.
// Whatever is left over belongs to only one of the words, and drop() returns an
// empty string if you ask for more than there is, so I just append both tails.

fun mergeAlternately(word1: String, word2: String): String =
    word1.zip(word2) { a, b -> "$a$b" }.joinToString("") +
        word1.drop(word2.length) +
        word2.drop(word1.length)
