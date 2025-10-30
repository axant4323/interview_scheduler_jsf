package com.iss.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "interviews")
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interview_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "candidate_id")
    private User candidate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "interviewer_id")
    private User interviewer;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.Scheduled;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getCandidate() { return candidate; }
    public void setCandidate(User candidate) { this.candidate = candidate; }
    public User getInterviewer() { return interviewer; }
    public void setInterviewer(User interviewer) { this.interviewer = interviewer; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
