import parser.Interpreter
import parser.Parser
import java.io.File
import kotlin.io.*

/**For -h -? or -help output*/
private enum class Command (val command : Array<String>, val commandparam : String, val description : String ){
    help    ( arrayOf("-h" , "-help" , "-?") , "" , "List Of All Commands and Options\n" ),
    file    ( arrayOf("-f" , "-F" , "-File") ,"<File>", "Use a File as Input "),
    string     ( arrayOf("-s") , "<String>" , "Pass a String as Input" );

}

val fileending  = arrayOf("b" , "bf")

fun printHelpContext() {

    println("Available Commands:")
    Command.values().forEach { it.command.forEach { x->
        println("\t${x} ${it.commandparam}")
    }
        println("\t\t${it.description}")
    }
}

fun runAsStringParam(data : String) {
    //println("DataString : $data")

    val interpreter = Interpreter()

    //println("Size : ${data.length} Chars")
    //interpreter.runOptimizedOperations(
    //    Parser().parseStringToOperationList(data)
    //)
    interpreter.runNoOptimized(
        Parser().parse2(data)
    )
}





fun runAsFileParam(data: String) {
    //println("FileString : $data")

    val file = File(data)

    if (file.exists() ){
        if (file.isFile) {
            if (file.extension in arrayOf("b" , "bf")) {
                file.reader().toString()
                //println("Filedata:${String(file.inputStream().readAllBytes())}")
                runAsStringParam(String(file.inputStream().readAllBytes()))

            } else println("Wrong file Type.")
        } else println("$data not a file")
    } else println("File $data does not exists")



}



fun main(args: Array<String>) {

    val arg = args.iterator()

    while (arg.hasNext()) {




        when (arg.next()) {

            in Command.help.command -> {
                printHelpContext()
                return
            }
            in Command.string.command -> {
                if (arg.hasNext()) runAsStringParam(arg.next())
                return
            }
            in Command.file.command -> {
                if(arg.hasNext()) runAsFileParam(arg.next())
                return
            }


        }

    }
}


