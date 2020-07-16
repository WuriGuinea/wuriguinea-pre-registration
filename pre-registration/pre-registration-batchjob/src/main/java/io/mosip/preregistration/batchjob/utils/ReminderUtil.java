package io.mosip.preregistration.batchjob.utils;

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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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
import io.mosip.preregistration.core.common.dto.MainRequestDTO;
import io.mosip.preregistration.core.common.dto.NotificationDTO;
import io.mosip.preregistration.core.common.entity.DemographicEntity;
import io.mosip.preregistration.core.config.LoggerConfiguration;
import io.mosip.preregistration.core.exception.NotificationException;
import io.mosip.preregistration.core.util.AuditLogUtil;

/**
 * 
 * @author CONDEIS
 *
 */
@Component
public class ReminderUtil {
	private static String JSON_FIELD_IDENTITY = "identity";
	private static String JSON_FIELD_FIRSTNAME = "firstName";
	private static String JSON_FIELD_LASTNAME = "lastName";
	private static String JSON_FIELD_GENDER = "gender";
	private static String JSON_FIELD_EMAIL = "email";
	private static String JSON_FIELD_PHONE = "phone";
	private static String JSON_FIELD_VALUE = "value";

	/**
	 * Autowired reference for {@link #batchServiceDAO}
	 */
	@Autowired
	private BatchJpaRepositoryImpl batchServiceDAO;

//	@Autowired
	private AuditLogUtil auditLogUtil;

	/** The Constant LOGGER. */
	private Logger log = LoggerConfiguration.logConfig(ReminderUtil.class);

	RestTemplate restTemplate;

	@Value("${notification.url}")
	private String notificationResourseurl;

	List<ReminderDTO> reminders;

	@Autowired
	SMSServiceProvider smsServiceProvider;

	public boolean processApplicantToRemind() {
		reminders = new ArrayList<ReminderDTO>();
		DemographicEntity demogEntity;
		List<ReminderEntity> toReminds = batchServiceDAO.findAppointmentsToRemind();
		for (ReminderEntity reminderEntiy : toReminds) {
			demogEntity = batchServiceDAO.getApplicantDemographicDetails(reminderEntiy.getPrereg_id());
			ReminderDTO remindDTO = extractRemindingDetails(reminderEntiy, demogEntity);
			log.debug("Value extracted:", "", "", ""+remindDTO);
			handleReminderDTO(remindDTO);
		}
		sendRemindingNotifications();

		return true;

	}
 private void handleReminderDTO(ReminderDTO reminderDTO)
 {
 	if (reminderDTO!=null)
 		reminders.add(reminderDTO);
 }
	private ReminderDTO extractRemindingDetails(ReminderEntity remind, DemographicEntity demogEntity) {
		ReminderDTO reminderDTO = null;

		try {
			JSONObject applicantDetails = new JSONObject(new String(demogEntity.getApplicantDetailJson()))
					.getJSONObject(JSON_FIELD_IDENTITY);
			String applicantfirstName = ((JSONObject) (applicantDetails.getJSONArray(JSON_FIELD_FIRSTNAME).get(0)))
					.getString(JSON_FIELD_VALUE);
			String appliantLastName = ((JSONObject) (applicantDetails.getJSONArray(JSON_FIELD_LASTNAME).get(0)))
					.getString(JSON_FIELD_VALUE);
			String applicantGender = ((JSONObject) (applicantDetails.getJSONArray(JSON_FIELD_GENDER).get(0)))
					.getString(JSON_FIELD_VALUE);
			String applicantMobNum = applicantDetails.getString(JSON_FIELD_PHONE);
			String applicantEmail = applicantDetails.getString(JSON_FIELD_EMAIL);
			String preRegId = remind.getPrereg_id();
			String centerID = remind.getRegistrationCenterId();
			String slotFrom = "" + remind.getSlotFromTime();
			String toSlot = "" + remind.getSlotToTime();
			String appointementDate = "" + remind.getRegDate();

			reminderDTO = new ReminderDTO(preRegId, appointementDate, slotFrom, toSlot, applicantfirstName,
					appliantLastName, applicantGender, applicantMobNum, applicantEmail, centerID);
		} catch (JSONException e) {
			log.debug("Error while processing applicants details", "", "", "" + e);

		}
		return reminderDTO;
	}

	private void sendRemindingNotifications() {
		for (ReminderDTO remindTo : reminders) {
			processNotificationSending(remindTo);
			log.info(" Sending dummy notifications to: " + remindTo.getApplicantfirstName() + " "
					+ remindTo.getAppliantLastName() + " " + "preregID " + remindTo.getPreRegId() + ""
					+ " for an appointement tomorrow from " + remindTo.getSlotFrom() + " to " + remindTo.getToSlot()
					+ "at center " + remindTo.getCenterID(), "", "", " ");
			}

	}

	private void processNotificationSending(ReminderDTO remindTo) {
		String message = remindingMessage(remindTo);
//		smsServiceProvider.sendSms(remindTo.getApplicantMobNum(), message);
		System.out.println(message); //for test purposes

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
