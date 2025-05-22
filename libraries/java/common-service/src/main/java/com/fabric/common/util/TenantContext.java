package com.fabric.common.util;

import lombok.extern.slf4j.Slf4j;

/**
 * Thread-local tenant context for multi-tenant applications
 */
@Slf4j
public final class TenantContext {
    
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();
    
    private TenantContext() {
        // Utility class
    }
    
    /**
     * Set the current tenant ID for the current thread
     */
    public static void setCurrentTenant(String tenantId) {
        if (tenantId != null) {
            CURRENT_TENANT.set(tenantId);
            log.debug("Set current tenant to: {}", tenantId);
        }
    }
    
    /**
     * Get the current tenant ID for the current thread
     */
    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }
    
    /**
     * Check if a tenant is currently set
     */
    public static boolean hasTenant() {
        return CURRENT_TENANT.get() != null;
    }
    
    /**
     * Clear the current tenant from the current thread
     */
    public static void clear() {
        String tenantId = CURRENT_TENANT.get();
        if (tenantId != null) {
            log.debug("Clearing tenant context for: {}", tenantId);
        }
        CURRENT_TENANT.remove();
    }
    
    /**
     * Execute a block of code with a specific tenant context
     */
    public static <T> T withTenant(String tenantId, TenantSupplier<T> supplier) {
        String originalTenant = getCurrentTenant();
        try {
            setCurrentTenant(tenantId);
            return supplier.get();
        } finally {
            if (originalTenant != null) {
                setCurrentTenant(originalTenant);
            } else {
                clear();
            }
        }
    }
    
    /**
     * Execute a block of code with a specific tenant context (void return)
     */
    public static void withTenant(String tenantId, TenantRunnable runnable) {
        String originalTenant = getCurrentTenant();
        try {
            setCurrentTenant(tenantId);
            runnable.run();
        } finally {
            if (originalTenant != null) {
                setCurrentTenant(originalTenant);
            } else {
                clear();
            }
        }
    }
    
    @FunctionalInterface
    public interface TenantSupplier<T> {
        T get();
    }
    
    @FunctionalInterface
    public interface TenantRunnable {
        void run();
    }
}