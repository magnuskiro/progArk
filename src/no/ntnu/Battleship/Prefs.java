package no.ntnu.Battleship;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Prefs extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// New practice is to use fragments, but TL;DR
		addPreferencesFromResource(R.xml.settings);
	}
}
