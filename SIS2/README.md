# SIS2 — Kotlin problems

Five Easy LeetCode problems solved in idiomatic Kotlin, one file per problem. Each solution file
carries the code and a short note on the approach; every solution is also covered by unit tests in
this project, so the whole set can be verified with a single command.

## Problems

| # | Problem | Topic | Kotlin features leaned on |
| --- | --- | --- | --- |
| 1768 | [Merge Strings Alternately](src/main/kotlin/P1768MergeStringsAlternately.kt) | Strings / two pointers | `zip` with a transform, `joinToString`, `drop` |
| 20 | [Valid Parentheses](src/main/kotlin/P20ValidParentheses.kt) | Stack | `fold` over a nullable accumulator, `ArrayDeque`, `when` |
| 1207 | [Unique Number of Occurrences](src/main/kotlin/P1207UniqueNumberOfOccurrences.kt) | Hash maps / frequency counting | `groupingBy`, `eachCount`, `let` |
| 136 | [Single Number](src/main/kotlin/P136SingleNumber.kt) | Bit manipulation | `reduce` with a method reference (`Int::xor`) |
| 101 | [Symmetric Tree](src/main/kotlin/P101SymmetricTree.kt) | Recursion / binary trees | expression-bodied recursion, `when`, nullable receivers |

Five problems, five distinct topics — no two share a pattern.

## Approaches

**1768 — Merge Strings Alternately.** `zip` already walks two sequences in lockstep and stops at the
shorter one, so the interleaved prefix is just a zip that glues each pair of characters together.
Whatever is left over belongs to exactly one of the two words, and `drop` returns an empty string
when the count exceeds the length, so concatenating both tails appends the surplus without a single
branch.

**20 — Valid Parentheses.** Brackets nest, so the string is valid exactly when every closer cancels
the most recently seen opener — which is a stack. The whole scan is a single `fold` over a nullable
`ArrayDeque`, where `null` is the absorbing state meaning "already proven invalid", so a mismatch
simply poisons the accumulator instead of needing an early exit.

**1207 — Unique Number of Occurrences.** `groupingBy { it }.eachCount()` builds the
value-to-frequency map in one pass without materialising the intermediate groups, which is what
makes it cheaper than `groupBy`. The question then reduces to asking whether those frequencies are
pairwise distinct, and a set collapses duplicates, so the counts are unique precisely when
converting them to a set loses nothing.

**136 — Single Number.** XOR is associative and commutative, every value cancels itself
(`x xor x == 0`), and zero is its identity (`x xor 0 == x`). So folding XOR across the whole array
annihilates each pair regardless of order and leaves only the element that appears once — constant
memory and a single pass, with no auxiliary set.

**101 — Symmetric Tree.** A tree is a mirror of itself when its two subtrees mirror each other,
which turns a one-tree question into a two-tree one and makes the recursion fall out. Two subtrees
mirror when their roots hold the same value and each one's left child mirrors the other's right
child. The base case is `a == b`, which holds only when both sides ran out at the same time.

## Folder structure

```
SIS2/
├── build.gradle.kts                                 # Kotlin/JVM project, kotlin-test on JUnit 5
├── settings.gradle.kts
├── gradle/wrapper/                                  # Gradle wrapper jar + properties
├── gradlew / gradlew.bat
├── .gitignore
├── README.md
└── src/
    ├── main/kotlin/
    │   ├── P1768MergeStringsAlternately.kt          # strings / two pointers
    │   ├── P20ValidParentheses.kt                   # stack
    │   ├── P1207UniqueNumberOfOccurrences.kt        # hash maps
    │   ├── P136SingleNumber.kt                      # bit manipulation
    │   └── P101SymmetricTree.kt                     # recursion / binary trees
    └── test/kotlin/
        ├── P1768MergeStringsAlternatelyTest.kt
        ├── P20ValidParenthesesTest.kt
        ├── P1207UniqueNumberOfOccurrencesTest.kt
        ├── P136SingleNumberTest.kt
        └── P101SymmetricTreeTest.kt
```

## Running the tests

```bash
cd SIS2
./gradlew test
```

32 tests cover the five solutions — the examples from each problem statement plus the edge cases
that usually break naive attempts: empty inputs, a bracket mismatch that a later valid pair must not
rescue, a tree whose shape is symmetric but whose values are not, negative values in the frequency
count, and a single-element array.

The HTML report lands in `build/reports/tests/test/index.html`.
