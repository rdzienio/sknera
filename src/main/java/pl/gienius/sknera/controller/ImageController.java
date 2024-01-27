package pl.gienius.sknera.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
public class ImageController {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @GetMapping("/images/{filename:.+}")
    @ResponseBody
    public Resource serveFile(@PathVariable String filename) {
        try {
            Path file = Paths.get(uploadDir).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @PostMapping("/upload-image")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            Path targetLocation = Paths.get("src/main/resources/static/img").resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return "index"; // Przekieruj po udanym przesłaniu
    }
}

