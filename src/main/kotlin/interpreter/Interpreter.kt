package interpreter

sealed class Operation {
    data class ChangeNodeValue(val amount: Int) : Operation()
    data class ChangePosition(val steps: Int) : Operation()
    data class Loop(val operationList: List<Operation>) : Operation()
    object PrintNodeValue : Operation()
    object SetNodeValue : Operation()
}

class Interpreter {

    fun parseStringToOperationList(rawOperationString: String): List<Operation>? {
        if (!isValidOperationString(rawOperationString)) {
            return null
        }

        return parseSegmentToOperationList(0, rawOperationString).second
    }

    fun isValidOperationString(rawOperationString: String): Boolean {
        var loopLevel = 0
        rawOperationString.forEach {
            when (it) {
                '[' -> loopLevel++
                ']' -> loopLevel--
            }
            if (loopLevel < 0) {
                return false
            }
        }

        return loopLevel == 0
    }

    fun sumSeriesOfRelatedOperations(relatedOperations: Pair<Char, Char>, _startPosition: Int, rawSegmentString: String): Pair<Int, Int> {
        var stepNumber = 0
        var position = _startPosition

        var currentChar = rawSegmentString.getOrNull(position)
        while ((currentChar != null) and ((currentChar == relatedOperations.first) or (currentChar == relatedOperations.second))) {
            if (currentChar == relatedOperations.first) {
                stepNumber--
            } else {
                stepNumber++
            }

            currentChar = rawSegmentString.getOrNull(++position)
        }

        position--
        return position to stepNumber
    }

    fun parseSegmentToOperationList(_startPosition: Int, rawSegmentString: String): Pair<Int, List<Operation>> {
        val operationList = mutableListOf<Operation>()

        var position = _startPosition
        while (position < rawSegmentString.length) {
            when (rawSegmentString[position]) {
                '-', '+' -> {
                    val result: Pair<Int, Int> = sumSeriesOfRelatedOperations(Pair('-', '+'), position, rawSegmentString)
                    operationList.add(Operation.ChangeNodeValue(result.second))
                    position = result.first
                }
                '<', '>' -> {
                    val result: Pair<Int, Int> = sumSeriesOfRelatedOperations(Pair('<', '>'), position, rawSegmentString)
                    operationList.add(Operation.ChangePosition(result.second))
                    position = result.first
                }
                '[' -> {
                    val loopSegment = parseSegmentToOperationList(position + 1, rawSegmentString)
                    operationList.add(Operation.Loop(loopSegment.second.toList()))
                    position = loopSegment.first
                }
                ']' -> return position to operationList.toList()
                '.' -> operationList.add(Operation.PrintNodeValue)
                ',' -> operationList.add(Operation.SetNodeValue)
            }
            position++
        }

        return position to operationList.toList()
    }
}


fun main() {
    val brainfuckProgram = ">+-+--[++[-+-] >>>>>>>>>>>>>>>>>>> + >> ]>><-+[+<+]>-"
    val brainfuckProgram1 = "<<[<++<<<><--+<+<<<]++.++.,<<<>>"

    val operationList = Interpreter().parseStringToOperationList(brainfuckProgram1)
    if (operationList == null) {
        println("$brainfuckProgram1 \nis not a valid brainfuck program.")
        println("Check if all loops start with '[' and end with ']'.")
    } else {
        println()
        operationList.forEach { println(it) }
    }
}
