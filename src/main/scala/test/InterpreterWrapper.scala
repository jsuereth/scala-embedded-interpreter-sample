package test

import scala.tools.nsc._
import interpreter._
import java.io._
import scala.reflect._
/**
 * A trait to ease the embedding of the scala interpreter
 */
trait InterpreterWrapper {

  private var bindings : Map[String, (java.lang.Class[_], AnyRef)] = Map()
  private var packageImports : List[String] = List()  
  private var files = List[String]()
  
  /**
   * Binds a given value into the interpreter when it starts with its most specific class
   */
  protected def bind[A <: AnyRef](name : String, value : A)(implicit m : Manifest[A]) {
    bindings +=  (( name,  (m.erasure, value)))
  }
  /**
   * Binds a given value itnot he interpreter with a given interface/higher-level class.
   */
  protected def bindAs[A <: AnyRef, B <: A](name : String, interface : java.lang.Class[A], value : B) {
    bindings += ((name,  (interface, value)))
  }
  
  /**
   * adds an auto-import for the interpreter.
   */
  protected def autoImport(importString : String) {
    packageImports = importString :: packageImports
  }
  
  /**
   * Adds a file that will be interpreter at the start of the interpreter
   */
  protected def addScriptFile(fileName : String) {
    files = fileName :: files
  }
  
  def helpMsg : String
  def welcomeMsg : String
  def prompt : String
  
  /**
   * This class actually runs the interpreter loop.
   */
  class MyInterpreterLoop(out : PrintWriter) extends InterpreterLoop(None, out) {
     override val prompt = InterpreterWrapper.this.prompt
    
     override def bindSettings() {       
       super.bindSettings()      
       interpreter beQuietDuring {
         for( (name, (clazz, value)) <- bindings) {
           interpreter.bind(name, clazz.getCanonicalName, value)
         }
         for( importString <- packageImports) {
        	 interpreter.interpret("import " + importString)
         } 
       }
     }
    override def printHelp {
       printWelcome()
       out.println(helpMsg)
       out.flush()
     }
     
     override def printWelcome() {
       out.println(welcomeMsg)
       out.flush()
     }
  }
  
  def startInterpreting() {
    val out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)))
    val settings = new GenericRunnerSettings(out.println _)
    files.foreach(settings.loadfiles.appendToValue)
    //Below for 2.8.0-SNAPSHOT
    //settings.completion.value = true
    
    val interpreter = new MyInterpreterLoop(out)
    interpreter.main(settings)
    ()
  }
}
