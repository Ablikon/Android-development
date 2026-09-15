import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private fun node(v: Int, l: TreeNode? = null, r: TreeNode? = null) =
    TreeNode(v).apply { left = l; right = r }

class P101Test {

    @Test
    fun example1() {
        // [1,2,2,3,4,4,3]
        val root = node(1, node(2, node(3), node(4)), node(2, node(4), node(3)))
        assertTrue(isSymmetric(root))
    }

    @Test
    fun example2() {
        // [1,2,2,null,3,null,3]
        val root = node(1, node(2, r = node(3)), node(2, r = node(3)))
        assertFalse(isSymmetric(root))
    }

    @Test fun emptyTree() = assertTrue(isSymmetric(null))

    @Test fun singleNode() = assertTrue(isSymmetric(node(1)))

    @Test
    fun sameValuesButWrongShape() {
        val root = node(1, node(2, node(2)), node(2))
        assertFalse(isSymmetric(root))
    }
}
