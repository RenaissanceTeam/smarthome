package smarthome.client.presentation.scripts.setup.graph.blockviews.dependency

import org.junit.Before
import org.junit.Test
import smarthome.client.util.Position

class BorderIntersectionHelperTest {
    private lateinit var helper: BorderIntersectionHelper
    private val start = DependencyTip(
            Position(10, 10),
            4, 2
    )

    @Before
    fun setUp() {
        helper = BorderIntersectionHelper()
    }

    @Test
    fun `when end tip is inside the block should return pair of center positions`() {

        val result = helper.tipPositionsWithIntersection(
                start,
                DependencyTip(Position(10, 10), 1, 1)
        )

        assert(result.first == start.center)
        assert(result.second == start.center)
    }

    @Test
    fun `straight above`() {
        val result = helper.tipPositionsWithIntersection(
                start,
                DependencyTip(Position(12, 5), 1, 1)
        )

        assert(result.first == Position(12, 10))
        assert(result.second == Position(12, 5))
    }

    @Test
    fun `straight below`() {
        val result = helper.tipPositionsWithIntersection(
                start,
                DependencyTip(Position(12, 15), 1, 1)
        )

        assert(result.first == Position(12, 12))
        assert(result.second == Position(12, 15))
    }

    @Test
    fun `straight left`() {
        val result = helper.tipPositionsWithIntersection(
                start,
                DependencyTip(Position(5, 11), 1, 1)
        )

        assert(result.first == Position(10, 11))
        assert(result.second == Position(5, 11))
    }

    @Test
    fun `straight right`() {
        val result = helper.tipPositionsWithIntersection(
                start,
                DependencyTip(Position(15, 11), 1, 1)
        )

        assert(result.first == Position(14, 11))
        assert(result.second == Position(15, 11))
    }

    @Test
    fun `end to top left should change x proportionally`() {
        // block center is in (12, 12)

        val result = helper.tipPositionsWithIntersection(
                start,
                DependencyTip(Position(10, 9), 1, 1)
        )

        assert(result.first == Position(11, 10))
        assert(result.second == Position(10, 9))

        val result2 = helper.tipPositionsWithIntersection(
                start,
                DependencyTip(Position(8, 7), 1, 1)
        )

        assert(result2.first == Position(11, 10))
        assert(result2.second == Position(8, 7))
    }

    @Test
    fun `end to right bottom should change x and y proportionally`() {
        val result = helper.tipPositionsWithIntersection(
                start,
                DependencyTip(Position(16, 15), 1, 1)
        )

        assert(result.first == Position(13, 12))
        assert(result.second == Position(16, 15))
    }


}