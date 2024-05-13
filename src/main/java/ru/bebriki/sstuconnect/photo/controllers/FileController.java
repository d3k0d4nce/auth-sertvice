package ru.bebriki.sstuconnect.photo.controllers;

import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.bebriki.sstuconnect.photo.services.FileService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    @GetMapping
    public ResponseEntity<Object> getFiles() {
        return new ResponseEntity<>(fileService.getListObjects(), HttpStatus.OK);
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResponseEntity.ok().body(fileService.uploadFile(file));
    }

    @GetMapping(path = "/images/download")
    public ResponseEntity<ByteArrayResource> downloadImage(@RequestParam(value = "file") String file) {
        return downloadFile(file, "image/jpeg", "attachment");
    }

    @GetMapping(path = "/images/show")
    public ResponseEntity<ByteArrayResource> showImage(@RequestParam(value = "file") String file) {
        return downloadFile(file, "image/jpeg", "inline");
    }

    @GetMapping(path = "/videos/download")
    public ResponseEntity<ByteArrayResource> downloadVideo(@RequestParam(value = "file") String file) {
        return downloadFile(file, "video/mp4", "attachment");
    }

    @GetMapping(path = "/videos/show")
    public ResponseEntity<ByteArrayResource> showVideo(@RequestParam(value = "file") String file) {
        return downloadFile(file, "video/mp4", "inline");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFile(@RequestParam("file") String file) {
        return new ResponseEntity<>(fileService.deleteFile(file), HttpStatus.OK);
    }

    private ResponseEntity<ByteArrayResource> downloadFile(String file, String contentType, String contentDisposition) {
        byte[] data = fileService.downloadFile(file);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", contentType)
                .header("Content-disposition", contentDisposition + "; filename=\"" + file + "\"")
                .body(resource);
    }

}
