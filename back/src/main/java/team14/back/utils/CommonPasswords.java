package team14.back.utils;

import org.springframework.core.io.ClassPathResource;
import team14.back.exception.InternalServerException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CommonPasswords {

    private CommonPasswords() {}

    public static List<String> getCommonPasswords() {
        ClassPathResource resource = new ClassPathResource("data/passwords.txt");

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        } catch (IOException e) {
            throw new InternalServerException("Unexpected error happened while changing password");
        }

        return lines;
    }
}
