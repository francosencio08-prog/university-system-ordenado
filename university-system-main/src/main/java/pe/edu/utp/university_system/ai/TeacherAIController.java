package pe.edu.utp.university_system.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teacher/reviews")
@RequiredArgsConstructor
public class TeacherAIController {

    private final AIRecommendationRepository repository;

    @GetMapping("/pending")
    public ResponseEntity<List<TeacherReviewResponse>> getPendingReviews(
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        if (!"TEACHER".equals(role)) {
            return ResponseEntity.status(403).build();
        }

        List<AIRecommendation> pendingList = repository.findAllPendingReviews();

        List<TeacherReviewResponse> dtoList = pendingList.stream().map(rec -> {
            TeacherReviewResponse dto = new TeacherReviewResponse();
            dto.setId(rec.getId());
            dto.setStudentName(rec.getEnrollment().getStudent().getUserAccount().getUsername());
            dto.setCourseName(rec.getEnrollment().getSection().getCourse().getName());
            dto.setEvaluationTitle(rec.getEvaluationTitle());
            dto.setGradeObtained(rec.getGradeObtained());
            dto.setRecommendationContent(rec.getRecommendationContent());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveRecommendation(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        if (!"TEACHER".equals(role)) return ResponseEntity.status(403).build();

        AIRecommendation rec = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recomendación no encontrada"));

        if (payload.containsKey("editedContent")) {
            rec.setRecommendationContent(payload.get("editedContent"));
        }

        rec.setStatus(RecommendationStatus.APPROVED);
        repository.save(rec);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectRecommendation(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        if (!"TEACHER".equals(role)) return ResponseEntity.status(403).build();

        AIRecommendation rec = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recomendación no encontrada"));

        rec.setStatus(RecommendationStatus.REJECTED);
        repository.save(rec);

        return ResponseEntity.ok().build();
    }
}