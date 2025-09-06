package goormton.team.gotjob.global.config;

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
        // 특정 파일 경로를 지정하고 싶다면 아래 주석처럼 FileInputStream을 사용할 수 있습니다.
        // GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("path/to/your/keyfile.json"));

        // 클라이언트의 엔드포인트를 설정합니다. (예: 미국 us, 유럽 eu)
        String endpoint = "us-documentai.googleapis.com:443";
        DocumentProcessorServiceSettings settings = DocumentProcessorServiceSettings.newBuilder()
                .setEndpoint(endpoint)
                // .setCredentialsProvider(FixedCredentialsProvider.create(credentials)) // 파일 경로를 직접 지정할 경우
                .build();
        return DocumentProcessorServiceClient.create(settings);
    }
}
