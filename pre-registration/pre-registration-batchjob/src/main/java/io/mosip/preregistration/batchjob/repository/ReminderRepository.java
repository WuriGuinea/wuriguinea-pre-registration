
package io.mosip.preregistration.batchjob.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
import io.mosip.preregistration.batchjob.entity.ReminderEntity;

/**
 * This repository interface is used to define the JPA methods for Reminder
 * application.
 * 
 * @author CONDEIS
 *
 */

@Repository("reminderRepository")
public interface ReminderRepository extends BaseRepository<ReminderEntity, String> {
		@Query("  FROM ReminderEntity re  WHERE re.regDate='tomorrow'")
	public List<ReminderEntity> findAppointmentsToRemind( );

}