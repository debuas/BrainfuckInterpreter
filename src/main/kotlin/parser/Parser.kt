package parser

sealed class Operation {
    object DecrementNodeValue : Operation()
    object IncrementNodeValue : Operation()
    object MovePointerLeft: Operation()
    object MovePointerRight: Operation()
    object StartLoop: Operation()
    object EndLoop: Operation()
    object PrintNodeValue : Operation()
    object SetNodeValue : Operation()
}

sealed class OptimizedOperation {
    data class ChangeNodeValue(val amount: Int) : OptimizedOperation()
    data class ChangePosition(val steps: Int) : OptimizedOperation()
    data class Loop(val operationList: List<OptimizedOperation>) : OptimizedOperation()
    object PrintNodeValue : OptimizedOperation()
    object SetNodeValue : OptimizedOperation()
}

sealed class Operation2 {
    object DecrementNodeValue : Operation2()
    object IncrementNodeValue : Operation2()
    object MovePointerLeft: Operation2()
    object MovePointerRight: Operation2()
    object PrintNodeValue : Operation2()
    object SetNodeValue : Operation2()
    data class Loop(val operationList: List<Operation2>): Operation2()
}

class Parser {

    fun parseStringToOperationList(rawOperationString: String): List<OptimizedOperation> {
        validate(rawOperationString)

        val operations: List<Operation> = lexer(rawOperationString)
        return optimize(0, operations).second
    }

    fun validate(rawOperationString: String) {
        var loopLevel = 0
        val openLoops = mutableListOf<Pair<Int,Int>>()

        for (currentPosition in 0..rawOperationString.lastIndex) {
            when (rawOperationString[currentPosition]) {
                '[' -> {
                    loopLevel++
                    openLoops.add(Pair(currentPosition, loopLevel))
                }
                ']' -> {
                    val closed: Boolean = openLoops.removeIf{ it.second == loopLevel}
                    if (!closed) {
                        throw Exception("At Position $currentPosition: Tried to close non-existent loop.")
                    }
                    loopLevel--
                }
            }
        }

        if (openLoops.isNotEmpty()) {
            val openLoop = openLoops.first()
            throw Exception("At Position ${openLoop.first}: Loop started with '[' but was never closed.")
        }
    }

    fun lexer(rawOperationString: String): List<Operation> {
        return rawOperationString.mapNotNull { currentChar ->
            when (currentChar) {
                '-' -> Operation.DecrementNodeValue
                '+' -> Operation.IncrementNodeValue
                '<' -> Operation.MovePointerLeft
                '>' -> Operation.MovePointerRight
                '[' -> Operation.StartLoop
                ']' -> Operation.EndLoop
                '.' -> Operation.PrintNodeValue
                ',' -> Operation.SetNodeValue
                else -> null
            }
        }
    }

    fun optimize(_startPosition: Int, operations: List<Operation>): Pair<Int, List<OptimizedOperation>> {
        val optimizedOperations = mutableListOf<OptimizedOperation>()

        var position = _startPosition
        while (position < operations.size) {
            when (operations[position]) {
                Operation.DecrementNodeValue, Operation.IncrementNodeValue -> {
                    val result: Pair<Int, Int> =
                        combineOperations(
                            Pair(Operation.DecrementNodeValue, Operation.IncrementNodeValue),
                            position,
                            operations)

                    optimizedOperations.add(OptimizedOperation.ChangeNodeValue(result.second))
                    position = result.first
                }
                Operation.MovePointerLeft, Operation.MovePointerRight -> {
                    val result: Pair<Int, Int> =
                        combineOperations(
                            Pair(Operation.MovePointerLeft, Operation.MovePointerRight),
                            position,
                            operations)

                    optimizedOperations.add(OptimizedOperation.ChangePosition(result.second))
                    position = result.first
                }
                Operation.StartLoop -> {
                    val loopSegment = optimize(position + 1, operations)
                    optimizedOperations.add( OptimizedOperation.Loop(loopSegment.second))
                    position = loopSegment.first
                }
                Operation.EndLoop -> return Pair(position, optimizedOperations.toList())
                Operation.PrintNodeValue -> optimizedOperations.add(OptimizedOperation.PrintNodeValue)
                Operation.SetNodeValue -> optimizedOperations.add(OptimizedOperation.SetNodeValue)
            }
            position++
        }

        return Pair(position, optimizedOperations.toList())
    }

    fun combineOperations(relatedOperations: Pair<Operation, Operation>, _startPosition: Int, operations: List<Operation>): Pair<Int, Int> {
        var stepNumber = 0
        var position = _startPosition

        var currentOperation = operations.getOrNull(position)
        while ((currentOperation != null) and ((currentOperation == relatedOperations.first) or (currentOperation == relatedOperations.second))) {
            if (currentOperation == relatedOperations.first) {
                stepNumber--
            } else {
                stepNumber++
            }

            position++
            currentOperation = operations.getOrNull(position)
        }

        position--
        return Pair(position, stepNumber)
    }

    fun parse2(rawOperationString: String): List<Operation2> {
        validate(rawOperationString)
        return lexer2(0, rawOperationString).second
    }

    fun lexer2(_startPosition: Int, rawOperationString: String): Pair<Int,List<Operation2>> {
        val operations = mutableListOf<Operation2>()

        var position = _startPosition
        while (position < rawOperationString.lastIndex) {
            when (rawOperationString[position]) {
                '-' -> operations.add(Operation2.DecrementNodeValue)
                '+' -> operations.add(Operation2.IncrementNodeValue)
                '<' -> operations.add(Operation2.MovePointerLeft)
                '>' -> operations.add(Operation2.MovePointerRight)
                '.' -> operations.add(Operation2.PrintNodeValue)
                ',' -> operations.add(Operation2.SetNodeValue)
                '[' -> {
                    val loopSegment = lexer2(position + 1, rawOperationString )
                    operations.add(Operation2.Loop(loopSegment.second))
                    position = loopSegment.first
                }
                ']' -> return Pair(position, operations.toList())
            }
            position++
        }
        return Pair(position, operations.toList())
    }
}


fun main() {
    val brainfuckProgram0 = ">+-+--[++[-+-] >>>>>>>>>>>>>>>>>>> + >> ]>><-+[+<+]>-"
    val brainfuckProgram1 = "<<[<++<<<><--+<+<<<]++.++.,<<<>>"
    val brainfuckProgram2 = "<<[<++]just[ [some ]comments[<[<<><[--+[<+<<<]++.++].],<<<>>"
    val brainfuckProgram3 = ""

    // val operationList = Parser().parseStringToOperationList(brainfuckProgram1)
    val operationList = Parser().parse2(brainfuckProgram0)
    operationList.forEach { println(it) }
}
