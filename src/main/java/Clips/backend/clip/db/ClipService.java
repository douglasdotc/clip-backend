package Clips.backend.clip.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static java.util.List.of;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClipService {

    // Interface that use to access database:
    private final ClipRepository clipRepository;

    public List<Clip> getAllClipsForUser(String uid, String sort) {
        log.info("[ClipService|getAllClipsForUser] Fetching user's clips with uid {}", uid);
        Optional<List<Clip>> clips = clipRepository.findClipsByUid(
            uid,
            Sort.by(
                sort.equals("1") ? Sort.Direction.DESC : Sort.Direction.ASC,
                "timestamp"
            )
        );

        return clips.isEmpty() ? of() : clips.get();
    }

    public Clip getClipByDocID(String docID) {
        log.info("[ClipService|getClipByDocID] Fetching user's clip with docID {}", docID);
        Optional<Clip> clip = clipRepository.findClipByDocId(docID);
        if (clip.isEmpty()) {
            throw new IllegalStateException(
                String.format("[ClipService|getClipByDocID] Clip with docID %s for the user does not exist.", docID)
            );
        }
        return clip.get();
    }

    public Clip createClip(NewClipRequest newClip) {
        log.info("[ClipService|createClip] Saving new clip.");
        // We allow user to create clips with same title and content
        Clip clip = new Clip(
            newClip.getUid(),
            newClip.getDisplayName(),
            newClip.getTitle(),
            newClip.getFileName(),
            newClip.getUrl(),
            newClip.getScreenshotUrl(),
            newClip.getScreenshotFileName()
        );
        // Set timestamp:
        clip.setTimestamp(now());
        // Set unique ID for identification:
        clip.setDocId(UUID.randomUUID().toString());
        return clipRepository.save(clip);
    }

    public Boolean updateClip(String docID, String title) {
        log.info("[ClipService|updateClip] Updating clip with docID: {}", docID);
        Clip clip = clipRepository.findClipByDocId(docID).orElseThrow(
            () -> new IllegalStateException(
                String.format("[ClipService|updateClip] Clip with docID %s does not exist.", docID)
            )
        );
        if (title != null && title.length() > 0 && !Objects.equals(clip.getTitle(), title)) {
            clip.setTitle(title);
        }
        return true;
    }

    public Boolean deleteClip(String docID) {
        log.info("[ClipService|deleteClip] Deleting clip with ID: {}", docID);
        Clip clip = clipRepository.findClipByDocId(docID).orElseThrow(
            () -> new IllegalStateException(
                String.format("[ClipService|deleteClip] Clip with docID %s for the user does not exist", docID)
            )
        );
        clipRepository.deleteClipByDocId(docID);
        return true;
    }
}
