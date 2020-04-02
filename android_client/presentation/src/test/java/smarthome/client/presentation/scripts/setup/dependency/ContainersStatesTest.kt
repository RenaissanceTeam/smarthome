package smarthome.client.presentation.scripts.setup.dependency

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyUnit
import smarthome.client.entity.script.dependency.DependencyUnitData
import kotlin.test.assertEquals

data class MockDependencyUnit(
    override val id: String,
    override val data: DependencyUnitData
) : DependencyUnit

class A : DependencyUnitData
class B : DependencyUnitData
class C : DependencyUnitData

class ContainersStatesTest {
    private lateinit var emptyUnitsCreator: (Dependency) -> List<MockDependencyUnit>
    private lateinit var states: ContainersStates<MockDependencyUnit>
    private lateinit var dependency: Dependency
    private val a1 = A()
    private val a2 = A()
    private val b1 = B()
    private val c1 = C()
    val id = "id"
    private val unit_a1 = MockDependencyUnit(id, a1)
    private val unit_b1 = MockDependencyUnit(id, b1)
    private val unit_c1 = MockDependencyUnit(id, c1)
    
    @Before
    fun setUp() {
        dependency = Dependency(id, "blockId", "blockId2")
        emptyUnitsCreator = mock {
            on { invoke(dependency) }.then { listOf(unit_a1, unit_b1, unit_c1) }
        }
        states = ContainersStates(emptyUnitsCreator)
    }
    
    @Test
    fun `when pass data with one extra unit should add container for it`() {
        val newData = MockDependencyUnit(id, B())
        
        val containers = states.apply { setData(listOf(newData), dependency) }.asList()
        
        assertEquals(1, containers.size)
        assertEquals(3, containers.first().allData.size)
        assertEquals(newData, containers.first().allData[1])
        assertEquals(unit_a1, containers.first().allData[0])
    }
    
    @Test
    fun `data units should be current data of each container`() {
        val newData = listOf(
            MockDependencyUnit(id, B()),
            MockDependencyUnit(id, C())
        )
        
        val containers = states.apply { setData(newData, dependency) }.asList()
        
        assertEquals(newData[0], containers[0].currentData)
        assertEquals(newData[1], containers[1].currentData)
    }
    
    @Test
    fun `when pass data without some saved unit should remove its container `() {
        val willStay = MockDependencyUnit(id, C())
        val newData = listOf(
            MockDependencyUnit(id, B()),
            willStay
        )
        
        val containers = states.apply {
            setData(newData, dependency)
            setData(listOf(willStay), dependency)
        }.asList()
        
        assertEquals(1, containers.size)
        assertEquals(willStay, containers.first().currentData)
    }
    
    @Test
    fun `when send the same data units should not create new containers`() {
        val newData = listOf(MockDependencyUnit(id, B()))
        
        val containers = states.apply {
            setData(newData, dependency)
            setData(newData, dependency)
        }.asList()
        
        assertEquals(1, containers.size)
        assertEquals(3, containers.first().allData.size)
        assertEquals(newData.first(), containers.first().allData[1])
        assertEquals(unit_a1, containers.first().allData[0])
    }
    
    @Test
    fun `container data should be updated with new units`() {
        val id = id
        val firstData = B()
        val secondData = B()
        
        val containers = states.apply {
            setData(listOf(MockDependencyUnit(id, firstData)), dependency)
            setData(listOf(MockDependencyUnit(id, secondData)), dependency)
        }.asList()
        
        
        assertEquals(secondData, containers.first().allData[1].data)
    }
    
    @Test
    fun `when send data with changed unit should find its container and update currentData field`() {
        val newData = A()
        val empty_1 = MockDependencyUnit(id, a1)
        val empty_2 = MockDependencyUnit(id, b1)
        val firstUnit = MockDependencyUnit(id, B())
        
        whenever(emptyUnitsCreator.invoke(dependency)).then { listOf(empty_1, empty_2) }
        
        val containers = states.apply {
            setData(listOf(firstUnit), dependency)
            setData(listOf(empty_1.copy(data = newData)), dependency)
        }.asList()
        
        assertEquals(empty_1.id, containers.first().currentData.id)
        assertEquals(newData, containers.first().currentData.data)
        assertEquals(firstUnit, containers.first().allData[1])
    }
}