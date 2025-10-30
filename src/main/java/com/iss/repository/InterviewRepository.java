package com.iss.repository;

import com.iss.model.Interview;
import com.iss.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    boolean existsByInterviewerAndDateAndTime(User interviewer, LocalDate date, LocalTime time);
    boolean existsByCandidateAndDateAndTime(User candidate, LocalDate date, LocalTime time);
    List<Interview> findByCandidate(User candidate);
    List<Interview> findByInterviewer(User interviewer);
}
