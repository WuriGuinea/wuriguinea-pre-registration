package io.mosip.preregistration.core.util;

import com.fasterxml.uuid.Generators;

/**
 * UUID Generator
 *
 * @author M1043226
 * @version 1.0.0
 */
public class UUIDGeneratorUtil {

    public UUIDGeneratorUtil() {
    }

    public static String generateId() {
        return Generators.timeBasedGenerator().generate().toString();
    }
}
