package com.syaru.crazycapacity;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;
import org.junit.jupiter.api.Test;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;

class PublicationMetadataTest {
    @Test void onlyAcoAndCrazyAeAreRequiredAddonProjects() throws Exception {
        try (var input = getClass().getResourceAsStream("/META-INF/mods.toml")) {
            assertNotNull(input);
            var metadata = new TomlParser().parse(new InputStreamReader(input, StandardCharsets.UTF_8));
            List<Config> dependencies = metadata.get("dependencies.crazy_capacity_bridge");
            var required = dependencies.stream().filter(c -> Boolean.TRUE.equals(c.get("mandatory")))
                    .map(c -> (String) c.get("modId")).filter(id -> !Set.of("forge", "minecraft").contains(id))
                    .collect(Collectors.toSet());
            assertEquals(Set.of("crazyae", "ae2_crafting_optimizer"), required);
        }
    }

    @Test void resourcePackMetadataExistsForMinecraft1201() throws Exception {
        try (var input = getClass().getResourceAsStream("/pack.mcmeta")) {
            assertNotNull(input, "Missing pack.mcmeta causes the reported Forge warning");
            var json = com.google.gson.JsonParser.parseString(new String(input.readAllBytes(), StandardCharsets.UTF_8));
            assertEquals(15, json.getAsJsonObject().getAsJsonObject("pack").get("pack_format").getAsInt());
        }
    }
}
