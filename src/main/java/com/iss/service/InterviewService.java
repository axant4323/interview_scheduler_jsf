package com.iss.service;

import com.iss.model.Interview;
import com.iss.model.Status;
import com.iss.model.User;
import com.iss.repository.InterviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class InterviewService {
    private final InterviewRepository interviewRepository;

    public InterviewService(InterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }

    @Transactional
    public Interview schedule(User candidate, User interviewer, LocalDate date, LocalTime time) {
        if (interviewRepository.existsByInterviewerAndDateAndTime(interviewer, date, time)) {
            throw new IllegalStateException("Interviewer already has an interview at this time.");
        }
        if (interviewRepository.existsByCandidateAndDateAndTime(candidate, date, time)) {
            throw new IllegalStateException("Candidate already has an interview at this time.");
        }
        Interview interview = new Interview();
        interview.setCandidate(candidate);
        interview.setInterviewer(interviewer);
        interview.setDate(date);
        interview.setTime(time);
        interview.setStatus(Status.Scheduled);
        return interviewRepository.save(interview);
    }

    public List<Interview> getForCandidate(User candidate) {
        return interviewRepository.findByCandidate(candidate);
    }

    public List<Interview> getForInterviewer(User interviewer) {
        return interviewRepository.findByInterviewer(interviewer);
    }

    public Interview updateStatus(Long id, Status status) {
        Interview i = interviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Interview not found"));
        i.setStatus(status);
        return interviewRepository.save(i);
    }
}
