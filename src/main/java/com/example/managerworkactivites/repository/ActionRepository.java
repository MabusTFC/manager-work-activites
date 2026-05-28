package com.example.managerworkactivites.repository;

import com.example.managerworkactivites.domain.Action;
import com.example.managerworkactivites.domain.ActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {
    List<Action> findByType(ActionType type);
    List<Action> findByCreatedAtGreaterThanEqual(LocalDateTime date);


    @Query("SELECT a FROM Action a WHERE a.startTime < :endTime AND a.endTime > :startTime")
    List<Action> findOverlappingIntervals(
            @Param("startTime") Integer startTime,
            @Param("endTime") Integer endTime
    );
}