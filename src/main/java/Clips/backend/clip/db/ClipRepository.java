package Clips.backend.clip.db;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClipRepository extends JpaRepository<Clip, Long> {
    @Query(value="SELECT * FROM clip ORDER BY timestamp DESC LIMIT :startAfter, :limit", nativeQuery = true)
    Optional<List<Clip>>findClipsOrderByTimestampDESC(@Param("startAfter") Integer startAfter, @Param("limit") Integer limit);

    @Query("SELECT c FROM Clip c WHERE c.uid = :uid")
    Optional<List<Clip>> findClipsByUid(@Param("uid") String uid, Sort sort);

    @Query("SELECT c FROM Clip c WHERE c.docId = :docID")
    Optional<Clip> findClipByDocId(@Param("docID") String docID);

    @Modifying
    @Query("DELETE FROM Clip c WHERE c.docId = :docID")
    void deleteClipByDocId(@Param("docID") String docID);
}
