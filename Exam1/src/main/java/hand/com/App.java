package hand.com;

import java.io.*;
import java.util.Properties;

public class App {

    private static boolean importSQL(Properties properties) {
        String host = properties.getProperty("jdbc.host");
        String port = properties.getProperty("jdbc.port");
        String username = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");
        String databasename = properties.getProperty("jdbc.databasename");
        String file = properties.getProperty("jdbc.file");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("cmd /C ");
        stringBuilder.append(" mysql ").append(" -h").append(host).append(" -p").append(port);
        stringBuilder.append(" -u").append(username).append(" -p").append(password);
        stringBuilder.append(" --default-character-set=utf8 ").append(databasename);
        stringBuilder.append(" < ").append(file);

        try {
            Process process = Runtime.getRuntime().exec(stringBuilder.toString());
            input(process.getInputStream());
            if (process.waitFor() == 0) {
                return true;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void input(final InputStream inputStream) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Reader reader = new InputStreamReader(inputStream);
                        BufferedReader bf = new BufferedReader(reader);
                        String line = null;
                        try {
                            while ((line = bf.readLine()) != null) {
                                System.out.println(line);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                reader.close();
                                bf.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

        ).start();
    }

    public static void main(String[] args) {

        Properties properties = new Properties();
        InputStream is = App.class.getClassLoader().getResourceAsStream("jdbc.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (importSQL(properties)) {
            System.out.println("导入成功");
        } else {
            System.out.println("导入失败");
        }

    }


}