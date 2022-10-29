package Clips.backend.clip.storage.configurations;

import Clips.backend.clip.storage.FileSystemStorageService;
import Clips.backend.clip.storage.properties.StorageProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfiguration {
    @Bean
    public StorageProperties storageProperties() {
        return new StorageProperties();
    }

    @Bean
    CommandLineRunner init(FileSystemStorageService fileSystemStorageService) {
        return (args) -> {
            fileSystemStorageService.deleteAll();
            fileSystemStorageService.init();
        };
    }
}
