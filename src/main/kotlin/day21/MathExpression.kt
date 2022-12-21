package day21

sealed class MathExpression {

    enum class Operator(private val char: Char) {
        PLUS('+') {
            override fun apply(a: Long, b: Long): Long = a + b
            override fun solveLeft(target: MathExpression, right: MathExpression) = Equation.build(target, MINUS, right)
            override fun solveRight(target: MathExpression, left: MathExpression) = Equation.build(target, MINUS, left)
        },
        MINUS('-') {
            override fun apply(a: Long, b: Long): Long = a - b
            override fun solveLeft(target: MathExpression, right: MathExpression) = Equation.build(target, PLUS, right)
            override fun solveRight(target: MathExpression, left: MathExpression) = Equation.build(left, MINUS, target)
        },
        MUL('*') {
            override fun apply(a: Long, b: Long): Long = a * b
            override fun solveLeft(target: MathExpression, right: MathExpression) = Equation.build(target, DIV, right)
            override fun solveRight(target: MathExpression, left: MathExpression) = Equation.build(target, DIV, left)
        },
        DIV('/') {
            override fun apply(a: Long, b: Long): Long = a / b
            override fun solveLeft(target: MathExpression, right: MathExpression) = Equation.build(target, MUL, right)
            override fun solveRight(target: MathExpression, left: MathExpression) = Equation.build(left, DIV, target)
        };

        abstract fun apply(a: Long, b: Long): Long
        abstract fun solveRight(target: MathExpression, left: MathExpression): MathExpression
        abstract fun solveLeft(target: MathExpression, right: MathExpression): MathExpression
        override fun toString(): String = char.toString()

        companion object {
            fun parse(string: String) = when (string) {
                "+" -> PLUS
                "-" -> MINUS
                "*" -> MUL
                "/" -> DIV
                else -> throw IllegalArgumentException("Unknown operator $string")
            }

        }
    }

    fun solve(): Long = trySolve() ?: throw IllegalArgumentException("Unknown can't be solved")
    abstract fun trySolve(): Long?
    abstract override fun toString(): String

    class Value(val value: Long) : MathExpression() {
        override fun trySolve(): Long = value
        override fun toString(): String = value.toString()
    }

    sealed class SolvableExpression : MathExpression() {
        abstract fun solveUnknown(target: MathExpression): MathExpression
    }

    object Unknown : SolvableExpression() {
        override fun trySolve(): Long? = null
        override fun toString(): String = "x"
        override fun solveUnknown(target: MathExpression): MathExpression = target
    }

    class Equation private constructor(val left: MathExpression, val operator: Operator, val right: MathExpression) : SolvableExpression() {

        override fun trySolve(): Long? {
            val a = left.trySolve()
            val b = right.trySolve()

            return if (a != null && b != null) {
                operator.apply(a, b)
            } else {
                null
            }
        }

        private fun simplify(): MathExpression {
            val sideA = left.trySolve()
            val sideB = right.trySolve()

            return if (sideA != null && sideB != null) {
                Value(operator.apply(sideA, sideB))
            } else if (sideA != null) {
                Equation(Value(sideA), operator, right)
            } else if (sideB != null) {
                Equation(left, operator, Value(sideB))
            } else {
                throw IllegalArgumentException("Both sides can't be unknown")
            }
        }

        override fun toString(): String = "($left $operator $right)"

        override fun solveUnknown(target: MathExpression): MathExpression {
            return if (left is SolvableExpression && right is SolvableExpression) {
                throw IllegalArgumentException("Both sides can't be unknown")
            } else if (left is SolvableExpression) {
                left.solveUnknown(operator.solveLeft(target, right))
            } else if (right is SolvableExpression) {
                right.solveUnknown(operator.solveRight(target, left))
            } else {
                throw IllegalArgumentException("Missed a state: $left, $right")
            }
        }

        companion object {
            fun build(left: MathExpression, operator: Operator, right: MathExpression) = Equation(left, operator, right).simplify()
        }
    }
}