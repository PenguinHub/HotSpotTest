package hotspot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

public class Hotspot 
{
	private Context context;
	
	public Hotspot(Context context)
	{
		this.context=context;
	}
	
	public void createHotSpot()
    {
    	//connect to wifi manager
    	WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled())
        {
        	//disable wifi if connected
            wifiManager.setWifiEnabled(false);          
        }
      //Get all declared methods in WifiManager class  
        Method[] wmMethods = wifiManager.getClass().getDeclaredMethods();      
        boolean methodFound=false;
        for(Method method: wmMethods)
        {
            if(method.getName().equals("setWifiApEnabled"))
            {
            	// set up hotspot
                methodFound=true;
                WifiConfiguration netConfig = new WifiConfiguration();
                //choose name for hotspot
                netConfig.SSID = "Game";
                netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);

                try {
                    boolean apstatus=(Boolean) method.invoke(wifiManager, netConfig,true);          
                    for (Method isWifiApEnabledmethod: wmMethods)
                    {
                        if(isWifiApEnabledmethod.getName().equals("isWifiApEnabled"))
                        {
                            while(!(Boolean)isWifiApEnabledmethod.invoke(wifiManager)){
                            };
                            for(Method method1: wmMethods)
                            {
                                if(method1.getName().equals("getWifiApState")){
                                    int apstate;
                                    apstate=(Integer)method1.invoke(wifiManager);
                                }
                            }
                        }
                    }
                    if(apstatus)
                        System.out.println("SUCCESS");  
                    else
                        System.out.println("FAILED");  
                    
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }      
        }
    }

	public void findHotspots()
	{
		
	}
	
	public void connectToHotspot()
	{
		String hotspotName="Game";
		WifiConfiguration conf = new WifiConfiguration();
	    conf.SSID =  hotspotName;
	    conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
	    WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE); 
	    wifiManager.addNetwork(conf);

	    List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
	    for( WifiConfiguration i : list ) 
	    {
	        if(i.SSID != null && i.SSID.equals(hotspotName))
	        {
	            try 
	            {
	                wifiManager.disconnect();
	                wifiManager.enableNetwork(i.networkId, true);
	                System.out.print("i.networkId " + i.networkId + "\n");
	                wifiManager.reconnect();               
	                break;
	            }
	            catch (Exception e) {
	                e.printStackTrace();
	            }

	        }           
	    }
	}

}
