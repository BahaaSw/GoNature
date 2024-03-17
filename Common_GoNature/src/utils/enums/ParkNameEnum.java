package utils.enums;

import java.util.HashMap;
import java.util.Map;

public enum ParkNameEnum {
	None (0),
	Banias (1),
	Masada (2),
	Herodium (3);

    private final Integer parkId;

    ParkNameEnum(Integer parkId) {
        this.parkId = parkId;
    }

    public Integer getParkId() {
        return this.parkId;
    }

    public static ParkNameEnum fromParkId(Integer parkId) {
        for (ParkNameEnum parkName : ParkNameEnum.values()) {
            if ((parkId == null && parkName.getParkId() == null) || 
                (parkId != null && parkId.equals(parkName.getParkId()))) {
                return parkName;
            }
        }
        return None;
    }
}
