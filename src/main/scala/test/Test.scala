package test

object Test {
  
   def main(args : Array[String]) : Unit =  {
     val interpreter = new InterpreterWrapper() {
       bind("josh", "awesome")
     }
     
     interpreter.startInterpreting
   }
}
