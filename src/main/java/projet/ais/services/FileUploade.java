package projet.ais.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.ByteArrayOutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

@Service
public class FileUploade {

    private static final String FTP_SERVER = "ftp.koumi.ml";
    private static final int FTP_PORT = 21; // Mise à jour si nécessaire
    private static final String FTP_USER = "default_koumi";
    private static final String FTP_PASSWORD = "H8hd#e3KejJR";
    private static final String FTP_IMAGES_DIRECTORY = "/images";
    
    public String uploadImageToFTP(Path imagePath, String imageName) throws Exception {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(FTP_SERVER, FTP_PORT);
            ftpClient.login(FTP_USER, FTP_PASSWORD);
            ftpClient.enterLocalPassiveMode();
    
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
    
            try (InputStream inputStream = Files.newInputStream(imagePath)) {
                String remoteFilePath = "/web/koumi-server/images/" + imageName; // Chemin d'acc                                                         ès complet sur le serveur FTP
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
      // Méthode pour récupérer une image à partir de son nom
      public byte[] getImageByName(String imageName) throws IOException {
        // Chemin où les images sont stockées sur le serveur FTP
        String imagePath = "/web/koumi-server/images/";
    
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
}
