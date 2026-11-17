package pe.edu.utp.university_system.ai;
import lombok.Data;

@Data
public class TeacherReviewResponse {
    private Long id;
    private String studentName;
    private String courseName;
    private String evaluationTitle;
    private Double gradeObtained;
    private String recommendationContent;
}