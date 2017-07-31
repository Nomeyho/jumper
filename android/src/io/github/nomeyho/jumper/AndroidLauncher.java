package io.github.nomeyho.jumper;

import android.os.Bundle;

import android.widget.Toast;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import io.github.nomeyho.jumper.files.PlayerStats;
import io.github.nomeyho.jumper.files.UserPreferences;

public class AndroidLauncher extends AndroidApplication implements RewardedVideoAdListener {
    private static final String APP_ID = "ca-app-pub-3227008925572350~2198960964";
    private static final String AD_ID = "ca-app-pub-3940256099942544/5224354917";
    private RewardedVideoAd mAd;
    private AbstractGame game;
    private AndroidAdService adService;

    @Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Config
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;

		// Ads
        MobileAds.initialize(this, APP_ID);
        this.mAd = MobileAds.getRewardedVideoAdInstance(this);
        this.mAd.setRewardedVideoAdListener(this);

        // Init
        this.adService = new AndroidAdService(this);
        this.game = new JumperGame(adService);
		initialize(this.game, config);
	}

    @Override
    public void onResume() {
        this.mAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        this.mAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        this.mAd.destroy(this);
        super.onDestroy();
    }

    /* API */
    protected void loadAdd () {
        mAd.loadAd(AD_ID, new AdRequest.Builder().build());
    }

	/* Adds callback */
    @Override
    public void onRewardedVideoAdLoaded() {
        this.adService.loading = false;
        Gdx.app.log(Application.TAG, "onRewardedVideoAdLoaded");
        if (this.mAd.isLoaded()) {
            // Volume
            float volume = Math.min(UserPreferences.get().music, UserPreferences.get().sound);
            MobileAds.setAppVolume(volume/100f);
            // Open add
            this.mAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Gdx.app.log(Application.TAG, "onRewardedVideoAdOpened");
    }

    @Override
    public void onRewardedVideoStarted() {
        Gdx.app.log(Application.TAG, "onRewardedVideoStarted");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Gdx.app.log(Application.TAG, "onRewardedVideoAdClosed");
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Gdx.app.log(Application.TAG, "onRewarded");
        PlayerStats.get().increaseLifes(25);
        PlayerStats.get().save();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Gdx.app.log(Application.TAG, "onRewardedVideoAdLeftApplication");
        // If the user left the app in the middle of the video...
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Gdx.app.log(Application.TAG, "onRewardedVideoAdFailedToLoad");
        Toast.makeText(this,
                "Error loading the video",
                Toast.LENGTH_SHORT
        ).show();
    }
}
