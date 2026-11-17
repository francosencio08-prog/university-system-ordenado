package pe.edu.utp.university_system.ai;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIRecommendationRepository extends JpaRepository<AIRecommendation, Long> {

    @Query("""
        SELECT r
        FROM AIRecommendation r
        WHERE r.enrollment.student.userAccount.id = :accountId
          AND r.status = pe.edu.utp.university_system.ai.RecommendationStatus.APPROVED
        ORDER BY r.generatedAt DESC
    """)
    List<AIRecommendation> findByStudentAccountId(
            @Param("accountId") Long accountId
    );

    @Query("""
        SELECT COUNT(r)
        FROM AIRecommendation r
        WHERE r.enrollment.student.userAccount.id = :accountId
          AND r.status = pe.edu.utp.university_system.ai.RecommendationStatus.APPROVED
          AND r.readByStudent = false
    """)
    Long countUnreadRecommendations(
            @Param("accountId") Long accountId
    );


    @Modifying
    @Transactional
    @Query("""
        UPDATE AIRecommendation r
           SET r.readByStudent = true
         WHERE r.enrollment.student.userAccount.id = :accountId
           AND r.status = pe.edu.utp.university_system.ai.RecommendationStatus.APPROVED
    """)
    void markAllAsRead(
            @Param("accountId") Long accountId
    );

    @Query("""
        SELECT r
        FROM AIRecommendation r
        WHERE r.status = pe.edu.utp.university_system.ai.RecommendationStatus.PENDING_REVIEW
        ORDER BY r.generatedAt ASC
    """)
    List<AIRecommendation> findAllPendingReviews();

    @Modifying
    @Transactional
    @Query("""
        UPDATE AIRecommendation r
        SET r.readByStudent = true
        WHERE r.id = :id
    """)
    void markAsRead(Long id);

    @Query("""
        SELECT r
        FROM AIRecommendation r
        ORDER BY r.generatedAt DESC
    """)
    List<AIRecommendation> findGenerationHistory();
}