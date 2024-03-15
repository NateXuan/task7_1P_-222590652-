package sit707_week7;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BodyTemperatureMonitorTest {

    private TemperatureSensor temperatureSensor;
    private NotificationSender notificationSender;
    private CloudService cloudService;
    private BodyTemperatureMonitor bodyTemperatureMonitor;

    @Before
    public void setUp() {
        // Mock the dependencies
        temperatureSensor = Mockito.mock(TemperatureSensor.class);
        notificationSender = Mockito.mock(NotificationSender.class);
        cloudService = Mockito.mock(CloudService.class);
        
        // Create the instance of BodyTemperatureMonitor with mocked dependencies
        bodyTemperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, cloudService, notificationSender);
    }

    @Test
    public void testReadTemperatureNegative() {
        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(-1.0);
        bodyTemperatureMonitor.readTemperature();
        Mockito.verify(temperatureSensor).readTemperatureValue();
    }

    @Test
    public void testReadTemperatureZero() {
        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(0.0);
        bodyTemperatureMonitor.readTemperature();
        Mockito.verify(temperatureSensor).readTemperatureValue();
    }

    @Test
    public void testReadTemperatureNormal() {
        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(36.5);
        bodyTemperatureMonitor.readTemperature();
        Mockito.verify(temperatureSensor).readTemperatureValue();
    }

    @Test
    public void testReadTemperatureAbnormallyHigh() {
        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(39.0);
        bodyTemperatureMonitor.readTemperature();
        Mockito.verify(temperatureSensor).readTemperatureValue();
    }

    @Test
    public void testReportTemperatureReadingToCloud() {
        TemperatureReading reading = new TemperatureReading();
        bodyTemperatureMonitor.reportTemperatureReadingToCloud(reading);
        Mockito.verify(cloudService).sendTemperatureToCloud(reading);
    }

    @Test
    public void testInquireBodyStatusNormalNotification() {
        Mockito.when(cloudService.queryCustomerBodyStatus(bodyTemperatureMonitor.getFixedCustomer())).thenReturn("NORMAL");
        bodyTemperatureMonitor.inquireBodyStatus();
        Mockito.verify(notificationSender).sendEmailNotification(Mockito.eq(bodyTemperatureMonitor.getFixedCustomer()), Mockito.anyString());
    }

    @Test
    public void testInquireBodyStatusAbnormalNotification() {
        Mockito.when(cloudService.queryCustomerBodyStatus(bodyTemperatureMonitor.getFixedCustomer())).thenReturn("ABNORMAL");
        bodyTemperatureMonitor.inquireBodyStatus();
        Mockito.verify(notificationSender).sendEmailNotification(Mockito.eq(bodyTemperatureMonitor.getFamilyDoctor()), Mockito.anyString());
    }

}
