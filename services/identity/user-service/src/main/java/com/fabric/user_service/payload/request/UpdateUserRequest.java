package com.fabric.user_service.payload.request;

import jakarta.validation.Valid;
import java.util.List;

public record UpdateUserRequest(
        Boolean enabled,

        @Valid
        List<CreateUserContactRequest> addContacts,

        List<Long> removeContactIds,

        @Valid
        List<CreateUserCompanyRequest> addCompanies,

        List<Long> removeCompanyIds
) {}