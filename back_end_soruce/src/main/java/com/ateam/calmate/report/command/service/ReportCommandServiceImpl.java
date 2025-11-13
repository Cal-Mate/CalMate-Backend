package com.ateam.calmate.report.command.service;

import com.ateam.calmate.member.command.entity.Member;
import com.ateam.calmate.member.command.repository.MemberRepository;
import com.ateam.calmate.report.command.dto.ReportCreateRequest;
import com.ateam.calmate.report.command.entity.*;
import com.ateam.calmate.report.command.repository.*;
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
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportCommandServiceImpl implements ReportCommandService {

    private final ReportRepository reportRepository;
    private final ReportFileUploadRepository reportFileUploadRepository;
    private final ReportExtendFilePathRepository reportExtendFilePathRepository;
    private final ReportPostRepository reportPostRepository;
    private final ReportCommentRepository reportCommentRepository;
    private final ReportBaseRepository reportBaseRepository;
    private final MemberRepository memberRepository;

    private static final String UPLOAD_ROOT_DIR = System.getProperty("user.dir") + "/img/report";

    @Override
    @Transactional
    public Long createReport(ReportCreateRequest request, List<MultipartFile> files) {

        ReportBase base = reportBaseRepository.findById(request.getReportBaseId())
                .orElseThrow(() -> new IllegalArgumentException("report base not found"));

        Report report = Report.builder()
                .title(request.getTitle())
                .contents(request.getContents())
                .yn(false)
                .date(LocalDateTime.now())
                .memberId(request.getReporterMemberId())
                .memberId2(request.getReportedMemberId())
                .postId(optLong(request.getPostId()))
                .commentId(optLong(request.getCommentId()))
                .reportId(base.getId())
                .build();

        reportRepository.save(report);

        if (files != null && !files.isEmpty()) {
            int order = 1;
            for (MultipartFile f : files) {
                if (f != null && !f.isEmpty()) {
                    saveFile(report, f, order++);
                }
            }

            if (report.getReportImageUrl() == null
                    && report.getFileUploads() != null
                    && !report.getFileUploads().isEmpty()) {

                report.setReportImageUrl(report.getFileUploads().get(0).getThumbPath());
                reportRepository.save(report);
            }
        }

        return report.getId();
    }

    @Override
    @Transactional
    public void processReport(Long reportId) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("report not found"));

        if (Boolean.TRUE.equals(report.getYn())) return;

        report.setYn(true);
        reportRepository.save(report);

        Member reported = memberRepository.findById(report.getMemberId2())
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        reported.setStatus(3L);
        memberRepository.save(reported);

        // 게시글 신고
        if (report.getPostId() != null) {
            reportPostRepository.findById(report.getPostId()).ifPresent(post -> {
                post.setVisibility(1);
                reportPostRepository.save(post);
            });
        }

        // 댓글 신고
        if (report.getCommentId() != null) {
            reportCommentRepository.findById(report.getCommentId().intValue())
                    .ifPresent(comment -> {
                        comment.setVisibility(1);
                        reportCommentRepository.save(comment);
                    });
        }
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

    private void saveFile(Report report, MultipartFile file, int order) {
        try {

            String originalName = Optional.ofNullable(file.getOriginalFilename()).orElse("file");
            String ext = Optional.ofNullable(StringUtils.getFilenameExtension(originalName)).orElse("");
            String storeName = UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);

            Path dir = Paths.get(UPLOAD_ROOT_DIR).toAbsolutePath().normalize();
            Files.createDirectories(dir);

            Path filePath = dir.resolve(storeName);
            file.transferTo(filePath.toFile());

            String urlPath = "/img/report/" + storeName;

            ReportExtendFilePath extend = ReportExtendFilePath.builder()
                    .urlPath(urlPath)
                    .build();
            reportExtendFilePathRepository.save(extend);

            ReportFileUpload upload = ReportFileUpload.builder()
                    .report(report)
                    .name(originalName)
                    .type(file.getContentType())
                    .reName(storeName)
                    .path(dir.toString())
                    .createAt(LocalDateTime.now())
                    .thumbPath(urlPath)
                    .uploadOrder(order)
                    .extendFilePath(extend)
                    .build();

            report.addFile(upload);
            reportFileUploadRepository.save(upload);

            if (order == 1 && report.getReportImageUrl() == null) {
                report.setReportImageUrl(urlPath);
                reportRepository.save(report);
            }

        } catch (Exception e) {
            throw new RuntimeException("file save error: " + e.getMessage(), e);
        }
    }

    private void deletePhysicalFiles(Report report) {
        if (report.getFileUploads() == null || report.getFileUploads().isEmpty()) return;

        report.getFileUploads().forEach(f -> {
            try {
                if (f.getPath() == null || f.getReName() == null) return;
                Files.deleteIfExists(Paths.get(f.getPath(), f.getReName()));
            } catch (Exception ignored) {}
        });
    }

    private Long optLong(Integer v) {
        return v == null ? null : v.longValue();
    }
}
