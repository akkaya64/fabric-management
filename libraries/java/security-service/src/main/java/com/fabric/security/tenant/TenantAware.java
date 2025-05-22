package com.fabric.security.tenant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks entities or repositories as tenant-aware.
 * These will be automatically filtered by tenant ID.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TenantAware {
    String tenantField() default "tenantId";
}