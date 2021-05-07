/* ******************************************************************************** */
/*                                                                                  */
/* Togh Project */
/*                                                                                  */
/* This component is part of the Togh Project, developed by Pierre-Yves Monnet */
/*                                                                                  */
/*                                                                                  */
/* ******************************************************************************** */
package com.togh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.togh.entity.AdminStatsUsageEntity;
import com.togh.entity.AdminStatsUsageEntity.TypeStatsEnum;
import com.togh.entity.ToghUserEntity.SubscriptionUserEnum;
import com.togh.service.SubscriptionService.LimitReach;

public interface AdminStatsUsageRepository extends JpaRepository<AdminStatsUsageEntity, Long> {

    @Query("select adminstats from AdminStatsUsageEntity adminstats"
            + " where adminstats.yearMonthDay = :dateToSearch "
            + " and typeStatistique = :typeStats")
    public AdminStatsUsageEntity findByDate(@Param("dateToSearch") String dateToSearch, 
            @Param("typeStats") TypeStatsEnum typeStats);

    @Query("select adminstats from AdminStatsUsageEntity adminstats "
            + " where adminstats.yearMonthDay = :dateToSearch "
            + " and typeStatistique = :typeStats "
            + " and subscriptionuser = :subscriptionUser "
            + " and limitreach = :limitReach")
    /** Using the enumerate for subscriptionUser and limitReach provoque an error ( ! ) 
     */
    public AdminStatsUsageEntity findByDateLimit(@Param("dateToSearch") String dateToSearch, 
            @Param("typeStats") TypeStatsEnum typeStats,
            @Param("subscriptionUser") String subscriptionUser,
            @Param("limitReach") String limitReach);

    @Query(value= "select adminstats from AdminStatsUsageEntity adminstats order by adminstats.yearMonthDay asc")
    public AdminStatsUsageEntity getAllDates();

}
