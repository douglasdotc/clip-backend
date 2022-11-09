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

@Service
@AllArgsConstructor
public class FileSystemStorageService {
    private final Path rootLocation;

    @Autowired // Dependency injection
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    public boolean store(String directoryPath, String fileName, MultipartFile file) throws StorageException {
        try {
            // directoryPath can be null. Check if the file is empty:
            if (file.isEmpty()) {
                throw new StorageException(
                    "[FileSystemStorageService|store] Received file is empty."
                );
            }

            // Init destination path as root path:
            Path destinationPath = this.rootLocation;

            // Create directories:
            if (!directoryPath.isEmpty()) {
                // Update destination path with given directories and create the directory if not exist:
                destinationPath = Paths.get(this.rootLocation.toString() + "/" + directoryPath);

                if (!Files.isDirectory(destinationPath)) {
                    try {
                        Files.createDirectories(destinationPath);

                    } catch (IOException e) {
                        throw new StorageException(
                            "[FileSystemStorageService|store] Could not initialize storage", e
                        );
                    }
                }
            }

            // Get destination path with the generated UUID:
            Path destinationFile = destinationPath.resolve(
                Paths.get(fileName)
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

            return true;

        } catch (IOException e) {
            throw new StorageException("[FileSystemStorageService|store] Failed to store file.", e);
        }
    }

    public Path load(String filePath) {
        // Get file path:
        return rootLocation.resolve(filePath);
    }

    public boolean isValidFilePath(String receivedFilePath) {
        try {
            // Load file path and create an url resource:
            Path filePath = load(receivedFilePath);
            Resource resource = new UrlResource(filePath.toUri());

            // Check if the file is readable, only return true if yes:
            if (resource.exists() || resource.isReadable()) {
                return true;
            }
            return false;

        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException(
                "[FileSystemStorageService|isValidFilePath] Could not read file: " + receivedFilePath, e
            );
        }
    }

    public Resource loadAsResource(String receivedFilePath) throws StorageFileNotFoundException {
        try {
            // Load file path and create an url resource:
            Path filePath = load(receivedFilePath);
            Resource resource = new UrlResource(filePath.toUri());

            // Check if the file is readable, only return if yes:
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                    "[FileSystemStorageService|loadAsResource] Could not read file: " + receivedFilePath
                );
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException(
                "[FileSystemStorageService|loadAsResource] Could not read file: " + receivedFilePath, e
            );
        }
    }

    public boolean delete(String receivedFilePath) throws StorageFileNotFoundException {
        try {
            // Load the file path and delete the file:
            Path filePath = load(receivedFilePath);
            Files.delete(filePath);

            return true;

        } catch (IOException e) {
            throw new StorageFileNotFoundException(
                "[FileSystemStorageService|delete] Could not read file: " + receivedFilePath
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
