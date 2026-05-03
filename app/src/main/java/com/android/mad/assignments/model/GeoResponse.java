package com.android.mad.assignments.model;

import java.util.List;

public class GeoResponse {
    public List<Result> results;

    public static class Result {
        public double latitude;
        public double longitude;
        public String name;
    }
}