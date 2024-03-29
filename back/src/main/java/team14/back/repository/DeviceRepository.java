package team14.back.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Repository;
import team14.back.dto.DeviceInfoDTO;
import team14.back.exception.BadRequestException;
import team14.back.model.Device;
import team14.back.model.DeviceMessage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DeviceRepository {

    private final String facilityConfigFilePath = "src/main/resources/data/facilityConfigFiles/";
    private final String deviceMessagesFilePath = "src/main/resources/data/deviceMessagesFiles/";
    private final String deviceInfosFilePath = "src/main/resources/data/devices/availableDevices.json";
    private ObjectMapper objectMapper = new ObjectMapper();

    public DeviceRepository(){
        objectMapper.registerModule(new JavaTimeModule());
    }

    public List<Device> getDevicesFromFacility(String facilityName){
        List<Device> devices = null;
        String filename = facilityName.replace(" ", "").concat("config.json");
        try {
            File jsonFile = new File(facilityConfigFilePath+filename);
            devices = objectMapper.readValue(jsonFile, new TypeReference<>() {});
        } catch (IOException e) {
            throw new BadRequestException("Problem with reading file: "+filename);
        }
        return devices;
    }

    public List<DeviceMessage> getDeviceMessages(String filename){
        List<DeviceMessage> deviceMessages = new ArrayList<>();
        try {
            File jsonFile = new File(deviceMessagesFilePath+filename);
            deviceMessages = objectMapper.readValue(jsonFile, new TypeReference<>() {});
        } catch (IOException e) {
            File jsonFile = new File(deviceMessagesFilePath+filename);
            try {
                jsonFile.createNewFile();
            }catch (IOException ex){
                throw new BadRequestException("Problem with reading file: "+filename);
            }
        }
        return deviceMessages;
    }

    public void saveDeviceMessages(String filename,List<DeviceMessage> deviceMessages){
        try {
            File jsonFile = new File(deviceMessagesFilePath+filename);
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
            objectWriter.writeValue(jsonFile, deviceMessages);
        } catch (IOException e) {
            throw new BadRequestException("Problem with writing file: "+filename);
        }
    }

    public List<DeviceInfoDTO> getDevicesInfo(){
        List<DeviceInfoDTO> deviceInfos;
        try {
            File jsonFile = new File(deviceInfosFilePath);
            deviceInfos = objectMapper.readValue(jsonFile, new TypeReference<>() {});
        } catch (IOException e) {
            throw new BadRequestException("Problem with reading file: "+ deviceInfosFilePath);
        }
        return deviceInfos;
    }
}
