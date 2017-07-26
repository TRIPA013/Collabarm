package com.abhirishi.personal.collabarm

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.abhirishi.personal.collabarm.models.Alarm
import com.abhirishi.personal.collabarm.util.FirebaseUtil
import com.abhirishi.personal.collabarm.util.FirebaseUtil.Companion.checkForAlarms

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnAlarmListFragmentInteractionListener]
 * interface.
 */
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class AlarmsFragment : Fragment(), AlarmsListener {

    var alarmsRecyclerViewAdapter: AlarmsRecyclerViewAdapter? = null
    override fun onAlarmsChange() {
        alarmsRecyclerViewAdapter?.notifyDataSetChanged()
    }

    // TODO: Customize parameters
    private var mColumnCount = 1
    private var mListener: OnAlarmListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_alarms_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            val recyclerView = view
            if (mColumnCount <= 1) {
                recyclerView.layoutManager = LinearLayoutManager(context)
            } else {
                val verticalLayoutmanager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                recyclerView.layoutManager = verticalLayoutmanager
            }
            alarmsRecyclerViewAdapter = AlarmsRecyclerViewAdapter(getAllAlarmsOnCollabarm(context), mListener)
            recyclerView.adapter = alarmsRecyclerViewAdapter
        }
        return view
    }

    private fun getAllAlarmsOnCollabarm(context: Context): List<Alarm> {
        checkForAlarms(context, FirebaseUtil.getUsername(), this)
        return FirebaseUtil.alarms
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnAlarmListFragmentInteractionListener) {
            mListener = context as OnAlarmListFragmentInteractionListener?
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnAlarmListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnAlarmListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: Alarm)
    }

    companion object {

        // TODO: Customize parameter argument names
        private val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        fun newInstance(columnCount: Int): AlarmsFragment {
            val fragment = AlarmsFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }
}
