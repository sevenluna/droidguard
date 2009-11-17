package com.ccmtc.android.droidguard;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class DroidGuard extends Activity {
	// private static final int CHOICE_MODE_SINGLE = 1;

	public static final int DIALOG_SENSITIVITY_ID = 0;
	protected static final int LISTITEM_SEN_DIALOG = 0;
	protected static final int DIALOG_ALT_ID = 2;
	protected static final int LISTITEM_ALT_DIALOG = 2;

	protected static final int LISTITEM_WAIT_DIALOG = 3;
	protected static final int DIALOG_WAIT_ID = 3;

	protected static final int LISTITEM_DET_DIALOG = 1;
	protected static final int DIALOG_DET_ID = 1;

	protected static final int LISTITEM_ABOUT_DIALOG = 4;
	protected static final int DIALOG_ABOUT_ID = 4;

	private int currentSen = -1;
	private int currentWaits = -1;
	
	private CharSequence currentSenText ="";
	private String currentAlt = "";
	private String currentDet = "";
	

	private final CharSequence[] sensiItems = new CharSequence[]{
			"significante","high","medium","low","tiny"		
	};
	private final CharSequence[] items = new CharSequence[] { "Ringtone",
			"Vibration", "Calling" };
	private final boolean[] checkedItems = new boolean[NotifierManager.NOTIFIER_COUNT];

	private final CharSequence[] detectItems = new CharSequence[] {
			"Accelerometer", "Orientation", "Wifi Field", "Location" };

	private final boolean[] enabledDets = new boolean[DetectorManager.DETECTOR_COUNT];


	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		// 绑定XML中的ListView，作为Item的容器
		InitSettings();
		ListView list = (ListView) findViewById(R.id.lvSettings);

		SimpleAdapter sa = CreatSettingList();

		list.setAdapter(sa);

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View view, int pos,
					long viewId) {
				// TODO Auto-generated method stub

				switch (pos) {
				case LISTITEM_SEN_DIALOG:
					showDialog(DIALOG_SENSITIVITY_ID);
					break;
				case LISTITEM_ALT_DIALOG:
					showDialog(DIALOG_ALT_ID);
					break;
				case LISTITEM_WAIT_DIALOG:
					showDialog(DIALOG_WAIT_ID);
					break;
				case LISTITEM_DET_DIALOG:
					showDialog(DIALOG_DET_ID);
					break;
				case LISTITEM_ABOUT_DIALOG:
					showDialog(DIALOG_ABOUT_ID);
					break;
				default:
					;
				}

				InitSettings();
			}
		});
		InitSettings();
	}

	/*
	 * 对话框生成器
	 * 
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog dialog;
		Builder builder;
		switch (id) {
		case DIALOG_SENSITIVITY_ID:

			final Context mContext = getApplicationContext();
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			final View contentlayout = inflater.inflate(R.layout.sen_dialog,
					(ViewGroup) findViewById(R.id.layoutDlg));

			ImageView image = (ImageView) contentlayout
					.findViewById(R.id.logoIcon);
			image.setImageResource(R.drawable.icon);

			SeekBar sb = (SeekBar) contentlayout.findViewById(R.id.seekBar);
			sb.setMax(Detector.DETECTOR_CHANGELEVEL_SIGNIFICANT);
			sb.setProgress(currentSen);
			sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				public void onProgressChanged(SeekBar s, int progress,
						boolean touch) {
					PrefStore
							.setDetectorSensitivity(mContext,
									DetectorManager.DETECTOR_TYPE_ACCELEROMETER,
									progress);
					PrefStore
							.setDetectorSensitivity(mContext,
									DetectorManager.DETECTOR_TYPE_ORIENTATION,
									progress);

					Log.d("seekbar", "" + progress);
					currentSen = progress;
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub

				}
			});

			builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.sensitivity).setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							TextView tvSen = (TextView) DroidGuard.this
									.findViewById(R.id.CurrentOp);
							tvSen.setText(sensiItems[currentSen]);
						}
					}).setView(contentlayout);

			dialog = builder.create();
			return dialog;
		case DIALOG_DET_ID:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("Detectors").setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// 确认，并更新checkedItems
							// TODO update item
							ListView lv = (ListView) DroidGuard.this
									.findViewById(R.id.lvSettings);
							Object objAlt = lv.getChildAt(LISTITEM_DET_DIALOG);
							TextView tvCuAlt = (TextView) ((RelativeLayout) objAlt)
									.getChildAt(2);
							UpdateDets();
							tvCuAlt.setText(currentDet);
						}
					});
			builder.setMultiChoiceItems(detectItems, enabledDets,
					new DialogInterface.OnMultiChoiceClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							// TODO Auto-generated method stub
							// Toast.makeText(getApplicationContext(),
							// items[which], Toast.LENGTH_SHORT).show();
							PrefStore.toggleDetector(DroidGuard.this, which,
									isChecked);

							Log.d("toggle detector", "detector at " + which);

						}
					});
			return builder.create();

		case DIALOG_ALT_ID:

			builder = new AlertDialog.Builder(this);
			builder.setTitle("Alert ways").setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// 确认，并更新checkedItems
							// TODO update item
							ListView lv = (ListView) DroidGuard.this
									.findViewById(R.id.lvSettings);
							Object objAlt = lv.getChildAt(LISTITEM_ALT_DIALOG);
							TextView tvCuAlt = (TextView) ((RelativeLayout) objAlt)
									.getChildAt(2);
							UpdateAlts();
							tvCuAlt.setText(currentAlt);
						}
					});
			builder.setMultiChoiceItems(items, checkedItems,
					new DialogInterface.OnMultiChoiceClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							// TODO Auto-generated method stub
							// Toast.makeText(getApplicationContext(),
							// items[which], Toast.LENGTH_SHORT).show();
							PrefStore.toggleNotifier(DroidGuard.this, which,
									isChecked);

							Log.d("toggle notify", "notify at " + which);

						}
					});
			return builder.create();

		case DIALOG_WAIT_ID:
			final CharSequence[] waits = new CharSequence[] { "None",
					"5 Seconds", "10 Seconds", "15 Seconds" };
			builder = new AlertDialog.Builder(this);

			builder.setTitle("Set waitting time").setSingleChoiceItems(waits,
					currentWaits / 5, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							Toast.makeText(getApplicationContext(),
									waits[item], Toast.LENGTH_SHORT).show();
							PrefStore.setStartGuardWaitSeconds(DroidGuard.this,
									item * 5);

							ListView lv = (ListView) DroidGuard.this
									.findViewById(R.id.lvSettings);
							Object objWaits = lv
									.getChildAt(LISTITEM_WAIT_DIALOG);
							TextView tvWaits = (TextView) ((RelativeLayout) objWaits)
									.getChildAt(2);
							tvWaits.setText(item * 5 + " seconds");

							dialog.dismiss();
						}
					}).setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});

			return builder.create();

		case DIALOG_ABOUT_ID:
			Context abContext = getApplicationContext();
			LayoutInflater abInflater = (LayoutInflater) abContext
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View ablayout = abInflater.inflate(R.layout.about_dialog,
					(ViewGroup) findViewById(R.id.lvAbout));

			ListView lvabout = (ListView) ablayout.findViewById(R.id.lvAbout);
			SimpleAdapter saAbout = CreatAboutList();

			lvabout.setAdapter(saAbout);

			lvabout.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					dismissDialog(DIALOG_ABOUT_ID);
				}
			});

			Builder aboutAlert = new AlertDialog.Builder(this).setTitle(
					"About us").setView(ablayout);

			return aboutAlert.create();
		default:
			return null;
		}
	}

	private SimpleAdapter CreatSettingList() {
		ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> map0 = new HashMap<String, Object>();
		map0.put("ItemImage", R.drawable.icon);
		map0.put("OpTitle", getText(R.string.sensitivity));
		map0.put("CurrentOp", "" + currentSenText);
		mylist.add(map0);
		
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("ItemImage", R.drawable.icon);
		map1.put("OpTitle", getText(R.string.Det_ways));
		map1.put("CurrentOp", currentDet);
		mylist.add(map1);

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("ItemImage", R.drawable.icon);
		map2.put("OpTitle", getText(R.string.alt_ways));
		map2.put("CurrentOp", currentAlt);
		mylist.add(map2);

		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("ItemImage", R.drawable.icon);
		map3.put("OpTitle", getText(R.string.wait_time));
		map3.put("CurrentOp", "" + currentWaits + " seconds");
		mylist.add(map3);

		HashMap<String, Object> map4 = new HashMap<String, Object>();
		map4.put("ItemImage", R.drawable.icon);
		map4.put("OpTitle", getText(R.string.about_us));
		map4.put("CurrentOp", "");
		mylist.add(map4);

		return new SimpleAdapter(this,
				mylist,// 数据来源
				R.layout.lvitem,// ListItem的XML实现

				// 动态数组与ListItem对应的子项
				new String[] { "ItemImage", "OpTitle", "CurrentOp" },
				new int[] { R.id.ItemImage, R.id.OpTitle, R.id.CurrentOp });
	}

	private void InitSettings() {
		currentSen = PrefStore.getDetectorSensitivity(DroidGuard.this,
				DetectorManager.DETECTOR_TYPE_ACCELEROMETER);
		
		currentSenText = sensiItems[currentSen];
		UpdateDets();
		UpdateAlts();

		currentWaits = PrefStore.getStartGuardWaitSeconds(DroidGuard.this);
	}

	private SimpleAdapter CreatAboutList() {
		ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> map0 = new HashMap<String, Object>();
		map0.put("ItemImage", R.drawable.icon);
		map0.put("OpTitle", "Ken Wang");
		map0.put("CurrentOp", "wanghao0000 AT Gmail");
		mylist.add(map0);

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("ItemImage", R.drawable.icon);
		map1.put("OpTitle", "Pengyu Fu");
		map1.put("CurrentOp", "lhc.simaoji AT Gmail");
		mylist.add(map1);

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("ItemImage", R.drawable.icon);
		map2.put("OpTitle", "Freesc Huang");
		map2.put("CurrentOp", "hjd.click AT Gmail");
		mylist.add(map2);

		return new SimpleAdapter(this,
				mylist,// 数据来源
				R.layout.lvitem,// ListItem的XML实现

				// 动态数组与ListItem对应的子项
				new String[] { "ItemImage", "OpTitle", "CurrentOp" },
				new int[] { R.id.ItemImage, R.id.OpTitle, R.id.CurrentOp });
	}

	private String UpdateAlts() {
		currentAlt = "";
		checkedItems[NotifierManager.NOTIFIER_TYPE_RINGTONE] = PrefStore
				.isNotifierSelected(DroidGuard.this,
						NotifierManager.NOTIFIER_TYPE_RINGTONE);
		checkedItems[NotifierManager.NOTIFIER_TYPE_VIBERATION] = PrefStore
				.isNotifierSelected(DroidGuard.this,
						NotifierManager.NOTIFIER_TYPE_VIBERATION);
		checkedItems[NotifierManager.NOTIFIER_TYPE_CALL] = PrefStore
				.isNotifierSelected(DroidGuard.this,
						NotifierManager.NOTIFIER_TYPE_CALL);

		for (int i = 0; i < NotifierManager.NOTIFIER_COUNT; i++) {
			if (checkedItems[i] && !currentAlt.contains(items[i]))
				currentAlt += items[i] + " ";
		}
		return currentAlt;
	}

	private String UpdateDets() {
		currentDet = "";
		enabledDets[DetectorManager.DETECTOR_TYPE_ACCELEROMETER] = PrefStore
				.isDetectorSelected(DroidGuard.this,
						DetectorManager.DETECTOR_TYPE_ACCELEROMETER);
		enabledDets[DetectorManager.DETECTOR_TYPE_ORIENTATION] = PrefStore
				.isDetectorSelected(DroidGuard.this,
						DetectorManager.DETECTOR_TYPE_ORIENTATION);
		enabledDets[DetectorManager.DETECTOR_TYPE_WIFI] = PrefStore
				.isDetectorSelected(DroidGuard.this,
						DetectorManager.DETECTOR_TYPE_WIFI);
		enabledDets[DetectorManager.DETECTOR_TYPE_LOCATION] = PrefStore
				.isDetectorSelected(DroidGuard.this,
						DetectorManager.DETECTOR_TYPE_LOCATION);

		for (int i = 0; i < DetectorManager.DETECTOR_COUNT; i++) {
			if (enabledDets[i] && !currentDet.contains(detectItems[i]))
				currentDet += detectItems[i] + " ";
		}

		return currentDet;
	}
}