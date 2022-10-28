package Clips.backend.clip;

import Clips.backend.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "api/v1/clip")
@RequiredArgsConstructor
public class ClipController {
    private final ClipService clipService;

    @GetMapping("/getAllClipsForUser")
    public ResponseEntity<Response> getAllClipsForUser(
        @RequestParam(required = true) String uid,
        @RequestParam(required = true) String sort
    ) {
        return ResponseEntity.ok(
            Response.builder()
                .timeStamp(now())
                .data(of("clips", clipService.getAllClipsForUser(uid, sort)))
                .message("[ClipController|getAllClipsForUser] Clips retrieved.")
                .httpStatus(OK)
                .statusCode(OK.value())
                .build()
        );
    }

    @GetMapping("/getClipByDocID/{docID}")
    public ResponseEntity<Response> getClipByDocID(@PathVariable("docID") String docID) {
        return ResponseEntity.ok(
            Response.builder()
                .timeStamp(now())
                .data(of("clip", clipService.getClipByDocID(docID)))
                .message(String.format("[ClipController|getClipByDocID] Clip with docID: %s retrieved", docID))
                .httpStatus(OK)
                .statusCode(OK.value())
                .build()
        );
    }

    @PostMapping("/createClip")
    public ResponseEntity<Response> createClip(@RequestBody NewClipRequest newClip) {
        return ResponseEntity.ok(
            Response.builder()
                .timeStamp(now())
                .data(of("clip", clipService.createClip(newClip)))
                .message("[ClipController|createClip] Clip is created.")
                .httpStatus(CREATED)
                .statusCode(CREATED.value())
                .build()
        );
    }

    @PutMapping("/updateClip")
    public ResponseEntity<Response> updateClip(
        @RequestParam(required = true) String docID,
        @RequestParam(required = true) String title
    ) {
        if (title.length() == 0) {
            return ResponseEntity.ok(
                Response.builder()
                    .timeStamp(now())
                    .data(of("isClipUpdated", false))
                    .message(
                        String.format("[ClipController|updateClip] Clip with docID: %s is not updated because there is no input",
                            docID
                        )
                    )
                    .httpStatus(OK)
                    .statusCode(OK.value())
                    .build()
            );
        }

        return ResponseEntity.ok(
            Response.builder()
                .timeStamp(now())
                .data(of("isClipUpdated", clipService.updateClip(docID, title)))
                .message(String.format("[ClipController|updateClip] Clip with docID: %s updated", docID))
                .httpStatus(OK)
                .statusCode(OK.value())
                .build()
        );
    }

    @DeleteMapping(path = "/deleteClip/{docID}")
    public ResponseEntity<Response> deleteClip(@PathVariable("docID") String docID) {
        return ResponseEntity.ok(
            Response.builder()
                .timeStamp(now())
                .data(of("isClipDeleted", clipService.deleteClip(docID)))
                .message(String.format("[ClipController|deleteClip] Clip with docID: %s is deleted.", docID))
                .httpStatus(OK)
                .statusCode(OK.value())
                .build()
        );
    }
}
