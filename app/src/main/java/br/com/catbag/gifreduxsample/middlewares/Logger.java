package br.com.catbag.gifreduxsample.middlewares;

import com.umaplay.fluxxan.Action;
import com.umaplay.fluxxan.impl.BaseMiddleware;

import br.com.catbag.gifreduxsample.models.AppState;

/**
 * Created by niltonvasques on 11/13/16.
 */

public class Logger extends BaseMiddleware<AppState> {
    @Override
    public void intercept(AppState appState, Action action) throws Exception {
        System.out.println("Logger.intercept "+action.Type);
    }
}
