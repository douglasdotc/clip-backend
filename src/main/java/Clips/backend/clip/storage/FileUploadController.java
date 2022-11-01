package Clips.backend.clip.storage;

import Clips.backend.clip.storage.exceptions.StorageFileNotFoundException;
import Clips.backend.response.Response;
import Clips.backend.response.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.nio.file.Path;

import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "api/v1/clip/storage")
@AllArgsConstructor
public class FileUploadController {
    private FileSystemStorageService fileSystemStorageService;
    private ResponseService responseService;

    @PostMapping("/upload")
    public ResponseEntity<Response> upload(
        @RequestParam("filePath") String filePath,
        @RequestParam("file") MultipartFile file
    ) {
        // Get directory Path (0) and fileName (1):
        String[] splits = filePath.split("/");

        // Store the file:
        boolean isSaved = fileSystemStorageService.store(splits[0], splits[1], file);

        return ResponseEntity.ok(
            responseService.ResponseBuilder(
                of("isSaved", isSaved),
                "[FileUploadController|upload] Upload request sent.",
                CREATED
            )
        );
    }

    @GetMapping("/getDownloadURL")
    public ResponseEntity<Response> getDownloadURL(
        @RequestParam(required = true) String filePath
    ) {
        // Check if the given path is valid:
        boolean isValid = fileSystemStorageService.isValidFilePath(filePath);
        if (!isValid) {
            return ResponseEntity.ok(
                responseService.ResponseBuilder(
                    of(),
                    "[FileUploadController|getDownloadURL] Download URL not found.",
                    OK
                )
            );
        }

        String downloadURL = MvcUriComponentsBuilder
            .fromMethodName(
                FileUploadController.class,
                "getFile",
                filePath
            )
            .build()
            .toUri()
            .toString();

        return ResponseEntity.ok(
            responseService.ResponseBuilder(
                of("downloadURL", downloadURL),
                "[FileUploadController|getDownloadURL] Download URL retrieved.",
                OK
            )
        );
    }

    @GetMapping("/getFile")
    @ResponseBody
    public ResponseEntity<Resource> getFile(
        @RequestParam(required = true) String filePath
    ) {
        Resource file = fileSystemStorageService.loadAsResource(filePath);
        return ResponseEntity.ok()
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\""
            )
            .body(file);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Response> delete(
        @RequestParam("filePath") String filePath
    ) {
        boolean isDeleted = fileSystemStorageService.delete(filePath);
        return ResponseEntity.ok(
            responseService.ResponseBuilder(
                of("isDeleted", isDeleted),
                "[FileUploadController|delete] Delete request sent.",
                OK
            )
        );
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
