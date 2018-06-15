package com.example.astrodashalib.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.astrodashalib.R;
import com.example.astrodashalib.data.models.Remedy;
import com.example.astrodashalib.utils.BaseConfiguration;
import java.util.ArrayList;

/**
 * Created by himanshu on 12/10/17.
 */

public class PurchaseRemedyAdapter extends RecyclerView.Adapter<PurchaseRemedyAdapter.VHItem> {
    Context mContext;
    ArrayList<Remedy> mRemedyArrayList;

    public PurchaseRemedyAdapter(Context context, ArrayList<Remedy> remedyArrayList) {
        mContext = context;
        mRemedyArrayList = remedyArrayList;
    }

    @Override
    public VHItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.purchase_remedy_item, parent, false);
        return new VHItem(view);
    }

    @Override
    public void onBindViewHolder(VHItem vhItem, int position) {
        try {
            final Remedy remedy = mRemedyArrayList.get(position);
            vhItem.purchaseRemedyTxt.setText(remedy.upayHindi);

            /*if (BaseConfiguration.isProdEnvironnment()) {*/
                vhItem.ruleIdTv.setVisibility(View.GONE);
                vhItem.upayIdTv.setVisibility(View.GONE);
                vhItem.ruleTv.setVisibility(View.GONE);
            /*} else {
                vhItem.ruleIdTv.setText("Rule Id : " + remedy.ruleId);
                if (remedy.id != 0)
                    vhItem.upayIdTv.setText("Upay Id : " + remedy.id);
                else
                    vhItem.upayIdTv.setVisibility(View.GONE);
                vhItem.ruleTv.setText("Rule : " + remedy.ruleText);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        try {
            return mRemedyArrayList.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public class VHItem extends RecyclerView.ViewHolder {
        public TextView purchaseRemedyTxt, ruleIdTv, upayIdTv, ruleTv;

        public VHItem(View view) {
            super(view);
            purchaseRemedyTxt = (TextView) view.findViewById(R.id.remedy_description);
            ruleIdTv = (TextView) view.findViewById(R.id.rule_id_tv);
            upayIdTv = (TextView) view.findViewById(R.id.upay_id_tv);
            ruleTv = (TextView) view.findViewById(R.id.rule_tv);
        }
    }
}

