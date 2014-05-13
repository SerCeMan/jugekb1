package ru.serce.sample;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class MyProgram {
    
    static ScriptService service = new ScriptService();
    
    public static void main(String[] args) throws ScriptException {
        String script = "while(true) { Thread.sleep(1000L); print 1 }";
        Object result = service.execute(script);
        System.out.println(result);
    }

    private static String template = 
              "public class Main {" + 
              "    public static Object doSome() {" + 
              "        %s" +
              "    }" + 
              "}";
    
    private static Object executeScriptGroovyShell(String script) throws ScriptException {
        GroovyShell shell = new GroovyShell();
        return shell.evaluate(script);     
    }
    
    private static Object executeScriptJavascript(String script) throws ScriptException {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("js");
    
        // basic example
        System.out.println(engine.eval("var a = 2; a*a"));
        
        return 1;
    }
    
    private static Object executeScriptJavassist(String script) {
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass cc = pool.makeClass("Main");
            CtMethod method = CtMethod.make(
                    "public static Object doSome() {" + 
                            script + 
                    "}", cc);
            cc.addMethod(method);
            Method m = cc.toClass().getMethod("doSome");
            return m.invoke(null, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object executeScriptJSR199(String script) {
        JavaSourceFromString javaSource = 
                new JavaSourceFromString("Main", format(template, script));
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        StringWriter sw = new StringWriter();
        Boolean result = compiler.getTask(sw, null, null, null, null, 
                asList(javaSource)).call();
        if (result) {
            try (URLClassLoader ucl = new URLClassLoader(new URL[] { 
                    new File("Main.class").getAbsoluteFile()
                        .getParentFile().toURI().toURL() })) {
                
                Method method = ucl.loadClass("Main").getMethod("doSome");
                return method.invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException("Error during execution", e);
            }
        } else {
            throw new RuntimeException("Error during compiling:\n" + sw.toString());
        }
    }
    
    // Сергей Целовальников
    // Naumen
    // sergeicelov@gmail.com
    // @serceman
    
    /**
     * Сергей Целовальников
     * Naumen
     * sergeicelov@gmail.com
     * @serceman
     * 
     * github.com/SerCeMan/jugekb1.git
     */
    
    
}
