package smarthome.client.presentation.scripts.setup.dependency

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyUnit
import kotlin.test.assertEquals


data class MockDependencyUnit_A(
    override val id: String = "idA",
    override val data: String = "dataA"
) : MockDependencyUnit(data)

data class MockDependencyUnit_B(
    override val id: String = "idB",
    override val data: String = "dataB"
) : MockDependencyUnit(data)


data class MockDependencyUnit_C(
    override val id: String = "idC",
    override val data: String = "dataC"
) : MockDependencyUnit(data)

abstract class MockDependencyUnit(open val data: String) : DependencyUnit

//class A : DependencyUnitData
//class B : DependencyUnitData
//class C : DependencyUnitData

class ContainersStatesTest {
    private lateinit var emptyUnitsCreator: (Dependency) -> List<MockDependencyUnit>
    private lateinit var states: ContainersStates<MockDependencyUnit>
    private lateinit var dependency: Dependency
    
    private val unit_a1 = MockDependencyUnit_A()
    private val unit_b1 = MockDependencyUnit_B()
    private val unit_c1 = MockDependencyUnit_C()
    
    @Before
    fun setUp() {
        dependency = Dependency("dependencyId", "blockId", "blockId2")
        emptyUnitsCreator = mock {
            on { invoke(dependency) }.then { listOf(unit_a1, unit_b1, unit_c1) }
        }
        states = ContainersStates(emptyUnitsCreator)
    }
    
    @Test
    fun `when pass data with one extra unit should add container for it`() {
        val newData = MockDependencyUnit_B()
        
        val containers = states.apply { setData(listOf(newData), dependency) }.asList()
        
        assertEquals(1, containers.size)
        assertEquals(3, containers.first().allData.size)
        assertEquals(newData, containers.first().allData[1])
        assertEquals(unit_a1, containers.first().allData[0])
    }
    
    @Test
    fun `data units should be current data of each container`() {
        val newData = listOf(
            MockDependencyUnit_B(),
            MockDependencyUnit_C()
        )
        
        val containers = states.apply { setData(newData, dependency) }.asList()
        
        assertEquals(newData[0], containers[0].currentData)
        assertEquals(newData[1], containers[1].currentData)
    }
    
    @Test
    fun `when pass data without some saved unit should remove its container `() {
        val willStay = MockDependencyUnit_C()
        val newData = listOf(
            MockDependencyUnit_B(),
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
        val newData = listOf(MockDependencyUnit_B())
        
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
        
        val firstData = MockDependencyUnit_B()
        val secondData = MockDependencyUnit_B()
        
        val containers = states.apply {
            setData(listOf(firstData), dependency)
            setData(listOf(secondData), dependency)
        }.asList()
        
        
        assertEquals(secondData, containers.first().allData[1])
    }
    
    @Test
    fun `when send data with changed unit should find its container and update currentData field`() {
        val newData = "newData"
        
        val empty_1 = MockDependencyUnit_A()
        val empty_2 = MockDependencyUnit_B()
        
        val firstUnit = MockDependencyUnit_B()
        
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