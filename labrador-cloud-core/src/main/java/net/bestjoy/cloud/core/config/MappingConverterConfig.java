package net.bestjoy.cloud.core.config;

import org.mapstruct.MapperConfig;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * @author ray
 */
@MapperConfig(
        componentModel = "spring",
        uses = {},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public class MappingConverterConfig {
}
