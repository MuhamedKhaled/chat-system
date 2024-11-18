package com.chat.mappers;

import com.chat.api.controllers.resources.ApplicationRequest;
import com.chat.api.controllers.resources.ApplicationResponse;
import com.chat.data.entities.Application;
import com.chat.utils.TokenConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring", uses = TokenConverter.class)
public interface ApplicationMapper {

    @Mapping(target = "token", ignore = true) // Token will be generated in the service layer
    Application toEntity(ApplicationRequest request);

    List<ApplicationResponse> toResponse(List<Application> entities);

    @Mapping(source = "token", target = "token", qualifiedByName = "bytesToUuidString")
    ApplicationResponse toResponse(Application application);

}