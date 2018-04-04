import java.util.Date;

public class AppointmentImpl implements Appointment {

	private String description;
	private String location;
	private Date startTime;

	public AppointmentImpl() {
	}

	public AppointmentImpl(String description, String location, Date startTime) {
		super();
		this.description = description;
		this.location = location;
		this.startTime = startTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

}
