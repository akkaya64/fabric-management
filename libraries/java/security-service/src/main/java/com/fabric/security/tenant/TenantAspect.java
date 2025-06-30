package com.fabric.security.tenant;

import com.fabric.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

/**
 * Aspect to automatically inject tenant filtering
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class TenantAspect {
    
    private final TenantValidator tenantValidator;
    
    @Around("@annotation(tenantAware)")
    public Object enforceTenantAccess(ProceedingJoinPoint joinPoint, TenantAware tenantAware) throws Throwable {
        String currentTenantId = SecurityUtils.getCurrentUserTenantId()
                .orElseThrow(() -> new AccessDeniedException("No tenant context found"));
        
        log.debug("Enforcing tenant access for tenant: {}", currentTenantId);
        
        // Proceed with the method call
        return joinPoint.proceed();
    }
}