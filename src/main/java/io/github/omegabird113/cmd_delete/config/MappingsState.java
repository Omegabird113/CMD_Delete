package io.github.omegabird113.cmd_delete.config;

import io.github.omegabird113.cmd_delete.mappings.INavMappings;

public record MappingsState(INavMappings mappings, ActiveMappingsManager.Type type, String id) {

}
