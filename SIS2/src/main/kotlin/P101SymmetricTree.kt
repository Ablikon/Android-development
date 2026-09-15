// LeetCode 101. Symmetric Tree (Easy) - recursion

// TreeNode as LeetCode gives it (val is a keyword in Kotlin, hence the backticks).
class TreeNode(var `val`: Int) {
    var left: TreeNode? = null
    var right: TreeNode? = null
}

// A tree is symmetric when its two subtrees mirror each other, so I turned a
// one-tree question into a two-tree one. Two nodes mirror if their values match
// and each one's left mirrors the other's right. a == b covers the base case:
// it's only true when both sides ran out at the same time.

fun isSymmetric(root: TreeNode?): Boolean =
    root == null || mirrors(root.left, root.right)

private fun mirrors(a: TreeNode?, b: TreeNode?): Boolean = when {
    a == null || b == null -> a == b
    else -> a.`val` == b.`val` && mirrors(a.left, b.right) && mirrors(a.right, b.left)
}
