package io.github.omegabird113.cmddelete;

import io.github.omegabird113.cmddelete.actions.KeyConstants;
import io.github.omegabird113.cmddelete.actions.NavActionManager;
import io.github.omegabird113.cmddelete.actions.mappings.INavMapping;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmdDeleteClient implements ClientModInitializer {
    public static final String MODID = "cmd-delete";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final INavMapping NAV_MAPPING = NavActionManager.getMapping();

    @Override
    public void onInitializeClient() {

    }
}
