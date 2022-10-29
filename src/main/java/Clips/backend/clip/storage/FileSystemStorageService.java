package Clips.backend.clip.storage;

import Clips.backend.clip.storage.exceptions.StorageException;
import Clips.backend.clip.storage.exceptions.StorageFileNotFoundException;
import Clips.backend.clip.storage.properties.StorageProperties;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class FileSystemStorageService {
    private final Path rootLocation;
    private final Pattern extPattern = Pattern.compile("\\.[^/.]+$");

    @Autowired // Dependency injection
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    public String store(String directoryPath, MultipartFile file) throws StorageException {
        try {
            // directoryPath can be null. Check if the file is empty:
            if (file.isEmpty()) {
                throw new StorageException(
                    "[FileSystemStorageService|store] Received file is empty."
                );
            }

            Path destinationPath = this.rootLocation;

            // Create directories:
            if (!directoryPath.isEmpty()) {
                destinationPath = Paths.get(this.rootLocation.toString() + "/" + directoryPath);
                try {
                    Files.createDirectories(destinationPath);

                } catch (IOException e) {
                    throw new StorageException(
                        "[FileSystemStorageService|store] Could not initialize storage", e
                    );
                }
            }

            // Get file name extension, we have restricted the user to enter a filename in frontend:
            Matcher extMatcher = extPattern.matcher(file.getOriginalFilename());
            String ext = extMatcher.find() ? extMatcher.group() : null;

            // Use UUID to generate a name for the file:
            String uniqueFileName = UUID.randomUUID() + ext;

            // Get destination path with the generated UUID:
            Path destinationFile = destinationPath.resolve(
                Paths.get(uniqueFileName)
            ).normalize().toAbsolutePath();

            // Security check destination folder
            // by checking if the parent of destination is the root location:
            if (!destinationFile.getParent().equals(destinationPath.toAbsolutePath())) {
                throw new StorageException(
                    "[FileSystemStorageService|store] Cannot store file outside specified upload directory"
                );
            }

            // Copy the given file to destination:
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                    StandardCopyOption.REPLACE_EXISTING
                );
            }

            // Return the filename:
            return uniqueFileName;

        } catch (IOException e) {
            throw new StorageException("[FileSystemStorageService|store] Failed to store file.", e);
        }
    }

    public Path load(String directories, String fileName) {
        return rootLocation.resolve(directories + "/" + fileName);
    }

    public Resource loadAsResource(String directories, String fileName) throws StorageFileNotFoundException {
        try {
            Path filePath = load(directories, fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                    "[FileSystemStorageService|loadAsResource] Could not read file: " + fileName
                );
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException(
                "[FileSystemStorageService|loadAsResource] Could not read file: " + fileName, e
            );
        }
    }

    public boolean delete(String directories, String fileNameWithExt) throws StorageFileNotFoundException {
        try {
            Path filePath = load(directories, fileNameWithExt);
            Files.delete(filePath);
            return true;

        } catch (IOException e) {
            throw new StorageFileNotFoundException(
                "[FileSystemStorageService|delete] Could not read file: " + fileNameWithExt
            );
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public void init() throws StorageException {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
