package com.example.astrodashalib.view

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.astrodashalib.R
import com.example.astrodashalib.inflate
import com.example.astrodashalib.view.adapter.ExpandableListAdapter
import kotlinx.android.synthetic.main.fragment_sample_question_list.*


class SampleQuestionListFragment : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null
    var listDataHeader: ArrayList<String> = ArrayList()
    var listDataChild: HashMap<String, ArrayList<String>> = HashMap()

    var listAdapter: ExpandableListAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareListData()
    }

    private fun prepareListData() {
        listDataHeader = ArrayList<String>()
        listDataChild = HashMap<String, ArrayList<String>>()

        // Adding child data
        listDataHeader.add("Love")
        listDataHeader.add("Self")
        listDataHeader.add("Everyday Life")

        // Adding child data
        val loveQna = ArrayList<String>()
        loveQna.add("When will I meet the true love of my life ?")
        loveQna.add("Am I going to be in a new relationship soon ? ")
        loveQna.add("Should I move in with my boyfriend?")
        loveQna.add("Why did we break up last year ?")
        loveQna.add("How will I meet my future husband ?")

        val selfQna = ArrayList<String>()
        selfQna.add("Do people like to be around me ?")
        selfQna.add("Does my chart indicate anything about realizing my life purpose?")
        selfQna.add("Am I on the right path according to astrology and destiny? What would be a good path for me right now?")
        selfQna.add("When is the best time to start a fitness program and quit smoking to avoid extra stress?")
        selfQna.add("I want to have a pet. Which animal will I be happy with?")

        val lifeQna = ArrayList<String>()
        lifeQna.add("Can I expect any type of surprises this week either good or bad? If so, what kind?")
        lifeQna.add("Can I expect to hear from my friends today? Do you know how they may be feeling about me?")
        lifeQna.add("Feeling nervous about my competition this weekend. Will Ido ok? is there anything I can do to ensure everything runs smoothly on the day?")
        lifeQna.add("I have lost my wedding ring. When will I need to tell what happened? What is the best time and what will be the outcome?")
        lifeQna.add("Will I meet someone special if I go to the party on Friday?")

        listDataChild.put(listDataHeader.get(0), loveQna) // Header, Child data
        listDataChild.put(listDataHeader.get(1), selfQna)
        listDataChild.put(listDataHeader.get(2), lifeQna)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = container?.inflate(R.layout.fragment_sample_question_list)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val header = layoutInflater.inflate(R.layout.list_header_view,expandableListView,false) as ViewGroup
        expandableListView.addHeaderView(header)
        backIv.setOnClickListener {
            mListener?.closeDrawer()
        }
        listAdapter = ExpandableListAdapter(context, listDataHeader, listDataChild)
        expandableListView.setAdapter(listAdapter)
        expandableListView.setGroupIndicator(null)

    }




    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener)
            mListener = context
        else
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        fun closeDrawer()
    }

    companion object {
        @JvmField
        val TAG = "SampleQnaListFrag"

        @JvmStatic
        fun newInstance(): SampleQuestionListFragment {
            val fragment = SampleQuestionListFragment()
            return fragment
        }
    }
}
