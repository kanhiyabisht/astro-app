package com.example.astrodashalib.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.astrodashalib.Constant
import com.example.astrodashalib.R
import com.example.astrodashalib.inflate
import kotlinx.android.synthetic.main.fragment_main_menu.*


class MainMenuFragment : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = container?.inflate(R.layout.fragment_main_menu)


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backIv.setOnClickListener {
            mListener?.closeDrawer()
        }
        faq.setOnClickListener {
            mListener?.onFaqClick()
        }
        tmlTuts.setOnClickListener {
            mListener?.onTutsClick()
        }

        customerSupport.setOnClickListener {
            try {
                val phoneNumber = Constant.CUSTOMER_CARE_NO
                val uri = "tel:" + phoneNumber
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse(uri)
                context.startActivity(intent)
            } catch (e: Exception) {
            }
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
        fun onFaqClick()
        fun onTutsClick()
        fun closeDrawer()
    }

    companion object {

        @JvmField
        val TAG = "MainManu"

        @JvmStatic
        fun newInstance(): MainMenuFragment {
            val fragment = MainMenuFragment()
            return fragment
        }
    }
}
