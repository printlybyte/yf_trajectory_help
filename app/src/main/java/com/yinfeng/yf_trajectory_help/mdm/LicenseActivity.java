package com.yinfeng.yf_trajectory_help.mdm;//package com.yinfeng.yinfengtrajectory;


import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.yinfeng.yf_trajectory_help.R;


public class LicenseActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.license_layout);
        Button acceptBtn = (Button)findViewById(R.id.cancelBtn);
        acceptBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        TextView licenseText = (TextView) findViewById(R.id.license_content);
        String filename = "huawei_software_license.html";
        String content = Utils.getStringFromHtmlFile(this, filename);
        licenseText.setText(Html.fromHtml(content));
    }


}
