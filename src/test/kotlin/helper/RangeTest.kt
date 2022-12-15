package helper

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RangeTest {

    @Test
    fun `Overlap - fully contained`() {
        val a = 0..10
        val b = 5..7
        assertTrue(a.overlaps(b), "Outer First")
        assertTrue(b.overlaps(a), "Inner First")
    }

    @Test
    fun `Overlap - identical`() {
        val a = 0..10
        assertTrue(a.overlaps(a))
    }

    @Test
    fun `Overlap - no overlap`() {
        val a = 0..5
        val b = 6..10
        assertFalse(a.overlaps(b), "Lower First")
        assertFalse(b.overlaps(a), "Higher First")
    }

    @Test
    fun `Overlap - partial overlap`() {
        val a = 0..5
        val b = 5..10
        assertTrue(a.overlaps(b), "Lower First")
        assertTrue(b.overlaps(a), "Higher First")
    }
}