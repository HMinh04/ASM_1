package org.example.asm.mapper;

import org.example.asm.dto.request.RegisterRequest;
import org.example.asm.dto.request.UpdateRequest;
import org.example.asm.dto.response.ProfileResponse;
import org.example.asm.model.Profile;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile toProfile(RegisterRequest request);
    ProfileResponse toProfileResponse(Profile profile);
    Profile toUpdateProfile(UpdateRequest request);
    UpdateRequest toUpdateUserResponse(Profile profile);

}
