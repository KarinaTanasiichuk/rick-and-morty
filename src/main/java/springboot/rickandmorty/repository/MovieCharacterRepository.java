package springboot.rickandmorty.repository;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import springboot.rickandmorty.model.MovieCharacter;

public interface MovieCharacterRepository extends JpaRepository<MovieCharacter, Long> {
    List<MovieCharacter> findAllByExternalIdIn(Set<Long> externalIds);

    List<MovieCharacter> findAllByNameContains(String name);

    @Query("From MovieCharacter mc Where mc.externalId = ?1")
    MovieCharacter findByRandomId(Long id);
}
