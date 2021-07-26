import parser.Interpreter
import parser.Operation
import parser.OptimizedOperation
import parser.Parser
import java.io.File
import kotlin.io.*

/**For -h -? or -help output*/
private enum class Command (val command : Array<String>, val commandparam : String, val description : String ){
    help    ( arrayOf("-h" , "-help" , "-?") , "" , "List Of All Commands and Options\n" ),
    file    ( arrayOf("-f" , "-F" , "-File") ,"<File>", "Use a File as Input "),
    string  ( arrayOf("-s") , "<String>" , "Pass a String as Input" ),
    optimized ( arrayOf("-o"), "" , "Run in optimized Mode "),
    runtime ( arrayOf("-r"), "", "Start Runtime Mode");
}

enum class Mode {
    Optimized ,
    Unoptimized,
    Stringmode,
    Filemode
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

fun runAsStringParam(data : String , mode : Mode) {


    val interpreter = Interpreter()
    when (mode) {
        Mode.Unoptimized -> {
            interpreter.runNoOptimized(
                Parser().parse(data)
            )
        }
        Mode. Optimized -> {
            interpreter.runOptimizedOperations(
                run {
                val p = Parser()
                p.validate(data)
                p.optimize(0, p.lexer(data)).second
                })
            }

        else -> {}
        }
    }



fun runAsFileParam(data: String , mode : Mode) {

    val file = File(data)

    if (file.exists() ){
        if (file.isFile) {
            if (file.extension in arrayOf("b" , "bf")) {
                file.reader().toString()
                runAsStringParam(String(file.inputStream().readAllBytes()),mode)

            } else println("Wrong file Type.")
        } else println("$data not a file")
    } else println("File $data does not exists")



}



fun main(args: Array<String>) {

    val arg = args.iterator()

    var config : Pair<MutableList<Mode>, String>? = null

    val tmplist = mutableListOf<Mode>()

    while (arg.hasNext()) {

        when (arg.next()) {

            in Command.optimized.command -> {
                tmplist.add(Mode.Optimized)
            }

            in Command.help.command -> {
                printHelpContext()
                return
            }
            in Command.runtime.command -> {
                Interpreter().runtime()
                return
            }

            in Command.string.command -> {
                if (arg.hasNext()) {
                    tmplist.add(Mode.Stringmode)
                    if(!tmplist.contains(Mode.Optimized)) tmplist.add(Mode.Unoptimized)
                    config = Pair(tmplist, arg.next())
                    break
                }

            }
            in Command.file.command -> {
                if(arg.hasNext()) {
                    tmplist.add(Mode.Filemode)
                    if(!tmplist.contains(Mode.Optimized)) tmplist.add(Mode.Unoptimized)
                    config = Pair(tmplist, arg.next())
                    break
                }
            }

        }
    }
        if (config != null) {
            if (config.first.contains(Mode.Stringmode)) {
                if (config.first.contains(Mode.Optimized)) runAsStringParam(config.second, Mode.Optimized)
                else if (config.first.contains(Mode.Unoptimized)) runAsStringParam(config.second, Mode.Unoptimized)
            } else if (config.first.contains(Mode.Filemode)) {
                if (config.first.contains(Mode.Optimized)) runAsFileParam(config.second, Mode.Optimized)
                else if (config.first.contains(Mode.Unoptimized)) runAsFileParam(config.second, Mode.Unoptimized)
            }
        }


}


