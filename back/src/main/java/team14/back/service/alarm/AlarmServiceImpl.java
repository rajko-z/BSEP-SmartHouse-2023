package team14.back.service.alarm;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import team14.back.config.DRoolsConfig;
import team14.back.converters.AlarmConverters;
import team14.back.dto.UpdateDeviceStateDTO;
import team14.back.dto.alarms.ActivatedDeviceAlarmDTO;
import team14.back.dto.rules.DeviceSignal;
import team14.back.enumerations.DeviceType;
import team14.back.enumerations.LogAction;
import team14.back.model.ActivatedDeviceAlarm;
import team14.back.model.DeviceAlarmTrigger;
import team14.back.repository.ActivatedDeviceAlarmRepository;
import team14.back.repository.LogRepository;
import team14.back.service.alarm.trigger.DeviceAlarmTriggerService;
import team14.back.service.facility.FacilityService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlarmServiceImpl implements AlarmService {

    private final LogRepository logRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final DeviceAlarmTriggerService deviceAlarmTriggerService;
    private final DRoolsConfig dRoolsConfig;
    private final FacilityService facilityService;
    private final ActivatedDeviceAlarmRepository activatedDeviceAlarmRepository;

    public AlarmServiceImpl(LogRepository logRepository, SimpMessagingTemplate simpMessagingTemplate, @Lazy DeviceAlarmTriggerService deviceAlarmTriggerService, DRoolsConfig dRoolsConfig, FacilityService facilityService, ActivatedDeviceAlarmRepository activatedDeviceAlarmRepository) {
        this.logRepository = logRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.deviceAlarmTriggerService = deviceAlarmTriggerService;
        this.dRoolsConfig = dRoolsConfig;
        this.facilityService = facilityService;
        this.activatedDeviceAlarmRepository = activatedDeviceAlarmRepository;
    }

    private KieContainer getKieContainer(){
        KieServices ks = KieServices.Factory.get();
        return ks.newKieClasspathContainer();
    }

    @Override
    public void checkForRedundantCSRRequest() {
        KieSession ksession = getKieContainer().newKieSession("cepKsession");
        ksession.setGlobal("logActionType", LogAction.CREATING_NEW_CSR_REQUEST);
        ksession.setGlobal("simpMessagingTemplate", simpMessagingTemplate);
        this.logRepository.findAll().forEach(ksession::insert);
        ksession.fireAllRules();
    }

    @Override
    public void checkForDeviceAlarms(UpdateDeviceStateDTO newDeviceState, HttpServletRequest request) {
        DeviceSignal deviceSignal = DeviceSignal.builder()
                .deviceId(newDeviceState.getId())
                .state(newDeviceState.getState())
                .message(newDeviceState.getMessage())
                .deviceType(newDeviceState.getDeviceType())
                .activatedAlarms(new ArrayList<>())
                .temperature(getTemperatureForDevice(newDeviceState))
                .build();

        KieSession kieSession = dRoolsConfig.getOrCreateKieSession("deviceAlarmTriggersKS");
        kieSession.insert(deviceSignal);
        kieSession.fireAllRules();

        for (String activatedAlarm : deviceSignal.getActivatedAlarms()) {
            triggerAlarm(activatedAlarm, deviceSignal, request);
        }
        dRoolsConfig.clearKieSession(kieSession);
    }

    @Override
    public List<ActivatedDeviceAlarmDTO> getAllActivatedAlarms() {
        return this.activatedDeviceAlarmRepository.findAll().stream()
                .map(AlarmConverters::convertActivatedDeviceAlarm)
                .sorted((a1, a2) -> a2.getTimestamp().compareTo(a1.getTimestamp()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ActivatedDeviceAlarmDTO> getAllActivatedAlarmsForFacility(String facility) {
        return this.activatedDeviceAlarmRepository.findByFacilityName(facility).stream()
                .map(AlarmConverters::convertActivatedDeviceAlarm)
                .collect(Collectors.toList());
    }

    private void triggerAlarm(String activatedAlarm, DeviceSignal deviceSignal, HttpServletRequest request) {
        DeviceAlarmTrigger trigger = deviceAlarmTriggerService.findTriggerByName(activatedAlarm, request);
        System.out.println("----------------------------");
        System.out.println("Trigger for alarm activated........");
        System.out.println("Device id: " + deviceSignal.getDeviceId());
        System.out.println("DeviceType: " + deviceSignal.getDeviceType());
        System.out.println("DeviceState: " + deviceSignal.getState());
        System.out.println("Trigger name: " + trigger.getAlarmName());
        System.out.println("Trigger rule: " + trigger.getRuleName());
        System.out.println("Message from device: " + deviceSignal.getMessage());
        System.out.println("------------------------------------");

        ActivatedDeviceAlarm alarm = new ActivatedDeviceAlarm();
        alarm.setDeviceAlarmTrigger(trigger);
        alarm.setMessage(deviceSignal.getMessage());
        alarm.setTimestamp(new Date());
        alarm.setFacilityName(facilityService.getFacilityNameByDeviceId(deviceSignal.getDeviceId()));
        activatedDeviceAlarmRepository.save(alarm);

        simpMessagingTemplate.convertAndSend("/activated-device-alarm", AlarmConverters.convertActivatedDeviceAlarm(alarm));
        //TODO: upisi u bazu vrati preko soketa na front
    }

    private double getTemperatureForDevice(UpdateDeviceStateDTO newDeviceState) {
        DeviceType type = newDeviceState.getDeviceType();
        if (!type.equals(DeviceType.THERMOMETER) &&
            !type.equals(DeviceType.WATER_HEATER) &&
            !type.equals(DeviceType.COOKER)) {
            return 0.0;
        }
        String temp = newDeviceState.getState().substring(0, newDeviceState.getState().length() - 1);
        return Double.parseDouble(temp);
    }
}
