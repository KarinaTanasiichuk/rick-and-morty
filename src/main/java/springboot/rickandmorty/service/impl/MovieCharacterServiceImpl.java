package springboot.rickandmorty.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import springboot.rickandmorty.dto.external.ApiCharacterDto;
import springboot.rickandmorty.dto.external.ApiResponseDto;
import springboot.rickandmorty.dto.mapper.MovieCharacterMapper;
import springboot.rickandmorty.model.MovieCharacter;
import springboot.rickandmorty.repository.MovieCharacterRepository;
import springboot.rickandmorty.service.HttpClient;
import springboot.rickandmorty.service.MovieCharacterService;

@AllArgsConstructor
@Service
public class MovieCharacterServiceImpl implements MovieCharacterService {
    private final HttpClient httpClient;
    private final MovieCharacterRepository movieCharacterRepository;
    private final MovieCharacterMapper mapper;

    @PostConstruct
    @Scheduled(cron = "0 8 * * * ?")
    @Override
    public void syncExternalCharacters() {
        ApiResponseDto apiResponseDto = httpClient.get("https://rickandmortyapi.com/api/character",
                ApiResponseDto.class);
        saveDtosToDb(apiResponseDto);
        while (apiResponseDto.getInfo().getNext() != null) {
            apiResponseDto = httpClient.get(apiResponseDto.getInfo().getNext(),
                    ApiResponseDto.class);
            saveDtosToDb(apiResponseDto);
        }
    }

    @Override
    public MovieCharacter getRandomCharacter() {
        long count = movieCharacterRepository.count();
        long randomId = (long) (Math.random() * count);
        return movieCharacterRepository.findByRandomId(randomId);
    }

    @Override
    public List<MovieCharacter> findAllByNameContains(String name) {
        return movieCharacterRepository.findAllByNameContains(name);
    }

    private void saveDtosToDb(ApiResponseDto responseDto) {
        Map<Long, ApiCharacterDto> externalDtos = Arrays.stream(responseDto.getResults())
                .collect(Collectors.toMap(ApiCharacterDto::getId, Function.identity()));
        Set<Long> externalIds = externalDtos.keySet();
        List<MovieCharacter> existingCharacters = movieCharacterRepository
                .findAllByExternalIdIn(externalIds);
        Map<Long, MovieCharacter> existingCharactersWithIds = existingCharacters.stream()
                .collect(Collectors.toMap(MovieCharacter::getExternalId, Function.identity()));
        Set<Long> existingIds = existingCharactersWithIds.keySet();
        externalIds.removeAll(existingIds);
        List<MovieCharacter> charactersToSave = externalIds.stream()
                .map(i -> mapper.parseApiCharacterResponseDto(externalDtos.get(i)))
                .collect(Collectors.toList());
        movieCharacterRepository.saveAll(charactersToSave);
    }
}
