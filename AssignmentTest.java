import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AssignmentTest {

	// Set up JUnit to be able to check for expected exceptions
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	// This will make it a bit easier for us to make Date objects
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	// Helper method to build the example calendar
	private Calendar buildSortedCalendar() {
		Calendar calendar = new Assignment();
		try {
			calendar.add("Tutorial", df.parse("2016/09/03 09:00:00"), "SIT lab 117");
			calendar.add("Seminar", df.parse("2016/09/03 09:10:00"), "SIT lab 111");
			calendar.add("Tutorial", df.parse("2016/09/03 09:30:10"), "SIT lab 111");
			calendar.add("Seminar", df.parse("2016/09/03 16:00:00"), "SIT lab 117");
			calendar.add("Tutorial", df.parse("2016/09/03 16:00:00"), "SIT lab 118");
			calendar.add("Seminar", df.parse("2016/09/03 18:25:00"), "SIT lab 117");
			calendar.add("Meeting", df.parse("2016/09/03 18:25:00"), "SIT lab 117");
			calendar.add("Presentation", df.parse("2016/09/03 19:00:25"), "New Law 110");
		} catch (ParseException e) {
			e.printStackTrace();
			fail("The test case is broken, invalid SimpleDateFormat parse");
		}
		return calendar;
	}

	private Calendar buildUnsortedCalendar() {
		Calendar unsortCal = new Assignment();
		try {
			unsortCal.add("Tutorial", df.parse("2016/09/03 09:00:00"), "SIT lab 112");
			unsortCal.add("Meeting", df.parse("2016/09/03 10:10:10"), "SIT lab 111");
			unsortCal.add("Seminar", df.parse("2016/09/03 16:00:00"), "SIT lab 117");
			unsortCal.add("Presentation", df.parse("2016/09/03 12:15:00"), "SIT lab 113");
			unsortCal.add("Tutorial", df.parse("2016/09/03 16:00:00"), "SIT lab 117");
			unsortCal.add("Seminar", df.parse("2016/09/03 18:25:00"), "SIT lab 117");
			unsortCal.add("Exam", df.parse("2016/09/03 08:30:00"), "SIT lab 112");
		} catch (ParseException e) {
			e.printStackTrace();
			fail("The test case is broken, invalid SimpleDateFormat parse");
		}
		return unsortCal;
	}

	private Calendar buildSameValCalendar() {
		Calendar unsortCal = new Assignment();
		try {
			unsortCal.add("Tutorial", df.parse("2016/09/03 09:00:00"), "SIT lab 112");
			unsortCal.add("Tutorial", df.parse("2016/09/03 09:00:00"), "SIT lab 112");
			unsortCal.add("Tutorial", df.parse("2016/09/03 09:00:00"), "SIT lab 112");
			unsortCal.add("Tutorial", df.parse("2016/09/03 09:00:00"), "SIT lab 112");
			unsortCal.add("Tutorial", df.parse("2016/09/03 09:00:00"), "SIT lab 112");
			unsortCal.add("Tutorial", df.parse("2016/09/03 09:00:00"), "SIT lab 112");
			unsortCal.add("Tutorial", df.parse("2016/09/03 09:00:00"), "SIT lab 112");
			unsortCal.add("Tutorial", df.parse("2016/09/03 09:00:00"), "SIT lab 112");
		} catch (ParseException e) {
			e.printStackTrace();
			fail("The test case is broken, invalid SimpleDateFormat parse");
		}
		return unsortCal;
	}

	private Calendar buildEmptyCalendar() {
		Calendar emptyCalendar = new Assignment();
		return emptyCalendar;
	}
	
	@Test
	// test add
	public void testAdd() {
		Calendar addCal =  buildEmptyCalendar();
		try{
		    addCal.add("Assisstance", df.parse("2016/09/03 09:00:00"), "New Law 101");
		    addCal.add("Tutorial", df.parse("2016/09/03 10:00:00"), "New Law 101");
		    addCal.add("Lecture", df.parse("2016/09/03 11:00:00"), "New Law 101");
		    addCal.add("Exam", df.parse("2016/09/03 12:00:00"), "New Law 101");
		    addCal.add("Tutorial", df.parse("2016/09/03 13:00:00"), "New Law 101");
		    List<Appointment> apps = addCal.getAppointments("New Law 101");
		    List<String> descriptions = new ArrayList<String>();
			for(Appointment a: apps) {
				descriptions.add(a.getDescription());
			}
			assertEquals(5, apps.size());
			assertEquals(Arrays.asList("Assisstance", "Tutorial", "Lecture","Exam","Tutorial"), descriptions);
		}catch (ParseException e){
			e.printStackTrace();
			fail("The test case is broken, invalid SimpleDateFormat parse");
		}

	}


	@Test
	public void testRemoveOne_SortedCalendar() {
		// test removal of one appointment in sorted calendar
		Calendar sortedCal = buildSortedCalendar();

		try {
			Appointment app = sortedCal.getNextAppointment(df.parse("2016/09/03 09:00:00"));
			sortedCal.remove(app);
			app = sortedCal.getNextAppointment(df.parse("2016/09/03 09:00:00"));
			assertEquals("Seminar", app.getDescription());

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testRemoveOne_UnsortedCalendar() {
		// test removal of one appointment in sorted
		Calendar unsortedCal = buildUnsortedCalendar();

		try {
			Appointment app = unsortedCal.getNextAppointment(df.parse("2016/09/03 09:00:00"));
			unsortedCal.remove(app);
			app = unsortedCal.getNextAppointment(df.parse("2016/09/03 09:00:00"));
			assertEquals("Meeting", app.getDescription());

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testRemoveDuplicate() {
		// test when get next appointment the first duplicate appointment is
		// removed

		Calendar sortedCal = buildSortedCalendar();

		try {
			Appointment app = sortedCal.getNextAppointment(df.parse("2016/09/03 16:00:00"));
			sortedCal.remove(app);

			app = sortedCal.getNextAppointment(df.parse("2016/09/03 16:00:00"));
			sortedCal.remove(app);

			app = sortedCal.getNextAppointment(df.parse("2016/09/03 16:00:00"));

			assertEquals(df.parse("2016/09/03 18:25:00"), app.getStartTime());

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testRemoveAll_getNextAppDate() {
		// test when get next all the appointments by date
		// all removed
		Calendar sortedCal = buildSortedCalendar();
		try {
			for (int i = 0; i < 8; i++) {
				Appointment app = sortedCal.getNextAppointment(df.parse("2015/09/03 16:00:00"));
				sortedCal.remove(app);
			}

			Appointment app = sortedCal.getNextAppointment(df.parse("2015/09/03 16:00:00"));
			assertNull(app);

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testRemoveAll_getNextAppLocation() {
		// test when get next all the appointments by Location
		// all removed
		Calendar sortedCal = buildSortedCalendar();
		try {
			for (int i = 0; i < 8; i++) {
				Appointment app = sortedCal.getNextAppointment(df.parse("2015/09/03 16:00:00"));
				sortedCal.remove(app);
			}

			Appointment app = sortedCal.getNextAppointment(df.parse("2015/09/03 16:00:00"), "location");
			assertNull(app);

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetAppointments() {
		
		// test get appointments for sorted
		Calendar sortedCal = buildSortedCalendar();
		//test when location does not exist
		List<Appointment> apps = sortedCal.getAppointments("SIT lab 120");
		List<String> descriptions = new ArrayList<String>();
		for (Appointment a : apps) {
			descriptions.add(a.getDescription());
		}
		
		assertTrue(apps.isEmpty());
		//test when location is in map
		List<Appointment> apps2 = sortedCal.getAppointments("SIT lab 117");
		List<String> des2 = new ArrayList<String>();
		for (Appointment b : apps2) {
			des2.add(b.getDescription());
		}
		Collections.sort(des2);
		assertEquals(Arrays.asList("Meeting", "Seminar", "Seminar", "Tutorial"), des2);
		//test when location does not exist in unsorted
		Calendar unsortedCal = buildUnsortedCalendar();
		List<Appointment> unApps = sortedCal.getAppointments("SIT lab 120");
		List<String> unDes = new ArrayList<String>();
		for (Appointment c : unApps) {
			unDes.add(c.getDescription());
		}
		assertTrue(unApps.isEmpty());
		//test when location does exist in unsorted
		List<Appointment> unsortApp = unsortedCal.getAppointments("SIT lab 117");
		List<String> unsortDes = new ArrayList<String>();
		for (Appointment b : unsortApp) {
			unsortDes.add(b.getDescription());
		}
		Collections.sort(unsortDes);
		assertEquals(Arrays.asList("Seminar", "Seminar", "Tutorial"), unsortDes);

		// test empty calendar 
		Calendar emptyCal = buildEmptyCalendar();
		List<Appointment> noApp = emptyCal.getAppointments("SIT lab 112");
		assertEquals(0, noApp.size());
	}

	//// test get next appointment by date ////

	@Test
	public void testGetNextAppointment_Sorted() {
		
		Calendar sortedCal = buildSortedCalendar();
		try {
			Appointment app1 = sortedCal.getNextAppointment(df.parse("2016/09/03 08:00:00"));
			String des1 = app1.getDescription();
			assertTrue(des1.equals("Tutorial"));
			Appointment app2 = sortedCal.getNextAppointment(df.parse("2016/09/03 09:00:00"));
			String des2 = app2.getDescription();
			assertTrue(des2.equals("Tutorial"));
			Appointment app3 = sortedCal.getNextAppointment(df.parse("2016/09/03 09:20:50"));
			String des3 = app3.getDescription();
			assertTrue(des3.equals("Tutorial"));
			Appointment app4 = sortedCal.getNextAppointment(df.parse("2016/09/03 15:00:00"));
			String des4 = app4.getDescription();
			assertTrue(des4.equals("Seminar") || des4.equals("Tutorial"));
			Appointment app5 = sortedCal.getNextAppointment(df.parse("2016/09/03 18:00:00"));
			String des5 = app5.getDescription();
			assertTrue(des5.equals("Seminar") || des5.equals("Meeting"));
			Appointment app6 = sortedCal.getNextAppointment(df.parse("2016/09/03 19:00:00"));
			String des6 = app6.getDescription();
			assertTrue(des6.equals("Presentation"));

		} catch (ParseException e) {
			e.printStackTrace();
			fail("The test case is broken, invalid SimpleDateFormat parse");
		}
		// test get next appointment after last one
		try {
			assertNull(sortedCal.getNextAppointment(df.parse("2017/01/01 13:00:00")));
		} catch (ParseException e) {
			e.printStackTrace();
			fail("The test case is broken, invalid SimpleDateFormat parse");
		}
	}

	@Test
	public void testGetNextAppointment_Unsorted() {
		
	
		Calendar unsortedCal = buildUnsortedCalendar();
		try {
			Appointment app1 = unsortedCal.getNextAppointment(df.parse("2016/09/03 08:00:00"));
			String des1 = app1.getDescription();
			assertTrue(des1.equals("Exam"));
			Appointment app2 = unsortedCal.getNextAppointment(df.parse("2016/09/03 08:55:00"));
			String des2 = app2.getDescription();
			assertTrue(des2.equals("Tutorial"));
			Appointment app3 = unsortedCal.getNextAppointment(df.parse("2016/09/03 09:15:26"));
			String des3 = app3.getDescription();
			assertTrue(des3.equals("Meeting"));
			Appointment app4 = unsortedCal.getNextAppointment(df.parse("2016/09/03 12:00:00"));
			String des4 = app4.getDescription();
			assertTrue(des4.equals("Presentation"));
			Appointment app5 = unsortedCal.getNextAppointment(df.parse("2016/09/03 15:00:00"));
			String des5 = app5.getDescription();
			assertTrue(des5.equals("Seminar") || des5.equals("Tutorial"));
			Appointment app6 = unsortedCal.getNextAppointment(df.parse("2016/09/03 18:00:00"));
			String des6 = app6.getDescription();
			assertTrue(des6.equals("Seminar"));
		} catch (ParseException e) {
			e.printStackTrace();
			fail("The test case is broken, invalid SimpleDateFormat parse");
		}
		// test get next appointment after last one
		try {
			assertNull(unsortedCal.getNextAppointment(df.parse("2017/01/01 13:00:00")));
		} catch (ParseException e) {
			e.printStackTrace();
			fail("The test case is broken, invalid SimpleDateFormat parse");
		}

	}


	@Test
	public void testGetNextAppointment_EmptyCalendar() {
		Calendar cal = this.buildEmptyCalendar();
		try {
			assertNull(cal.getNextAppointment(df.parse("2020/01/01 13:00:00")));
			thrown.expect(IllegalArgumentException.class);
			assertNull(cal.getNextAppointment(null));
		} catch (ParseException e) {
			e.printStackTrace();
			fail("The test case is broken, invalid SimpleDateFormat parse");
		}
	}

	//// test test get next appointment by date and location////

	@Test
	public void testGetNextAppointmentLocation_Sorted() {
		
		Calendar sortedCal = buildSortedCalendar();
		try {
			Appointment app1 = sortedCal.getNextAppointment(df.parse("2016/09/03 08:00:00"), "SIT lab 117");
			String des1 = app1.getDescription();
			assertTrue(des1.equals("Tutorial"));
			Appointment app2 = sortedCal.getNextAppointment(df.parse("2016/09/03 09:00:00"), "SIT lab 111");
			String des2 = app2.getDescription();
			assertTrue(des2.equals("Seminar"));
			Appointment app3 = sortedCal.getNextAppointment(df.parse("2016/09/03 09:20:50"), "SIT lab 111");
			String des3 = app3.getDescription();
			assertTrue(des3.equals("Tutorial"));
			Appointment app4 = sortedCal.getNextAppointment(df.parse("2016/09/03 15:00:00"), "SIT lab 117");
			String des4 = app4.getDescription();
			assertTrue(des4.equals("Seminar"));
			Appointment app5 = sortedCal.getNextAppointment(df.parse("2016/09/03 18:00:00"), "SIT lab 117");
			String des5 = app5.getDescription();
			assertTrue(des5.equals("Seminar") || des5.equals("Meeting"));
			Appointment app6 = sortedCal.getNextAppointment(df.parse("2016/09/03 19:00:00"), "New Law 110");
			String des6 = app6.getDescription();
			assertTrue(des6.equals("Presentation"));

			// test get next appointment after last one
			assertNull(sortedCal.getNextAppointment(df.parse("2017/01/01 13:00:00"), "xxx"));
		} catch (ParseException e) {
			e.printStackTrace();
			fail("The test case is broken, invalid SimpleDateFormat parse");
		}
	}

	@Test
	public void testGetNextAppointmentLocation_Unsorted() {

		Calendar unsortedCal = buildUnsortedCalendar();
		try {
			Appointment app1 = unsortedCal.getNextAppointment(df.parse("2016/09/03 08:00:00"), "SIT lab 112");
			String des1 = app1.getDescription();
			assertTrue(des1.equals("Exam"));
			Appointment app2 = unsortedCal.getNextAppointment(df.parse("2016/09/03 09:00:00"), "SIT lab 112");
			String des2 = app2.getDescription();
			assertTrue(des2.equals("Tutorial"));
			Appointment app3 = unsortedCal.getNextAppointment(df.parse("2016/09/03 09:20:50"), "SIT lab 111");
			String des3 = app3.getDescription();
			assertTrue(des3.equals("Meeting"));
			Appointment app4 = unsortedCal.getNextAppointment(df.parse("2016/09/03 12:15:00"), "SIT lab 113");
			String des4 = app4.getDescription();
			assertTrue(des4.equals("Presentation"));
			Appointment app5 = unsortedCal.getNextAppointment(df.parse("2016/09/03 14:00:00"), "SIT lab 117");
			String des5 = app5.getDescription();
			assertTrue(des5.equals("Seminar") || des5.equals("Tutorial"));
			Appointment app6 = unsortedCal.getNextAppointment(df.parse("2016/09/03 17:00:00"), "SIT lab 117");
			String des6 = app6.getDescription();
			assertTrue(des6.equals("Seminar"));

			// test get next appointment after last one
			assertNull(unsortedCal.getNextAppointment(df.parse("2017/01/01 13:00:00"), "xxx"));
		} catch (ParseException e) {
			e.printStackTrace();
			fail("The test case is broken, invalid SimpleDateFormat parse");
		}
	}

	@Test
	//Test get next apointment from calendar when all appointments are removed
	public void testGetNextAppointmentLocation_RemoveAll() {
		Calendar sortedCal = buildSortedCalendar();
		try {
			for (int i = 0; i < 8; i++) {
				Appointment app = sortedCal.getNextAppointment(df.parse("2015/09/03 16:00:00"));
				sortedCal.remove(app);
			}

			Appointment app = sortedCal.getNextAppointment(df.parse("2015/09/03 16:00:00"), "SIT lab 117");
			assertNull(app);

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetNextAppointmentLocation_RemoveOne() {
		// test removal of one appointment in sorted calendar
		Calendar sortedCal = buildSortedCalendar();

		try {
			Appointment app = sortedCal.getNextAppointment(df.parse("2016/09/03 09:00:00"));
			sortedCal.remove(app);
			app = sortedCal.getNextAppointment(df.parse("2016/09/03 09:00:00"), "SIT lab 117");
			assertEquals(df.parse("2016/09/03 16:00:00"), app.getStartTime());

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetNextAppointmentLocation_EmptyCalendar() {
		Calendar cal = this.buildEmptyCalendar();
		try {
			assertNull(cal.getNextAppointment(df.parse("2020/01/01 13:00:00"), "XXX"));
			thrown.expect(IllegalArgumentException.class);
			assertNull(cal.getNextAppointment(null, null));
		} catch (ParseException e) {
			e.printStackTrace();
			fail("The test case is broken, invalid SimpleDateFormat parse");
		}
	}

	//

	@Test
	public void testSameVal_remove() {
		//test when removing duplicates
		Calendar cal = buildSameValCalendar();
		try {
			for (int i = 0; i < 8; i++) {
				Appointment app = cal.getNextAppointment(df.parse("2016/09/03 09:00:00"));
				assertEquals("Tutorial", app.getDescription());
				cal.remove(app);
			}
			Appointment app = cal.getNextAppointment(df.parse("2016/09/03 09:00:00"));
			assertNull(app);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("The test case is broken, invalid SimpleDateFormat parse");
		}
	}
	@Test
	public void testIllegalArgument() {

		Calendar sortCal = buildSortedCalendar();
		Calendar unsortCal = buildUnsortedCalendar();
		Calendar sameValCal = buildSameValCalendar();
		Calendar emptyCal = buildEmptyCalendar();

		// Tell JUnit to expect an IllegalArgumentException
		thrown.expect(IllegalArgumentException.class);
		
		// This should cause an IllegalArgumentException to be thrown
		sortCal.add(null, null, null);
		sortCal.add("", null, "ab");
		sortCal.add("ab", null, "");
		sortCal.getNextAppointment(null);
		sortCal.getAppointments(null);
		sortCal.getAppointments("");
		sortCal.getNextAppointment(null, null);
		sortCal.getNextAppointment(null, "");
		sortCal.remove(null);
		
		unsortCal.add(null, null, null);
		unsortCal.add("", null, "ab");
		unsortCal.add("ab", null, "");
		unsortCal.getNextAppointment(null);
		unsortCal.getAppointments(null);
		unsortCal.getAppointments("");
		unsortCal.getNextAppointment(null, null);
		unsortCal.getNextAppointment(null, "");
		unsortCal.remove(null);
		
		sameValCal.add(null, null, null);
		sameValCal.add("", null, "ab");
		sameValCal.add("ab", null, "");
		sameValCal.getNextAppointment(null);
		sameValCal.getAppointments(null);
		sameValCal.getAppointments("");
		sameValCal.getNextAppointment(null, null);
		sameValCal.getNextAppointment(null, "");
		sameValCal.remove(null);
		
		emptyCal.add(null, null, null);
		emptyCal.add("", null, "ab");
		emptyCal.add("ab", null, "");
		emptyCal.getNextAppointment(null);
		emptyCal.getAppointments(null);
		emptyCal.getAppointments("");
		emptyCal.getNextAppointment(null, null);
		emptyCal.getNextAppointment(null, "");
		emptyCal.remove(null);
		
		
	}
}
