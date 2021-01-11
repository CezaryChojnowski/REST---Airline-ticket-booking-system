package pl.edu.pb.mongodbapplication.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Aspect
@Component
public class ExampleAspect {

    @Around("@annotation(LogExecutionInfo)")
    public Object logExecutionInfo(ProceedingJoinPoint joinPoint) throws Throwable {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String user = userDetails.getUsername();

        Logger logger;

        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        InetAddress ip;
        String hostname;
        ip = InetAddress.getLocalHost();
        hostname = ip.getHostName();

        StringBuilder sb = new StringBuilder();

        sb.append(ip.getHostAddress())
                .append(", ")
                .append(hostname)
                .append(", ")
                .append(joinPoint.getSignature().getDeclaringTypeName())
                .append(", ")
                .append(joinPoint.getSignature().getName()).append(", args:");
        for (Object o : joinPoint.getArgs()) {
            sb.append(o).append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(", ")
                .append(executionTime)
                .append(" ms");

        FileHandler fileHandler = new FileHandler(user + ".log", true);
        logger = Logger.getLogger(user);
        logger.addHandler(fileHandler);
        SimpleFormatter simpleFormatter = new SimpleFormatter();
        fileHandler.setFormatter(simpleFormatter);

        logger.log(Level.INFO, sb.toString());

        fileHandler.close();
        return proceed;
    }
}
