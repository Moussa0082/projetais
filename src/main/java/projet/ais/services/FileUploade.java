package projet.ais.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.ByteArrayOutputStream;

import org.slf4j.*;
// import org.slf4j.LoggerFactory;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

@Service
public class FileUploade {

    private static final String FTP_SERVER = "ftp.koumi.ml";
    private static final int FTP_PORT = 21; // Mise à jour si nécessaire
    private static final String FTP_USER = "admin_koumi.ml";
    private static final String FTP_PASSWORD = "oMwCBwVpr*qyv";
    int retryCount = 3; 
    
    @Async
    public String uploadImageToFTP(Path imagePath, String imageName) throws Exception {
        FTPClient ftpClient = new FTPClient();
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

    public String uploadAudioToFTP(Path audioPath, String audioName) throws Exception {
        FTPClient ftpClient = new FTPClient();
        while (retryCount > 0) {
            try {
                ftpClient.connect(FTP_SERVER, FTP_PORT);
                ftpClient.login(FTP_USER, FTP_PASSWORD);
                ftpClient.enterLocalPassiveMode();
        
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        
                try (InputStream inputStream = Files.newInputStream(audioPath)) {
                    String remoteFilePath = "/web/api-koumi/audio/" + audioName; // Chemin d'accès complet sur le serveur FTP
                    boolean uploadResult = ftpClient.storeFile(remoteFilePath, inputStream);
                    if (uploadResult) {
                        return "ftp://" + FTP_USER + "@" + FTP_SERVER + remoteFilePath; // Retourne le lien complet du fichier audio en ligne
                    } else {
                        throw new Exception("Erreur lors du chargement du fichier audio sur le serveur FTP.");
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
    
    public byte[] getAudioByName(String audioName) throws IOException {
        // Chemin où les fichiers audio sont stockés sur le serveur FTP
        String audioPath = "/web/api-koumi/audio/";
    
        // Télécharger le fichier audio à partir du serveur FTP en utilisant son nom
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            FTPClient ftpClient = new FTPClient();
            try {
                ftpClient.connect(FTP_SERVER, FTP_PORT);
                ftpClient.login(FTP_USER, FTP_PASSWORD);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
    
                // Chemin d'accès complet du fichier audio sur le serveur FTP
                String remoteFilePath = audioPath + audioName;
    
                // Télécharger le fichier audio depuis le serveur FTP
                if (ftpClient.retrieveFile(remoteFilePath, outputStream)) {
                    return outputStream.toByteArray(); // Retourner le tableau d'octets du fichier audio
                } else {
                    throw new IOException("Erreur lors du téléchargement du fichier audio depuis le serveur FTP.");
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
    
    
    public String uploadVideoToFTP(Path videoPath, String videoName) throws Exception {
        FTPClient ftpClient = new FTPClient();

        while (retryCount > 0) {
            try {
                ftpClient.connect(FTP_SERVER, FTP_PORT);
                ftpClient.login(FTP_USER, FTP_PASSWORD);
                ftpClient.enterLocalPassiveMode();
        
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        
                try (InputStream inputStream = Files.newInputStream(videoPath)) {
                    String remoteFilePath = "/web/api-koumi/videos/" + videoName;
                    // logger.info("Début du téléchargement de la video : {}", videoName);
                    boolean uploadResult = ftpClient.storeFile(remoteFilePath, inputStream);
                    if (uploadResult) {
                        return "ftp://" + FTP_USER + "@" + FTP_SERVER + remoteFilePath; // Retourne le lien complet de la vidéo en ligne
                    } else {
                        throw new Exception("Erreur lors du chargement de la vidéo sur le serveur FTP.");
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
    
    public byte[] getVideoByName(String videoName) throws IOException {
        // Chemin où les vidéos sont stockées sur le serveur FTP
        String videoPath = "/web/api-koumi/videos/";
    
        // Télécharger la vidéo à partir du serveur FTP en utilisant son nom
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            FTPClient ftpClient = new FTPClient();
            try {
                ftpClient.connect(FTP_SERVER, FTP_PORT);
                ftpClient.login(FTP_USER, FTP_PASSWORD);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
    
                // Chemin d'accès complet de la vidéo sur le serveur FTP
                String remoteFilePath = videoPath + videoName;
    
                // Télécharger la vidéo depuis le serveur FTP
                if (ftpClient.retrieveFile(remoteFilePath, outputStream)) {
                    return outputStream.toByteArray(); // Retourner le tableau d'octets de la vidéo
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
    
}
