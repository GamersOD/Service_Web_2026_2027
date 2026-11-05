package rendu.georgesvalerian.demo.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.auth.aws.signer.RegionSet;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.utils.Validate;

import java.net.URI;
import java.time.Duration;

@Repository
public class S3Repository {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    //build le repository avec les valeur des clés
    public S3Repository(@Value("${s3.accesskey}") String accessKey, @Value("${s3.secretkey}") String secretKey){

        this.s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey,secretKey)))
                .endpointOverride(URI.create("https://s3.bober.ovh"))
                .region(Region.US_EAST_1)
                .sigv4aSigningRegionSet(RegionSet.GLOBAL)
                .forcePathStyle(true).build();

        S3Configuration configuration = S3Configuration.builder().pathStyleAccessEnabled(true).build();

        this.s3Presigner = S3Presigner.builder().credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.builder().accessKeyId(accessKey).secretAccessKey(secretKey).build()))
                .endpointOverride(URI.create("https://s3.bober.ovh"))
                .serviceConfiguration(configuration).region(Region.US_EAST_1).build();
    }

    public String getUploadFileUrl(String filePath,String BUCKET) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(filePath)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(request)
                .build();

        return this.s3Presigner.presignPutObject(presignRequest).url().toExternalForm();
    }

    //recupère l'URL d'un fichier
    public String getFileUrl(String filePath,String BUCKET) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(BUCKET)
                .key(filePath)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(request)
                .build();

        return this.s3Presigner.presignGetObject(presignRequest).url().toExternalForm();
    }

    //verifie qu'un objet existe
    public boolean doesObjectExist(String bucketName, String key) {
        try {
            Validate.notEmpty(bucketName, "The bucket name must not be null or an empty string.", "");
            Validate.notEmpty(key, "The object key must not be null or an empty string.", "");
            this.s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build());
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }
}
