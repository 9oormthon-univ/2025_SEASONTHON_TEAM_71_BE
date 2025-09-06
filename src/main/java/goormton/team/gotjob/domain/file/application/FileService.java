package goormton.team.gotjob.domain.file.application;


import com.amazonaws.services.s3.model.S3Object;
import goormton.team.gotjob.domain.file.domain.File;
import goormton.team.gotjob.domain.file.domain.repository.FileRepository;
import goormton.team.gotjob.domain.file.dto.response.FileDownloadResponse;
import goormton.team.gotjob.domain.user.domain.User;
import goormton.team.gotjob.domain.user.repository.UserRepository;
import goormton.team.gotjob.global.error.DefaultException;
import goormton.team.gotjob.global.payload.ErrorCode;
import goormton.team.gotjob.global.security.JwtTokenProvider;
import goormton.team.gotjob.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtUtil;
//    private final DocumentAiService documentAiService;
    private final FileRepository fileRepository;

    // S3 버킷에 파일 업로드
    @Transactional
    public String uploadAndSaveFile(MultipartFile multipartFile, String token) {
        S3Service.S3UploadResult uploadResult = s3Service.uploadFile(multipartFile);

        String rawToken = jwtUtil.resolveToken(token);
        Long userId = jwtUtil.getUserId(rawToken);
        log.info("userId: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new DefaultException(ErrorCode.USER_NOT_FOUND_ERROR));

        File file = File.builder()
                .user(user)
                .originalFileName(uploadResult.getOriginalFileName())
                .storedFileName(uploadResult.getStoredFileName())
                .fileUrl(uploadResult.getFileUrl())
                .build();

        fileRepository.save(file);

        // documentAiService.extractKeywordsWithWeights(file.getId());

        return file.getFileUrl();
    }

    // 파일 ID로 파일을 찾아 다운로드 DTO를 반환
    public FileDownloadResponse downloadFile(Long fileId, String token) {
        File file = fileRepository.findById(fileId).orElseThrow(() -> new DefaultException(ErrorCode.FILE_NOT_FOUND));

        String rawToken = jwtUtil.resolveToken(token);
        Long userId = jwtUtil.getUserId(rawToken);
        User user = userRepository.findById(userId).orElseThrow(() -> new DefaultException(ErrorCode.USER_NOT_FOUND_ERROR));

        if (!file.getUser().getId().equals(user.getId())) {
            throw new DefaultException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        S3Object s3Object = s3Service.downloadFile(file.getStoredFileName());

        return new FileDownloadResponse(s3Object, file.getOriginalFileName());
    }
}
