package org.trad.pcl.annotation;

import com.diogonunes.jcolor.Attribute;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import static com.diogonunes.jcolor.Ansi.colorize;

@Aspect
public class MethodLoggerAspect {

    @Pointcut("@annotation(org.trad.pcl.annotation.PrintMethodName) && execution(* *(..))")
    public void log() {
    }

    @Before("log()")
    public void logMethod(JoinPoint jp) {
        System.out.println("\t↪️ " + colorize("Parser rule ", Attribute.GREEN_TEXT()) + colorize(jp.getSignature().getName(), Attribute.MAGENTA_TEXT()) + colorize(" called", Attribute.GREEN_TEXT()));
    }

}

