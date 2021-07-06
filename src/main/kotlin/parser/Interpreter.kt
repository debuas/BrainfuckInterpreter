package parser

import com.sun.tools.attach.VirtualMachine
import virtualmachine.VirtualMachineInterface
import virtualmachine.Virtualmachine
import java.io.File
import java.nio.ByteBuffer


class Interpreter {

    val vm : VirtualMachineInterface

    init {
        @ExperimentalUnsignedTypes
        this.vm = Virtualmachine()
    }
    /**
     * Interpreter using optimized Operations
     * @param ops List containing an Operations to be executed by the VM
     * */
    fun runOptimizedOperations(ops: List<OptimizedOperation>?) {

        if (ops == null) return

        ops.forEach {

            when (it) {

                is OptimizedOperation.ChangeNodeValue -> {
                    if (it.amount > 0) this.vm.incrementPointer(it.amount)
                    else if (it.amount < 0) this.vm.decrementPointer(it.amount*-1)
                }
                is OptimizedOperation.ChangePosition -> {
                    if (it.steps > 0) this.vm.movePointerRight(it.steps)
                    else if (it.steps < 0) this.vm.movePointerLeft(it.steps*-1)
                }
                is OptimizedOperation.Loop -> {
                    this.vm.loop({ this.runOptimizedOperations(it.operationList) } )
                }
                is OptimizedOperation.PrintNodeValue -> {
                    this.vm.printChar()
                }
                is OptimizedOperation.SetNodeValue -> {
                    this.vm.readChar()
                }

            }

        }
    }
}