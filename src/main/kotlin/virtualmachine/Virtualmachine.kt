package virtualmachine

import virtualmachine.util.DataNode
import java.nio.charset.Charset
import kotlin.text.Charsets.US_ASCII
@ExperimentalUnsignedTypes
class Virtualmachine(val charset: Charset = US_ASCII) : VirtualMachineInterface {


    private var currentNode = DataNode()



    override fun movePointerLeft(x : Int) {
        var i = x
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
        var i = x
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
        currentNode.incrementData(x)
    }

    override fun decrementPointer(x : Int) {
        currentNode.decrementData(x)
    }

    override fun printChar() {
        print(currentNode.getData().toInt().toChar())
    }

    override fun readChar() {
        val data = readLine()?.get(0)?.toInt()
        if (data != null) {
            currentNode.setData(data)
        }
    }

    override fun loop(function : () -> Unit ) {


        while (this.currentNode.getData().toUInt() > 0u ){
            function()

        }
    }




    override fun getValue(): UByte {
        return this.currentNode.getData()
    }


}


