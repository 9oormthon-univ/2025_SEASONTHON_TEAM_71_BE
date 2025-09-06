package goormton.team.gotjob.global.config;

import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.documentai.v1.DocumentProcessorServiceClient;
import com.google.cloud.documentai.v1.DocumentProcessorServiceSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class DocumentAiConfig {

    @Bean
    public DocumentProcessorServiceClient documentProcessorServiceClient() throws IOException {
        // 환경 변수가 설정되어 있으면 GoogleCredentials.getApplicationDefault()가 자동으로 인증 정보를 찾습니다.
        // 클라이언트의 엔드포인트를 설정합니다.
        String endpoint = "us-documentai.googleapis.com:443";

        // 1. 기본 인증 정보 제공자(CredentialsProvider)를 가져옵니다.
        //    이 부분이 GOOGLE_APPLICATION_CREDENTIALS 환경 변수를 자동으로 읽어옵니다.
        CredentialsProvider credentialsProvider = DocumentProcessorServiceSettings.defaultCredentialsProviderBuilder().build();

        // 2. 설정(Settings)을 만들 때 엔드포인트와 함께 인증 정보 제공자를 명시적으로 설정해줍니다.
        DocumentProcessorServiceSettings settings = DocumentProcessorServiceSettings.newBuilder()
                .setEndpoint(endpoint)
                .setCredentialsProvider(credentialsProvider) // <--- 이 줄이 추가되었습니다!
                .build();
        return DocumentProcessorServiceClient.create(settings);
    }
}
