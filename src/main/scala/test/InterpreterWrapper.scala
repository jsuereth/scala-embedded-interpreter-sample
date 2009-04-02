package test

import scala.tools.nsc._
import interpreter._
import java.io._

class InterpreterWrapper {

  class MyInterpreterLoop(out : PrintWriter) extends InterpreterLoop(None, out) {
    
    
     override def bindSettings() {       
       super.bindSettings()      
       interpreter beQuietDuring {
         interpreter.bind("josh", "java.lang.String", "awesome")
         //interpreter.bind("settings", "scala.tools.nsc.InterpreterSettings", isettings)
       }
     }
  }
  
  def startInterpreting_!() {
    val settings = new Settings()
    settings.completion.value = true
    
    val interpreter = new MyInterpreterLoop(new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out))))
    interpreter.main(settings)
    
    ()
  }
}
