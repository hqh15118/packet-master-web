import com.zjucsc.application.system.service.common_impl.CapturePacketServiceImpl;
import com.zjucsc.application.tshark.capture.ProcessCallback;
import com.zjucsc.tshark.TsharkCommon;
import com.zjucsc.tshark.pre_processor.BasePreProcessor;

public class Test {


    @org.junit.Test
    public void test() throws InterruptedException {
        TsharkCommon.setErrorCallback(new TsharkCommon.ErrorCallback() {
            @Override
            public void errorCallback(String errorMsg) {
                System.out.println(errorMsg);
            }
        });
        BasePreProcessor.setCaptureDeviceNameAndMacAddress("11:22:33:44:55:66","eth0");
        CapturePacketServiceImpl capturePacketService = new CapturePacketServiceImpl();
        capturePacketService.start(new ProcessCallback<String, String>() {
            @Override
            public void error(Exception e) {

            }

            @Override
            public void start(String start) {

            }

            @Override
            public void end(String end) {

            }
        });
        Thread.sleep(1000000);
    }
}
