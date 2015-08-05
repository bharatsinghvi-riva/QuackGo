package riva.init.quackgo;

import java.util.ArrayList;

/**
 * Created by bharat.s on 8/5/15.
 */
public abstract class SearchCallback {

    public abstract void onFail();
    public abstract void onSuccess(ArrayList<String> result);

}
