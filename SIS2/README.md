# SIS2 - Kotlin problems

Five Easy problems from LeetCode, one file per problem, solved with Kotlin collection
operators instead of hand-written loops.

| # | Problem | Topic |
| --- | --- | --- |
| 1768 | [Merge Strings Alternately](src/main/kotlin/P1768MergeStringsAlternately.kt) | strings |
| 20 | [Valid Parentheses](src/main/kotlin/P20ValidParentheses.kt) | stack |
| 1207 | [Unique Number of Occurrences](src/main/kotlin/P1207UniqueNumberOfOccurrences.kt) | hash map |
| 136 | [Single Number](src/main/kotlin/P136SingleNumber.kt) | bit manipulation |
| 101 | [Symmetric Tree](src/main/kotlin/P101SymmetricTree.kt) | recursion |

## Approach

The short write-up for each problem is in a comment at the top of its file. Summary:

**1768.** zip stops at the shorter string, so it does the interleaving by itself. The leftover
tail belongs to only one word, and drop() returns an empty string when you ask for more than
there is, so both tails can just be appended.

**20.** Every closing bracket has to cancel the last opener, which is a stack. The scan is a
fold over a nullable stack where null means "already broken", so a mismatch poisons the rest
and there is no need to break out early.

**1207.** groupingBy + eachCount counts the values in one pass. The counts are unique exactly
when putting them in a Set doesn't lose anything.

**136.** XOR cancels equal numbers and leaves zero, and x xor 0 is x, so xoring the whole array
kills the pairs and leaves the single element. Constant memory, one pass.

**101.** A tree is symmetric when its two subtrees mirror each other, so the one-tree question
becomes a two-tree one. Two nodes mirror when their values are equal and each one's left mirrors
the other's right.

## Running

```bash
./gradlew test
```

The tests are the examples from the problem statements plus a few edge cases I wanted to be sure
about.
