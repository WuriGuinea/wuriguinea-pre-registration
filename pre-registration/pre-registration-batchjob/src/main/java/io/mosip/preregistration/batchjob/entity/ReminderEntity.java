package io.mosip.preregistration.batchjob.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "reg_appointment", schema = "prereg")
@Entity
public class ReminderEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Id. */
	@Id
	@Column(name = "id")
	private String id;

	/** Registration center id. */
	@Column(name = "regcntr_id")
	private String registrationCenterId;

	/** Updated date time. */
	@Column(name = "prereg_id")
	private String prereg_id;

	@Column(name = "booking_dtimes")
	private LocalDateTime booking_dtimes;

	/** Appointment date. */
	@Column(name = "appointment_date")
	private LocalDate regDate;

	/** Slot from time. */
	@Column(name = "slot_from_time")
	private LocalTime slotFromTime;

	/** Slot to time. */
	@Column(name = "slot_to_time")
	private LocalTime slotToTime;

	/** Created by. */
	@Column(name = "cr_by")
	private String crBy;

	/** Created date time. */
	@Column(name = "cr_dtimes")
	private LocalDateTime crDate;

	/** Created by. */
	@Column(name = "upd_by")
	private String upBy;

	/** Updated date time. */
	@Column(name = "upd_dtimes")
	private LocalDateTime updDate;

	@Column(name = "lang_code")
	private String lang_code;
	// "
	// "booking_dtimes","appointment_date","slot_from_time","slot_to_time","lang_code","cr_by","cr_dtimes","upd_by","upd_dtimes"

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRegistrationCenterId() {
		return registrationCenterId;
	}

	public void setRegistrationCenterId(String registrationCenterId) {
		this.registrationCenterId = registrationCenterId;
	}

	public LocalDateTime getBooking_dtimes() {
		return booking_dtimes;
	}

	public void setBooking_dtimes(LocalDateTime booking_dtimes) {
		this.booking_dtimes = booking_dtimes;
	}

	public LocalDate getRegDate() {
		return regDate;
	}

	public void setRegDate(LocalDate regDate) {
		this.regDate = regDate;
	}

	public LocalTime getSlotFromTime() {
		return slotFromTime;
	}

	public void setSlotFromTime(LocalTime slotFromTime) {
		this.slotFromTime = slotFromTime;
	}

	public LocalTime getSlotToTime() {
		return slotToTime;
	}

	public void setSlotToTime(LocalTime slotToTime) {
		this.slotToTime = slotToTime;
	}

	public String getCrBy() {
		return crBy;
	}

	public void setCrBy(String crBy) {
		this.crBy = crBy;
	}

	public LocalDateTime getCrDate() {
		return crDate;
	}

	public void setCrDate(LocalDateTime crDate) {
		this.crDate = crDate;
	}

	public String getUpBy() {
		return upBy;
	}

	public void setUpBy(String upBy) {
		this.upBy = upBy;
	}

	public LocalDateTime getUpdDate() {
		return updDate;
	}

	public void setUpdDate(LocalDateTime updDate) {
		this.updDate = updDate;
	}

	public String getPrereg_id() {
		return prereg_id;
	}

	public void setPrereg_id(String prereg_id) {
		this.prereg_id = prereg_id;
	}

	public String getLang_code() {
		return lang_code;
	}

	public void setLang_code(String lang_code) {
		this.lang_code = lang_code;
	}

	@Override
	public String toString() {
		return "ReminderEntity [registrationCenterId=" + registrationCenterId + ", prereg_id=" + prereg_id
				+ ", booking_dtimes=" + booking_dtimes + ", slotFromTime=" + slotFromTime + ", slotToTime=" + slotToTime
				+ "]";
	}

}
