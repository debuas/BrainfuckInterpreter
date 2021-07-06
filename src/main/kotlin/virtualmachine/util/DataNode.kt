package virtualmachine.util



class DataNode {

    private var Left : DataNode? = null
    private var Right : DataNode? = null

    private var data : UByte = 0u


    public fun setData(x : Int = 0) {
        data = x.toUByte()
    }

    public fun getData() : UByte{
        return data
    }

    public fun incrementData(x : Int = 1) {
        data = ((data + x.toUInt()) % UByte.MAX_VALUE).toUByte()
    }
    public fun decrementData(x : Int = 1) {
        data = ((data - x.toUInt()) % UByte.MAX_VALUE).toUByte()
    }

    public fun setLeft(){
        if(Left == null) {
            Left = DataNode()
        }
    }

    public fun setRight(){
        if(Right == null) {
            Right = DataNode()
        }
    }

    public fun addLeft(x : DataNode ){
        if(Left == null) {
            Left = x;
        }
    }

    public fun addRight(x : DataNode ){
        if(Right == null) {
            Right = x;
        }
    }

    public fun getLeft() : DataNode? {
        return Left
    }
    public fun getRight() : DataNode? {
        return Right
    }





}