package net.flytre.resource_core;

import net.minecraft.resource.Resource;
import net.minecraft.resource.metadata.ResourceMetadataReader;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FakeBlockStateResource implements Resource {

    private final Identifier id;
    private final InputStream stream;

    public FakeBlockStateResource(Identifier id) {
        this.id = id;
        String str = String.format("""
                {
                  "variants": {
                    "": {
                      "model": "%s"
                    }
                  }
                }
                """, id.toString().replace("blockstates/", "block/").replace(".json", ""));
        stream = new ByteArrayInputStream(str.getBytes());
    }


    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public InputStream getInputStream() {
        return stream;
    }

    @Override
    public boolean hasMetadata() {
        return false;
    }

    @Nullable
    @Override
    public <T> T getMetadata(ResourceMetadataReader<T> metaReader) {
        return null;
    }

    @Override
    public String getResourcePackName() {
        return "resource_core";
    }

    @Override
    public void close() throws IOException {

    }
}
