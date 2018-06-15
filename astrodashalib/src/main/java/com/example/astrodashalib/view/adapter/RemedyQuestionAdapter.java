package com.example.astrodashalib.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.astrodashalib.R;
import com.example.astrodashalib.data.models.CategoryQuestionAnswer;
import com.example.astrodashalib.data.models.QuestionAnswer;
import java.util.ArrayList;

/**
 * Created by himanshu on 11/10/17.
 */

public class RemedyQuestionAdapter extends RecyclerView.Adapter<RemedyQuestionAdapter.VHItem> {
    Context mContext;
    ArrayList<CategoryQuestionAnswer> mCategoryQuestionAnswers = new ArrayList<>();
    OnQuestionItemClickListener mOnQuestionItemClickListener;

    public RemedyQuestionAdapter(Context context, ArrayList<CategoryQuestionAnswer> categoryQuestionAnswerArrayList, OnQuestionItemClickListener onQuestionItemClickListener) {
        mContext = context;
        mCategoryQuestionAnswers = categoryQuestionAnswerArrayList;
        mOnQuestionItemClickListener = onQuestionItemClickListener;
    }

    @Override
    public VHItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_remedy_list_item, parent, false);
        return new VHItem(view);
    }

    @Override
    public void onBindViewHolder(VHItem vhItem, int position) {
        try {
            final CategoryQuestionAnswer categoryQuestionAnswer = mCategoryQuestionAnswers.get(position);
            long purchaseTimestamp = categoryQuestionAnswer.mFirstRemedyPurchaseTimestamp;

            final QuestionAnswer questionAnswer = categoryQuestionAnswer.mQuestionAnswer;
            final String categoryName = categoryQuestionAnswer.categoryName;
            vhItem.questionTv.setText(questionAnswer.question);

            vhItem.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnQuestionItemClickListener != null)
                        mOnQuestionItemClickListener.onClick(categoryName, questionAnswer);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        try {
            return mCategoryQuestionAnswers.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public class VHItem extends RecyclerView.ViewHolder {
        public View mView;
        public TextView questionTv;
        public LinearLayout mLinearLayout;

        public VHItem(View view) {
            super(view);
            mView = view;
            questionTv = (TextView) view.findViewById(R.id.question_tv);
            mLinearLayout = (LinearLayout) view.findViewById(R.id.ll);

        }
    }

    public interface OnQuestionItemClickListener {
        void onClick(String categoryName, QuestionAnswer questionAnswer);
    }
}

