package com.project.server.exception;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException() {
        super("COURSE_NOT_FOUND");
    }
}
