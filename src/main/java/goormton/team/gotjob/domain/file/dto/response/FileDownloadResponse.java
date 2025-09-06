package goormton.team.gotjob.domain.file.dto.response;

import com.amazonaws.services.s3.model.S3Object;

public record FileDownloadResponse(
        S3Object s3Object,
        String originalFilename
) {
}
