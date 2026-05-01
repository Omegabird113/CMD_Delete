package io.github.omegabird113.cmddelete;

import io.github.omegabird113.cmddelete.actions.NavActionManager;
import io.github.omegabird113.cmddelete.actions.mapping.INavMapping;
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
        LOGGER.debug("The mapping says the supported keys are '{}'", List.of(NAV_MAPPING.getPossibleActions()));
    }
}
