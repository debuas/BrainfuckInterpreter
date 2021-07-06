package virtualmachine

import virtualmachine.util.DataNode
import java.nio.charset.Charset
import kotlin.text.Charsets.US_ASCII
@ExperimentalUnsignedTypes
class Virtualmachine(val charset: Charset = US_ASCII) : VirtualMachineInterface {


    private var currentNode = DataNode()



    override fun movePointerLeft(x : Int) {
        //TODO("Not yet implemented")
        var i = x
    //    println("MoveLeft by $x")
        while(i > 0){
            if (currentNode.getLeft() == null) {
                currentNode.setLeft()
                currentNode.getLeft()?.addRight(currentNode)
            }
            currentNode = currentNode.getLeft()!!
            i--
        }
    }

    override fun movePointerRight(x : Int) {
        //TODO("Not yet implemented")
        var i = x
    //    println("MoveRight by $x")
        while (i > 0){
            if (currentNode.getRight() == null) {
                currentNode.setRight()
                currentNode.getRight()?.addLeft(currentNode)
            }
            this.currentNode = this.currentNode.getRight()!!
            i--
        }
}

    override fun incrementPointer(x : Int) {
        //TODO("Not yet implemented")
    //    println("Increment by $x")
        currentNode.incrementData(x)
    }

    override fun decrementPointer(x : Int) {
    //    println("Decrement by $x")
        //TODO("Not yet implemented")
        currentNode.decrementData(x)
    }

    override fun printChar() {
        //TODO("Not yet implemented")
    //    println("\n ${currentNode.getData()} \n")
        print(currentNode.getData().toInt().toChar())
    }

    override fun readChar() {
        //TODO("Not yet implemented")
        val data = readLine()?.get(0)?.toInt()
        if (data != null) {
            currentNode.setData(data)
        }
    }

    override fun loop(function : () -> Unit ) {
        //TODO("Not yet implemented")


        while (this.currentNode.getData().toUInt() > 0u ){
            //println("Loop Value Begin = ${getValue()}")
            function()
            //println("Loop Value End = ${getValue()}")

        }
    }




    override fun getValue(): UByte {
        //TODO("Not yet implemented")
    //    println("Debug Node Value : ${this.currentNode.getData()}")
        return this.currentNode.getData()
    }


}


