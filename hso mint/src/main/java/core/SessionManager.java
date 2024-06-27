
package core;

import io.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class SessionManager {
    public static final ConcurrentHashMap<String, Integer> BandWidthSizes = new ConcurrentHashMap<>();
    public static synchronized void AddBandWidth(String ipAddress, int size) {
        int errorCount = BandWidthSizes.getOrDefault(ipAddress, 0);
        if(errorCount>2_000_000_000)
        {
            CheckDDOS.blockIP(ipAddress,"Max Size BandWidth");
            return;
        }
        long currentTime = System.currentTimeMillis();
        BandWidthSizes.put(ipAddress, size);
    }
    public static void CheckBandWidth()
    {
        try {
            String ip="";
            int size=0;
            Map<String, Integer> map = BandWidthSizes;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                if(key!=null && value!=null && value>size)
                {
                    ip = key;
                    size = value;
                }
            }
            map.clear();
            BandWidthSizes.clear();
            if(size>0)
            {
                double n = size; // ví dụ: giá trị đếm băng thông là 1,5 MB
                String result;

                if (n < 1024) {
                   result = n + " b"; // nếu giá trị đếm băng thông nhỏ hơn 1024 thì sử dụng đơn vị bytes
                } else if (n < 1048576) {
                   double kb = n / 1024;
                   result = String.format("%.2f", kb) + " KB"; // nếu giá trị đếm băng thông nhỏ hơn 1048576 (1024^2) thì chuyển sang đơn vị KB
                } else if (n < 1073741824) {
                   double mb = n / 1048576;
                   result = String.format("%.2f", mb) + " MB"; // nếu giá trị đếm băng thông nhỏ hơn 1073741824 (1024^3) thì chuyển sang đơn vị MB
                } else {
                   double gb = n / 1073741824;
                   result = String.format("%.2f", gb) + " GB"; // nếu giá trị đếm băng thông lớn hơn hoặc bằng 1073741824 thì chuyển sang đơn vị GB
                }
                Log.gI().add_Log_Server("BandWidth", "IP : ["+ip+"] "+result);
            }
                
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void RemoveClient(){
        long time = System.currentTimeMillis();
        for(int i = Session.client_entrys.size() -1; i>=0; i--){
            try{
                Session s = Session.client_entrys.get(i);
                if(s==null)continue;
                if(time - s.timeConnect > Manager.timeRemoveClient && !s.get_in4)
                    s.close();
            }catch(Exception e){}
        }
    }
    
}
