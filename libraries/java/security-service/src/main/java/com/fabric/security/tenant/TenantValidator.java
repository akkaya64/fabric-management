package com.fabric.security.tenant;

import com.fabric.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

/**
 * Validates tenant access for operations
 */
@Slf4j
@Component
public class TenantValidator {
    
    /**
     * Validates that the current user has access to the specified tenant
     */
    public void validateTenantAccess(String tenantId) {
        String currentTenantId = SecurityUtils.getCurrentUserTenantId()
                .orElseThrow(() -> new AccessDeniedException("No tenant context found"));
        
        if (!currentTenantId.equals(tenantId)) {
            log.warn("Tenant access violation: user from tenant {} trying to access tenant {}", 
                    currentTenantId, tenantId);
            throw new AccessDeniedException("Access denied to tenant: " + tenantId);
        }
    }
    
    /**
     * Gets the current tenant ID and validates it exists
     */
    public String getCurrentTenantId() {
        return SecurityUtils.getCurrentUserTenantId()
                .orElseThrow(() -> new AccessDeniedException("No tenant context found"));
    }
    
    /**
     * Checks if current user belongs to specific tenant
     */
    public boolean belongsToTenant(String tenantId) {
        return SecurityUtils.getCurrentUserTenantId()
                .map(currentTenant -> currentTenant.equals(tenantId))
                .orElse(false);
    }
}