package com.rfcontrols.wms.bo;

import com.rfcontrols.wms.domain.Zone;

import javax.media.j3d.BoundingBox;
import javax.vecmath.Point3d;

/**
 * Zone business object that contains all logic for zone associations
 */
public class ZoneBO {

    private final BoundingBox boundingBox;
    private final Zone zone;

    public ZoneBO(Zone zone){
        this.zone = zone;

        double lowerX = zone.getx1() < zone.getx2() ? zone.getx1() : zone.getx2();
        double lowerY = zone.gety1() < zone.gety2() ? zone.gety1() : zone.gety2();
        double lowerZ = zone.getz1() < zone.getz2() ? zone.getz1() : zone.getz2();

        double upperX = zone.getx1() > zone.getx2() ? zone.getx1() : zone.getx2();
        double upperY = zone.gety1() > zone.gety2() ? zone.gety1() : zone.gety2();
        double upperZ = zone.getz1() > zone.getz2() ? zone.getz1() : zone.getz2();

        boundingBox = new BoundingBox(new Point3d(lowerX, lowerY, lowerZ), new Point3d(upperX, upperY, upperZ));
    }

    public boolean intersectsZone(Point3d location){
        //If not inside the current zone in the loop see if we are close to it.
        // Be sure to use different values if the tag is assumed zone, triangulated, or phase ranged since it may be in the air trying to associate to a shelf zone

        return boundingBox.intersect(location);
    }

    public Zone getZone() {
        return zone;
    }
}
