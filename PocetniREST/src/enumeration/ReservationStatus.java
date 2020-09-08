package enumeration;

import java.io.Serializable;

public enum ReservationStatus implements Serializable{
	CREATED,
	DECLINED,
	ACCEPTED,
	FINISHED,
	CANCELED
}
