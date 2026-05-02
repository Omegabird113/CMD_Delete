package io.github.omegabird113.cmd_delete;

import io.github.omegabird113.cmd_delete.actions.NavActionManager;
import io.github.omegabird113.cmd_delete.actions.mapping.INavMapping;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CmdDeleteClient implements ClientModInitializer {
    public static final String MODID = "cmd-delete";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final INavMapping NAV_MAPPING = NavActionManager.getMapping();

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing '{}'. MappingOs='{}'", MODID, NAV_MAPPING.getMappingOs());
        LOGGER.info("The mapping says the supported actions are '{}'", List.of(NAV_MAPPING.getPossibleActions()));
    }
}
