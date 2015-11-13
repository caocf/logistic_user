package com.cn.xtouch.fodelforuser.fragment;

import java.util.List;
import com.cn.xtouch.fodelforuser.R;
import com.cn.xtouch.fodelforuser.adapter.OrderStatusAdapter;
import com.cn.xtouch.fodelforuser.constant.Constants;
import com.cn.xtouch.fodelforuser.http.Response.OrderRouteInfo.RouteInfo;
import com.cn.xtouch.fodelforuser.utils.UIUtils;
import com.cn.xtouch.fodelforuser.widget.SlideBottomPanel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class LogisticInfoMapFragment extends BaseFragment implements
		OnMapReadyCallback {
	private MapFragment mapFragment;
	@ViewInject(R.id.sbv)
	private SlideBottomPanel sbp;
	@ViewInject(R.id.tv_order_no)
	private TextView tv_order_no;
	@ViewInject(R.id.listview)
	private ListView listView;
	private OrderStatusAdapter orderStatusAdapter;

	@Override
	public View initUI() {
		View view = View.inflate(context, R.layout.fragment_logistic_info_map,
				null);
		ViewUtils.inject(this, view);
		return view;
	}

	private LatLng latLng;

	@Override
	public void initData() {
		Bundle data = getArguments();// 获得从activity中传递过来的值
		setUpMapIfNeeded();
		double lt = Double.valueOf(data.getString("lt"));
		double lg = Double.valueOf(data.getString("lg"));
		latLng = new LatLng(lt, lg);
		List<RouteInfo> list = data.getParcelableArrayList("info_list");
		tv_order_no.setText(data.getString(Constants.ORDER_NO));
		orderStatusAdapter = new OrderStatusAdapter(getActivity(), list,true);
		listView.setAdapter(orderStatusAdapter);
	}

	private void setUpMapIfNeeded() {
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(UIUtils.getContext());
		if (status == ConnectionResult.SUCCESS) {
			mapFragment = ((MapFragment) getActivity().getFragmentManager()
					.findFragmentById(R.id.google_map));
			mapFragment.getMapAsync(this);
		}
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		// latLng=new LatLng(22.595238,113.857928);
		// googleMap.moveCamera(CameraUpdateFactory.zoomBy(16f));
		googleMap.getUiSettings().setAllGesturesEnabled(true);
		googleMap.getUiSettings().setRotateGesturesEnabled(false);
		googleMap.getUiSettings().setMapToolbarEnabled(true); // 路径规划，Place API
		googleMap.getUiSettings().setCompassEnabled(true);
		googleMap.getUiSettings().setIndoorLevelPickerEnabled(true);
		googleMap.getUiSettings().setMyLocationButtonEnabled(false);
		googleMap.getUiSettings().setZoomControlsEnabled(false);
		googleMap.addMarker(new MarkerOptions().position(latLng).icon(
				BitmapDescriptorFactory
						.fromResource(R.drawable.location_red_50x74)));
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 16);
		googleMap.moveCamera(update);
		// googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		try {
			if (mapFragment != null)
				getActivity().getFragmentManager().beginTransaction()
						.remove(mapFragment).commit();
		} catch (IllegalStateException e) {
		}
	}
}
