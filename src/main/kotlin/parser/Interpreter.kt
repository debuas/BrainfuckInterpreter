package parser

import virtualmachine.VirtualMachineInterface
import virtualmachine.Virtualmachine
import java.io.File
import java.io.InputStream
import java.io.Reader
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


        var buffer : String? = ""
        var level = 0

        var loopbuffer  = ""

        println("Running in RunTime Mode. Type 'exit' to leave .")
        println("To get the current Cell numerical value type '?'.")

        while (true) {
            if (buffer == null) println()
            for (i in 0..level ) {
                print(">")
            }
            print(" ")
            buffer = readLine()



            if (buffer == null) {
                continue
            }

            if (buffer == "exit") {
                return
            }

            buffer.forEach {
                    when (it) {
                        '+' -> {
                            if (level == 0) this.vm.incrementPointer() else loopbuffer += it
                        }
                        '-' -> {
                            if (level == 0) this.vm.decrementPointer() else loopbuffer += it
                        }
                        '<' -> {
                            if (level == 0) this.vm.movePointerLeft() else loopbuffer += it
                        }
                        '>' -> {
                            if (level == 0) this.vm.movePointerRight() else loopbuffer += it
                        }
                        '.' -> {
                            if (level == 0) this.vm.printChar() else loopbuffer += it
                        }
                        ',' -> {
                            if (level == 0) this.vm.readChar() else loopbuffer += it
                        }
                        '[' -> {
                            level++
                            loopbuffer += it
                        }
                        ']' -> {
                            if (level == 0) {
                                println("Syntax Error")

                            }else {
                                level--
                                loopbuffer += it
                                if (level == 0) {
                                    run { this.runNoOptimized(Parser().parse("+-" + loopbuffer)) }
                                    loopbuffer = ""
                                }
                            }
                        }
                        '?' -> println(this.vm.getValue())
                    }



            }
        }
    }


}