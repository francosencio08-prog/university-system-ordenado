package pe.edu.utp.university_system.ai;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AILogResponse {

    private LocalDateTime generatedAt;

    private String studentCode;

    private String courseName;

    private String evaluationTitle;

    private String modelName;

    private RecommendationStatus status;
}