package day19

import helper.enumMap
import helper.enumMapOf
import helper.graph.LongestPathNode
import helper.graph.findLongestPathInTime
import java.util.*

val regex = ("Blueprint (\\d+): " +
        "Each ore robot costs (\\d+) ore. " +
        "Each clay robot costs (\\d+) ore. " +
        "Each obsidian robot costs (\\d+) ore and (\\d+) clay. " +
        "Each geode robot costs (\\d+) ore and (\\d+) obsidian.").toRegex()

//What do you get if you add up the quality level of all of the blueprints in your list?
//24 minutes
//Determine the quality level of each blueprint by multiplying that blueprint's ID number with the largest number of geodes that can be opened in 24 minutes using that blueprint.
fun solveA(text: String): Int {
    val blueprints = text.lines().map { parseBlueprint(it) }

    return blueprints.sumOf {
        it.id * it.simulate(24)
    }
}

fun parseBlueprint(it: String): Blueprint {
    val (id, aOreCost, bOreCost, cOreCost, cClayCost, dOreCost, dObsidianCost) = regex.matchEntire(it)!!.destructured

    val oreRobotCost = listOf(ResourceCost(aOreCost.toInt(), ResourceType.ORE))
    val clayRobotCost = listOf(ResourceCost(bOreCost.toInt(), ResourceType.ORE))
    val obsidianRobotCost = listOf(
        ResourceCost(cOreCost.toInt(), ResourceType.ORE),
        ResourceCost(cClayCost.toInt(), ResourceType.CLAY)
    )
    val geodeRobotCost = listOf(
        ResourceCost(dOreCost.toInt(), ResourceType.ORE),
        ResourceCost(dObsidianCost.toInt(), ResourceType.OBSIDIAN)
    )
    return Blueprint(id.toInt(), oreRobotCost, clayRobotCost, obsidianRobotCost, geodeRobotCost)
}

enum class ResourceType {
    ORE,
    CLAY,
    OBSIDIAN,
    GEODE
}

data class ResourceCost(val amount: Int, val type: ResourceType) {}

fun solveB(text: String): Int {
    val blueprints = text.lines().map { parseBlueprint(it) }.take(3)

    val items = blueprints.map { it.simulate(32) }
    return items.reduce { acc, i -> i * acc }
}

class Blueprint(
    val id: Int,
    oreRobotCost: List<ResourceCost>,
    clayRobotCost: List<ResourceCost>,
    obsidianRobotCost: List<ResourceCost>,
    geodeRobotCost: List<ResourceCost>
) {
    val robotCosts: Map<ResourceType, List<ResourceCost>> = enumMapOf(
        ResourceType.GEODE to geodeRobotCost,
        ResourceType.OBSIDIAN to obsidianRobotCost,
        ResourceType.CLAY to clayRobotCost,
        ResourceType.ORE to oreRobotCost,
    )
    val robotOreCosts: Map<ResourceType, Int> = robotCosts.mapValuesTo(enumMap()) { (_, value) -> value.first { it.type == ResourceType.ORE }.amount }

    fun cost(robotType: ResourceType) = robotCosts[robotType]!!
    fun cost(robotType: ResourceType, resourceType: ResourceType): Int = robotCosts[robotType]!!.first { it.type == resourceType }.amount

    fun simulate(endTime: Int): Int {
        val startingState = mapOf(ResourceType.ORE to 1)
        val start = SearchState(0, emptyMap(), startingState, endTime, this)
        val end = findLongestPathInTime(start, endTime)
        return end.score
    }

    fun canAfford(resources: SearchState, robotType: ResourceType): Boolean {
        return robotCosts[robotType]!!.all { (costAmount, costType) -> resources.resourceCount(costType) >= costAmount }
    }
}

typealias ResourceCounts = Map<ResourceType, Int>

data class SearchState(
    val minute: Int,
    val resources: ResourceCounts,
    val robots: ResourceCounts,
    private val endTime: Int,
    private val blueprint: Blueprint
) : LongestPathNode<SearchState, ResourceCounts> {

    override val score get() = resourceCount(ResourceType.GEODE)
    override fun cacheKey() = robots

    override fun neighbours(): List<SearchState> {
        val canAffordOre = blueprint.canAfford(this, ResourceType.ORE)
        val maxOreCost = blueprint.robotOreCosts.filterKeys { it != ResourceType.ORE }.values.max()

        val oreMakesSense = buyMakesSense(maxOreCost, ResourceType.ORE)

        val canAffordClay = blueprint.canAfford(this, ResourceType.CLAY)
        val clayMakesSense = buyMakesSense(blueprint.cost(ResourceType.OBSIDIAN, ResourceType.CLAY), ResourceType.CLAY)

        val canAffordObsidian = blueprint.canAfford(this, ResourceType.OBSIDIAN)
        val obsidianMakesSense = buyMakesSense(blueprint.cost(ResourceType.GEODE, ResourceType.OBSIDIAN), ResourceType.OBSIDIAN)

        val canAffordGeode = blueprint.canAfford(this, ResourceType.GEODE)

        val skipMakesSense = !canAffordOre || !canAffordClay || !canAffordObsidian || !canAffordGeode

        return buildList {
            if (skipMakesSense) {
                add(nextSearchState(blueprint, null))
            }
            if (canAffordOre && oreMakesSense) {
                add(nextSearchState(blueprint, ResourceType.ORE))
            }
            if (canAffordClay && clayMakesSense) {
                add(nextSearchState(blueprint, ResourceType.CLAY))
            }
            if (canAffordObsidian && obsidianMakesSense) {
                add(nextSearchState(blueprint, ResourceType.OBSIDIAN))
            }
            if (canAffordGeode) {
                add(nextSearchState(blueprint, ResourceType.GEODE))
            }
        }
    }

    private fun buyMakesSense(maxUsagePerMinute: Int, resourceType: ResourceType): Boolean {
        val remainingMinutes = endTime - minute
        val productionPerMinute = robotCount(resourceType)
        val currentCount = resourceCount(resourceType)
        //Buying the robot only makes sense if I can not already buy the next tier robot in every remaining minute
        return productionPerMinute < maxUsagePerMinute
                && currentCount + productionPerMinute * remainingMinutes < maxUsagePerMinute * remainingMinutes
    }

    fun robotCount(type: ResourceType) = robots.getOrDefault(type, 0)
    fun resourceCount(type: ResourceType) = resources.getOrDefault(type, 0)

    private fun nextSearchState(blueprint: Blueprint, robotType: ResourceType?): SearchState {
        val newResources = getNewResources(blueprint, robotType, robots)
        return when (robotType) {
            null -> SearchState(minute + 1, newResources, robots, endTime, blueprint)
            else -> SearchState(minute + 1, newResources, robots.increment(robotType, 1), endTime, blueprint)
        }
    }

    private fun getNewResources(blueprint: Blueprint, newRobotType: ResourceType?, currentRobots: Map<ResourceType, Int>): Map<ResourceType, Int> {
        val newResources = resources.toMap(enumMap())

        if (newRobotType != null) {
            blueprint.cost(newRobotType).forEach { (amount, costType) ->
                newResources[costType] = newResources.getOrDefault(costType, 0) - amount
            }
        }

        currentRobots.forEach { (robotType, count) ->
            newResources[robotType] = newResources.getOrDefault(robotType, 0) + count
        }

        return newResources
    }

    override fun toString() = "Minute $minute : Resources=$resources, Robots=${robots}, Score=${score})"

    override fun canReplace(other: SearchState): Boolean {
        return robots == other.robots && minute >= other.minute && resourceMore(other, ResourceType.GEODE) &&
                resourceMore(other, ResourceType.OBSIDIAN) &&
                resourceMore(other, ResourceType.CLAY) &&
                resourceMore(other, ResourceType.ORE)
    }

    private fun resourceMore(other: SearchState, type: ResourceType) = resourceCount(type) >= other.resourceCount(type)
}

private fun ResourceCounts.increment(type: ResourceType, count: Int): Map<ResourceType, Int> {
    val copy = toMap(EnumMap(ResourceType::class.java))
    copy[type] = copy.getOrDefault(type, 0) + count
    return copy
}

