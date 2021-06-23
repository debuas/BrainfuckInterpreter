
// directly to ChangeNodeValue or with lowering from
// Increase/DecreaseNodeValue to ChangeNodeValue?
sealed class Operation {
    data class ChangePointerPosition(val steps: Int): Operation()
    data class ChangeNodeValue(val amount: Int): Operation()
    data class StartLoop(val loopLevel: Int): Operation()
    data class EndLoop(val loopLevel: Int): Operation()
    class PrintNodeValue : Operation()  // data class needs at least one property
    class SetNodeValue: Operation()     // use object instead??
}

// alternative: data class Loop(val startOperation: OperationNode, val endOperation: OperationNode)
// ??


class BrainfuckInterpreter {


    // double linked lists for data and commands

    fun executeOperation(operation: Operation): Boolean {
        when (operation) {
            is Operation.ChangePointerPosition -> TODO()
            is Operation.ChangeNodeValue -> TODO()
            is Operation.StartLoop -> TODO()
            is Operation.EndLoop -> TODO()
            is Operation.PrintNodeValue -> TODO()
            is Operation.SetNodeValue -> TODO()
        }
    }
}



fun main() {
    val test: Operation = Operation.PrintNodeValue()
    println(test)
}