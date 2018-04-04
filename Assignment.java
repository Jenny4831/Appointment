import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assignment implements Calendar {

	private List<Appointment> dateAppointments;
	private Map<String, List<Appointment>> locationMap;

	public Assignment() {
		dateAppointments = new ArrayList<Appointment>();
		locationMap = new HashMap<String, List<Appointment>>();
	}

	@Override
	// O(1) when using hash map
	//stores lists of appointments with key location.
    //	reference: (Goodrich,M.T., Tamassia,R. & Goldwasser,M.H. 2014,Data Dtructures & Algorithms in Java
    //	Sixth Edition, p.405. Wiley, Hoboken, NJ)
	public List<Appointment> getAppointments(String location) {
		if (location == null) {
			throw new IllegalArgumentException("location was null");
		}
		if (locationMap.containsKey(location)) {

			return locationMap.get(location);
		} else {
			return new ArrayList<Appointment>();
		}
	}

	// O(logN) 
	//binary search
	//reference: (Goodrich,M.T., Tamassia,R. & Goldwasser,M.H. 2014,Data Dtructures & Algorithms in Java Sixth Edition,
	//p.197. Wiley, Hoboken, NJ)
	@Override
	public Appointment getNextAppointment(Date when) {
		if (when == null) {
			throw new IllegalArgumentException("time was null");
		}

		int min = 0, max = this.dateAppointments.size() - 1;
		Appointment result = null;

		while (min <= max) {
			int mid = (min + max) / 2;
			Appointment app = this.dateAppointments.get(mid);
			if (when.before(app.getStartTime()) || when.equals(app.getStartTime())) {
				// when < app
				// move the cursor to left
				result = app;
				max = mid - 1;
			} else {
				// when >= app
				// move the cursor to right
				min = mid + 1;
			}
		}

		return result;
	}

	private Appointment nextAppointment = null;

	// half O(logN) half O(n)
	@Override
	public Appointment getNextAppointment(Date when, String location) {
		if (when == null) {
			throw new IllegalArgumentException("time was null");
		}
		nextAppointment = null;
		getNextApp(when, location, 0, this.dateAppointments.size() - 1);
		return nextAppointment;
	}
//	reference: (Goodrich,M.T., Tamassia,R. & Goldwasser,M.H. 2014,Data Dtructures & Algorithms in Java
//	Sixth Edition, p.197. Wiley, Hoboken, NJ)
//For binary recursion
//(Goodrich,M.T., Tamassia,R. & Goldwasser,M.H. 2014,Data Dtructures & Algorithms in Java
//	Sixth Edition, p.211. Wiley, Hoboken, NJ)
	private void getNextApp(Date when, String location, int min, int max) {

		if (min > max) {
			return;
		}

		int mid = (min + max) / 2;
		Appointment app = this.dateAppointments.get(mid);

		// when <= mid then go both left and right
		// when > mid then go right
		if (when.before(app.getStartTime()) || when.equals(app.getStartTime())) {

			if (app.getLocation().equals(location)) {
				// it's a possible result
				if (nextAppointment == null || app.getStartTime().before(nextAppointment.getStartTime())) {
					nextAppointment = app;
				}
			}

			// right
			getNextApp(when, location, mid + 1, max);
			// left
			getNextApp(when, location, min, mid - 1);
		} else {
			// go right
			getNextApp(when, location, mid + 1, max);
		}

	}
    //	reference: (Goodrich,M.T., Tamassia,R. & Goldwasser,M.H. 2014,Data Dtructures & Algorithms in Java
    //	Sixth Edition, p.197. Wiley, Hoboken, NJ)
	// O(logn)
	@Override
	public void add(String description, Date when, String location) {
		if (description == null || description.isEmpty() || when == null || location == null || location.isEmpty()) {
			throw new IllegalArgumentException("Illegal arguments;");
		}

		Appointment newApp = new AppointmentImpl(description, location, when);


		if (this.dateAppointments.size() == 0) {
			this.dateAppointments.add(newApp);
		} else {
			Appointment lastApp = this.dateAppointments.get(this.dateAppointments.size() - 1);
			if (newApp.getStartTime().after(lastApp.getStartTime())) {
				this.dateAppointments.add(newApp);
			} else {
				int min = 0, max = this.dateAppointments.size();
				// index of first element that is after new value
				int index = -1;

				while (min <= max) {
					int mid = (min + max) / 2;
					Appointment app = this.dateAppointments.get(mid);

					if (newApp.getStartTime().before(app.getStartTime())
							|| newApp.getStartTime().equals(app.getStartTime())) {
						// insert in front
						index = mid;
						max = mid - 1;
					} else {
						min = mid + 1;
					}

				}

				this.dateAppointments.add(index, newApp);
			}
		}

		// update location hash map
		List<Appointment> existingList = new ArrayList<Appointment>();
		if (locationMap.containsKey(location)) {
			existingList = locationMap.get(location);
		}
		existingList.add(newApp);
		this.locationMap.put(location, existingList);

	}
   //	reference: (Goodrich,M.T., Tamassia,R. & Goldwasser,M.H. 2014,Data Dtructures & Algorithms in Java
   //	Sixth Edition, p.197. Wiley, Hoboken, NJ)
	// O(logn)
	@Override
	public void remove(Appointment appointment) {

		if (appointment == null) {
			throw new IllegalArgumentException("Illegal arguments;");
		}

		// use BST to remove app
		int min = 0, max = this.dateAppointments.size();

		while (min <= max) {
			int mid = (min + max) / 2;
			Appointment app = this.dateAppointments.get(mid);

			if (appointment.getStartTime().equals(app.getStartTime())) {
				// remove
				this.dateAppointments.remove(mid);
				break;
			} else if (appointment.getStartTime().before(app.getStartTime())) {
				max = mid - 1;
			} else {
				min = mid + 1;
			}

		}

		if (locationMap.containsKey(appointment.getLocation())) {
			List<Appointment> existingList = locationMap.get(appointment.getLocation());
			if (existingList.contains(appointment)) {
				existingList.remove(appointment);
				if (existingList.isEmpty()) {
					this.locationMap.remove(appointment.getLocation());
				} else {
					this.locationMap.put(appointment.getLocation(), existingList);
				}
			}
		}
	}
}
