package com.fabric.user_service.payload.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateUserRequest(
        @Email(message = "Email should be valid")
        String email,

        @Valid
        @NotEmpty(message = "At least one contact must be provided")
        List<CreateUserContactRequest> contacts,

        @Valid
        @NotEmpty(message = "At least one company must be provided")
        List<CreateUserCompanyRequest> companies
) {}
