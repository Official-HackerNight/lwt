package com.hackernight.lwt.repository;

import com.hackernight.lwt.domain.Episode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Episode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {

    @Query(value = "select distinct episode from Episode episode left join fetch episode.tags",
        countQuery = "select count(distinct episode) from Episode episode")
    Page<Episode> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct episode from Episode episode left join fetch episode.tags")
    List<Episode> findAllWithEagerRelationships();

    @Query("select episode from Episode episode left join fetch episode.tags where episode.id =:id")
    Optional<Episode> findOneWithEagerRelationships(@Param("id") Long id);

}
