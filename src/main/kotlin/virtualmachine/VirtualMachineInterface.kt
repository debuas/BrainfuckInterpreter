package virtualmachine

interface VirtualMachineInterface {
    
    fun movePointerLeft(x : Int = 1)

    fun movePointerRight(x : Int = 1)

    fun incrementPointer(x : Int = 1)

    fun decrementPointer(x : Int = 1)

    fun printChar()

    fun readChar()

    fun loop(function : () -> Unit)

    fun getValue() : UByte?






}