package test

class MyClass(val name : String) {
  def is = "Brillant!!!" //mispelled to be like PaulaBean
  override def toString = "MyClass("+name+")"
}

object Test {
  
   def main(args : Array[String]) : Unit =  {
     val interpreter = new InterpreterWrapper() {
       def prompt = "TestInterpreter> "
       def welcomeMsg = """Welcome to Awesomeness!
This is my version of the Scala interpreter"""
       def helpMsg = """This is where I should
list help of commands.  For now:
:help - Prints this message
:quit - Makes me sad"""
       bind("josh", new MyClass("josh"))
       autoImport("test._")
     }
     
     interpreter.startInterpreting
   }
}
