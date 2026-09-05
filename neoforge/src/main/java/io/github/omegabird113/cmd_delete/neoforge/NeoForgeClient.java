/*
 * Copyright (c) 2026 Omegabird113.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.omegabird113.cmd_delete.neoforge;

import io.github.omegabird113.cmd_delete.CmdDeleteClient;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = CmdDeleteClient.MODID, dist = Dist.CLIENT)
public final class NeoForgeClient {
    private static boolean started;

    public NeoForgeClient() {
        NeoForge.EVENT_BUS.addListener(ClientTickEvent.Post.class, _ -> {
            //noinspection ConstantValue
            if (!started && Minecraft.getInstance() != null) {
                started = true;
                CmdDeleteClient.start(new NeoForgePlatform());
            }
        });
    }
}
