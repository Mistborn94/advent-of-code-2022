package day21

import day21.MathExpression.*

//What number will the monkey named root yell?
fun solveA(text: String): Long {
    val monkeyMap = parseMonkeys(text)

    return buildExpressionTree("root", monkeyMap).solve()
}

private fun parseMonkeys(text: String): Map<String, String> {
    val monkeyMap = text.lines().associate {
        val (a, b) = it.split(":")
        a.trim() to b.trim()
    }
    return monkeyMap
}

const val UNKNOWN = "unknown"
fun buildExpressionTree(name: String, monkeyMap: Map<String, String>): MathExpression {
    val job = monkeyMap[name]!!

    val intJob = job.toLongOrNull()
    return if (job == UNKNOWN) {
        Unknown
    } else if (intJob != null) {
        Value(intJob)
    } else {
        val split = job.split(" ")
        val firstNumber = buildExpressionTree(split[0], monkeyMap)
        val secondNumber = buildExpressionTree(split[2], monkeyMap)
        val operatorEnum = when (split[1]) {
            "+" -> Operator.PLUS
            "-" -> Operator.MINUS
            "*" -> Operator.MUL
            "/" -> Operator.DIV
            else -> throw IllegalArgumentException(job)
        }
        Equation.build(firstNumber, operatorEnum, secondNumber)
    }
}

private const val HUMN = "humn"


fun solveB(text: String): Long {
    val monkeyMap = parseMonkeys(text).toMutableMap()
    monkeyMap[HUMN] = UNKNOWN

    val rootExpression = buildExpressionTree("root", monkeyMap)
    println("Root: $rootExpression")

    return if (rootExpression !is Equation) {
        throw IllegalArgumentException("Root expression already solved")
    } else {
        val left = rootExpression.left
        val right = rootExpression.right
        if (right is Equation) {
            right.solveUnknown(left).solve()
        } else if (left is Equation) {
            left.solveUnknown(right).solve()
        } else {
            throw IllegalArgumentException("Left or right must be an equation")
        }
    }
}

