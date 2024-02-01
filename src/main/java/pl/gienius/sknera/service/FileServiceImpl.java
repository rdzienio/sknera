package pl.gienius.sknera.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

//@Service
/*public class FileServiceImpl implements FileService {
    @Value("${app.upload.dir}")
    private String photoDir;
    

    public void saveFile(MultipartFile multipartFile) throws IOException {
        //Upewniamy się by struktura katalogów była utworzona
        new File(photoDir).mkdirs();
        var path = Path.of(photoDir, multipartFile.getOriginalFilename());
        var fos = new FileOutputStream(path.toFile());
        fos.write(multipartFile.getBytes());
        fos.close();
    }

}*/
