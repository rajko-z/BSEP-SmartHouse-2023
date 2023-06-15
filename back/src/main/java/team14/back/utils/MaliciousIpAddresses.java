package team14.back.utils;

import org.springframework.core.io.ClassPathResource;
import team14.back.exception.InternalServerException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MaliciousIpAddresses {

    public static List<String> getMaliciousAddresses() {
        ClassPathResource resource = new ClassPathResource("data/maliciousIpAddresses.txt");

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
