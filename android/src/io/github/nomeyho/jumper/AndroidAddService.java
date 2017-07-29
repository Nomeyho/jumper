package io.github.nomeyho.jumper;

public class AndroidAddService implements AdService {
    AndroidLauncher app;

    protected AndroidAddService(AndroidLauncher app) {
        this.app = app;
    }

    /* Cross platform */
    @Override
    public void openAdd() {
        System.out.println("ICI ICI openAdd");
        app.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                app.openAdd();
            }
        });
        // Message message = ;

        // TODO send msg
        // mAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
    }
}
