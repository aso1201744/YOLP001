package jp.ac.st.asojuku.yolp001;

import jp.co.yahoo.android.maps.GeoPoint;
import jp.co.yahoo.android.maps.MapController;
import jp.co.yahoo.android.maps.MapView;
import jp.co.yahoo.android.maps.weather.WeatherOverlay;
import jp.co.yahoo.android.maps.weather.WeatherOverlay.WeatherOverlayListener;
import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity implements LocationListener, WeatherOverlayListener {

	@Override
	public void errorUpdateWeather(WeatherOverlay arg0, int arg1) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void finishUpdateWeather(WeatherOverlay arg0) {
		// TODO 自動生成されたメソッド・スタブ

	}

	// LocationManagerを準備
	LocationManager mLocationManager = null;
	// MapView を準備
	MapView mMapView = null;
	// 直前の緯度(1000000倍精度)
	int lastLatitude = 0;
	// 直前の経度(1000000倍精度）
	int lastLongitude = 0;

	// 雨雲レーダー表示用のオーバーレイクラス変数を準備
	WeatherOverlay mWeatherOverlay = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// 地図表示用のYahooライブラリview部品を用意
		mMapView = new MapView(this,"dj0zaiZpPTdhZ1hERlB4QU01ViZzPWNvbnN1bWVyc2VjcmV0Jng9Mjg-");
		// ズームボタンを画面にON
		mMapView.setBuiltInZoomControls(true);
		mMapView.setScalebar(true);

		// ここから、手動で地図をセット
		// 渋谷駅の緯度経度のGeoPointを手書きで設定
		double lat = 35.658516;
		double lon = 139.701773;
		GeoPoint gp = new GeoPoint((int)(lat * 1000000),(int)(lon * 1000000));
		// 地図本体を取得
		MapController c = mMapView.getMapController();

		// 地図本体にGeoPointoを設定
		c.setCenter(gp);
		// 地図本体のズームを3に設定
		c.setZoom(3);
		// 地図本体を画面にセット
		setContentView(mMapView);

		// ここから、GPSの使用
		// LocationManagerを取得
		mLocationManager =
				(LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// 位置測定のためのGPS精度や使用消費量を設定するふるいにかけるためのCriteriaオブジェクトを生成
		Criteria criteria = new Criteria();

		// Accuracyを指定(低精度)
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);

		// Powerrequirementを指定(低消費電力)
		criteria.setPowerRequirement(Criteria.POWER_LOW);

		// 位置情報を伝達してくれるロケーションプロバイダの取得
		String provider = mLocationManager.getBestProvider(criteria,true);

		// 位置情報のイベントリスナーであるLocationListenerを登録
		mLocationManager.requestLocationUpdates(provider,0,0, this);

		// ここから、雨雲レーダー処理
		// 雨雲レーダー用のオーバーレイ(WeatherOverlay) 設定処理
		mWeatherOverlay = new WeatherOverlay(this);

		// WeatherOverlayListenerを設定
		mWeatherOverlay.setWeatherOverlayListener(this);

		// 雨雲レーダーの更新間隔を、分単位で指定
		mWeatherOverlay.startAutoUpdate(1);

		// MapViewにWeatherOverlayを追加
		mMapView.getOverlays().add(mWeatherOverlay);

	}



	@Override
	public void onLocationChanged(Location location) {
		// TODO 自動生成されたメソッド・スタブ

		// 緯度の取得
		double lat = location.getLatitude();
		int latitude = (int)(lat * 1000000);
		// 経度の取得
		double lon = location.getLongitude();
		int longitude = (int)(lon * 1000000);

		if(latitude/1000 != this.lastLatitude/1000 || longitude/1000 != this.lastLongitude/1000){
			GeoPoint gp = new GeoPoint(latitude, longitude);

			MapController c = mMapView.getMapController();

			c.setCenter(gp);

			this.lastLatitude = latitude;
			this.lastLongitude = longitude;
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
