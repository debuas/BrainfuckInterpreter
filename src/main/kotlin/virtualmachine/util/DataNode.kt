package virtualmachine.util


class DataNode {

    private var Left : DataNode? = null
    private var Right : DataNode? = null

    private var data : Int = 0


    public fun setData(x : Int = 0) {
        data = x
    }

    public fun getData() : Int{
        return data
    }

    public fun incrementData(x : Int = 1) {
        data += x
    }
    public fun decrement(x : Int = 1) {
        data -= x
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