package ru.serce.sample;

import static java.util.Arrays.asList;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.transform.ConditionalInterrupt;
import groovy.transform.ThreadInterrupt;
import groovy.transform.TimedInterrupt;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptException;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.tools.GeneralUtils;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;

public class ScriptService {
    
    GroovyShell shell;
    
    public static volatile boolean run = true;
    
    
    public ScriptService() {
        Binding bindings = new Binding();
        bindings.setVariable("user", new User());
        bindings.setVariable("timeService", new TimeService());
        
        CompilerConfiguration configuration = new CompilerConfiguration();
        Map<String, Object> map = new HashMap<>();
        map.put("value", 5);
        map.put("unit", new PropertyExpression(
                new ClassExpression(ClassHelper.make(TimeUnit.class)), "SECONDS"));
        configuration.addCompilationCustomizers(
                new ASTTransformationCustomizer(map, TimedInterrupt.class));
        configuration.setOutput(new PrintWriter(System.out));
        
        //...
        configuration.addCompilationCustomizers(
                new ASTTransformationCustomizer(ThreadInterrupt.class));
        //...

        //...
        Map<String, Object> mapCondInterr = new HashMap<>();
        try {
            mapCondInterr.put("value", new ClosureExpression(Parameter.EMPTY_ARRAY, 
                                GeneralUtils.returnS(
                                        GeneralUtils.eqX(
                                            GeneralUtils.fieldX(
                                                    FieldNode.newStatic(
                                                            ScriptService.class, "run")), 
                                            GeneralUtils.constX(false)) 
                                            )));
        } catch (SecurityException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        configuration.addCompilationCustomizers(
                new ASTTransformationCustomizer(mapCondInterr, ConditionalInterrupt.class));
        //...
        
        //...
        SecureASTCustomizer secureASTCustomizer = new SecureASTCustomizer();
        secureASTCustomizer.setClosuresAllowed(false);
        secureASTCustomizer.setImportsBlacklist(asList("java.lang.Math"));
        // etc...
        configuration.addCompilationCustomizers(secureASTCustomizer);
        //...
        
        //.. 
        ImportCustomizer importCustomizer = new ImportCustomizer();
        importCustomizer.addImports("sun.misc.Unsafe");
        importCustomizer.addStaticImport("java.lang.Double", "POSITIVE_INFINITY");
        configuration.addCompilationCustomizers(importCustomizer);
        //..
        
        shell = new GroovyShell(bindings, configuration);
    }

    public Object execute(String script) throws ScriptException {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(3000L);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                run = false;
//            }
//        }).start();
        return shell.evaluate(script);
    }
}

