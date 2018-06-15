package com.example.astrodashalib.view

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.astrodashalib.R
import com.example.astrodashalib.inflate
import kotlinx.android.synthetic.main.fragment_tutorial.*


class TutorialFragment : Fragment() {


    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?  = container?.inflate(R.layout.fragment_tutorial)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backIv.setOnClickListener {
            mListener?.closeDrawer()
        }

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
        val TAG ="TutorialFragment"

        @JvmStatic
        fun newInstance(): TutorialFragment {
            val fragment = TutorialFragment()
            return fragment
        }
    }
}
