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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

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
        // Get directory Path:
        String directoryPath = filePath.split("/")[0];

        // Store the file:
        String fileName = fileSystemStorageService.store(directoryPath, file);

        // Try to serve and retrieve the URI to the file:
        String storagePath = MvcUriComponentsBuilder
            .fromMethodName(
                FileUploadController.class,
                "getFile",
                directoryPath,
                fileName
            )
            .build()
            .toUri()
            .toString();

        return ResponseEntity.ok(
            responseService.ResponseBuilder(
                of("storagePath", storagePath),
                "[FileUploadController|upload] Upload request sent.",
                CREATED
            )
        );
    }

    @GetMapping("/getFile/{directories}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(
        @PathVariable String directories,
        @PathVariable String filename
    ) {
        Resource file = fileSystemStorageService.loadAsResource(directories, filename);
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
        String[] filePathSplit = filePath.split("/"); // directories and file name
        boolean isDeleted = fileSystemStorageService.delete(filePathSplit[0], filePathSplit[1]);
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
