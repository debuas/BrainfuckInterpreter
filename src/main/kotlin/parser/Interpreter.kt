package parser

import virtualmachine.VirtualMachineInterface
import virtualmachine.Virtualmachine
import java.io.File
import java.io.InputStream
import java.io.Reader
import java.lang.Exception
import kotlin.time.measureTime


class Interpreter {

    val vm: VirtualMachineInterface

    init {
        @ExperimentalUnsignedTypes
        this.vm = Virtualmachine()
    }

    /**
     * Interpreter using optimized Operations
     * @param ops List containing an Operations to be executed by the VM
     * */
    fun runOptimizedOperations(ops: List<OptimizedOperation>) {



        ops.forEach {

            when (it) {

                is OptimizedOperation.ChangeNodeValue -> {
                    if (it.amount > 0) this.vm.incrementPointer(it.amount)
                    else if (it.amount < 0) this.vm.decrementPointer(it.amount * -1)
                }
                is OptimizedOperation.ChangePosition -> {
                    if (it.steps > 0) this.vm.movePointerRight(it.steps)
                    else if (it.steps < 0) this.vm.movePointerLeft(it.steps * -1)
                }
                is OptimizedOperation.Loop -> {
                    this.vm.loop({ this.runOptimizedOperations(it.operationList) })
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


    fun runNoOptimized(ops: List<StructuredOperation>) {




        ops.forEach {

            when (it) {

                is StructuredOperation.IncrementNodeValue -> {
                    this.vm.incrementPointer()
                }
                is StructuredOperation.DecrementNodeValue -> {
                    this.vm.decrementPointer()
                }
                is StructuredOperation.MovePointerLeft -> {
                    this.vm.movePointerLeft()
                }
                is StructuredOperation.MovePointerRight -> {
                    this.vm.movePointerRight()
                }
                is StructuredOperation.SetNodeValue -> {
                    this.vm.readChar()
                }
                is StructuredOperation.PrintNodeValue -> {
                    this.vm.printChar()
                }
                is StructuredOperation.Loop -> {
                   this.vm.loop { this.runNoOptimized(it.operationList) }
                }
            }

        }

    }


    fun runtime() {
        println("Running in Runtime Mode. Type '?' to get the decimal value of the current cell. Type 'exit' to leave.")

        val parser = Parser()
        while (true) {
            println()
            print(">> ")

            val buffer = readLine()
            if (buffer == null) {
                println("Please enter your commands.")
                continue
            }
            if (buffer == "exit") {
                return
            }
            if (buffer == "?") {
                println(this.vm.getValue())
                continue
            }

            try {
                runNoOptimized(parser.parse(buffer))
            } catch (e: Exception) {
                println(e.message)
                continue
            }
        }
    }
}