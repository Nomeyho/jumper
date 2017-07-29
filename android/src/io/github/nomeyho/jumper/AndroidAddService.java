package io.github.nomeyho.jumper;

import android.content.Context;
import com.badlogic.gdx.Game;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class AndroidAddService implements AdService, RewardedVideoAdListener {
    private RewardedVideoAd mAd;
    private Game game;

    public AndroidAddService(Context context) {
        mAd = MobileAds.getRewardedVideoAdInstance(context);
        mAd.setRewardedVideoAdListener(this);
    }

    /* Cross platform */
    @Override
    public void register(Game game) {
        this.game = game;
    }

    @Override
    public void openAdd() {
        if(this.game != null) {
            System.out.println("ICI openAdd");
            mAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
        }
    }

    /* Platform specific */
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
