package ru.bebriki.sstuconnect.photo.services;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.bebriki.sstuconnect.photo.dtos.FileDTO;
import ru.bebriki.sstuconnect.photo.exceptions.ImageDownloadException;
import ru.bebriki.sstuconnect.photo.exceptions.ImageUploadException;
import ru.bebriki.sstuconnect.photo.properties.MinioProperties;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @Override
    public List<FileDTO> getListObjects() {
        List<FileDTO> objects = new ArrayList<>();
        try {
            Iterable<Result<Item>> result = minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .recursive(true)
                    .build());
            for (Result<Item> item : result) {
                objects.add(FileDTO.builder()
                        .filename(item.get().objectName())
                        .size(item.get().size())
                        .url(getPreSignedUrl(item.get().objectName()))
                        .build());
            }
        } catch (Exception e) {
            throw new ImageUploadException("Error occurred when getting list of objects from Minio", e);
        }
        return objects;
    }

    public FileDTO uploadFile(MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        validateFile(file);
        checkIfBucketNotExists();
        String objectName = generateRandomFileName(file);

        try {
            if (minioClient.statObject(StatObjectArgs.builder().bucket(minioProperties.getBucket()).object(objectName).build()) != null) {
                throw new ImageUploadException("File with the same name already exists: " + objectName);
            }
        } catch (ErrorResponseException e) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());
        }

        return createFileDTO(file, objectName);
    }

    public byte[] downloadFile(String filename) {
        try {
            if (minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(filename)
                            .build()
            ) != null) {
                return minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(minioProperties.getBucket())
                                .object(filename)
                                .build()).readAllBytes();
            } else {
                throw new ImageDownloadException("Error occurred while downloading file: " + filename);
            }

        } catch (Exception e) {
            throw new ImageDownloadException("Requested file does not exist: " + filename);
        }
    }


    @Override
    public String deleteFile(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            throw new ImageUploadException("Error occurred when deleting file", e);
        }

        return "successfully deleted";
    }

    private String getPreSignedUrl(String filename) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(minioProperties.getBucket())
                        .object(filename)
                        .expiry(60 * 60 * 24) // 24 hours
                        .build());
    }

    private FileDTO createFileDTO(MultipartFile file, String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return FileDTO.builder()
                .size(file.getSize())
                .url(getPreSignedUrl(objectName))
                .filename(objectName)
                .build();
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ImageUploadException("File is empty");
        }
    }

    private String generateRandomFileName(MultipartFile file) {
        return UUID.randomUUID() + "." + Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1];
    }

    private void checkIfBucketNotExists() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucket()).build())) {
            throw new ImageUploadException("Bucket does not exist: " + minioProperties.getBucket());
        }
    }

}
