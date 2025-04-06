package com.fabric.user_service.domain.enums;

public enum Position {
    INTERN("Intern"),
    MACHINE_OPERATOR("Machine Operator"),
    QUALITY_INSPECTOR("Quality Inspector"),
    WAREHOUSE_WORKER("Warehouse Worker"),
    CUTTING_SPECIALIST("Cutting Specialist"),
    SEWING_SPECIALIST("Sewing Specialist"),
    PRODUCTION_SUPERVISOR("Production Supervisor"),
    LOGISTICS_COORDINATOR("Logistics Coordinator"),
    PROCUREMENT_OFFICER("Procurement Officer"),
    MAINTENANCE_TECHNICIAN("Maintenance Technician"),
    SHIFT_LEAD("Shift Lead"),
    DEPARTMENT_MANAGER("Department Manager"),
    PLANT_MANAGER("Plant Manager"),
    OPERATIONS_DIRECTOR("Operations Director"),
    SUPPLY_CHAIN_MANAGER("Supply Chain Manager"),
    HEAD_OF_PRODUCTION("Head of Production"),
    VP_OPERATIONS("Vice President of Operations"),
    COO("Chief Operating Officer");

    private final String displayName;

    Position(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
