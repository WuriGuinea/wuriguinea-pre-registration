/*
 * Copyright
 *
 */
package io.mosip.preregistration.batchjob.tasklets;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.preregistration.batchjob.utils.ReminderUtil;
import io.mosip.preregistration.core.config.LoggerConfiguration;
import io.mosip.preregistration.core.util.AuthTokenUtil;

/**
 * This class is a batch tasklet job to remind applicant
 *
 * @author CONDEIS
 */
@Component
public class ReminderTasklet implements Tasklet {

	@Autowired
	private ReminderUtil reminderUtil;
	@Autowired
	private AuthTokenUtil tokenUtil;

	@Value("${version}")
	String version;

	private Logger log = LoggerConfiguration.logConfig(ReminderTasklet.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.
	 * springframework.batch.core.StepContribution,
	 * org.springframework.batch.core.scope.context.ChunkContext)
	 */
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext arg1) throws Exception {

		try {
			HttpHeaders headers = tokenUtil.getTokenHeader();
			reminderUtil.processApplicantToRemind(headers);
		} catch (Exception e) {
			log.error("Reminder  ", " Tasklet ", " encountered exception ", e.getMessage());
			contribution.setExitStatus(new ExitStatus(e.getMessage()));
		}
		return RepeatStatus.FINISHED;
	}

}
