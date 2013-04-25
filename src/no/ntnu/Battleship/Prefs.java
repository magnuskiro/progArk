package no.ntnu.Battleship;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Prefs extends PreferenceActivity {
	
	private static final String OPT_MUSIC = "music";
	private static final boolean OPT_MUSIC_DEF = true;
	private static final String OPT_SFX = "sfx";
	private static final boolean OPT_SFX_DEF = true;
	

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// New practice is to use fragments, but TL;DR
		addPreferencesFromResource(R.xml.settings);
	}
	
	public static boolean getMusic(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_MUSIC, OPT_MUSIC_DEF);
	}
	
	public static boolean getSFX(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_SFX, OPT_SFX_DEF);
	}
}
