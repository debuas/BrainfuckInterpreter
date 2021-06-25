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

    fun parseSegmentToOperationList(_startPosition: Int, rawSegmentString: String): Pair<Int, List<Operation>> {
        val operationList = mutableListOf<Operation>()
        var stepNumber = 0

        var position = _startPosition
        while (position < rawSegmentString.length) {
            when (val currentChar = rawSegmentString[position]) {
                '+', '-' -> {
                    if (currentChar == '+') stepNumber++ else stepNumber--

                    val nextChar = (rawSegmentString.getOrNull(position + 1))
                    if ((nextChar == null) or (nextChar != '+') and (nextChar != '-')) {
                        operationList.add(Operation.ChangeNodeValue(stepNumber))
                        stepNumber = 0
                    }
                }
                '>', '<' -> {
                    if (currentChar == '>') stepNumber++ else stepNumber--

                    val nextChar = (rawSegmentString.getOrNull(position + 1))
                    if ((nextChar == null) or (nextChar != '>') and (nextChar != '<')) {
                        operationList.add(Operation.ChangePosition(stepNumber))
                        stepNumber = 0
                    }
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
