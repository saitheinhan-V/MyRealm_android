package com.example.myrealm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DataModel extends RealmObject {

    @PrimaryKey
    private long id;
    private String courseName;
    private String courseDescription;
    private String courseTracks;
    private String courseDuration;


    public DataModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseTracks() {
        return courseTracks;
    }

    public void setCourseTracks(String courseTracks) {
        this.courseTracks = courseTracks;
    }

    public String getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(String courseDuration) {
        this.courseDuration = courseDuration;
    }
}
