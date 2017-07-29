package io.github.nomeyho.jumper;

public class AndroidAdService implements AdService {
    public boolean loading = false;
    private AndroidLauncher app;

    protected AndroidAdService(AndroidLauncher app) {
        this.app = app;
    }

    /* Cross platform */
    @Override
    public void openAdd() {
        System.out.println("ICI ICI openAdd");
        app.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                app.loadAdd(); // callback will open it
                loading = true;
            }
        });
    }

    @Override
    public boolean isLoading() {
        return loading;
    }
}
