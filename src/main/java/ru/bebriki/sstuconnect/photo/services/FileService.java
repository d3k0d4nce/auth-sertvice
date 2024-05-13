package ru.bebriki.sstuconnect.photo.services;

import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;
import ru.bebriki.sstuconnect.photo.dtos.FileDTO;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface FileService {
    List<FileDTO> getListObjects();

    FileDTO uploadFile(MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    byte[] downloadFile(String filename);

    String deleteFile(String file);
}
