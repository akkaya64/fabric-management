package com.fabric.common.domain;

public enum Role {
    
    // System Level
    SYSTEM_ADMIN,
    TENANT_ADMIN,
    
    // Executive Level
    CEO,
    COO,
    CFO,
    
    // Management Level
    PRODUCTION_MANAGER,
    INVENTORY_MANAGER,
    LOGISTICS_MANAGER,
    HR_MANAGER,
    FINANCE_MANAGER,
    QUALITY_MANAGER,
    
    // Supervisory Level
    PRODUCTION_SUPERVISOR,
    WAREHOUSE_SUPERVISOR,
    SHIFT_SUPERVISOR,
    
    // Operational Level - Production
    WEAVING_OPERATOR,
    DYEING_OPERATOR,
    FINISHING_OPERATOR,
    YARN_SPECIALIST,
    
    // Operational Level - Quality & Inventory
    QUALITY_INSPECTOR,
    WAREHOUSE_WORKER,
    STOCK_CLERK,
    
    // Operational Level - Logistics
    SHIPPING_CLERK,
    TRANSPORTATION_COORDINATOR,
    
    // Support Level
    ACCOUNTANT,
    HR_SPECIALIST,
    CUSTOMER_SERVICE,
    
    // External
    SUPPLIER,
    CUSTOMER,
    AUDITOR
}