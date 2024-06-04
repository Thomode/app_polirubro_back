package app.polirubro.firebaseStorage.services;

import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FirebaseStorageService {
    @Autowired
    private  FirebaseApp firebaseApp;
    @Resource
    private Environment env;

    public String uploadFile(MultipartFile file) throws IOException {
        // Unique file name
        String fileName = UUID.randomUUID().toString();

        StorageClient storageClient = StorageClient.getInstance(firebaseApp);
        storageClient.bucket().create(fileName, file.getInputStream(), file.getContentType());

        return fileName;
    }

    public void deleteFile(String fileName) {
        StorageClient storageClient = StorageClient.getInstance(firebaseApp);
        storageClient.bucket().get(fileName).delete();
    }

    public String getUrl(String fileName){
        return this.env.getProperty("firebase-storage.base-url") + fileName + "?alt=media";
    }

    public String extractFileNameFromUrl(String url) {
        // Find the position of the last '/'
        int lastIndex = url.lastIndexOf('/');

        // Extract the part of the URL after the last '/'
        String fileNameWithParams = url.substring(lastIndex + 1);

        // If there are query parameters, extract only the file name
        int queryParamsIndex = fileNameWithParams.indexOf('?');

        if (queryParamsIndex != -1) {
            fileNameWithParams = fileNameWithParams.substring(0, queryParamsIndex);
        }

        return fileNameWithParams;
    }
}
