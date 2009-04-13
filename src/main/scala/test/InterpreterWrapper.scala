package test

import scala.tools.nsc._
import interpreter._
import java.io._
import scala.reflect._
trait InterpreterWrapper {

  private var bindings : Map[String, (java.lang.Class[_], AnyRef)] = Map()
  /**
   * Binds a given value into the interpreter when it starts with its most specific class
   */
  def bind[A <: AnyRef](name : String, value : A)(implicit m : Manifest[A]) {
    bindings +=  (( name,  (m.erasure, value)))
  }
  /**
   * Binds a given value itnot he interpreter with a given interface/higher-level class.
   */
  def bindAs[A <: AnyRef, B <: A](name : String, interface : java.lang.Class[A], value : B) {
    bindings += ((name,  (interface, value)))
  }
  
  /**
   * This class actually runs the interpreter loop.
   */
  class MyInterpreterLoop(out : PrintWriter) extends InterpreterLoop(None, out) {
    
    
     override def bindSettings() {       
       super.bindSettings()      
       interpreter beQuietDuring {
         for( (name, (clazz, value)) <- bindings) {
           interpreter.bind(name, clazz.getCanonicalName, value)
         }
         
         
        
       }
     }
  }
  
  def startInterpreting() {
    val settings = new Settings()
    //Below for 2.8.0-SNAPSHOT
    //settings.completion.value = true
    
    val interpreter = new MyInterpreterLoop(new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out))))
    interpreter.main(settings)
    
    ()
  }
}
