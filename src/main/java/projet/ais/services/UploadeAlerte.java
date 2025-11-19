package projet.ais.services;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class UploadeAlerte {
    
    private static final String FTP_SERVER = "ftp.koumi.ml";
    private static final int FTP_PORT = 21; // Mise à jour si nécessaire
    private static final String FTP_USER = "admin_koumi.ml";
    private static final String FTP_PASSWORD = "oMwCBwVpr*qyv";

    int retryCount = 3; 
    private static final Logger logger = LoggerFactory.getLogger(UploadeAlerte.class);

    @Async
    public String uploadVideoToFTP(Path videoPath, String videoName) throws Exception {
        FTPClient ftpClient = new FTPClient();
        int attempts = retryCount;

        long fileSizeInBytes = Files.size(videoPath);
        double fileSizeInMB = fileSizeInBytes / (1024.0 * 1024.0);
        logger.info(String.format("Taille du fichier avant compression : %.2f MB", fileSizeInMB));


        while (attempts > 0) {
            try {
                ftpClient.connect(FTP_SERVER, FTP_PORT);
                ftpClient.login(FTP_USER, FTP_PASSWORD);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                try (InputStream inputStream = Files.newInputStream(videoPath);
                     ByteArrayOutputStream baos = new ByteArrayOutputStream();
                     GZIPOutputStream gzipOutputStream = new GZIPOutputStream(baos)) {

                    // Copy file content to compressed output stream
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        gzipOutputStream.write(buffer, 0, bytesRead);
                    }
                    gzipOutputStream.finish(); // Ensure all bytes are written to the stream

                    // Upload the compressed data
                    byte[] compressedData = baos.toByteArray();
                    String remoteFilePath = "/web/api-koumi/videos/" + videoName + ".gz"; // Added .gz extension
                    try (ByteArrayInputStream compressedStream = new ByteArrayInputStream(compressedData)) {
                        boolean uploadResult = ftpClient.storeFile(remoteFilePath, compressedStream);
                        if (uploadResult) {
                            return "ftp://" + FTP_USER + "@" + FTP_SERVER + remoteFilePath;
                        } else {
                            throw new Exception("Erreur lors du chargement de la vidéo sur le serveur FTP.");
                        }
                    }
                }
            } catch (IOException e) {
                attempts--;
                if (attempts == 0) {
                    throw new Exception("Erreur lors de la connexion au serveur FTP : " + e.getMessage());
                }
            } finally {
                try {
                    if (ftpClient.isConnected()) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        throw new Exception("Échec du téléchargement du fichier après plusieurs tentatives.");
    }
    
    @Async
    public String uploadImageToFTP(Path imagePath, String imageName) throws Exception {
        FTPClient ftpClient = new FTPClient();

        long fileSizeInBytes = Files.size(imagePath);
        double fileSizeInMB = fileSizeInBytes / (1024.0 * 1024.0);
        logger.info(String.format("Taille du fichier avant compression : %.2f MB", fileSizeInMB));

        while (retryCount > 0) {
            try {
                ftpClient.connect(FTP_SERVER, FTP_PORT);
                ftpClient.login(FTP_USER, FTP_PASSWORD);
                ftpClient.enterLocalPassiveMode();
        
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        
                try (InputStream inputStream = Files.newInputStream(imagePath)) {
                    String remoteFilePath = "/web/api-koumi/images/" + imageName; // Chemin d'acc                                                         ès complet sur le serveur FTP
                    boolean uploadResult = ftpClient.storeFile(remoteFilePath, inputStream);
                    if (uploadResult) {
                        return "ftp://" + FTP_USER + "@" + FTP_SERVER + remoteFilePath; // Retourne le lien complet de l'image en ligne
                    } else {
                        throw new Exception("Erreur lors du chargement de l'image sur le serveur FTP.");
                    }
                }
            } catch (IOException e) {
                throw new Exception("Erreur lors de la connexion au serveur FTP : " + e.getMessage());
            } finally {
                try {
                    if (ftpClient.isConnected()) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } 
                }
                throw new Exception("Échec du téléchargement du fichier après plusieurs tentatives.");
    }


      // Méthode pour récupérer une image à partir de son nom
    public byte[] getImageByName(String imageName) throws IOException {
        // Chemin où les images sont stockées sur le serveur FTP
        String imagePath = "/web/api-koumi/images/";
    
        // Télécharger l'image à partir du serveur FTP en utilisant son nom
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            FTPClient ftpClient = new FTPClient();
            try {
                ftpClient.connect(FTP_SERVER, FTP_PORT);
                ftpClient.login(FTP_USER, FTP_PASSWORD);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
    
                // Chemin d'accès complet de l'image sur le serveur FTP
                String remoteFilePath = imagePath + imageName;
    
                // Télécharger l'image depuis le serveur FTP
                if (ftpClient.retrieveFile(remoteFilePath, outputStream)) {
                    return outputStream.toByteArray(); // Retourner le tableau d'octets de l'image
                } else {
                    throw new IOException("Erreur lors du téléchargement de l'image depuis le serveur FTP.");
                }
            } finally {
                try {
                    if (ftpClient.isConnected()) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

  
 public byte[] getVideoByName(String videoName) throws IOException {
        // Chemin où les vidéos sont stockées sur le serveur FTP
        String videoPath = "/web/api-koumi/videos/";
        String remoteFilePath = videoPath + videoName + ".gz"; // Assurez-vous d'ajouter l'extension .gz
    
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            FTPClient ftpClient = new FTPClient();
            try {
                ftpClient.connect(FTP_SERVER, FTP_PORT);
                ftpClient.login(FTP_USER, FTP_PASSWORD);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
    
                // Télécharger la vidéo compressée depuis le serveur FTP
                if (ftpClient.retrieveFile(remoteFilePath, outputStream)) {
                    byte[] compressedData = outputStream.toByteArray();
                    
                    // Décompresser les données
                    try (ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
                         GZIPInputStream gzipInputStream = new GZIPInputStream(bais);
                         ByteArrayOutputStream decompressedOutputStream = new ByteArrayOutputStream()) {
                        
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = gzipInputStream.read(buffer)) != -1) {
                            decompressedOutputStream.write(buffer, 0, bytesRead);
                        }
                        return decompressedOutputStream.toByteArray(); // Retourner le tableau d'octets de la vidéo décompressée
                    }
                } else {
                    throw new IOException("Erreur lors du téléchargement de la vidéo depuis le serveur FTP.");
                }
            } finally {
                try {
                    if (ftpClient.isConnected()) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    @Async
    public String uploadAudioToFTP(Path audioPath, String audioName) throws Exception {
        FTPClient ftpClient = new FTPClient();
        int attempts = retryCount;

        long fileSizeInBytes = Files.size(audioPath);
        double fileSizeInMB = fileSizeInBytes / (1024.0 * 1024.0);
        logger.info(String.format("Taille du fichier avant compression : %.2f MB", fileSizeInMB));

        while (attempts > 0) {
            try {
                ftpClient.connect(FTP_SERVER, FTP_PORT);
                ftpClient.login(FTP_USER, FTP_PASSWORD);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                try (InputStream inputStream = Files.newInputStream(audioPath);
                     ByteArrayOutputStream baos = new ByteArrayOutputStream();
                     GZIPOutputStream gzipOutputStream = new GZIPOutputStream(baos)) {

                    // Copy file content to compressed output stream
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        gzipOutputStream.write(buffer, 0, bytesRead);
                    }
                    gzipOutputStream.finish(); // Ensure all bytes are written to the stream

                    // Upload the compressed data
                    byte[] compressedData = baos.toByteArray();
                    String remoteFilePath = "/web/api-koumi/audio/" + audioName + ".gz"; // Added .gz extension
                    try (ByteArrayInputStream compressedStream = new ByteArrayInputStream(compressedData)) {
                        boolean uploadResult = ftpClient.storeFile(remoteFilePath, compressedStream);
                        if (uploadResult) {
                            return "ftp://" + FTP_USER + "@" + FTP_SERVER + remoteFilePath;
                        } else {
                            throw new Exception("Erreur lors du chargement de l'audio sur le serveur FTP.");
                        }
                    }
                }
            } catch (IOException e) {
                attempts--;
                if (attempts == 0) {
                    throw new Exception("Erreur lors de la connexion au serveur FTP : " + e.getMessage());
                }
            } finally {
                try {
                    if (ftpClient.isConnected()) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        throw new Exception("Échec du téléchargement du fichier après plusieurs tentatives.");
    }

    public byte[] getAudioByName(String audioName) throws IOException {
        // Chemin où les audios sont stockées sur le serveur FTP
        String audioPath = "/web/api-koumi/audio/";
        String remoteFilePath = audioPath + audioName + ".gz"; // Assurez-vous d'ajouter l'extension .gz

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            FTPClient ftpClient = new FTPClient();
            try {
                ftpClient.connect(FTP_SERVER, FTP_PORT);
                ftpClient.login(FTP_USER, FTP_PASSWORD);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                // Télécharger l'audio compressée depuis le serveur FTP
                if (ftpClient.retrieveFile(remoteFilePath, outputStream)) {
                    byte[] compressedData = outputStream.toByteArray();

                    // Décompresser les données
                    try (ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
                         GZIPInputStream gzipInputStream = new GZIPInputStream(bais);
                         ByteArrayOutputStream decompressedOutputStream = new ByteArrayOutputStream()) {

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = gzipInputStream.read(buffer)) != -1) {
                            decompressedOutputStream.write(buffer, 0, bytesRead);
                        }
                        return decompressedOutputStream.toByteArray(); // Retourner le tableau d'octets de l'audio décompressée
                    }
                } else {
                    throw new IOException("Erreur lors du téléchargement de l'audio depuis le serveur FTP.");
                }
            } finally {
                try {
                    if (ftpClient.isConnected()) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
