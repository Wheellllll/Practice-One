package server.config;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by Kris Chan on 1:24 PM 4/17/16 .
 * All right reserved.
 */
public class YamlAdapter extends ConfigAdapter{
    /**
     * {@inheritDoc}
     */
    public <T> T loadToBean(InputStream streamIn, Class<T> clazz) {
        return new Yaml().loadAs(streamIn, clazz);
    }

    /**
     * {@inheritDoc}
     */
    public void writeToStream(Object data, OutputStream streamOut) {
        DumperOptions options = new DumperOptions();
        options.setExplicitEnd(false);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndent(2);

        Representer representer = new Representer();
        representer.addClassTag(data.getClass(), Tag.MAP);

        Yaml yaml = new Yaml(representer, options);
        yaml.dump(data, new OutputStreamWriter(streamOut));
    }

}
