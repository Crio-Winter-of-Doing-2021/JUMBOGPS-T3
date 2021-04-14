package com.raghul.asset_tracker.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raghul.asset_tracker.R
import com.raghul.asset_tracker.adapter.AssetListAdapter
import com.raghul.asset_tracker.config.RetrofitServiceBuilder
import com.raghul.asset_tracker.model.Asset
import com.raghul.asset_tracker.repository.AssetRepository
import com.raghul.asset_tracker.service.AssetService
import com.raghul.asset_tracker.utils.CommonConstants
import com.raghul.asset_tracker.viewmodel.AssetViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AssetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AssetFragment : Fragment(),AssetListAdapter.TimelineClickListener,AssetListAdapter.DeleteItemListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var assetListView: RecyclerView
    private lateinit var adapter: AssetListAdapter
    private lateinit var assetRepository: AssetRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_asset, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        assetListView = view.findViewById(R.id.asset_items)
        context?.let {
            assetRepository = AssetRepository(it)
        }
        adapter = AssetListAdapter(this,this)

        activity?.title = "Asset"
        ViewModelProviders.of(this).get(AssetViewModel::class.java)
                 .getAllAssets().observe(viewLifecycleOwner,{
                     if(it.isNotEmpty()){
                         adapter.setAssetList(it)
                     }
                 })

        assetListView.layoutManager = LinearLayoutManager(context)
        assetListView.adapter = adapter

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AssetFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AssetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onTimelineClick(asset: Asset) {
        var dashboardFragment = DashboardFragment()
        var argumentValue:Bundle = Bundle()
        argumentValue.putString("assetId",asset?.id)
        argumentValue.putString(CommonConstants.MODULE,CommonConstants.TIMELINE_BUNDLE)
        dashboardFragment.arguments = argumentValue
        parentFragmentManager?.beginTransaction()?.
        replace(R.id.fragment_container,dashboardFragment)?.addToBackStack(AssetFragment.javaClass.name)?.commit()

    }

    override fun onDeleteClick(asset: Asset) {

        assetRepository.deleteAsset(asset){
            AssetListAdapter.assetsList?.remove(asset)
            adapter.notifyDataSetChanged()
        }

    }
}