package no.ntnu.Battleship;


/**
 * used to easily create a set of devices.
 * @author Håvard
 *
 */
public class DeviceFactory {

	public DeviceFactory(){
		//TODO: fetch settings  from somewhere
	}
	
	/**
	 * Creates a set of devices 
	 * @param devTypes - set of devicetypes. Length of this set determines number of devices. 
	 * @return
	 */
	public Device[] createDeviceSet(int[] devTypes){
		Device[] devs = new Device[devTypes.length];
		
		//TODO: use settings to affect creation
		
		for(int i= 0; i < devTypes.length; i++){
			devs[i] = new Device(devTypes[i]);
		}
		
		
		return devs;
	}
	
}
