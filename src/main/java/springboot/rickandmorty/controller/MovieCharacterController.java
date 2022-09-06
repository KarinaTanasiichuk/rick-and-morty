package springboot.rickandmorty.controller;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.rickandmorty.dto.external.CharacterResponseDto;
import springboot.rickandmorty.dto.mapper.MovieCharacterMapper;
import springboot.rickandmorty.model.MovieCharacter;
import springboot.rickandmorty.service.MovieCharacterService;

@AllArgsConstructor
@RestController
@RequestMapping("/movie-characters")
public class MovieCharacterController {
    private final MovieCharacterService service;
    private final MovieCharacterMapper mapper;

    @GetMapping("/random")
    public CharacterResponseDto getRandom() {
        MovieCharacter character = service.getRandomCharacter();
        return mapper.toResponseDto(character);
    }

    @GetMapping("/by-name")
    public List<CharacterResponseDto> getByName(@RequestParam("name") String name) {
        List<MovieCharacter> characters = service.findAllByNameContains(name);
        return characters.stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
