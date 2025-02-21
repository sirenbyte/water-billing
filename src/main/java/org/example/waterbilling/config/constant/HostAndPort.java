package org.example.waterbilling.config.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HostAndPort {
    public static String HOST_AND_PORT_CMS_SERVICE;
    @Value("${cms.service}")
    public void setCmsService(String value) {
        HOST_AND_PORT_CMS_SERVICE = value;
    }
}
