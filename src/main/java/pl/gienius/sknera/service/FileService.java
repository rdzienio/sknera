package pl.gienius.sknera.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

//@Service
public interface FileService {
    public void saveFile(MultipartFile multipartFile) throws IOException;
}
