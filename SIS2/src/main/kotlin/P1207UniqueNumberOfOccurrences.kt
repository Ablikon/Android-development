// LeetCode 1207. Unique Number of Occurrences (Easy) - hash map

// groupingBy + eachCount counts every value in one pass without building the
// intermediate lists that groupBy would. After that the counts are unique exactly
// when dumping them into a Set doesn't lose anything.

fun uniqueOccurrences(arr: IntArray): Boolean =
    arr.toList()
        .groupingBy { it }
        .eachCount()
        .values
        .let { it.size == it.toSet().size }
