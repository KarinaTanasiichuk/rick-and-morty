package springboot.rickandmorty.dto.external;

import lombok.Data;
import springboot.rickandmorty.model.Gender;
import springboot.rickandmorty.model.Status;

@Data
public class CharacterResponseDto {
    private Long id;
    private Long externalId;
    private String name;
    private Status status;
    private Gender gender;
}
