package Clips.backend.clip.db;

import Clips.backend.responses.Response;
import Clips.backend.responses.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "api/v1/clip/db")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@AllArgsConstructor
public class ClipController {
    private ClipService clipService;
    private ResponseService responseService;

    @GetMapping("/getClips")
    public ResponseEntity<Response> getClips(
        @RequestParam(required = false) Integer startAfter,
        @RequestParam(required = true) Integer limit
    ) {
        return ResponseEntity.ok(
            responseService.ResponseBuilder(
                of("clips", clipService.getClips(startAfter, limit)),
                "[ClipController|getClips] Clips retrieved.",
                OK
            )
        );
    }

    @GetMapping("/getAllClipsForUser")
    public ResponseEntity<Response> getAllClipsForUser(
        @RequestParam(required = true) String uid,
        @RequestParam(required = true) String sort
    ) {
        return ResponseEntity.ok(
            responseService.ResponseBuilder(
                of("clips", clipService.getAllClipsForUser(uid, sort)),
                "[ClipController|getAllClipsForUser] Clips retrieved.",
                OK
            )
        );
    }
    
    @GetMapping("/getClipByDocID")
    public ResponseEntity<Response> getClipByDocID(@RequestParam(required = true) String docID) {
        return ResponseEntity.ok(
            responseService.ResponseBuilder(
                of("clip", clipService.getClipByDocID(docID)),
                String.format("[ClipController|getClipByDocID] Clip with docID: %s retrieved", docID),
                OK
            )
        );
    }

    @PostMapping("/createClip")
    public ResponseEntity<Response> createClip(@RequestBody TransportingClip newClip) {
        return ResponseEntity.ok(
            responseService.ResponseBuilder(
                of("clip", clipService.createClip(newClip)),
                "[ClipController|createClip] Clip is created.",
                CREATED
            )
        );
    }

    @PutMapping("/updateClip")
    public ResponseEntity<Response> updateClip(
        @RequestParam(required = true) String docID,
        @RequestParam(required = true) String title
    ) {
        if (title.length() == 0) {
            return ResponseEntity.ok(
                responseService.ResponseBuilder(
                    of("isClipUpdated", false),
                    String.format(
                        "[ClipController|updateClip] Clip with docID: %s is not updated because there is no input", docID
                    ),
                    OK
                )
            );
        }
        return ResponseEntity.ok(
            responseService.ResponseBuilder(
                of("isClipUpdated", clipService.updateClip(docID, title)),
                String.format("[ClipController|updateClip] Clip with docID: %s updated", docID),
                OK
            )
        );
    }

    @DeleteMapping(path = "/deleteClip")
    public ResponseEntity<Response> deleteClip(@RequestParam(required = true) String docID) {
        return ResponseEntity.ok(
            responseService.ResponseBuilder(
                of("isClipDeleted", clipService.deleteClip(docID)),
                String.format("[ClipController|deleteClip] Clip with docID: %s is deleted.", docID),
                OK
            )
        );
    }
}
