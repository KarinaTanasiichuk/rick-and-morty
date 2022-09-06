package springboot.rickandmorty.dto.mapper;

import org.springframework.stereotype.Component;
import springboot.rickandmorty.dto.external.ApiCharacterDto;
import springboot.rickandmorty.dto.external.CharacterResponseDto;
import springboot.rickandmorty.model.Gender;
import springboot.rickandmorty.model.MovieCharacter;
import springboot.rickandmorty.model.Status;

@Component
public class MovieCharacterMapper {
    public MovieCharacter parseApiCharacterResponseDto(ApiCharacterDto responseDtp) {
        MovieCharacter movieCharacter = new MovieCharacter();
        movieCharacter.setName(responseDtp.getName());
        movieCharacter.setExternalId(responseDtp.getId());
        movieCharacter.setStatus(Status.valueOf(responseDtp.getStatus().toUpperCase()));
        movieCharacter.setGender(Gender.valueOf(responseDtp.getGender().toUpperCase()));
        return movieCharacter;
    }

    public CharacterResponseDto toResponseDto(MovieCharacter movieCharacter) {
        CharacterResponseDto responseDto = new CharacterResponseDto();
        responseDto.setId(movieCharacter.getId());
        responseDto.setExternalId(movieCharacter.getExternalId());
        responseDto.setName(movieCharacter.getName());
        responseDto.setStatus(movieCharacter.getStatus());
        responseDto.setGender(movieCharacter.getGender());
        return responseDto;
    }
}
