package server;

import server.config.ConfigAdapter;

import java.io.*;

/**
 * Created by Kris Chan on 11:00 AM 4/17/16 .
 * All right reserved.
 */
public class ConfigManager {

    /**
     * 配置加载器
     */
    private ConfigAdapter adapter;

    /**
     * 候选配置
     */
    private String[] pathCandidates;

    /**
     * 给定配置加载器, 并给定配置的一个或多个候选路径
     *
     * @param adapter        配置加载器
     * @param pathCandidates 候选路径
     */
    public ConfigManager(ConfigAdapter adapter, String... pathCandidates) {
        this.pathCandidates = pathCandidates;
        this.adapter = adapter;
    }

    /**
     * 从候选路径中加载配置到 Java Bean
     *
     * @param clazz Java Bean 的类
     * @param <T>   任意 Java Bean
     * @return Bean
     * @throws IOException 若所有候选路径都无法获得配置, 则抛出此异常
     */
    public <T> T loadToBean(Class<T> clazz) throws IOException {
        InputStream in = null;
        for (String path : pathCandidates) {
            try {
                in = new FileInputStream(path);
                break;
            } catch (IOException ignore) {
            }
        }
        if (in == null) {
            in = new FileInputStream("config.json");
        }
        return adapter.loadToBean(in, clazz);
    }

    /**
     * 向候选路径中第一个可以写入的路径写入配置
     *
     * @param data Java Bean
     * @throws IOException 若所有候选路径都无法写入配置, 则抛出此异常
     */
    public void writeToFirstCandidate(Object data) throws IOException {
        for (String path : pathCandidates) {
            try {
                File f = new File(path);
                f.getParentFile().mkdirs();
                f.createNewFile();
                FileOutputStream out = new FileOutputStream(path);
                adapter.writeToStream(data, out);
                out.close();
                return;
            } catch (IOException ignore) {
            }
        }
        throw new IOException("None of the specified config path can be written");
    }

}
