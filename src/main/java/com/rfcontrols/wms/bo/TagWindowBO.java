package com.rfcontrols.wms.bo;

import com.rfcontrols.wms.service.dto.TagBlinkLite;
import com.rfcontrols.wms.util.AutoDiscardingDeque;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.vecmath.Point3d;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TagWindowBO {

    private static final Logger logger = LoggerFactory.getLogger(TagWindowBO.class);

    private static final int CLUSTERING_MIN_PTS = 3;

    public static final int MAX_RECORDS = 200;
    private final String epc;
    private final AutoDiscardingDeque<TagBlinkLite> previousLocations;
    private final AutoDiscardingDeque<Instant> recentTimeStamps;

    private double clusteringEpsilonFeet = 3.0;


    public TagWindowBO(String epc){
        this.epc = epc;
        this.previousLocations = new AutoDiscardingDeque<>(MAX_RECORDS);
        this.recentTimeStamps = new AutoDiscardingDeque<>(15);
    }

    public void updateLocation(TagBlinkLite tagBlinkLite){
        logger.trace("Updating location for tag: {} with {}, {}, {}", tagBlinkLite.getTagID(), tagBlinkLite.getX(), tagBlinkLite.getY(), tagBlinkLite.getZ());
        previousLocations.offerFirst(tagBlinkLite);
        recentTimeStamps.offerFirst(tagBlinkLite.getLocateTime().toInstant());
    }

    public Optional<Point3d> getBestLocation(){
        //Determine what portion of the window to use, start by looking at the most recent and see what the read rate is in that time span. Only reach back as far as the read rate allows. Toss the extra locations
        Duration timeWindowDuration = Duration.between(recentTimeStamps.getFirst(), recentTimeStamps.getLast());
        long millisBetweenWindow = timeWindowDuration.abs().get(ChronoUnit.SECONDS) * 3l * 1000;
        Instant oldestTimestamp = recentTimeStamps.getFirst().minusMillis(millisBetweenWindow);

        List<LocationWrapper> locationWrapperList = previousLocations.stream()
            .filter(tagBlinkLite -> tagBlinkLite.getLocateTime().toInstant().isAfter(oldestTimestamp))
            .map(tagBlinkLite -> new LocationWrapper(new double[]{tagBlinkLite.getX(), tagBlinkLite.getY(), tagBlinkLite.getZ()}, tagBlinkLite.getConfidenceInterval(), tagBlinkLite.getLocateTime().getTime()))
            .collect(Collectors.toList());

        //Then determine if the locations are close to each other and find the best possible location using confidence interval and mabye DBScan to toss outliers
        DBSCANClusterer<LocationWrapper> clusterer = new DBSCANClusterer<>(clusteringEpsilonFeet, CLUSTERING_MIN_PTS);
        List<Cluster<LocationWrapper>> clusters = clusterer.cluster(locationWrapperList);

        Optional<List<LocationWrapper>> cluster = processClusters(locationWrapperList, clusters);

        if(cluster.isPresent()) return Optional.of(findCenterOfMass(cluster.get()));

        //See if this "best" location is inside any zone

        //If it's not inside any zones see if its really close to any zones


        return Optional.empty();
    }

    private Optional<List<LocationWrapper>> processClusters(List<LocationWrapper> locationWrapperList, List<Cluster<LocationWrapper>> clusters) {
        if (clusters.size() > 0) {
            List<LocationWrapper> cluster;
            if(clusters.size() == 1){
                cluster = clusters.get(0).getPoints();
            }else{
                cluster = handleMultipleClusters(clusters, locationWrapperList.size());
            }
            return Optional.of(cluster);
        }else {
            //The tag is all over the place
           // if(logger.isDebugEnabled())logger.debug("The tag is all over the place, looks like it's in motion. Used this many points: " + previousLocations.size());


            //Maybe try DBScan with a bigger epsilon?

            //If there aren't enough hits to cluster up maybe check the confidence intervals on the hits we do have and if they are
            //high enough then return a location

            return Optional.empty();
        }
    }

    private List<LocationWrapper> handleMultipleClusters(List<Cluster<LocationWrapper>> clusters, final Integer totalNumberOfPoints) {
        Cluster<LocationWrapper> cluster = findBiggestCluster(clusters);

        //If we are returning most of the list back run DBScan again to see if there is a smaller cluster hiding in there somewhere.
       if(cluster.getPoints().size() > (totalNumberOfPoints.doubleValue() * .6)){
           double newEpsilon = clusteringEpsilonFeet * 0.6;

            DBSCANClusterer<LocationWrapper> clusterer = new DBSCANClusterer<>(newEpsilon, CLUSTERING_MIN_PTS);
            List<Cluster<LocationWrapper>> smallerClusters = clusterer.cluster(cluster.getPoints());

            if(smallerClusters.size() == 0) return cluster.getPoints();
            if(smallerClusters.size() == 1) return smallerClusters.get(0).getPoints();

            cluster = findBiggestCluster(smallerClusters);
            this.clusteringEpsilonFeet = newEpsilon;        //Since this new epsilon was able to find a more precise cluster it is our new go to epsilon
        }

        return cluster.getPoints();
    }

    private Cluster<LocationWrapper> findBiggestCluster(List<Cluster<LocationWrapper>> clusters) {
        Cluster<LocationWrapper> cluster = null;

        for(Cluster<LocationWrapper> curCluster : clusters){
            if(cluster == null || curCluster.getPoints().size() > cluster.getPoints().size()){
                cluster = curCluster;
            }
        }

        return cluster;
    }

    private Point3d findCenterOfMass(List<LocationWrapper> cluster) {
        double clusterSize = cluster.size();

        //The tag has settled
        //Find the center of mass for the cluster
        double xStats = 0;
        double yStats = 0;
        double zStats = 0;
        double confidenceTotal = 0.0;
        for(LocationWrapper point : cluster){
            xStats += point.getPoint()[0] * point.getConfidenceInterval();
            yStats += point.getPoint()[1] * point.getConfidenceInterval();
            zStats += point.getPoint()[2] * point.getConfidenceInterval();
            confidenceTotal += point.getConfidenceInterval();
        }
        double xAvg = xStats / confidenceTotal;
        double yAvg = yStats / confidenceTotal;
        double zAvg = zStats / confidenceTotal;

        if(logger.isDebugEnabled())logger.debug("Just found a new cluster at ({},{},{}) of location data holding this number of points: {} ",xAvg, yAvg, zAvg, clusterSize);
        return new Point3d(xAvg, yAvg, zAvg);
    }
}
