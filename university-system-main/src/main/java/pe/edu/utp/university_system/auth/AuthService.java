package pe.edu.utp.university_system.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.utp.university_system.student.*;
import pe.edu.utp.university_system.teacher.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAccountRepository repository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public LoginResponse authenticate(LoginRequest request) {

        UserAccount account =
                repository.findByUsername(
                                request.getUsername())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid credentials"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                account.getPassword())) {

            throw new RuntimeException(
                    "Invalid credentials");
        }

        if (!account.getActive()) {
            throw new RuntimeException(
                    "Account is inactive");
        }

        LoginResponse response = new LoginResponse();
        response.setAccountId(account.getId());
        response.setUsername(account.getUsername());
        response.setRole(account.getRole().name());

        if (account.getRole().name().equals("TEACHER")) {
            Teacher teacher = teacherRepository.findByUserAccountId(account.getId())
                    .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));

            response.setTeacherId(teacher.getId());
        }

        if (account.getRole().name().equals("STUDENT")) {
            Student student = studentRepository.findByUserAccountId(account.getId())
                    .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
            response.setStudentId(student.getId());
        }

        return response;
    }
}