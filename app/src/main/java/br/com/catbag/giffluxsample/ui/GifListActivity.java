package br.com.catbag.giffluxsample.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.Toast;

import com.umaplay.fluxxan.Fluxxan;
import com.umaplay.fluxxan.ui.StateListenerActivity;
import com.umaplay.fluxxan.util.ThreadUtils;

import br.com.catbag.giffluxsample.App;
import br.com.catbag.giffluxsample.R;
import br.com.catbag.giffluxsample.actions.GifActionCreator;
import br.com.catbag.giffluxsample.models.AppState;
import br.com.catbag.giffluxsample.ui.wrappers.GlideWrapper;
import trikita.anvil.Anvil;

import static trikita.anvil.DSL.backgroundColor;
import static trikita.anvil.DSL.onClick;
import static trikita.anvil.DSL.visibility;
import static trikita.anvil.DSL.withId;

public class GifListActivity extends StateListenerActivity<AppState> {

    private AppState mAppState = getFlux().getState();
    private GifActionCreator mActionCreator = GifActionCreator.getInstance();

    //Views
    private GlideWrapper mGlideWrapper;

    //Bindings
    private boolean gifProgressVisibility;
    private int gifBackgroundColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_list);
        bindingViews();
        initializeGifView();
        mActionCreator.gifDownloadStart(mAppState.getGifUrl(), mAppState.getGifTitle(), this);
    }

    private void bindingViews() {
        //Bindings Defaults
        gifBackgroundColor = ContextCompat.getColor(this, R.color.notWatched);
        gifProgressVisibility = false;

        Anvil.mount(findViewById(R.id.activity_gif_list), () -> {
            backgroundColor(gifBackgroundColor);

            withId(R.id.loading, () -> {
                visibility(gifProgressVisibility);
            });
            withId(R.id.gif_image, () -> {
                onClick(v -> mActionCreator.gifClick(mAppState.getGifStatus()));
            });
        });
    }

    @Override
    protected Fluxxan<AppState> getFlux() {
        return App.getFluxxan();
    }

    @Override
    public void onStateChanged(AppState appState) {
        mAppState = appState;
        switch (mAppState.getGifStatus()) {
            case NOT_DOWNLOADED:
                String errorMsg = appState.getGifDownloadFailureMsg();
                if (!errorMsg.isEmpty()) {
                    showToast(errorMsg);
                    gifProgressVisibility = false;
                }
                break;
            case DOWNLOADING:
                gifProgressVisibility = true;
                break;
            case DOWNLOADED:
                mGlideWrapper.load(appState.getGifLocalPath());
                break;
            case LOOPING:
                mGlideWrapper.play();
                break;
            case PAUSED:
                mGlideWrapper.stop();
                break;
            default:
        }

        if (appState.getGifWatched()) {
            gifBackgroundColor = ContextCompat.getColor(this, R.color.watched);
        }

        Anvil.render();
    }

    public GlideWrapper getGlideWrapper() {
        return mGlideWrapper;
    }

    private void showToast(String msg) {
        ThreadUtils.runOnMain(() -> {
            Toast.makeText(GifListActivity.this, msg, Toast.LENGTH_LONG).show();
        });
    }

    private void initializeGifView() {
        ImageView imageView = (ImageView) findViewById(R.id.gif_image);
        mGlideWrapper = new GlideWrapper(imageView)
                .onException((e) -> showToast(e.getMessage()))
                .onLoaded(() -> {
                    gifProgressVisibility = false;
                    Anvil.render();
                });
    }
}
