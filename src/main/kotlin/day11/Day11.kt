package day11

fun solveA(text: String): Long {
    val monkeys = text.lines().chunked(7).map { buildMonkeyA(it) }
    return runSimulation(
        20,
        monkeys,
        3,
    )
}

fun solveB(text: String): Long {
    val monkeys = text.lines().chunked(7).map {
        buildMonkeyB(it)
    }
    val divisor = monkeys.fold(1) { acc, monkey -> acc * monkey.testDivisor }
    return runSimulation(10_000, monkeys, divisor)
}

private fun runSimulation(rounds: Int, monkeys: List<Monkey>, divisor: Int): Long {

    repeat(rounds) {
        monkeys.forEach { monkey ->
            monkey.playTurn(monkeys, divisor)
        }
    }
    return monkeys.asSequence().sortedByDescending { it.itemCount }.take(2).fold(1) { acc, monkey -> acc * monkey.itemCount }
}

fun buildMonkeyA(lines: List<String>): Monkey.MonkeyA {
    val items = startingItems(lines)
    val operation: (Long) -> Long = operation(lines)
    val (testDivisor, trueMonkey, falseMonkey) = nextMonkey(lines)
    return Monkey.MonkeyA(items, operation, testDivisor, trueMonkey, falseMonkey)
}

fun buildMonkeyB(lines: List<String>): Monkey.MonkeyB {
    val items = startingItems(lines)
    val operation: (Long) -> Long = operation(lines)
    val (testDivisor, trueMonkey, falseMonkey) = nextMonkey(lines)
    return Monkey.MonkeyB(items, operation, testDivisor, trueMonkey, falseMonkey)
}

private fun nextMonkey(lines: List<String>): Triple<Int, Int, Int> {
    val testDivisor = lines[3].substringAfter("Test: divisible by ").trim().toLong().toInt()
    val trueMonkey = lines[4].substringAfter("If true: throw to monkey ").trim().toInt()
    val falseMonkey = lines[5].substringAfter("If false: throw to monkey ").trim().toInt()
    return Triple(testDivisor, trueMonkey, falseMonkey)
}

private fun operation(lines: List<String>): (Long) -> Long {
    val operationParts = lines[2].trim().substringAfter("Operation: new = old ").split(" ")
    val second = operationParts[1].trim().toLongOrNull()?.run { toLong() }
    val operation: (Long) -> Long = when (operationParts[0]) {
        "+" -> if (second == null) { old -> old + old } else { old -> old + second }
        "*" -> if (second == null) { old -> old * old } else { old -> old * second }
        else -> throw UnsupportedOperationException("Unsupported operation: " + lines[2])
    }
    return operation
}

private fun startingItems(lines: List<String>) = lines[1].substringAfter(':').split(",").mapTo(mutableListOf()) { it.trim().toLong() }


sealed class Monkey(private val items: MutableList<Long>, val operation: (Long) -> Long, val testDivisor: Int, private val trueTarget: Int, private val falseTarget: Int) {

    var itemCount = 0L

    private fun addItem(item: Long) {
        items.add(item)
    }

    fun playTurn(monkeys: List<Monkey>, divisor: Int) {
        items.forEach { oldLevel ->
            itemCount += 1
            val newLevel = calNewWorryLevel(oldLevel, divisor)

            if (newLevel % testDivisor == 0L) {
                monkeys[trueTarget].addItem(newLevel)
            } else {
                monkeys[falseTarget].addItem(newLevel)
            }
        }
        items.clear()
    }

    protected abstract fun calNewWorryLevel(worryLevel: Long, divisor: Int): Long

    class MonkeyA(items: MutableList<Long>, operation: (Long) -> Long, testDivisor: Int, trueTarget: Int, falseTarget: Int) : Monkey(items, operation, testDivisor, trueTarget, falseTarget) {
        override fun calNewWorryLevel(worryLevel: Long, divisor: Int): Long = operation(worryLevel) / divisor
    }

    class MonkeyB(items: MutableList<Long>, operation: (Long) -> Long, testDivisor: Int, trueTarget: Int, falseTarget: Int) : Monkey(items, operation, testDivisor, trueTarget, falseTarget) {
        override fun calNewWorryLevel(worryLevel: Long, divisor: Int): Long = operation(worryLevel) % divisor
    }
}
