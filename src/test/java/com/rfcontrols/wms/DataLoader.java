package com.rfcontrols.wms;

import com.rfcontrols.wms.service.dto.TagBlinkLite;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class DataLoader {

    public static List<TagBlinkLite> getTagBlinkLitesFromCsv(String filePath) throws IOException {
        List<String> lines = FileUtils.readLines(new ClassPathResource(filePath).getFile(), Charset.defaultCharset());
        lines.remove(0); //Remove the header
        return lines.stream()
            .filter(line -> StringUtils.isNotBlank(line))
            .filter(line -> !StringUtils.startsWith(line, "//"))
            .map(line -> {

            String[] parts = StringUtils.split(line, ",");
            TagBlinkLite lite = new TagBlinkLite();
            lite.setTagID(parts[0].trim());
            lite.setX(Double.valueOf(parts[1].trim()));
            lite.setY(Double.valueOf(parts[2].trim()));
            lite.setZ(Double.valueOf(parts[3].trim()));
            lite.setConfidenceInterval(Double.valueOf(parts[4].trim()));
            lite.setLocationMethod(parts[8].trim());
            lite.setLocateTime(Date.from(Instant.parse(parts[9])));

            return lite;
        }).collect(Collectors.toList());
    }
}
