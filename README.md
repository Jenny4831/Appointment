# Appointment #
Data Structures to store appointments in order to achieve efficient data retrieving and storing. 

##Features##
Each Appointment object has a Description(String), Location(String) and Starttime(java.util.Date).
Assignment.java implements Calendar interface. Descriptions for the methods can be found in Calendar.java.
Methods included are:

- getAppointments(String location)
- getNextAppointment(Date when)
- getNextAppointment(Date when, String location)
- add(String description, Date when, String location)
- remove(Appointment appointment)

Junit test has been included to test performance in both sorted and unsorted order.
