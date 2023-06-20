package C195;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

public class Appointment {
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private Time start;
    private Time end;
    private String createdBy;

    public Appointment(int appointmentId, String title, String description, String location, String type, Time start, Time end,
    String createdBy){
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.createdBy = createdBy;
    }

    public static ObservableList<Appointment> getAllAppointments(){
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        String query = "SELECT * from appointments";
        try{
            JDBC.makePreparedStatement(query, JDBC.getConnection());
            ResultSet rs = JDBC.getPreparedStatement().executeQuery();

            while(rs.next()){
                int tempId = rs.getInt(1);
                String tempTitle = rs.getString(2);
                String tempDescription = rs.getString(3);
                String tempLoc = rs.getString(4);
                String tempType = rs.getString(5);
                Time tempStart = rs.getTime(6);
                Time tempEnd = rs.getTime(7);
                String tempCreatedBy = rs.getString(9);

                Appointment tempAppointment = new Appointment(tempId, tempTitle, tempDescription,tempLoc, tempType,tempStart, tempEnd, tempCreatedBy);
                allAppointments.add(tempAppointment);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return allAppointments;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Time getStart() {
        return start;
    }

    public void setStart(Time start) {
        this.start = start;
    }

    public Time getEnd() {
        return end;
    }

    public void setEnd(Time end) {
        this.end = end;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
