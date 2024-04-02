package mate.academy.bookstore.mapper;

import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstore.dto.user.UserResponseDto;
import mate.academy.bookstore.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = PasswordEncoderMapper.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    User toModel(UserRegistrationRequestDto registrationRequestDto);
}
