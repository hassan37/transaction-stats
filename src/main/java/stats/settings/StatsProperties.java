package stats.settings;

import java.math.MathContext;
import java.math.RoundingMode;

//TODO: to be exported into properties file
public interface StatsProperties {

	int VALID_TRANS_WINDOW_TIME_IN_SEC = 60;
	int DECIMAL_PRECISION = 10;
	RoundingMode DECIMAL_ROUND_MODE = RoundingMode.HALF_UP;
	MathContext  DECIMAL_MATCH_CONTEXT = MathContext.DECIMAL128;
}
