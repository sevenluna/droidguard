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
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;

import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class DroidGuard extends Activity {
	// private static final int CHOICE_MODE_SINGLE = 1;

	public static final int DIALOG_SENSITIVITY_ID = 0;
	protected static final int LISTITEM_SEN_DIALOG = 0;
	protected static final int DIALOG_ALT_ID = 1;
	protected static final int LISTITEM_ALT_DIALOG = 1;
	protected static final int LISTITEM_ABOUT_DIALOG = 3;
	protected static final int DIALOG_ABOUT_ID = 3;
	protected static final int LISTITEM_WAIT_DIALOG = 2;
	protected static final int DIALOG_WAIT_ID = 2;

	private int currentSen = -1;
	private int currentWaits = -1;
	private String currentAlt = "";
	private final CharSequence[] items = new CharSequence[] { "Ringtone",
			"Vibration", "Calling" };
	private final boolean[] checkedItems = new boolean[NotifierManager.NOTIFIER_COUNT];

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
									DetectorManager.DETECTOR_TYPE_ACCELEMETER,
									progress);
					PrefStore
							.setDetectorSensitivity(mContext,
									DetectorManager.DETECTOR_TYPE_ORIENTATION,
									progress);
					
					
					
					Log.d("seekbar", "" + progress);
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
						public void onClick(DialogInterface dialog, int id) {
//							TextView lv = (TextView)contentlayout.findViewById(R.id.CurrentOp);
//							lv.setText(currentSen);
							
						}
					})
					.setView(contentlayout);

			dialog = builder.create();
			return dialog;

		case DIALOG_ALT_ID:

			builder = new AlertDialog.Builder(this);
			builder.setTitle("Alert ways").setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// 确认，并保存checkedItems

						}
					});
			builder.setMultiChoiceItems(items, checkedItems,
					new DialogInterface.OnMultiChoiceClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(),
									items[which], Toast.LENGTH_SHORT).show();
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

			builder.setTitle("Set waitting time") 
			.setSingleChoiceItems(waits, currentWaits,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							Toast.makeText(getApplicationContext(),
									waits[item], Toast.LENGTH_SHORT).show();
							PrefStore.setStartGuardWaitSeconds(DroidGuard.this,
									item * 5);
							dialog.dismiss();
						}
					})
			 .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
					(ViewGroup) findViewById(R.id.abLayoutDlg));

			Gallery ga = (Gallery) ablayout.findViewById(R.id.gallery);

			ga.setAdapter(new ImageAdapter(this));

			ga.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					Toast.makeText(DroidGuard.this, "" + position,
							Toast.LENGTH_SHORT).show();
				}
			});

			builder = new AlertDialog.Builder(this);
			builder.setTitle("About us").setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// 关闭about
						}
					});
			return builder.create();
		default:
			return null;
		}
	}

	private SimpleAdapter CreatSettingList() {
		ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> map0 = new HashMap<String, Object>();
		map0.put("ItemImage", R.drawable.icon);
		map0.put("OpTitle", getText(R.string.sensitivity));
		map0.put("CurrentOp", "" + currentSen);
		mylist.add(map0);

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("ItemImage", R.drawable.icon);
		map1.put("OpTitle", getText(R.string.alt_ways));
		map1.put("CurrentOp", currentAlt);
		mylist.add(map1);

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("ItemImage", R.drawable.icon);
		map2.put("OpTitle", getText(R.string.wait_time));
		map2.put("CurrentOp", "" + currentWaits);
		mylist.add(map2);

		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("ItemImage", R.drawable.icon);
		map3.put("OpTitle", getText(R.string.about_us));
		map3.put("CurrentOp", "");
		mylist.add(map3);

		return new SimpleAdapter(this,
				mylist,// 数据来源
				R.layout.lvitem,// ListItem的XML实现

				// 动态数组与ListItem对应的子项
				new String[] { "ItemImage", "OpTitle", "CurrentOp" },
				new int[] { R.id.ItemImage, R.id.OpTitle, R.id.CurrentOp });
	}

	private void InitSettings() {
		currentSen = PrefStore.getDetectorSensitivity(DroidGuard.this,
				DetectorManager.DETECTOR_TYPE_ACCELEMETER);

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
			if (checkedItems[i])
				currentAlt += items[i] + " ";
		}

		currentWaits = PrefStore.getStartGuardWaitSeconds(DroidGuard.this);
	}
}