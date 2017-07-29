package io.github.nomeyho.jumper;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class AndroidLauncher extends AndroidApplication implements RewardedVideoAdListener {
    private static final String APP_ID = "ca-app-pub-3227008925572350~2198960964";
    private static final String AD_ID = "ca-app-pub-3940256099942544/5224354917";
    private RewardedVideoAd mAd;
    private AbstractGame game;

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
        AndroidAddService addService = new AndroidAddService(this);
        this.game = new JumperGame(addService);
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
    protected void openAdd () {
        mAd.loadAd(AD_ID, new AdRequest.Builder().build());
    }

	/* Adds callback */
    @Override
    public void onRewardedVideoAdLoaded() {
        System.out.println("ICI onRewardedVideoAdLoaded");
        if (this.mAd.isLoaded()) {
            this.mAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {
        System.out.println("ICI onRewardedVideoAdOpened");
    }

    @Override
    public void onRewardedVideoStarted() {
        System.out.println("ICI onRewardedVideoStarted");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        System.out.println("ICI onRewardedVideoAdClosed");
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        System.out.println("ICI onRewarded");
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        System.out.println("ICI onRewardedVideoAdLeftApplication");
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        System.out.println("ICI onRewardedVideoAdFailedToLoad");
    }
}
