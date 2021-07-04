package parser

sealed class Operation {
    data class ChangeNodeValue(val amount: Int) : Operation()
    data class ChangePosition(val steps: Int) : Operation()
    data class Loop(val operationList: List<Operation>) : Operation()
    object PrintNodeValue : Operation()
    object SetNodeValue : Operation()
}

class Parser {

    fun parseStringToOperationList(rawOperationString: String): List<Operation>? {
        if (!isValidOperationString(rawOperationString)) {
            return null
        }

        return parseSegmentToOperationList(0, rawOperationString).second
    }

    fun isValidOperationString(rawOperationString: String): Boolean {
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
                    loopLevel--
                    if (!closed) {
                        throw Exception("At Position $currentPosition: Tried to close non-existent loop.")
                    }
                }
            }
        }

        if (openLoops.isNotEmpty()) {
            val openLoop = openLoops.first()
            throw Exception("At Position ${openLoop.first}: Loop started with '[' but was never closed.")
        } else {
            return true
        }
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
    val brainfuckProgram0 = ">+-+--[++[-+-] >>>>>>>>>>>>>>>>>>> + >> ]>><-+[+<+]>-"
    val brainfuckProgram1 = "<<[<++<<<><--+<+<<<]++.++.,<<<>>"
    val brainfuckProgram2 = "<<[<++]just some comments[<[<<><[--+[<+<<<]++.++].],<<<>>"
    val brainfuckProgram3 = ""

    val operationList = Parser().parseStringToOperationList(brainfuckProgram2)
    operationList?.forEach { println(it) }
}
