package com.togh.repository;

import com.togh.entity.EventEntity;
import com.togh.entity.EventEntity.StatusEventEnum;
import com.togh.entity.ParticipantEntity;
import com.togh.entity.ParticipantEntity.ParticipantRoleEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/*
 * This is the EventRepository, Spring implementation.
 * In Spring, the implementation is a interface, and should extends the CrudRepository. Then the service method use the @autowired to generate the class
 */
// extends EventRepository, CrudRepository<EventEntity, Long> JPARepository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    @Query("SELECT e FROM EventEntity e WHERE e.id = :id")
    EventEntity findByEventId(@Param("id") Long id);

    EventEntity findByName(@Param("name") String name);

    @Query("SELECT e FROM EventEntity e, ToghUserEntity t WHERE e.author = t and t.id = :userid order by e.dateCreation")
    List<EventEntity> findMyEventsUser(@Param("userid") Long userId);

    @Query("SELECT e FROM EventEntity e, ToghUserEntity t join e.participantList p where p.user = t and t.id = :userid order by e.dateCreation")
    List<EventEntity> findEventsUser(@Param("userid") Long userId);


    @Query("SELECT e FROM EventEntity e,  ToghUserEntity t join e.participantList p where p.user = t and p.status= :statusparticipant and t.id = :userid order by e.dateCreation")
    List<EventEntity> findEventsUserByStatusParticipant(@Param("userid") Long userId, @Param("statusparticipant") ParticipantEntity.StatusEnum statusParticipant);

    @Query("SELECT count(e) FROM EventEntity e, ToghUserEntity t "
            + "JOIN e.participantList p "
            + "WHERE p.user = t and t.id = :userid and p.role = :role and e.dateCreation > :datecreationevent")
    Long countLastEventsUser(@Param("userid") Long userId, @Param("role") ParticipantRoleEnum role, @Param("datecreationevent") LocalDateTime dateCreationEvent);

    /**
     * Search all past event OR, if the event is just modified in the last X time, then it survived
     *
     * @param dateLimit       if the event is created before this date, it is considered as old (except for the dateGrace mechanism)
     * @param dateGrace       if modification is AFTER this date, it is not considered as an Old event
     * @param notEqualsStatus status must not be equals to this status
     * @return the list of old event
     */
    @Query("SELECT e FROM EventEntity e "
            + "WHERE ("
            + " (e.datePolicy = 'ONEDATE' and e.dateEvent < :dateLimit)"
            + " or (e.datePolicy = 'PERIOD' and e.dateEndEvent < :dateLimit)"
            + ") and e.dateModification < :dateGrace "
            + " and e.statusEvent != :status")
    List<EventEntity> findOldEvents(@Param("dateLimit") LocalDateTime dateLimit, @Param("dateGrace") LocalDateTime dateGrace, @Param("status") StatusEventEnum notEqualsStatus, Pageable pageable);


    @Query("SELECT e FROM EventEntity e join e.participantList p "
            + "WHERE p.id = :participantId")
    EventEntity findByParticipant(@Param("participantId") Long participantId);

    /**
     * Search all past event OR, if the event is just modified in the last X time, then it survived
     *
     * @param timeGrace limit time under when
     * @param status
     * @param pageable
     * @return
     */
    @Query("SELECT e FROM EventEntity e "
            + "WHERE e.dateModification < :timeGrace "
            + " and e.statusEvent = :status ")
    List<EventEntity> findEventsToPurge(@Param("timeGrace") LocalDateTime timeGrace, @Param("status") StatusEventEnum status, Pageable pageable);

}
