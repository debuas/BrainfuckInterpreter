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

sealed class StructuredOperation {
    object DecrementNodeValue : StructuredOperation()
    object IncrementNodeValue : StructuredOperation()
    object MovePointerLeft: StructuredOperation()
    object MovePointerRight: StructuredOperation()
    object PrintNodeValue : StructuredOperation()
    object SetNodeValue : StructuredOperation()
    data class Loop(val operationList: List<StructuredOperation>): StructuredOperation()
}

sealed class OptimizedOperation {
    data class ChangeNodeValue(val amount: Int) : OptimizedOperation()
    data class ChangePosition(val steps: Int) : OptimizedOperation()
    data class Loop(val operationList: List<OptimizedOperation>) : OptimizedOperation()
    object PrintNodeValue : OptimizedOperation()
    object SetNodeValue : OptimizedOperation()
}

class Parser {

    fun parse(rawOperationString: String): List<StructuredOperation> {
        validate(rawOperationString)

        val operations: List<Operation> = lexer(rawOperationString)
        return structureLoops(0, operations).second
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

    fun structureLoops(_startPosition: Int, operations: List<Operation>): Pair<Int,List<StructuredOperation>> {
        val structuredOperations = mutableListOf<StructuredOperation>()

        var position = _startPosition
        while (position <= operations.lastIndex) {
            when (operations[position]) {
                Operation.DecrementNodeValue -> structuredOperations.add(StructuredOperation.DecrementNodeValue)
                Operation.IncrementNodeValue -> structuredOperations.add(StructuredOperation.IncrementNodeValue)
                Operation.MovePointerLeft -> structuredOperations.add(StructuredOperation.MovePointerLeft)
                Operation.MovePointerRight -> structuredOperations.add(StructuredOperation.MovePointerRight)
                Operation.PrintNodeValue -> structuredOperations.add(StructuredOperation.PrintNodeValue)
                Operation.SetNodeValue -> structuredOperations.add(StructuredOperation.SetNodeValue)
                Operation.StartLoop -> {
                    val loopSegment = structureLoops(position + 1, operations )
                    structuredOperations.add(StructuredOperation.Loop(loopSegment.second))
                    position = loopSegment.first
                }
                Operation.EndLoop -> return Pair(position, structuredOperations.toList())
            }
            position++
        }
        return Pair(position, structuredOperations.toList())
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
}


fun main() {
    val brainfuckProgram0 = ">+-+--[++[-+-] >>>>>>>>>>>>>>>>>>> + >> ]>><-+[+<+]>-"
    val brainfuckProgram1 = "<<[<++<<<><--+<+<<<]++.++.,<<<>>"
    val brainfuckProgram2 = "<<[<++]just[ [some ]comments[<[<<><[--+[<+<<<]++.++].],<<<>>"
    val brainfuckProgram3 = ""

    val operationList = Parser().parse(brainfuckProgram0)
    operationList.forEach { println(it) }
}
