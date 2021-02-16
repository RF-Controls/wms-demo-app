package com.rfcontrols.wms.bo;

import org.apache.commons.math3.ml.clustering.Clusterable;

public class LocationWrapper implements Clusterable {
    final double[] points;
    final long timestamp;
    final double confidenceInterval;


    LocationWrapper(double[] points, double confidenceInterval, long timestamp) {
        this.points = points;
        this.confidenceInterval = confidenceInterval;
        this.timestamp = timestamp;
    }

    public double[] getPoint() {
        return points;
    }

    public double getX(){
        return points[0];
    }

    public double getY(){
        return points[1];
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getConfidenceInterval() {
        return confidenceInterval;
    }
}
