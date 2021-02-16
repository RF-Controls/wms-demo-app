package com.rfcontrols.wms.bo;

import com.rfcontrols.wms.DataLoader;
import com.rfcontrols.wms.service.dto.TagBlinkLite;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import javax.vecmath.Point3d;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class TagWindowBO_UT {

    private static final String EPC = "AAA1";
    TagWindowBO tagWindowBO;

    @Before
    public void init(){
        tagWindowBO = new TagWindowBO(EPC);
    }

    @Test
    public void allHitsWithinPointCloudOnGround() throws Exception{
        List<TagBlinkLite> lites = DataLoader.getTagBlinkLitesFromCsv("data/allHitsWithinZoneOne.csv");
        lites.stream().forEach(lite -> tagWindowBO.updateLocation(lite));

        Optional<Point3d> bestLocationOptional = tagWindowBO.getBestLocation();
        Point3d bestLocation = bestLocationOptional.get();

        assertThat(bestLocation.getX()).isEqualTo(9.636, within(0.001));
        assertThat(bestLocation.getY()).isEqualTo(11.009, within(0.001));
        assertThat(bestLocation.getZ()).isEqualTo(3.0, within(0.001));
    }

    @Test
    public void movingTargetThenSettlesOnGround() throws Exception{
        List<TagBlinkLite> lites = DataLoader.getTagBlinkLitesFromCsv("data/trailOfHitsThenClusterAtEnd.csv");
        lites.stream().forEach(lite -> tagWindowBO.updateLocation(lite));

        Optional<Point3d> bestLocationOptional = tagWindowBO.getBestLocation();
        Point3d bestLocation = bestLocationOptional.get();

        assertThat(bestLocation.getX()).isEqualTo(25.482, within(0.001));
        assertThat(bestLocation.getY()).isEqualTo(11.1525, within(0.001));
        assertThat(bestLocation.getZ()).isEqualTo(3.0, within(0.001));
    }


    @Test
    public void filterOutReallyOldTagHits() throws Exception{
        List<TagBlinkLite> lites = DataLoader.getTagBlinkLitesFromCsv("data/oneRecentClusterOneOldCluster.csv");
        lites.stream().forEach(lite -> tagWindowBO.updateLocation(lite));

        Optional<Point3d> bestLocationOptional = tagWindowBO.getBestLocation();
        Point3d bestLocation = bestLocationOptional.get();

        assertThat(bestLocation.getX()).isEqualTo(25.3912, within(0.001));
        assertThat(bestLocation.getY()).isEqualTo(11.3545, within(0.001));
        assertThat(bestLocation.getZ()).isEqualTo(3.0, within(0.001));
    }

    @Test
    public void tagThatNeverSettles_shouldNotReturnALocation() throws Exception{
        List<TagBlinkLite> lites = DataLoader.getTagBlinkLitesFromCsv("data/tagNeverSettles.csv");
        lites.stream().forEach(lite -> tagWindowBO.updateLocation(lite));

        Optional<Point3d> bestLocationOptional = tagWindowBO.getBestLocation();
        assertThat(bestLocationOptional.isPresent()).isFalse();
    }
}

