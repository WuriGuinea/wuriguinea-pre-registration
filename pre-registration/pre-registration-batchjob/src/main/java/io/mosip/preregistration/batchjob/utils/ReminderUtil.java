package io.mosip.preregistration.batchjob.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import io.mosip.preregistration.demographic.dto.DemographicRequestDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.notification.spi.SMSServiceProvider;
import io.mosip.preregistration.batchjob.code.ErrorCodes;
import io.mosip.preregistration.batchjob.code.ErrorMessages;
import io.mosip.preregistration.batchjob.entity.ReminderEntity;
import io.mosip.preregistration.batchjob.exception.RestCallException;
import io.mosip.preregistration.batchjob.model.ReminderDTO;
import io.mosip.preregistration.batchjob.repository.utils.BatchJpaRepositoryImpl;
import io.mosip.preregistration.core.common.dto.DemographicResponseDTO;
import io.mosip.preregistration.core.common.dto.MainRequestDTO;
import io.mosip.preregistration.core.common.dto.MainResponseDTO;
import io.mosip.preregistration.core.common.dto.NotificationDTO;
import io.mosip.preregistration.core.common.entity.DemographicEntity;
import io.mosip.preregistration.core.config.LoggerConfiguration;
import io.mosip.preregistration.core.exception.NotificationException;
import io.mosip.preregistration.core.util.AuditLogUtil;
import io.mosip.preregistration.core.util.CryptoUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author CONDEIS
 */
@Component
public class ReminderUtil {
	/**
	 * Autowired reference for {@link #batchServiceDAO}
	 */
	@Autowired
	private BatchJpaRepositoryImpl batchServiceDAO;

	@Autowired
	private AuditLogUtil auditLogUtil;

	@Autowired
	CryptoUtil cryptoUtil;

	/** The Constant LOGGER. */
	private Logger log = LoggerConfiguration.logConfig(ReminderUtil.class);

	/**
	 * Autowired reference for {@link #restTemplateBuilder}
	 */
	@Autowired
	RestTemplate restTemplate;

	@Value("${notification.url}")
	private String notificationResourseurl;

	@Value("${mosip.primary-language}")
	String primaryLang;
	List<ReminderDTO> reminders;

	public boolean processApplicantToRemind(HttpHeaders headers) throws JsonProcessingException {
		reminders = new ArrayList<ReminderDTO>();
		DemographicEntity demogEntity;
		List<ReminderEntity> toReminds = batchServiceDAO.findAppointmentsToRemind();
		for (ReminderEntity reminderEntiy : toReminds) {
			demogEntity = batchServiceDAO.getApplicantDemographicDetails(reminderEntiy.getPrereg_id());
			ReminderDTO remindDTO = extractRemindingDetails(reminderEntiy, demogEntity);
			log.debug("Value extracted:", "", "", "" + remindDTO);
			handleReminderDTO(remindDTO);
		}
		sendRemindingNotifications(headers);

		return true;

	}

	private void handleReminderDTO(ReminderDTO reminderDTO) {
		if (reminderDTO != null)
			reminders.add(reminderDTO);
	}

	private ReminderDTO extractRemindingDetails(ReminderEntity remind, DemographicEntity demogEntity) {
		ReminderDTO reminderDTO = null;
		String tmp = "";
		try {
			String applicantfirstName = "UNASSIGNED";
			String appliantLastName = "UNASSIGNED";
			String applicantGender = "UNASSIGNED";
			String applicantMobNum = "UNASSIGNED";
			String applicantEmail = "UNASSIGNED";
			String preRegId = remind.getPrereg_id();
			String centerID = remind.getRegistrationCenterId();
			String slotFrom = "" + remind.getSlotFromTime();
			String toSlot = "" + remind.getSlotToTime();
			String appointementDate = "" + remind.getRegDate();

			reminderDTO = new ReminderDTO(preRegId, appointementDate, slotFrom, toSlot, applicantfirstName,
					appliantLastName, applicantGender, applicantMobNum, applicantEmail, centerID);
		} catch (Exception e) {
			log.debug("Error while processing applicants details", "", "", "" + e + "- " + tmp);

		}
		return reminderDTO;
	}

	private void sendRemindingNotifications(HttpHeaders headers) throws JsonProcessingException {
		for (ReminderDTO remindTo : reminders) {
			processNotificationSending(remindTo, headers);
			System.out.println("Processing " + remindTo.getAppliantLastName());
		}

	}

	private void processNotificationSending(ReminderDTO remindTo, HttpHeaders headers) throws JsonProcessingException {
		String message = remindingMessage(remindTo);
		NotificationDTO notification = new NotificationDTO();
		notification.setIsReminderBatch(true);
	//	notification.setEmailID(remindTo.getApplicantEmail());
	//	notification.setMobNum(remindTo.getApplicantMobNum());
	//	notification.setName(remindTo.getApplicantName());
		notification.setAppointmentDate(remindTo.getAppointementDate());
		notification.setPreRegistrationId(remindTo.getPreRegId());
		String time = LocalTime.parse(remindTo.getSlotFrom(), DateTimeFormatter.ofPattern("HH:mm"))
				.format(DateTimeFormatter.ofPattern("hh:mm a"));
		notification.setAppointmentTime(time);
		notification.setAdditionalRecipient(false);
		notification.setIsBatch(true);
		//added
		notification.setName("ABOU TEST");
		notification.setMobNum("625739085");
		notification.setEmailID("net6crash@gmail.com");
		log.info("sessionId", "idType", "id", "Sending reminder to" + remindTo.getApplicantName());
		System.out.println(message);
		emailNotification(notification, primaryLang, headers);

	}

	private String remindingMessage(ReminderDTO reminderDTO) {
		String message = "Bonjour " + reminderDTO.getAppliantLastName() + " " + "" + reminderDTO.getApplicantfirstName()
				+ "," + "\n" + "Nous vous rappelons que vous avez rendez-vous   demain à " + reminderDTO.getSlotFrom()
				+ " " + "au centre numero " + reminderDTO.getCenterID() + "muni de votre numero de pre-enregistrement "
				+ reminderDTO.getPreRegId() + " et des pieces justificatives.";
		return message;
	}

	/**
	 * 
	 * @param notificationDTO
	 * @param langCode
	 * @return NotificationResponseDTO
	 * @throws JsonProcessingException
	 */
	public void emailNotification(NotificationDTO notificationDTO, String langCode, HttpHeaders headers)
			throws JsonProcessingException {
		String emailResourseUrl = notificationResourseurl + "/notify";
		ResponseEntity<String> resp = null;
		MainRequestDTO<NotificationDTO> request = new MainRequestDTO<>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.setTimeZone(TimeZone.getDefault());
		try {
			request.setRequest(notificationDTO);
			request.setId("mosip.pre-registration.notification.notify");
			request.setVersion("1.0");
			request.setRequesttime(new Date());
			MultiValueMap<Object, Object> emailMap = new LinkedMultiValueMap<>();
			emailMap.add("NotificationRequestDTO", mapper.writeValueAsString(request));
			emailMap.add("langCode", langCode);
			HttpEntity<MultiValueMap<Object, Object>> httpEntity = new HttpEntity<>(emailMap, headers);
			log.info("sessionId", "idType", "id",
					"In emailNotification method of NotificationUtil service emailResourseUrl: " + emailResourseUrl);

			System.out.println("------------------------------"+httpEntity);
			resp = restTemplate.exchange(emailResourseUrl, HttpMethod.POST, httpEntity, String.class);
			List<ServiceError> validationErrorList = ExceptionUtils.getServiceErrorList(resp.getBody());
			if (validationErrorList != null && !validationErrorList.isEmpty()) {
				throw new NotificationException(validationErrorList, null);
			}
		} catch (HttpClientErrorException ex) {
			log.error("sessionId", "idType", "id",
					"In emailNotification method of reminder Service Util for HttpClientErrorException- "
							+ ex.getMessage());
			throw new RestCallException(ErrorCodes.PRG_PAM_BAT_012.getCode(),
					ErrorMessages.NOTIFICATION_CALL_FAILED.getMessage());

		}
	}
}
