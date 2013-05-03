package electrodynamics.configuration;

public class ConfigurationSettings {

	/* Ore settings */
	public static boolean ALTERNATE_ORE_DROPS;
	public static final String ALERNATE_ORE_DROPS_CONFIGNAME = "ore.alternate_drops";
	public static final boolean ALTERNATE_ORE_DROPS_DEFAULT = false;
	
	/* Magnetite spawn settings */
	public static boolean MAGNETITE_ENABLED;
	public static final String MAGNETITE_ENABLED_CONFIGNAME = "magnetite.enabled";
	public static final boolean MAGNETITE_ENABLED_DEFAULT = true;
	
	public static int MAGNETITE_MAX_Y_LEVEL;
	public static final String MAGNETITE_MAX_Y_LEVEL_CONFIGNAME = "magnetite.max_y_level";
	public static final int MAGNETITE_MAX_Y_LEVEL_DEFAULT = 64;
	
	public static int MAGNETITE_SPAWN_AMOUNT;
	public static final String MAGNETITE_SPAWN_AMOUNT_CONFIGNAME = "magnetite.spawn_amount";
	public static final int MAGNETITE_SPAWN_AMOUNT_DEFAULT = 8;
	
	public static int MAGNETITE_SPAWN_RARITY;
	public static final String MAGNETITE_SPAWN_RARITY_CONFIGNAME = "magnetite.spawn_rarity";
	public static final int MAGNETITE_SPAWN_RARITY_DEFAULT = 8;

	/* Nickel spawn settings */
	public static boolean NICKEL_ENABLED;
	public static final String NICKEL_ENABLED_CONFIGNAME = "nickel.enabled";
	public static final boolean NICKEL_ENABLED_DEFAULT = true;
	
	public static int NICKEL_MAX_Y_LEVEL;
	public static final String NICKEL_MAX_Y_LEVEL_CONFIGNAME = "nickle.max_y_level";
	public static final int NICKEL_MAX_Y_LEVEL_DEFAULT = 64;
	
	public static int NICKEL_SPAWN_AMOUNT;
	public static final String NICKEL_SPAWN_AMOUNT_CONFIGNAME = "nickle.spawn_amount";
	public static final int NICKEL_SPAWN_AMOUNT_DEFAULT = 8;
	
	public static int NICKEL_SPAWN_RARITY;
	public static final String NICKEL_SPAWN_RARITY_CONFIGNAME = "nickle.spawn_rarity";
	public static final int NICKEL_SPAWN_RARITY_DEFAULT = 8;

	/* Tesla armor ability settings */
	public static double MAGNETIC_RANGE;
	public static final String MAGNETIC_RANGE_CONFIGNAME = "tesla_armor.range";
	public static final double MAGNETIC_RANGE_DEFAULT = 3D;
	
	public static double MAGNETIC_ATTRACTION_SPEED;
	public static final String MAGNETIC_ATTRACTION_SPEED_CONFIGNAME = "tesla_armor.speed";
	public static final double MAGNETIC_ATTRACTION_SPEED_DEFAULT = 0.1D;
	
	public static double THERMAL_VIEW_RANGE;
	public static final String THERMAL_VIEW_RANGE_CONFIGNAME = "tesla_armor.thermal_view_range";
	public static final double THERMAL_VIEW_RANGE_DEFAULT = 16D;
	
	/* Key Bindings */
	public static int MAGNET_TOGGLE;
	public static final String MAGNET_TOGGLE_NAME = "tesla_armor.magnet.toggle";
	public static final String MAGNET_TOGGLE_CONFIGNAME = "tesla_armor.magnet.toggle";
	public static final int MAGNET_TOGGLE_DEFAULT = 34;
	
	public static int THERMAL_VIEW_TOGGLE;
	public static final String THERMAL_VIEW_TOGGLE_NAME = "tesla_armor.thermal_view.toggle";
	public static final String THERMAL_VIEW_TOGGLE_CONFIGNAME = "tesla_armor.thermal_view.toggle";
	public static final int THERMAL_VIEW_TOGGLE_DEFAULT = 35;
	
	public static int HOVER_MODE_TOGGLE;
	public static final String HOVER_MODE_TOGGLE_NAME = "tesla_armor.hover_mode.toggle";
	public static final String HOVER_MODE_TOGGLE_CONFIGNAME = "tesla_armor.hover_mode.toggle";
	public static final int HOVER_MODE_TOGGLE_DEFAULT = 36;
	
}
