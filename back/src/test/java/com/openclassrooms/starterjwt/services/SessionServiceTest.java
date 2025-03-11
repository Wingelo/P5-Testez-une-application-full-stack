package com.openclassrooms.starterjwt.services;


import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {
    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionService sessionService;

    @Mock
    private UserRepository userRepository;

    private List<Session> listSessions;

    @BeforeEach
    public void init() {

        List<User> users = new ArrayList<>();
        users.add(new User(1L, "john.cena@gmail.com", "Cena", "John", "12356", false, LocalDateTime.now(), LocalDateTime.now()));
        users.add(new User(2L, "john.levis@gmail.com", "Levis", "John", "678910", true, LocalDateTime.now(), LocalDateTime.now()));

        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher(1L, "Marc", "Antoine", LocalDateTime.now(), LocalDateTime.now()));
        teachers.add(new Teacher(2L, "Jerry", "Tom", LocalDateTime.now(), LocalDateTime.now()));

        this.listSessions = new ArrayList<>();
        this.listSessions.add(new Session(1L, "First session", new Date(), "A small description", teachers.get(0), users, LocalDateTime.now(), LocalDateTime.now()));
        this.listSessions.add(new Session(2L, "Second session", new Date(), "A very small description", teachers.get(1), users, LocalDateTime.now(), LocalDateTime.now()));
    }


    @Test
    void createSession() {
        // GIVEN
        Session session = this.listSessions.get(0);
        // WHEN
        when(sessionRepository.save(session)).thenReturn(session);
        Session result = sessionService.create(session);
        // THEN
        assertThat(result).isEqualTo(session);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void deleteSession() {
        // GIVEN
        Long sessionId = 1L;
        // WHEN
        sessionService.delete(sessionId);
        // THEN
        verify(sessionRepository, times(1)).deleteById(sessionId);
    }

    @Test
    void findAllSessions() {
        // GIVEN
        when(sessionRepository.findAll()).thenReturn(listSessions);
        // WHEN
        List<Session> result = sessionService.findAll();
        // THEN
        assertThat(result).isEqualTo(listSessions);
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    void findSessionById() {
        // GIVEN
        Long sessionId = 1L;
        Session session = this.listSessions.get(0);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        // WHEN
        Session result = sessionService.getById(sessionId);
        // THEN
        assertThat(result).isEqualTo(session);
        verify(sessionRepository, times(1)).findById(sessionId);
    }

    @Test
    void findSessionsByNoIdExisting() {
        // GIVEN
        Long sessionId = 1L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());
        // WHEN
        Session result = sessionService.getById(sessionId);
        // THEN
        assertThat(result).isNull();
        verify(sessionRepository, times(1)).findById(sessionId);
    }

    @Test
    void updateSession() {
        // GIVEN
        Long sessionId = 1L;
        String newDescription = "New description";
        Session session = this.listSessions.get(0);
        Session sessionUpdated = session.setDescription(newDescription);
        when(sessionRepository.save(session)).thenReturn(sessionUpdated);
        // WHEN
        Session result = sessionService.update(sessionId, sessionUpdated);
        // THEN
        assertThat(result).isEqualTo(sessionUpdated);
        assertThat(result.getId()).isEqualTo(sessionId);
        assertThat(sessionUpdated.getDescription()).isEqualTo(newDescription);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void participationSuccess() {
        // GIVEN
        Long sessionId = 1L;
        Long participantId = 3L;
        Session session = this.listSessions.get(0);
        User user = new User(participantId, "john.cena@gmail.com", "Cena", "John", "123569", false, LocalDateTime.now(), LocalDateTime.now());

        // WHEN
        when(sessionRepository.findById(any(Long.class))).thenReturn(Optional.of(session));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(sessionRepository.save(session)).thenReturn(session);
        sessionService.participate(sessionId, participantId);
        // THEN
        assertThat(session.getUsers()).contains(user);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void participationNotFound() {
        // GIVEN
        Long sessionId = 1L;
        Long participantId = 2L;
        // WHEN
        when(sessionRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        // THEN
        assertThatThrownBy(() -> sessionService.participate(sessionId, participantId)).isInstanceOf(NotFoundException.class);
        verify(sessionRepository, times(0)).save(any());
    }

    @Test
    void participationAlreadyParticipated() {
        // GIVEN
        Long sessionId = 1L;
        Long participantId = 2L;
        Session session = this.listSessions.get(0);
        User user = this.listSessions.get(0).getUsers().get(0);
        // WHEN
        when(sessionRepository.findById(any(Long.class))).thenReturn(Optional.of(session));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        // THEN
        assertThatThrownBy(() -> sessionService.participate(sessionId, participantId)).isInstanceOf(BadRequestException.class);
        verify(sessionRepository, times(0)).save(any());
    }

    @Test
    void noParticipateSuccess() {
        // GIVEN
        Long sessionId = 1L;
        Long participantId = 2L;
        Session session = this.listSessions.get(0);
        User user = this.listSessions.get(0).getUsers().get(1);
        // WHEN
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        sessionService.noLongerParticipate(sessionId, participantId);
        // THEN
        assertThat(session.getUsers()).doesNotContain(user);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void noParticipateNotFound() {
        // GIVEN
        Long sessionId = 1L;
        Long participantId = 2L;
        // WHEN
        when(sessionRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        // THEN
        assertThatThrownBy(() -> sessionService.noLongerParticipate(sessionId, participantId)).isInstanceOf(NotFoundException.class);
        verify(sessionRepository, times(0)).save(any());
    }

    @Test
    void noParticipateNotParticipated() {
        // GIVEN
        Long sessionId = 1L;
        Long participantId = 3L;
        Session session = this.listSessions.get(0);
        //WHEN
        when(sessionRepository.findById(any(Long.class))).thenReturn(Optional.of(session));
        //THEN
        assertThatThrownBy(() -> sessionService.noLongerParticipate(sessionId, participantId)).isInstanceOf(BadRequestException.class);
        verify(sessionRepository, times(0)).save(any());
    }

}
