package com.naturecurly.zimuzu.Views;

import java.util.List;

/**
 * Created by leveyleonhardt on 12/5/16.
 */

public interface NewsView {
    void updateRecyclerView(List dataset);

    void failGetData();
}
