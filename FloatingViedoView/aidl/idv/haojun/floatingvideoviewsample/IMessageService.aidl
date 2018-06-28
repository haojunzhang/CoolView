// IMessageService.aidl
package idv.haojun.floatingvideoviewsample;

// Declare any non-default types here with import statements
import idv.haojun.floatingvideoviewsample.IMessageServiceCallback;
interface IMessageService {

    void registerCallback(IMessageServiceCallback callback);

    void unregisterCallback(IMessageServiceCallback callback);

    void sendMessage(String text, int width, int height);

}
