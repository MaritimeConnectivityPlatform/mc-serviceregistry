/*
 * MaritimeCloud Service Registry
 * Copyright (c) 2016 Frequentis AG
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.frequentis.maritime.mcsr.web.rest.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import javax.xml.bind.DatatypeConverter;

/**
 * Utility class for HTTP headers creation.
 */
public class HeaderUtil {

    private static final Logger log = LoggerFactory.getLogger(HeaderUtil.class);

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-mcsrApp-alert", message);
        headers.add("X-mcsrApp-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert("mcsrApp." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert("mcsrApp." + entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert("mcsrApp." + entityName + ".deleted", param);
    }

    public static HttpHeaders createEntityStatusUpdateAlert(String entityName, String param) {
        return createAlert("mcsrApp." + entityName + ".statusupdate", param);
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        log.error("Entity creation failed, {}", defaultMessage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-mcsrApp-error", "error." + errorKey);
        headers.add("X-mcsrApp-params", entityName);
        return headers;
    }

    public static String extractOrganizationIdFromToken(String tokenHeader) throws Exception {
        String payload = tokenHeader.substring(tokenHeader.indexOf('.') + 1, tokenHeader.indexOf('.', tokenHeader.indexOf('.') + 1));
        //Make sure string length is a multiple of 4 to avoid issue in parseBase64Binary function
        while (payload.length()%4 != 0) {
            payload += "=";
        }
        String tokenJson = new String(DatatypeConverter.parseBase64Binary(payload), "UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(tokenJson);
        return json.get("org").textValue();
    }
}
