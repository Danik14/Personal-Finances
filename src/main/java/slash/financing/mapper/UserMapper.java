package slash.financing.mapper;

import org.mapstruct.Mapper;
import slash.financing.data.User;
import slash.financing.dto.User.UserDto;
import slash.financing.dto.User.UserUpdateDto;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UserMapper {
    UserDto toDto(User user);

    User updateDtoToEntity(UserUpdateDto dto);
}
