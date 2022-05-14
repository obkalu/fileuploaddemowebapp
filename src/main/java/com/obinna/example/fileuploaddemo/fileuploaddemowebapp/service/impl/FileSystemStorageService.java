package com.obinna.example.fileuploaddemo.fileuploaddemowebapp.service.impl;

import com.obinna.example.fileuploaddemo.fileuploaddemowebapp.config.StorageConfigProperties;
import com.obinna.example.fileuploaddemo.fileuploaddemowebapp.exception.StorageException;
import com.obinna.example.fileuploaddemo.fileuploaddemowebapp.exception.StorageFileNotFoundException;
import com.obinna.example.fileuploaddemo.fileuploaddemowebapp.service.StorageService;
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
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    public FileSystemStorageService(StorageConfigProperties storageConfigProperties) {
        this.rootLocation = Paths.get(storageConfigProperties.getLocation());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException ioException) {
            throw new StorageException("Error: The designated storage location could not be initialized/created", ioException);
        }
    }

    @Override
    public void store(MultipartFile multipartFile) {
        try {
            if(multipartFile.isEmpty()) {
                throw new StorageException("Error: Failed to store empty file.");
            }
            Path destinationFile = this.rootLocation.resolve(
                    Paths.get(multipartFile.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            if(!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is aa security check
                throw new StorageException("Error: Cannot store file outside current directory.");
            }
            try (InputStream inputStream = multipartFile.getInputStream()){
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ioException) {
            throw new StorageException("Error: Failed to store file.", ioException);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException ioException) {
            throw new StorageException("Error: Failed to read stored files.", ioException);
        }
    }

    @Override
    public Path load(String filename) {
        return this.rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Error: Could not read file: " + filename);
            }
        } catch (MalformedURLException malformedURLException) {
            throw new StorageFileNotFoundException("Error: Could not read file: " + filename,
                    malformedURLException);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(this.rootLocation.toFile());
    }

}
