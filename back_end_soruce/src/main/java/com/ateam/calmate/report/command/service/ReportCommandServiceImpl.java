package com.ateam.calmate.report.command.service;

import com.ateam.calmate.report.command.dto.ReportCreateRequest;
import com.ateam.calmate.report.command.entity.Report;
import com.ateam.calmate.report.command.entity.ReportExtendFilePath;
import com.ateam.calmate.report.command.entity.ReportFileUpload;
import com.ateam.calmate.report.command.repository.ReportExtendFilePathRepository;
import com.ateam.calmate.report.command.repository.ReportFileUploadRepository;
import com.ateam.calmate.report.command.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportCommandServiceImpl implements ReportCommandService {

    private final ReportRepository reportRepository;
    private final ReportFileUploadRepository reportFileUploadRepository;
    private final ReportExtendFilePathRepository reportExtendFilePathRepository;

    // 권한 문제 안 나게 프로젝트 내부 상대 경로
    private final String uploadRootDir = "uploads/report";

    @Override
    @Transactional
    public Long createReport(ReportCreateRequest request, List<MultipartFile> files) {

        Report report = Report.builder()
                .title(request.getTitle())
                .contents(request.getContents())
                .yn(false)
                .date(LocalDateTime.now())
                .reportImageUrl(null)
                .memberId2(request.getReportedMemberId())  // 피신고자
                .postId(request.getPostId())               // 게시글 신고면 값 있음
                .commentId(request.getCommentId())         // 댓글 신고면 값 있음
                .adminId(null)
                .reportBaseId(request.getReportBaseId())   // 신고 코드 (욕설/도배 등)
                .memberId(request.getReporterMemberId())   // 신고자
                .build();

        reportRepository.save(report);

        if (files != null && !files.isEmpty()) {
            int order = 1;
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;
                saveFile(report, file, order++);
            }
        }

        return report.getId();
    }

    @Override
    @Transactional
    public void deleteMyReport(Long reportId, Long reporterMemberId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("report not found"));

        if (!Objects.equals(report.getMemberId(), reporterMemberId)) {
            throw new IllegalStateException("you can delete only your own report");
        }

        deletePhysicalFiles(report);
        reportRepository.delete(report);
    }

    // ----------------- 내부 메서드 -----------------

    private void saveFile(Report report, MultipartFile file, int order) {
        try {
            String originalName = file.getOriginalFilename();
            String ext = StringUtils.getFilenameExtension(originalName);
            String storeName = UUID.randomUUID() + (ext != null ? "." + ext : "");

            Path root = Paths.get(uploadRootDir).toAbsolutePath().normalize();
            Path dir = root.resolve(String.valueOf(report.getId()));
            Files.createDirectories(dir);

            Path filePath = dir.resolve(storeName);
            file.transferTo(filePath.toFile());

            String urlPath = "/uploads/report/" + report.getId() + "/" + storeName;

            ReportExtendFilePath extendFilePath = ReportExtendFilePath.builder()
                    .urlPath(urlPath)
                    .build();
            reportExtendFilePathRepository.save(extendFilePath);

            ReportFileUpload upload = ReportFileUpload.builder()
                    .report(report)
                    .name(originalName)
                    .type(file.getContentType())
                    .reName(storeName)                     // ✅ rename → reName
                    .path(dir.toString())
                    .createAt(LocalDateTime.now())
                    .thumbPath(urlPath)
                    .uploadOrder(order)
                    .extendFilePath(extendFilePath)
                    .build();

            reportFileUploadRepository.save(upload);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("file save error: " + e.getMessage(), e);
        }
    }

    private void deletePhysicalFiles(Report report) {
        if (report.getFileUploads() == null || report.getFileUploads().isEmpty()) {
            return;
        }

        report.getFileUploads().forEach(f -> {
            try {
                if (f.getPath() == null || f.getReName() == null) return; // ✅ getRename → getReName
                Path path = Paths.get(f.getPath(), f.getReName());
                Files.deleteIfExists(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
