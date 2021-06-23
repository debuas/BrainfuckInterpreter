package virtualmachine

import virtualmachine.util.DataNode
import java.nio.charset.Charset
import kotlin.text.Charsets.US_ASCII
class Virtualmachine(val charset: Charset = US_ASCII) : VirtualMachineInterface {


    private var currentNode = DataNode()



    override fun movePointerLeft(x : Int) {
        //TODO("Not yet implemented")
        if (currentNode.getLeft() == null) {
            currentNode.setLeft()
            currentNode.getLeft()?.addRight(currentNode)
            currentNode = currentNode.getLeft()!!
        }
    }

    override fun movePointerRight(x : Int) {
        //TODO("Not yet implemented")
        if (currentNode.getRight() == null) {
            currentNode.setRight()
            currentNode.getRight()?.addLeft(currentNode)
            currentNode = currentNode.getRight()!!
        }
    }

    override fun incrementPointer(x : Int) {
        //TODO("Not yet implemented")
        currentNode.incrementData(x)
    }

    override fun decrementPointer(x : Int) {
        //TODO("Not yet implemented")
        currentNode.incrementData(x)
    }

    override fun printChar() {
        //TODO("Not yet implemented")
        print(currentNode.getData().toChar())
    }

    override fun readChar() {
        //TODO("Not yet implemented")
        val data = readLine()?.get(0)?.toInt()
        if (data != null) {
            currentNode.setData(data)
        }
    }

    override fun loop(function : Unit) {
        //TODO("Not yet implemented")


        while (this.currentNode.getData() != 0){
            function
        }
    }




    override fun getValue(): Int {
        //TODO("Not yet implemented")
        return this.currentNode.getData()
    }
}


