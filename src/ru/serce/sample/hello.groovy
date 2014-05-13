import groovy.transform.TimedInterrupt;



def a = 5

def hello() {
    'Hello'
}

assert hello() == 'Hello'


def clos = { ++a }

assert clos() == 6



def name = 'JUG'

println "Hello, ${name}!"

def longstr = '''
        Hello,
        Multi
        Line
    '''

// new methods
assert '123'.reverse() == '321'
assert 'abc'.capitalize() == 'Abc'

// etc...




def set = [] as Set

def list = [1, 2, 3, 4]

list << 4

assert list == [1, 2, 3, 4, 4]
assert list.findAll {  it == 4  }.size() == 2
assert 2 in list
assert [1, 2, 3].join('-') == '1-2-3'
assert [1] + [2] == [1, 2]


def someMap = [ 'a' : 1, 'b' : 2 ]

assert someMap['a'] == 1
assert someMap.b    == 2






class Pair {
    def l
    def r
    
    def setL(def n) {
        r = l = n
    }
}

Pair p = new Pair()

p.setL(1) // ok
p.l = 2   // setter called

assert p.r == 2 // getter called


//Author: SerCe
//Creation Date: 10.05.2014 
//API Version: 1.1
/**
 * Отправляет нас в 3000 год, но только если
 * пользователь - Бендер!
 */

if (user.name == 'Bender') {
    logger.info("Age = ${user.age}")
    
    timeService.go
}


def result = 3.km + 2.m - 5.cm



@Deprecated
class Bar {
    def foo() {}
}
def bar = new Bar() 
bar.recreate()






import groovy.transform.TimedInterrupt
import java.util.concurrent.TimeUnit

@TimedInterrupt(value = 300L, unit = TimeUnit.SECONDS)
class MyClass {
     def method() {
         println '...'
     }
}










import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

public class MyClass {
    // XXXXXX below is a placeholder for a hashCode value at runtime
    final private long timedInterruptXXXXXX$expireTime
    final private java.util.Date timedInterruptXXXXXX$startTime

    public MyClass() {
        timedInterruptXXXXXX$expireTime = System.nanoTime() + 
            TimeUnit.NANOSECONDS.convert(300, TimeUnit.SECONDS)
        timedInterruptXXXXXX$startTime = new java.util.Date()
    }

    public java.lang.Object method() {
        if (timedInterruptXXXXXX$expireTime < System.nanoTime()) {
            throw new TimeoutException('Execution timed out after 300 units. ' + '
                Start time: ' + timedInterruptXXXXXX$startTime)
        }
        return this.println('...')
    }
}






@ConditionalInterrupt({ counter++> 10})
import groovy.transform.ConditionalInterrupt

counter = 0
def scriptMethod() {
     4.times {
         println 'executing script method...'
     }
}





SecureASTCustomizer secure = new SecureASTCustomizer()
secure.with {
    closuresAllowed = false
    methodDefinitionAllowed = false

    importsWhitelist = []
    staticImportsWhitelist = []
    staticStarImportsWhitelist = ['java.lang.Math'] 

    tokensWhitelist = [
            PLUS, MINUS, MULTIPLY, DIVIDE, MOD, POWER, 
            PLUS_PLUS, MINUS_MINUS, COMPARE_EQUAL, COMPARE_NOT_EQUAL, 
            COMPARE_LESS_THAN, COMPARE_LESS_THAN_EQUAL, 
            COMPARE_GREATER_THAN, COMPARE_GREATER_THAN_EQUAL,
    ].asImmutable()

    constantTypesClassesWhiteList = [
            Integer, Float, Long, Double, BigDecimal,
            Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE
    ].asImmutable()

    receiversClassesWhiteList = [ Math, Integer, Float, Double, Long, 
        BigDecimal ].asImmutable()
}

// configuration ends here

CompilerConfiguration config = new CompilerConfiguration()
config.addCompilationCustomizers(imports, secure)
GroovyClassLoader loader = new GroovyClassLoader(this.class.classLoader, config)